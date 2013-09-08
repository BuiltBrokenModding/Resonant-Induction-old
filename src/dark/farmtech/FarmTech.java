package dark.farmtech;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import universalelectricity.prefab.TranslationHelper;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import dark.api.farm.CropAutomationHandler;
import dark.api.farm.DecayMatterList;
import dark.core.common.BlockRegistry;
import dark.core.common.BlockRegistry.BlockData;
import dark.core.common.DarkMain;
import dark.core.prefab.ModPrefab;
import dark.core.prefab.items.ItemBlockHolder;
import dark.farmtech.blocks.BlockFarmSoil;

@Mod(modid = FarmTech.MOD_ID, name = FarmTech.MOD_NAME, version = DarkMain.VERSION, dependencies = "after:DarkCore", useMetadata = true)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class FarmTech extends ModPrefab
{

    public static final String MOD_ID = "FarmTech";
    public static final String MOD_NAME = "Farm Tech";

    @Metadata(FarmTech.MOD_ID)
    public static ModMetadata meta;

    /* SUPPORTED LANGS */
    private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US" };

    /* CONFIG FILE */
    public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir() + "/Dark/", MOD_NAME + ".cfg"));

    /* BLOCKS */
    public static Block blockFarmSoil;

    @SidedProxy(clientSide = "dark.farmtech.client.ClientProxy", serverSide = "dark.farmtech.CommonProxy")
    public static CommonProxy proxy;

    @Instance(FarmTech.MOD_NAME)
    public static FarmTech instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        super.preInit(event);
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        proxy.init();

        /* LANG LOADING */
        FMLLog.info("[FarmTech] Loaded: " + TranslationHelper.loadLanguages(LANGUAGE_PATH, LANGUAGES_SUPPORTED) + " Languages.");

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        proxy.postInit();
        DecayMatterList.triggerPostBlockAddition();
        CropAutomationHandler.triggerPostBlockAddition();
    }

    @Override
    public String getDomain()
    {
        return "ft";
    }

    @Override
    public List<BlockData> getBlocks()
    {
        List<BlockData> dataList = new ArrayList<BlockData>();
        CONFIGURATION.load();

        blockFarmSoil = new BlockFarmSoil(this.getNextID());
        BlockRegistry.addBlockToRegister(new BlockData(blockFarmSoil, ItemBlockHolder.class, "FTFarmSoil"));

        String compostList = CONFIGURATION.get("DecayMatter", "List", "5::8000:1", "Items or blocks beyond the built in ones that can be turned into compost. Entries go BlockID:Meta:Time:Amount").getString();
        DecayMatterList.parseConfigString(compostList);

        CONFIGURATION.save();
        return dataList;
    }

    @Override
    public void loadModMeta()
    {
        meta.modId = MOD_ID;
        meta.name = MOD_NAME;
        meta.description = "Farming addon for Darks Core Machine";
        meta.url = "www.BuiltBroken.com";

        meta.logoFile = TEXTURE_DIRECTORY + "GP_Banner.png";
        meta.version = DarkMain.VERSION;
        meta.authorList = Arrays.asList(new String[] { "DarkGuardsman", "LiQuiD" });
        meta.credits = "Please see the website.";
        meta.autogenerated = false;

    }

    public static final CreativeTabs TabFarmTech = new CreativeTabs("FarmTech")
    {
        public ItemStack getIconItemStack()
        {
            return new ItemStack(Item.wheat.itemID, 1, 0);
        }
    };

}
