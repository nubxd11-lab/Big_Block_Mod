package com.danklin.playerevolutions.items;

import net.minecraft.item.Item;
import com.danklin.playerevolutions.PlayerEvolutions;
import net.minecraft.item.ItemGroup;

public class ItemBase extends Item {

    public ItemBase() {
        super(new Item.Properties().group(ItemGroup.MISC));
    }
}