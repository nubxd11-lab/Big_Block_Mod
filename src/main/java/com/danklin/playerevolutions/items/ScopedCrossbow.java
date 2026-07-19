package com.danklin.playerevolutions.items;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ScopedCrossbow extends CrossbowItem {

    float power;
    public ScopedCrossbow(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.POWER) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @SubscribeEvent
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        PlayerEntity player = event.getPlayer();
        ItemStack mainStack = player.getHeldItemMainhand();
        ItemStack offStack = player.getHeldItemOffhand();

        if (mainStack.getItem() == RegistryHandler.SCOPED_CROSSBOW.get() && CrossbowItem.isCharged(mainStack)) {
            event.getRenderer().getEntityModel().rightArmPose = BipedModel.ArmPose.CROSSBOW_HOLD;
        }
        else if (offStack.getItem() == RegistryHandler.SCOPED_CROSSBOW.get() && CrossbowItem.isCharged(offStack)) {
            event.getRenderer().getEntityModel().leftArmPose = BipedModel.ArmPose.CROSSBOW_HOLD;
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, net.minecraft.entity.LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        float f = getChargeProgression(i, stack);
        PlayerEntity player = (PlayerEntity) entityLiving;
        if (f >= 0.4F && !isCharged(stack)) {
            setCharged(stack, true);
            power = f;
            boolean isCreative = player.abilities.isCreativeMode;
            worldIn.playSound(null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(),
                    SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
            if (isCreative || (hasItem(player, Items.TNT) && hasItem(player, Items.ARROW))) {

                if (!isCreative) {
                    consumeItem(player, Items.TNT);
                    consumeItem(player, Items.ARROW);
                }

                setCharged(stack, true);
                worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(),
                        SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
            }
            return;
        }

        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    private static float getChargeProgression(int useTime, ItemStack stack) {
        float f = (float)useTime / (float)getChargeTime(stack);
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
    private boolean hasItem(PlayerEntity player, Item itemToCheck) {
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            if (player.inventory.getStackInSlot(i).getItem() == itemToCheck) {
                return true;
            }
        }
        return false;
    }

    private void consumeItem(PlayerEntity player, Item itemToConsume) {
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() == itemToConsume) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
                return;
            }
        }
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        System.out.println("Right-clicked the custom crossbow! Is it charged? " + isCharged(playerIn.getHeldItem(handIn)));
        if (isCharged(itemstack)) {
            if (!worldIn.isRemote) {

                int powerEnchantLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, itemstack);
                SnowballEntity projectile = new SnowballEntity(worldIn, playerIn);
                projectile.setItem(new ItemStack(Items.TNT));
                projectile.addTag("explosive_bolt");
                projectile.getPersistentData().putInt("PowerLevel", powerEnchantLevel);
                float velocityBonus = powerEnchantLevel * 0.5f;
                projectile.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, power * 3.5f + velocityBonus, 1.0F);
                worldIn.addEntity(projectile);
            }

            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            setCharged(itemstack, false);
            itemstack.damageItem(1, playerIn, (entity) -> entity.sendBreakAnimation(handIn));
            return ActionResult.resultConsume(itemstack);
        } else {
            boolean isCreative = playerIn.abilities.isCreativeMode;
            if (!isCreative && (!hasItem(playerIn, Items.TNT) || !hasItem(playerIn, Items.ARROW))) {
                return ActionResult.resultFail(itemstack);
            }
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }
    @Override
    public String getTranslationKey() {
        return "Scoped Crossbow";
    }
}