package com.danklin.playerevolutions.events;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID)
public class NightVisionEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        PlayerEntity player = event.player;
        if (player == null || player.world.isRemote) return;

        ItemStack mainHand = player.getHeldItemMainhand();
        ItemStack offHand = player.getHeldItemOffhand();

        boolean isHoldingActive = false;

        if (mainHand.getItem() == RegistryHandler.NIGHT_VISION_GOGGLES.get() && isActive(mainHand)) {
            isHoldingActive = true;
        }
        else if (offHand.getItem() == RegistryHandler.NIGHT_VISION_GOGGLES.get() && isActive(offHand)) {
            isHoldingActive = true;
        }

        if (isHoldingActive) {
            player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 300, 0, false, false, false));
        } else {
            if (player.isPotionActive(Effects.NIGHT_VISION)) {
                player.removePotionEffect(Effects.NIGHT_VISION);
            }
        }
    }

    private static boolean isActive(ItemStack stack) {
        return stack.hasTag() && stack.getOrCreateTag().getBoolean("active");
    }
}