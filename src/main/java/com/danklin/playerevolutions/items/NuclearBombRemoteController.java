package com.danklin.playerevolutions.items;

import com.danklin.playerevolutions.blocks.NuclearBombBlock.BombTileEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class NuclearBombRemoteController extends Item {

    public NuclearBombRemoteController(Properties properties) {
        super(properties.maxStackSize(1));
    }

    public NuclearBombRemoteController() {
        this(new Item.Properties().maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItem();

        if (player != null && player.isSneaking()) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof BombTileEntity) {
                if (!world.isRemote()) {
                    CompoundNBT nbt = stack.getOrCreateTag();
                    nbt.put("LinkedBomb", NBTUtil.writeBlockPos(pos));
                    world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, 1.0F, 1.5F);

                    player.sendMessage(new StringTextComponent("§a[REMOTE] Successfully paired to Nuclear Bomb at (" +
                            pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")"));
                }
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (playerIn.isSneaking()) {
            return ActionResult.resultPass(stack);
        }

        playerIn.swingArm(handIn);

        if (!worldIn.isRemote()) {
            CompoundNBT nbt = stack.getTag();

            if (nbt != null && nbt.contains("LinkedBomb")) {
                BlockPos targetPos = NBTUtil.readBlockPos(nbt.getCompound("LinkedBomb"));

                if (worldIn instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) worldIn;

                    int chunkX = targetPos.getX() >> 4;
                    int chunkZ = targetPos.getZ() >> 4;
                    serverWorld.getChunkProvider().forceChunk(new net.minecraft.util.math.ChunkPos(chunkX, chunkZ), true);

                    TileEntity te = serverWorld.getTileEntity(targetPos);

                    if (te instanceof BombTileEntity) {
                        BombTileEntity bomb = (BombTileEntity) te;

                        if (bomb.isIgnited()) {
                            bomb.defuse();
                            playerIn.sendMessage(new StringTextComponent("§c[REMOTE] Bomb ignition CANCELED!"));
                        } else {
                            bomb.ignite();
                            playerIn.sendMessage(new StringTextComponent("§e[REMOTE] Bomb ignited remotely! 4 seconds to detonation..."));
                        }

                        worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                                SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    } else {
                        playerIn.sendMessage(new StringTextComponent("§c[REMOTE] Error: No Nuclear Bomb exists at paired coordinates!"));
                    }

                    serverWorld.getChunkProvider().forceChunk(new net.minecraft.util.math.ChunkPos(chunkX, chunkZ), false);
                }
            } else {
                playerIn.sendMessage(new StringTextComponent("§c[REMOTE] Unpaired! Shift + Right-Click a Nuclear Bomb to pair."));
            }
        }

        return ActionResult.resultSuccess(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();
        if (nbt != null && nbt.contains("LinkedBomb")) {
            BlockPos pos = NBTUtil.readBlockPos(nbt.getCompound("LinkedBomb"));
            tooltip.add(new StringTextComponent("Paired Bomb: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ())
                    .setStyle(new Style().setColor(TextFormatting.GREEN)));
        } else {
            tooltip.add(new StringTextComponent("Unpaired - Shift + Right-Click a bomb to link")
                    .setStyle(new Style().setColor(TextFormatting.GRAY)));
        }
    }
}