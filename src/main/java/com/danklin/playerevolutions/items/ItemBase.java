package com.danklin.playerevolutions.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import com.danklin.playerevolutions.PlayerEvolutions;

public class ItemBase extends Item {

    public ItemBase() {
        super(new Item.Properties().group(PlayerEvolutions.TAB));
    }
}
