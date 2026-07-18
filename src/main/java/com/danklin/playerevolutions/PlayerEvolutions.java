package com.danklin.playerevolutions;

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
        public static final ItemGroup TAB = new ItemGroup("playerevolutions_tab") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Items.DIAMOND);
            }
        };

    public PlayerEvolutions() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup (final FMLCommonSetupEvent event) {

    }
    private void doClientStuff(final FMLClientSetupEvent event) {

    }
}
