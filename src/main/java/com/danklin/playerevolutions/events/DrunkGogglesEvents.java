package com.danklin.playerevolutions.events;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.items.DrunkGoggles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID)
public class DrunkGogglesEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        PlayerEntity player = event.player;
        if (player == null || player.world.isRemote) return;

        boolean isHoldingGoggles = player.getHeldItemMainhand().getItem() instanceof DrunkGoggles
                || player.getHeldItemOffhand().getItem() instanceof DrunkGoggles;

        if (isHoldingGoggles) {
            if (!player.isPotionActive(Effects.NAUSEA)) {
                player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 999999, 1, false, false, false));
            }

            if (!player.isPotionActive(Effects.SLOWNESS)) {
                player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 999999, 0, false, false, false));
            }
        } else {
            if (player.isPotionActive(Effects.NAUSEA)) {
                player.removePotionEffect(Effects.NAUSEA);
            }

            if (player.isPotionActive(Effects.SLOWNESS)) {
                player.removePotionEffect(Effects.SLOWNESS);
            }
        }
    }
}