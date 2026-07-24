package com.danklin.playerevolutions;

import com.danklin.playerevolutions.network.PacketFireMortar;
import com.danklin.playerevolutions.network.PistolShootPacket;
import com.danklin.playerevolutions.util.BulletRenderer;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("playerevolutions")
public class PlayerEvolutions {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "playerevolutions";
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public PlayerEvolutions() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        }
        RegistryHandler.init();
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        int id = 0;
        NETWORK.registerMessage(
                id++,
                PacketFireMortar.class,
                PacketFireMortar::encode,
                PacketFireMortar::decode,
                PacketFireMortar::handle
        );

        NETWORK.registerMessage(
                id++,
                PistolShootPacket.class,
                PistolShootPacket::encode,
                PistolShootPacket::decode,
                PistolShootPacket::handle
        );
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(RegistryHandler.MORTAR.get(), RenderType.getCutout());

        RenderingRegistry.registerEntityRenderingHandler(
                RegistryHandler.GRENADE_ENTITY.get(),
                renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer())
        );
        RenderingRegistry.registerEntityRenderingHandler(
                RegistryHandler.BULLET_ENTITY.get(),
                BulletRenderer::new
        );
        RenderingRegistry.registerEntityRenderingHandler(
                RegistryHandler.SLIPPER_ENTITY.get(),
                renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer())
        );
    }


    public static final ItemGroup TAB = new ItemGroup("playerEvolutions") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.RUBY.get());
        }
    };
}