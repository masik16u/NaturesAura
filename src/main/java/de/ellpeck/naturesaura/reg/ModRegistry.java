package de.ellpeck.naturesaura.reg;

import de.ellpeck.naturesaura.Helper;
import de.ellpeck.naturesaura.ModConfig;
import de.ellpeck.naturesaura.NaturesAura;
import de.ellpeck.naturesaura.blocks.*;
import de.ellpeck.naturesaura.blocks.tiles.ModTileEntities;
import de.ellpeck.naturesaura.entities.EntityEffectInhibitor;
import de.ellpeck.naturesaura.entities.EntityMoverMinecart;
import de.ellpeck.naturesaura.entities.ModEntities;
import de.ellpeck.naturesaura.items.*;
import de.ellpeck.naturesaura.items.tools.*;
import de.ellpeck.naturesaura.misc.BlockLootProvider;
import de.ellpeck.naturesaura.misc.BlockTagProvider;
import de.ellpeck.naturesaura.misc.ItemTagProvider;
import de.ellpeck.naturesaura.potion.ModPotions;
import de.ellpeck.naturesaura.potion.PotionBreathless;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModRegistry {

    public static final Set<IModItem> ALL_ITEMS = new HashSet<>();

    public static void add(IModItem item) {
        ALL_ITEMS.add(item);
        item.getRegistryEntry().setRegistryName(item.getBaseName());
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BlockImpl temp;
        event.getRegistry().registerAll(
                new BlockAncientLog("ancient_log"),
                new BlockAncientLog("ancient_bark"),
                temp = new BlockImpl("ancient_planks", ModBlocks.prop(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2F)),
                new BlockStairsNA("ancient_stairs", temp::getDefaultState, ModBlocks.prop(temp)),
                new Slab("ancient_slab", ModBlocks.prop(temp)),
                new BlockAncientLeaves(),
                new BlockAncientSapling(),
                new BlockNatureAltar(),
                new BlockDecayedLeaves(),
                new BlockGoldenLeaves(),
                new BlockGoldPowder(),
                new BlockWoodStand(),
                temp = new BlockImpl("infused_stone", ModBlocks.prop(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(1.75F)),
                new BlockStairsNA("infused_stairs", temp::getDefaultState, ModBlocks.prop(temp)),
                new Slab("infused_slab", ModBlocks.prop(temp)),
                temp = new BlockImpl("infused_brick", ModBlocks.prop(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(1.5F)),
                new BlockStairsNA("infused_brick_stairs", temp::getDefaultState, ModBlocks.prop(temp)),
                new Slab("infused_brick_slab", ModBlocks.prop(temp)),
                new BlockFurnaceHeater(),
                new BlockPotionGenerator(),
                new BlockAuraDetector(),
                new BlockImpl("conversion_catalyst", ModBlocks.prop(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(2.5F)),
                new BlockImpl("crushing_catalyst", ModBlocks.prop(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(2.5F)),
                new BlockFlowerGenerator(),
                new BlockPlacer(),
                new BlockHopperUpgrade(),
                new BlockFieldCreator(),
                new BlockOakGenerator(),
                new BlockImpl("infused_iron_block", ModBlocks.prop(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3F)),
                new BlockOfferingTable(),
                new BlockPickupStopper(),
                new BlockSpawnLamp(),
                new BlockAnimalGenerator(),
                new BlockEndFlower(),
                new BlockGratedChute(),
                new BlockAnimalSpawner(),
                new BlockAutoCrafter(),
                new BlockImpl("gold_brick", ModBlocks.prop(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(2F)),
                new BlockMossGenerator(),
                new BlockTimeChanger(),
                new BlockGeneratorLimitRemover(),
                new BlockEnderCrate(),
                new BlockPowderPlacer(),
                new BlockFireworkGenerator(),
                new BlockProjectileGenerator(),
                new BlockDimensionRail("overworld", DimensionType.OVERWORLD, DimensionType.THE_NETHER, DimensionType.THE_END),
                new BlockDimensionRail("nether", DimensionType.THE_NETHER, DimensionType.OVERWORLD),
                new BlockDimensionRail("end", DimensionType.THE_END, DimensionType.OVERWORLD)
        );

        if (ModConfig.enabledFeatures.rfConverter)
            event.getRegistry().register(new BlockRFConverter());
        if (ModConfig.enabledFeatures.chunkLoader)
            event.getRegistry().register(new BlockChunkLoader());

        Helper.populateObjectHolders(ModBlocks.class, event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (IModItem block : ALL_ITEMS) {
            if (block instanceof Block) {
                BlockItem item = new BlockItem((Block) block, new Item.Properties().group(NaturesAura.CREATIVE_TAB));
                item.setRegistryName(block.getBaseName());
                event.getRegistry().register(item);
            }
        }

        Item temp;
        event.getRegistry().registerAll(
                new Pickaxe("infused_iron_pickaxe", NAItemTier.INFUSED, 8, 3.2F),
                new Axe("infused_iron_axe", NAItemTier.INFUSED, 8.25F, 3.2F),
                new Shovel("infused_iron_shovel", NAItemTier.INFUSED, 8.25F, 3.2F),
                new Hoe("infused_iron_hoe", NAItemTier.INFUSED, 3.2F),
                new Sword("infused_iron_sword", NAItemTier.INFUSED, 3, 3), // TODO dmg and speed values need to be changed
                new Armor("infused_iron_helmet", NAArmorMaterial.INFUSED, EquipmentSlotType.HEAD),
                new Armor("infused_iron_chest", NAArmorMaterial.INFUSED, EquipmentSlotType.CHEST),
                new Armor("infused_iron_pants", NAArmorMaterial.INFUSED, EquipmentSlotType.LEGS),
                new Armor("infused_iron_shoes", NAArmorMaterial.INFUSED, EquipmentSlotType.FEET),
                new Eye("eye"),
                new Eye("eye_improved"),
                new GoldFiber(),
                new ItemImpl("gold_leaf"),
                new ItemImpl("infused_iron"),
                new ItemImpl("ancient_stick"),
                new ColorChanger(),
                new AuraCache("aura_cache", 400000),
                new AuraCache("aura_trove", 1200000),
                new ShockwaveCreator(),
                new MultiblockMaker(),
                temp = new ItemImpl("bottle_two_the_rebottling"),
                new AuraBottle(temp),
                new ItemImpl("farming_stencil"),
                new ItemImpl("sky_ingot"),
                new Glowing("calling_spirit"),
                new EffectPowder(),
                new BirthSpirit(),
                new MoverMinecart(),
                new RangeVisualizer(),
                new ItemImpl("clock_hand"),
                new ItemImpl("token_joy"),
                new ItemImpl("token_fear"),
                new ItemImpl("token_anger"),
                new ItemImpl("token_sorrow"),
                new ItemImpl("token_euphoria"),
                new ItemImpl("token_terror"),
                new ItemImpl("token_rage"),
                new ItemImpl("token_grief"),
                new EnderAccess(),
                new CaveFinder()
        );
        Helper.populateObjectHolders(ModItems.class, event.getRegistry());
    }

    @SubscribeEvent
    public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event) {
        for (IModItem item : ALL_ITEMS) {
            if (item instanceof ModTileType)
                event.getRegistry().register(((ModTileType) item).type);
        }
        Helper.populateObjectHolders(ModTileEntities.class, event.getRegistry());
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Effect> event) {
        event.getRegistry().registerAll(
                new PotionBreathless()
        );
        Helper.populateObjectHolders(ModPotions.class, event.getRegistry());
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(
                EntityType.Builder.create(EntityMoverMinecart::new, EntityClassification.MISC)
                        .setTrackingRange(64).setUpdateInterval(3).immuneToFire().build(NaturesAura.MOD_ID + ":mover_minecart")
                        .setRegistryName("mover_cart"),
                EntityType.Builder.create(EntityEffectInhibitor::new, EntityClassification.MISC)
                        .setTrackingRange(64).setUpdateInterval(20).immuneToFire().build(NaturesAura.MOD_ID + ":effect_inhibitor")
                        .setRegistryName("effect_inhibitor")
        );
        Helper.populateObjectHolders(ModEntities.class, event.getRegistry());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(new BlockLootProvider(generator));
        generator.addProvider(new BlockTagProvider(generator));
        generator.addProvider(new ItemTagProvider(generator));
    }

    public static void init() {
        for (IModItem item : ALL_ITEMS) {
            if (item instanceof IColorProvidingBlock)
                NaturesAura.proxy.addColorProvidingBlock((IColorProvidingBlock) item);
            if (item instanceof IColorProvidingItem)
                NaturesAura.proxy.addColorProvidingItem((IColorProvidingItem) item);
            if (item instanceof ITESRProvider)
                NaturesAura.proxy.registerTESR((ITESRProvider) item);
        }
    }
}
