package com.danklin.playerevolutions.util;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.blocks.*;
import com.danklin.playerevolutions.effects.Fatigue;
import com.danklin.playerevolutions.effects.SugarHigh;
import com.danklin.playerevolutions.entities.AerosolCanEntity;
import com.danklin.playerevolutions.entities.BulletEntity;
import com.danklin.playerevolutions.entities.GrenadeEntity;
import com.danklin.playerevolutions.entities.SlipperEntity;
import com.danklin.playerevolutions.items.*;
import com.danklin.playerevolutions.tileentities.MortarTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.item.ItemGroup.COMBAT;
import static net.minecraft.item.ItemGroup.TOOLS;

public class RegistryHandler {
    public static final String MOD_ID = "playerevolutions";
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, PlayerEvolutions.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, PlayerEvolutions.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, PlayerEvolutions.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, PlayerEvolutions.MOD_ID);
    public static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, PlayerEvolutions.MOD_ID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MOD_ID);

    public static final RegistryObject<Item> RUBBER = ITEMS.register("rubber", ItemBase::new);

    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", ItemBase::new);
    public static final RegistryObject<Block> RUBY_BLOCK = BLOCKS.register("ruby_block", RubyBlock::new);
    public static final RegistryObject<Item> RUBY_BLOCK_ITEM = ITEMS.register("ruby_block",
            () -> new BlockItemBase(RUBY_BLOCK.get()));

    public static final RegistryObject<Block> SAPPHIRE_BLOCK = BLOCKS.register("sapphire_block", SapphireBlock::new);
    public static final RegistryObject<Item> SAPPHIRE_BLOCK_ITEM = ITEMS.register("sapphire_block",
            () -> new BlockItemBase(SAPPHIRE_BLOCK.get()));

    public static final RegistryObject<Block> BAUXITE_BLOCK = BLOCKS.register("bauxite_block", BauxiteBlock::new);
    public static final RegistryObject<Item> BAUXITE_BLOCK_ITEM = ITEMS.register("bauxite_block",
            () -> new BlockItemBase(BAUXITE_BLOCK.get()));

    public static final RegistryObject<Item> SCOPED_CROSSBOW = ITEMS.register("scoped_crossbow", () ->
            new ScopedCrossbow(new Item.Properties().group(COMBAT).maxDamage(16)));
    public static final RegistryObject<Item> MANPADS = ITEMS.register("manpads", () ->
            new ManpadsItem(new Item.Properties().group(COMBAT).maxStackSize(1)));
    public static final RegistryObject<Item> MANPADS_AMMO = ITEMS.register("manpads_ammo", () ->
            new Item(new Item.Properties().group(COMBAT)));

    public static final RegistryObject<Block> RED_LEGO_BLOCK = BLOCKS.register("red_lego_block", RedLegoBlock::new);
    public static final RegistryObject<Item> RED_LEGO_BLOCK_ITEM = ITEMS.register("red_lego_block",
            () -> new BlockItemBase(RED_LEGO_BLOCK.get()));

    public static final RegistryObject<Block> NUCLEAR_BOMB_BLOCK = BLOCKS.register("nuclear_bomb_block", NuclearBombBlock::new);
    public static final RegistryObject<Item> NUCLEAR_BOMB_BLOCK_ITEM = ITEMS.register("nuclear_bomb_block",
            () -> new BlockItemBase(NUCLEAR_BOMB_BLOCK.get()));
    public static final RegistryObject<TileEntityType<NuclearBombBlock.BombTileEntity>> NUCLEAR_BOMB_BLOCK_TILE =
            TILE_ENTITIES.register("nuclear_bomb_tile", () ->
                    TileEntityType.Builder.create(NuclearBombBlock.BombTileEntity::new, NUCLEAR_BOMB_BLOCK.get()).build(null)
            );
    public static final RegistryObject<Item> NUCLEAR_BOMB_REMOTE_CONTROLLER = ITEMS.register("nuclear_bomb_remote_controller",
            () -> new NuclearBombRemoteController(new Item.Properties().group(COMBAT).maxStackSize(1)));

    public static final RegistryObject<Item> AIR_BLAST = ITEMS.register("air_blast",
            () -> new AirBlast(new Item.Properties().maxStackSize(1).group(COMBAT)));

    public static final RegistryObject<Item> GRENADE = ITEMS.register("grenade",
            () -> new Grenade(new Item.Properties().group(COMBAT)));
    public static final RegistryObject<EntityType<GrenadeEntity>> GRENADE_ENTITY = ENTITIES.register("grenade",
            () -> EntityType.Builder.<GrenadeEntity>create(GrenadeEntity::new, EntityClassification.MISC)
                    .size(0.35F, 0.35F)
                    .setCustomClientFactory((spawnEntity, world) -> new GrenadeEntity(RegistryHandler.GRENADE_ENTITY.get(), world))
                    .build("grenade"));

    public static final RegistryObject<Block> MORTAR = BLOCKS.register("mortar", Mortar::new);
    public static final RegistryObject<Item> MORTAR_ITEM = ITEMS.register("mortar",
            () -> new BlockItemBase(MORTAR.get()));
    public static final RegistryObject<TileEntityType<MortarTileEntity>> MORTAR_TILE_ENTITY = TILE_ENTITIES.register("mortar",
            () -> TileEntityType.Builder.create(MortarTileEntity::new, MORTAR.get()).build(null));

    public static final RegistryObject<Item> DAGGER = ITEMS.register("dagger",
            () -> new Dagger(new Item.Properties().group(COMBAT).defaultMaxDamage(250)));

    public static final RegistryObject<Item> BULLET = ITEMS.register("bullet",
            ()-> new Bullet(new Item.Properties().group(COMBAT)));
    public static final RegistryObject<EntityType<BulletEntity>> BULLET_ENTITY = ENTITY_TYPES.register("bullet",
            () -> EntityType.Builder.<BulletEntity>create(BulletEntity::new, EntityClassification.MISC)
                    .size(0.25F, 0.25F)
                    .build(new ResourceLocation(PlayerEvolutions.MOD_ID, "bullet").toString()));

    public static final RegistryObject<Item> PISTOL = ITEMS.register("pistol",
            ()-> new Pistol(new Item.Properties().group(COMBAT).defaultMaxDamage(500)));

    public static final RegistryObject<Item> SLIPPER = ITEMS.register("slipper",
            () -> new Slipper(new Item.Properties().group(COMBAT).defaultMaxDamage(100)));
    public static final RegistryObject<EntityType<SlipperEntity>> SLIPPER_ENTITY = ENTITIES.register("slipper_entity",
            () -> EntityType.Builder.<SlipperEntity>create(SlipperEntity::new, EntityClassification.MISC)
                    .size(0.6F, 0.6F)
                    .build("slipper_entity"));

    public static final RegistryObject<Item> BELT = ITEMS.register("belt",
            ()-> new Belt(new Item.Properties().group(COMBAT).defaultMaxDamage(200)));

    public static final RegistryObject<Item> AEROSOL_CAN = ITEMS.register("aerosol_can",
            () -> new AerosolCan(new Item.Properties().group(COMBAT)));
    public static final RegistryObject<EntityType<AerosolCanEntity>> AEROSOL_CAN_ENTITY = ENTITIES.register("aerosol_can",
            () -> EntityType.Builder.<AerosolCanEntity>create(AerosolCanEntity::new, EntityClassification.MISC)
                    .size(0.5F, 0.5F)
                    .build("aerosol_can"));

    public static final RegistryObject<Item> HONEY_GLAZED_PORKCHOP = ITEMS.register("honey_glazed_porkchop",
            () -> new HoneyGlazedPorkchop(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .hunger(8)
                            .saturation(12.8F)
                            .meat()
                            .build())));

    public static final RegistryObject<Item> CHAR_SIU_PORKCHOP = ITEMS.register("char_siu_porkchop",
            () -> new CharSiuPorkchop(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .hunger(8)
                            .saturation(12.8F)
                            .meat()
                            .build())));

    public static final RegistryObject<Item> HONEY_GLAZED_CHICKEN = ITEMS.register("honey_glazed_chicken",
            () -> new HoneyGlazedChicken(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .hunger(6)
                            .saturation(7.2F)
                            .meat()
                            .build())));

    public static final RegistryObject<Item> MELON_JUICE_BOTTLE = ITEMS.register("melon_juice_bottle",
            () -> new MelonJuiceBottle(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .hunger(3)
                            .setAlwaysEdible()
                            .saturation(4.6F)
                            .build())));

    public static final RegistryObject<Item> APPLE_JUICE_BOTTLE = ITEMS.register("apple_juice_bottle",
            () -> new AppleJuiceBottle(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .hunger(3)
                            .setAlwaysEdible()
                            .saturation(4.6F)
                            .build())));

    public static final RegistryObject<Item> CARROT_JUICE_BOTTLE = ITEMS.register("carrot_juice_bottle",
            () -> new CarrotJuiceBottle(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .hunger(3)
                            .setAlwaysEdible()
                            .saturation(4.6F)
                            .build())));

    public static final RegistryObject<Item> MJOLNIR = ITEMS.register("mjolnir",
            () -> new Mjolnir(new Item.Properties().group(COMBAT).defaultMaxDamage(250)));

    public static final RegistryObject<Block> STOOL = BLOCKS.register("stool", Stool::new);
    public static final RegistryObject<Item> STOOL_ITEM = ITEMS.register("stool",
            () -> new BlockItemBase(STOOL.get()));

    public static final RegistryObject<Item> ULTRA_TORCH = ITEMS.register("ultra_torch",
            () -> new UltraTorch(new Item.Properties().group(TOOLS).maxStackSize(1)));
    public static final RegistryObject<Block> INVISIBLE_LIGHT_BLOCK = BLOCKS.register("invisible_light_block", InvisibleLightBlock::new);

    public static final RegistryObject<Item> NIGHT_VISION_GOGGLES = ITEMS.register("night_vision_goggles",
            () -> new NightVisionGoggles(new Item.Properties().group(TOOLS).maxStackSize(1)));

    public static final RegistryObject<Effect> FATIGUE = EFFECTS.register("fatigue",
            () -> new Fatigue(EffectType.HARMFUL, 0x000000));

    public static final RegistryObject<Item> ADRENALINE_PILL = ITEMS.register("adrenaline_pill",
            () -> new AdrenalinePill (new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .setAlwaysEdible()
                            .fastToEat()
                            .effect(() -> new EffectInstance(Effects.STRENGTH, 1200, 0), 1.0f)
                            .build())));

    public static final RegistryObject<Item> PARACETAMOL_PILL = ITEMS.register("paracetamol_pill",
            () -> new ParacetamolPill(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .setAlwaysEdible()
                            .fastToEat()
                            .effect(() -> new EffectInstance(RegistryHandler.FATIGUE.get(), 1200, 0), 1.0f)
                            .build())));

    public static final RegistryObject<Item> RADAR = ITEMS.register("radar",
            () -> new Radar(new Item.Properties().group(TOOLS).maxStackSize(1)));

    public static final RegistryObject<Effect> SUGAR_HIGH = EFFECTS.register("sugar_high",
            () -> new SugarHigh (EffectType.NEUTRAL, 0x000000));

    public static final RegistryObject<Item> CANDY = ITEMS.register("candy",
            () -> new Candy(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .fastToEat()
                            .effect(() -> new EffectInstance(RegistryHandler.SUGAR_HIGH.get(), 1200, 0), 1.0f)
                            .build())));

    public static final RegistryObject<Item> COFFEE_CANDY = ITEMS.register("coffee_candy",
            () -> new CoffeeCandy(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .fastToEat()
                            .effect(() -> new EffectInstance(Effects.SPEED, 1200, 0), 1.0f)
                            .effect(() -> new EffectInstance(Effects.HASTE, 1200, 0), 1.0f)
                            .build())));

    public static final RegistryObject<Item> FIRE_SPIRIT = ITEMS.register("fire_spirit",
            () -> new FireSpirit(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .fastToEat()
                            .setAlwaysEdible()
                            .effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 1200, 0), 1.0f)
                            .build())));

    public static final RegistryObject<Item> DRUNK_GOGGLES = ITEMS.register("drunk_goggles",
            () -> new DrunkGoggles(new Item.Properties().group(TOOLS).maxStackSize(1)));

    public static final RegistryObject<Item> FEATHER_DUST = ITEMS.register("feather_dust",
            () -> new FeatherDust(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .fastToEat()
                            .setAlwaysEdible()
                            .effect(() -> new EffectInstance(Effects.SLOW_FALLING, 1200, 0), 1.0f)
                            .build())));

    public static final RegistryObject<Item> SUSPICIOUS_BERRY = ITEMS.register("suspicious_berry",
            () -> new SuspiciousBerry(new Item.Properties()
                    .group(ItemGroup.FOOD)
                    .food(new Food.Builder()
                            .fastToEat()
                            .setAlwaysEdible()
                            .effect(() -> new EffectInstance(Effects.UNLUCK, 1200, 0), 0.25f)
                            .effect(() -> new EffectInstance(Effects.LUCK, 1200, 0), 0.25f)
                            .effect(() -> new EffectInstance(Effects.BAD_OMEN, 1200, 0), 0.25f)
                            .effect(() -> new EffectInstance(Effects.HERO_OF_THE_VILLAGE, 1200, 0), 0.25f)
                            .build())));
}