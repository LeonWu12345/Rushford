package com.jam8ee.rushford.effect;

import com.jam8ee.rushford.Rushford;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static final RegistryEntry<StatusEffect> DIARRHEA = registerEffect("diarrhea", new DiarrheaEffect());

    private static RegistryEntry<StatusEffect> registerEffect(String name, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Rushford.MOD_ID, name), effect);
    }

    public static void registerModEffects() {
        Rushford.LOGGER.info("Registering effects for " + Rushford.MOD_ID);
    }
}