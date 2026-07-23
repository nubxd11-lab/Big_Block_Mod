package com.danklin.playerevolutions.blocks;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockItemBase extends BlockItem {
    public BlockItemBase(Block block) {
        super(block, new Item.Properties().group(PlayerEvolutions.TAB));
    }
    @Override
    public int getItemStackLimit(ItemStack stack) {
        if (this.getBlock() == RegistryHandler.MORTAR.get()) {
            return 1;
        }
        return super.getItemStackLimit(stack);
    }
}
