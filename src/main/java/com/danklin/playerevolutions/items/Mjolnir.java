package com.danklin.playerevolutions.items;

import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class Mjolnir extends Item {

    private final float attackDamage = 10.0F;
    private final float attackSpeed = -3.5F;

    public Mjolnir(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getEntityWorld();
        if (!world.isRemote && world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            LightningBoltEntity lightning = new LightningBoltEntity(
                    world,
                    target.getPosX(),
                    target.getPosY(),
                    target.getPosZ(),
                    false
            );
            serverWorld.addLightningBolt(lightning);
            stack.damageItem(1, attacker, (p) -> p.sendBreakAnimation(EquipmentSlotType.MAINHAND));
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        World world = entity.getEntityWorld();
        if (!world.isRemote && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!player.getCooldownTracker().hasCooldown(this)) {
                ServerWorld serverWorld = (ServerWorld) world;
                Vec3d eyePos = player.getEyePosition(1.0F);
                Vec3d lookVec = player.getLookVec();
                Vec3d reachVec = eyePos.add(lookVec.x * 50.0D, lookVec.y * 50.0D, lookVec.z * 50.0D);

                BlockRayTraceResult rayTrace = world.rayTraceBlocks(new RayTraceContext(
                        eyePos,
                        reachVec,
                        RayTraceContext.BlockMode.COLLIDER,
                        RayTraceContext.FluidMode.NONE,
                        player
                ));

                if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
                    double targetX = rayTrace.getPos().getX() + 0.5D;
                    double targetY = rayTrace.getPos().getY() + 1.0D;
                    double targetZ = rayTrace.getPos().getZ() + 0.5D;

                    LightningBoltEntity lightning = new LightningBoltEntity(
                            world,
                            targetX,
                            targetY,
                            targetZ,
                            false
                    );
                    serverWorld.addLightningBolt(lightning);
                    player.getCooldownTracker().setCooldown(this, 20);
                }
            }
        }
        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION)
            );
            multimap.put(
                    SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) this.attackSpeed, AttributeModifier.Operation.ADDITION)
            );
        }
        return multimap;
    }
}