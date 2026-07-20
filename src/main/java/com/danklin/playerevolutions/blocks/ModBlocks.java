package com.danklin.playerevolutions.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            new DeferredRegister<>(ForgeRegistries.BLOCKS, "playerevolutions");

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, "playerevolutions");

    // 1. Raw Text ID: "nuclear_bomb"
    public static final RegistryObject<Block> NUCLEAR_BOMB_BLOCK = BLOCKS.register("nuclear_bomb_block",
            () -> new NuclearBombBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0f, 3.0f))
    );

    // 2. Raw Text ID Changed To: "nuclear_bomb_tile_entity" -> This kills the duplicate ID crash!
    public static final RegistryObject<TileEntityType<NuclearBombBlock.BombTileEntity>> NUCLEAR_BOMB_BLOCK_TILE =
            TILE_ENTITIES.register("nuclear_bomb_block_entity", () ->
                    TileEntityType.Builder.create(NuclearBombBlock.BombTileEntity::new, NUCLEAR_BOMB_BLOCK.get()).build(null)
            );
}
