package com.danklin.playerevolutions;

import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("playerevolutions")
public class PlayerEvolutions {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "playerevolutions";

    public PlayerEvolutions() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        RegistryHandler.init();
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void clientSetup(final FMLClientSetupEvent event) {
        int sapphireHex = 0x0000FF;
        int bauxiteHex = 0x9A554A;

        Minecraft.getInstance().getBlockColors().register((state, view, pos, tintIndex) -> sapphireHex, RegistryHandler.SAPPHIRE_BLOCK.get());
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> sapphireHex, RegistryHandler.SAPPHIRE_BLOCK_ITEM.get());
        Minecraft.getInstance().getBlockColors().register((state, view, pos, tintIndex) -> bauxiteHex, RegistryHandler.BAUXITE_BLOCK.get());
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> bauxiteHex, RegistryHandler.BAUXITE_BLOCK_ITEM.get());
    }


    private void setup (final FMLCommonSetupEvent event) {

    }
    private void doClientStuff(final FMLClientSetupEvent event) {

    }
    public static final ItemGroup TAB = new ItemGroup("playerEvolutions") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.RUBY.get());
        }
    };
}
