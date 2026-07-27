package com.danklin.playerevolutions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

public class Fatigue extends Effect {
    public Fatigue(EffectType type, int liquidColor){
        super(type,liquidColor);

        this.addAttributesModifier(
                SharedMonsterAttributes.MOVEMENT_SPEED,
            "46328322-261B-40E7-9D67-338271790483",
            -0.15D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );

        this.addAttributesModifier(
                SharedMonsterAttributes.ATTACK_SPEED,
                "371B20A9-6933-4D9D-A3D9-9D6303212891",
                -0.20D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 40, amplifier, false, false));

            if (player.ticksExisted % 40 == 0) {
                player.addExhaustion(1.0F + (amplifier * 0.5F));
            }
        }
    }
}
