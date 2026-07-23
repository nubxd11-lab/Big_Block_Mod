package com.danklin.playerevolutions.items;

import com.danklin.playerevolutions.PlayerEvolutions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class NightVisionGogglesHelmet extends ArmorItem {

    public NightVisionGogglesHelmet() {
        super(
                GogglesMaterial.INSTANCE,
                EquipmentSlotType.HEAD,
                new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)
        );
    }

    public NightVisionGogglesHelmet(IArmorMaterial materialIn) {
        super(materialIn, EquipmentSlotType.HEAD, new Item.Properties().group(PlayerEvolutions.TAB).maxStackSize(1));
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (!world.isRemote) {
            player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 300, 0, false, false, true));
        }
    }
}