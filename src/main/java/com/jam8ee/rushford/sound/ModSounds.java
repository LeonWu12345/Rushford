package com.jam8ee.rushford.sound;

import com.jam8ee.rushford.Rushford;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final Identifier POOP_ID = Identifier.of(Rushford.MOD_ID, "poop");
    public static final SoundEvent POOP = SoundEvent.of(POOP_ID);

    public static void registerModSounds() {
        Registry.register(Registries.SOUND_EVENT, POOP_ID, POOP);
        Rushford.LOGGER.info("Registering sounds for " + Rushford.MOD_ID);
    }
}