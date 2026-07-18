package com.danklin.playerevolutions.util;

import com.danklin.playerevolutions.blocks.BauxiteBlock;
import com.danklin.playerevolutions.blocks.BlockItemBase;
import com.danklin.playerevolutions.blocks.RubyBlock;
import com.danklin.playerevolutions.blocks.SapphireBlock;
import com.danklin.playerevolutions.items.ItemBase;
import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.items.ManpadsItem;
import com.danklin.playerevolutions.items.ScopedCrossbow;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, PlayerEvolutions.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, PlayerEvolutions.MOD_ID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", ItemBase::new);
    public static final RegistryObject<Block> RUBY_BLOCK = BLOCKS.register("ruby_block", RubyBlock::new);
    public static final RegistryObject<Item> RUBY_BLOCK_ITEM = ITEMS.register("ruby_block", () -> new BlockItemBase(RUBY_BLOCK.get()));

    public static final RegistryObject<Block> SAPPHIRE_BLOCK = BLOCKS.register("sapphire_block", SapphireBlock::new);
    public static final RegistryObject<Item> SAPPHIRE_BLOCK_ITEM = ITEMS.register("sapphire_block",() -> new BlockItemBase(SAPPHIRE_BLOCK.get()));

    public static final RegistryObject<Block> BAUXITE_BLOCK = BLOCKS.register("bauxite_block", BauxiteBlock::new);
    public static final RegistryObject<Item> BAUXITE_BLOCK_ITEM = ITEMS.register("bauxite_block",() -> new BlockItemBase(BAUXITE_BLOCK.get()));

    public static final RegistryObject<Item> SCOPED_CROSSBOW = ITEMS.register("scoped_crossbow", () ->
            new ScopedCrossbow(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> MANPADS = ITEMS.register("manpads", () ->
            new ManpadsItem(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> ROCKET_AMMO = ITEMS.register("rocket_ammo", () ->
            new Item(new Item.Properties().group(PlayerEvolutions.TAB)));
}
