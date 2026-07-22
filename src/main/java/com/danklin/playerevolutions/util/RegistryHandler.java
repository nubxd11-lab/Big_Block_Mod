package com.danklin.playerevolutions.util;

import com.danklin.playerevolutions.blocks.*;
import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, PlayerEvolutions.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, PlayerEvolutions.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, PlayerEvolutions.MOD_ID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", () -> new ItemBase(new Item.Properties()));
    public static final RegistryObject<Block> RUBY_BLOCK = BLOCKS.register("ruby_block", RubyBlock::new);
    public static final RegistryObject<Item> RUBY_BLOCK_ITEM = ITEMS.register("ruby_block", () -> new BlockItemBase(RUBY_BLOCK.get()));

    public static final RegistryObject<Block> SAPPHIRE_BLOCK = BLOCKS.register("sapphire_block", SapphireBlock::new);
    public static final RegistryObject<Item> SAPPHIRE_BLOCK_ITEM = ITEMS.register("sapphire_block", () -> new BlockItemBase(SAPPHIRE_BLOCK.get()));

    public static final RegistryObject<Block> BAUXITE_BLOCK = BLOCKS.register("bauxite_block", BauxiteBlock::new);
    public static final RegistryObject<Item> BAUXITE_BLOCK_ITEM = ITEMS.register("bauxite_block", () -> new BlockItemBase(BAUXITE_BLOCK.get()));

    public static final RegistryObject<Item> SCOPED_CROSSBOW = ITEMS.register("scoped_crossbow", () ->
            new ScopedCrossbow(new Item.Properties().group(PlayerEvolutions.TAB).maxDamage(16)));
    public static final RegistryObject<Item> MANPADS = ITEMS.register("manpads", () ->
            new ManpadsItem(new Item.Properties().group(PlayerEvolutions.TAB).maxStackSize(1)));
    public static final RegistryObject<Item> MANPADS_AMMO = ITEMS.register("manpads_ammo", () ->
            new Item(new Item.Properties().group(PlayerEvolutions.TAB)));

    public static final RegistryObject<Block> RED_LEGO_BLOCK = BLOCKS.register("red_lego_block", RedLegoBlock::new);
    public static final RegistryObject<Item> RED_LEGO_BLOCK_ITEM = ITEMS.register("red_lego_block", () -> new BlockItemBase(RED_LEGO_BLOCK.get()));

    public static final RegistryObject<Block> NUCLEAR_BOMB_BLOCK = BLOCKS.register("nuclear_bomb_block", NuclearBombBlock::new);
    public static final RegistryObject<Item> NUCLEAR_BOMB_BLOCK_ITEM = ITEMS.register("nuclear_bomb_block", () -> new BlockItemBase(NUCLEAR_BOMB_BLOCK.get()));
    public static final RegistryObject<TileEntityType<NuclearBombBlock.BombTileEntity>> NUCLEAR_BOMB_BLOCK_TILE =
            TILE_ENTITIES.register("nuclear_bomb_tile", () ->
                    TileEntityType.Builder.create(NuclearBombBlock.BombTileEntity::new, NUCLEAR_BOMB_BLOCK.get()).build(null)
            );
    public static final RegistryObject<Item> NUCLEAR_BOMB_REMOTE_CONTROLLER = ITEMS.register("nuclear_bomb_remote_controller",
            () -> new NuclearBombRemoteController(new Item.Properties().maxStackSize(1)));

    public static final RegistryObject<Item> AIR_BLAST = ITEMS.register("air_blast",
            () -> new AirBlast(new Item.Properties().maxStackSize(1)));
}