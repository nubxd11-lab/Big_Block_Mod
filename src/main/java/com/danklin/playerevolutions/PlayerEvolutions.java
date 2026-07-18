package com.danklin.playerevolutions;

import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Map;

@Mod("playerevolutions")
public class PlayerEvolutions {
    public static final String MOD_ID = "playerevolutions";

    // Cleanest way to build a creative inventory tab
    public static final ItemGroup TAB = new ItemGroup("playerevolutions_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.DIAMOND);
        }
    };

    public PlayerEvolutions() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryHandler.init();
        modEventBus.addListener(this::clientSetup);
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

        @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public static class ClientModelEvents {
            @SubscribeEvent
            public static void onModelBake(final ModelBakeEvent event) {
                Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();

                IBakedModel woolWorldModel = registry.get(new ModelResourceLocation("minecraft:blue_wool", ""));
                IBakedModel woolItemModel  = registry.get(new ModelResourceLocation("minecraft:blue_wool", "inventory"));

                if (woolWorldModel == null) {
                    woolWorldModel = registry.get(new ModelResourceLocation("minecraft:blue_wool", "normal"));
                }
                if (woolItemModel == null) {
                    woolItemModel = woolWorldModel;
                }

                if (woolWorldModel != null) {
                    registry.put(new ModelResourceLocation(MOD_ID + ":sapphire_block", ""), woolWorldModel);
                    registry.put(new ModelResourceLocation(MOD_ID + ":sapphire_block", "normal"), woolWorldModel);
                    registry.put(new ModelResourceLocation(MOD_ID + ":sapphire_block", "inventory"), woolItemModel);
                }
            }
        }
    }
