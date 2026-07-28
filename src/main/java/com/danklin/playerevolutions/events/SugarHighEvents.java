package com.danklin.playerevolutions.events;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID)
public class SugarHighEvents {

    @SubscribeEvent
    public static void onpotionExpire(PotionEvent.PotionExpiryEvent event) {
        applySugarCrash(event.getEntityLiving(), event.getPotionEffect());
    }

    private static void applySugarCrash(LivingEntity entity, EffectInstance effectInstance) {
        if (!entity.world.isRemote && effectInstance != null && effectInstance.getPotion() == RegistryHandler.SUGAR_HIGH.get()) {
            entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200, 0));
            entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 160, 0));
        }
    }
}
