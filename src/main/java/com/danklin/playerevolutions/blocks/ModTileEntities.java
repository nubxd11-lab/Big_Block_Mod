package com.danklin.playerevolutions.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "playerevolutions", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileEntities {

    public static TileEntityType<NuclearBombBlock.BombTileEntity> NUCLEAR_BOMB_BLOCK;

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        NUCLEAR_BOMB_BLOCK = TileEntityType.Builder.create(NuclearBombBlock.BombTileEntity::new, ModBlocks.NUCLEAR_BOMB_BLOCK.get()).build(null);

        // FIX: Changed "nuclear_bomb" to "nuclear_bomb_tile" to stop the ID collision!
        NUCLEAR_BOMB_BLOCK.setRegistryName("playerevolutions", "nuclear_bomb_block");

        event.getRegistry().register(NUCLEAR_BOMB_BLOCK);
    }
}
