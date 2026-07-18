package com.danklin.playerevolutions.items;

import net.minecraft.item.Item;
import com.danklin.playerevolutions.PlayerEvolutions;

public class ItemBase extends Item {
    public ItemBase(Properties properties) {
        super(new Item.Properties().group(PlayerEvolutions.TAB));
    }
}
