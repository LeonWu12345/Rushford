package com.jam8ee.rushford.block;

import com.jam8ee.rushford.block.entity.ToiletBlockEntity;
import com.jam8ee.rushford.entity.ToiletEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToiletBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final MapCodec<ToiletBlock> CODEC = createCodec(ToiletBlock::new);

    private static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(2, 0, 2, 14, 8, 14),
            Block.createCuboidShape(2, 8, 2, 14, 10, 14)
    );

    public ToiletBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ToiletBlockEntity(pos, state);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // 潜行时打开物品栏
        if (player.isSneaking()) {
            if (!world.isClient()) {
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    player.openHandledScreen(screenHandlerFactory);
                }
            }
            return ItemActionResult.SUCCESS;
        }

        // 非潜行时坐下
        if (!world.isClient()) {
            List<ToiletEntity> existingSeats = world.getEntitiesByClass(
                    ToiletEntity.class,
                    new Box(pos),
                    entity -> true
            );

            if (existingSeats.isEmpty()) {
                ToiletEntity seat = new ToiletEntity(world, pos);
                world.spawnEntity(seat);
                player.startRiding(seat);
            }
        }
        return ItemActionResult.SUCCESS;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // 空手时的交互
        if (player.isSneaking()) {
            if (!world.isClient()) {
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    player.openHandledScreen(screenHandlerFactory);
                }
            }
            return ActionResult.SUCCESS;
        }

        // 非潜行时坐下
        if (!world.isClient()) {
            List<ToiletEntity> existingSeats = world.getEntitiesByClass(
                    ToiletEntity.class,
                    new Box(pos),
                    entity -> true
            );

            if (existingSeats.isEmpty()) {
                ToiletEntity seat = new ToiletEntity(world, pos);
                world.spawnEntity(seat);
                player.startRiding(seat);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            // 移除座位实体
            List<ToiletEntity> seats = world.getEntitiesByClass(
                    ToiletEntity.class,
                    new Box(pos),
                    entity -> true
            );
            for (ToiletEntity seat : seats) {
                seat.discard();
            }

            // 掉落物品
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ToiletBlockEntity toilet) {
                ItemScatterer.spawn(world, pos, toilet);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}