package com.danklin.playerevolutions;

import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("playerevolutions")
public class PlayerEvolutions {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "playerevolutions";

    public PlayerEvolutions() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        }
        RegistryHandler.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        // Render layer for non-solid blocks must be registered on the client thread during client setup
        RenderTypeLookup.setRenderLayer(RegistryHandler.MORTAR.get(), RenderType.getCutout());

        // Entity renderer registration
        RenderingRegistry.registerEntityRenderingHandler(
                RegistryHandler.GRENADE_ENTITY.get(),
                renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer())
        );
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    public static final ItemGroup TAB = new ItemGroup("playerEvolutions") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.RUBY.get());
        }
    };
}