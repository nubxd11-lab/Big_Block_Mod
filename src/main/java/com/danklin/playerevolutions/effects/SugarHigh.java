package com.danklin.playerevolutions.effects;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class SugarHigh extends Effect {

    public SugarHigh(EffectType type, int liquidColor) {
        super(type, liquidColor);

        this.addAttributesModifier(
                SharedMonsterAttributes.MOVEMENT_SPEED,
                "d8431e60-91bc-4e52-a50e-365287515b12",
                0.20D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );

        this.addAttributesModifier(
                SharedMonsterAttributes.ATTACK_SPEED,
                "a18a804a-810e-436d-9b51-1e9d1e3894b1",
                0.20D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}