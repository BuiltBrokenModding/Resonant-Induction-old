package resonantinduction.wire.multipart;

import java.util.List;

import resonantinduction.ResonantInduction;
import resonantinduction.TabRI;
import resonantinduction.wire.EnumWireMaterial;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPartWire extends JItemMultiPart
{
	public ItemPartWire(int id)
	{
		super(ResonantInduction.CONFIGURATION.get(Configuration.CATEGORY_ITEM, "wireMultipart", id).getInt(id));
		this.setUnlocalizedName(ResonantInduction.PREFIX + "multiwire");
		this.setCreativeTab(TabRI.INSTANCE);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public TMultiPart newPart(ItemStack arg0, EntityPlayer arg1, World arg2, BlockCoord arg3, int arg4, Vector3 arg5)
	{
		return new PartWire(this.getDamage(arg0));
	}
	
	private Icon[] icons = new Icon[EnumWireMaterial.values().length];

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return this.getUnlocalizedName() + "." + EnumWireMaterial.values()[itemStack.getItemDamage()].name().toLowerCase();
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List par3List, boolean par4)
	{
		par3List.add("Resistance: " + ElectricityDisplay.getDisplay(EnumWireMaterial.values()[itemstack.getItemDamage()].resistance, ElectricUnit.RESISTANCE));
		par3List.add("Max Amperage: " + ElectricityDisplay.getDisplay(EnumWireMaterial.values()[itemstack.getItemDamage()].maxAmps, ElectricUnit.AMPERE));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		for (int i = 0; i < EnumWireMaterial.values().length; i++)
		{
			this.icons[i] = iconRegister.registerIcon(this.getUnlocalizedName(new ItemStack(this.itemID, 1, i)).replaceAll("item.", ""));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int meta)
	{
		return this.icons[meta];
	}

    @Override
    public void getSubItems(int itemID, CreativeTabs tab, List listToAddTo) {
        for (EnumWireMaterial mat : EnumWireMaterial.values()) {
            listToAddTo.add(new ItemStack(itemID, 1, mat.ordinal()));
        }
    }

}
