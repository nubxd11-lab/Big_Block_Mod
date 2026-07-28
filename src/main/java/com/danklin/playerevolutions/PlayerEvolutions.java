package com.danklin.playerevolutions;

import com.danklin.playerevolutions.network.PacketFireMortar;
import com.danklin.playerevolutions.network.PistolShootPacket;
import com.danklin.playerevolutions.util.BulletRenderer;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

import java.util.List;

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
        RenderingRegistry.registerEntityRenderingHandler(
                RegistryHandler.AEROSOL_CAN_ENTITY.get(),
                renderManager -> new SpriteRenderer<>(renderManager, Minecraft.getInstance().getItemRenderer())
        );
    }


    @SubscribeEvent
    public static void onDismount(EntityMountEvent event){
        if(event.isDismounting() && !event.getWorldObj().isRemote()){
            ArmorStandEntity seat = (ArmorStandEntity) event.getEntityBeingMounted();
            if(seat.getCustomName() != null && seat.getCustomName().getString().equals("stool_seat")){
                seat.remove();
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        World world = (World) event.getWorld();
        BlockPos pos = event.getPos();

        if (!world.isRemote()) {
            AxisAlignedBB searchBox = new AxisAlignedBB(pos).grow(0.5D);
            List<ArmorStandEntity> seats = world.getEntitiesWithinAABB(ArmorStandEntity.class, searchBox,
                    e -> e.getCustomName() != null && e.getCustomName().getString().equals("stool_seat"));

            for (ArmorStandEntity seat : seats) {
                seat.removePassengers();
                seat.remove();
            }
        }
    }

    public static final ItemGroup TAB = new ItemGroup("PlayerEvolutions") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.RUBY.get());
        }
    };
}