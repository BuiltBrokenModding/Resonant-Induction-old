package resonantinduction.mechanical.gear;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import resonantinduction.mechanical.Mechanical;
import resonantinduction.mechanical.network.IMechanical;
import resonantinduction.mechanical.network.PartMechanical;
import codechicken.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * We assume all the force acting on the gear is 90 degrees.
 * 
 * @author Calclavia
 * 
 */
public class PartGear extends PartMechanical implements IMechanical
{
	private int manualCrankTime = 0;

	@Override
	public void update()
	{
		if (!this.world().isRemote)
		{
			if (manualCrankTime > 0)
			{

				manualCrankTime--;
			}
		}

		super.update();
	}

	@Override
	public float getResistance()
	{
		return 0.5f;
	}

	@Override
	public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack item)
	{
		System.out.println(world().isRemote + ": " + getNetwork());
		if (player.isSneaking())
		{
			getNetwork().onReceiveEnergy(this, 20, 0.3f);
			this.manualCrankTime = 20;
		}

		return false;
	}

	@Override
	protected ItemStack getItem()
	{
		return new ItemStack(Mechanical.itemGear);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderDynamic(Vector3 pos, float frame, int pass)
	{
		if (pass == 0)
		{
			RenderGear.INSTANCE.renderDynamic(this, pos.x, pos.y, pos.z, frame);
		}
	}

	@Override
	public boolean isRotationInversed()
	{
		return true;
	}

	@Override
	public String getType()
	{
		return "resonant_induction_gear";
	}
}