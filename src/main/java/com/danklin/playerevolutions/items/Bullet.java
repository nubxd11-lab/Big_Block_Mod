package com.danklin.playerevolutions.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Bullet extends Item {

    public Bullet() {
        super(new Item.Properties()
                .group(ItemGroup.COMBAT)
                .maxStackSize(64)
        );
    }
}