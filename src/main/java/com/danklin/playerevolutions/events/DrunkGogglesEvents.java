package com.danklin.playerevolutions.events;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.items.DrunkGoggles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID, value = Dist.CLIENT)
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
                player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 220, 1, false, false, false));
            }

            if (!player.isPotionActive(Effects.SLOWNESS)) {
                player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 220, 0, false, false));
            }
        }
    }
}