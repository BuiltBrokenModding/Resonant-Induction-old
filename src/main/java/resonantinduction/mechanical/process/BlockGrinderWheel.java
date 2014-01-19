package resonantinduction.mechanical.process;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import resonantinduction.core.Settings;
import resonantinduction.core.prefab.block.BlockRIRotatable;
import resonantinduction.core.render.RIBlockRenderingHandler;
import universalelectricity.api.vector.VectorWorld;

/**
 * A block used to build machines.
 * 
 * @author Calclavia
 * 
 */
public class BlockGrinderWheel extends BlockRIRotatable implements ITileEntityProvider
{
	public BlockGrinderWheel()
	{
		super("grindingWheel", Settings.getNextBlockID());
		this.setBlockBounds(0.05f, 0.05f, 0.05f, 0.95f, 0.95f, 0.95f);
		rotationMask = 0b111111;
	}

	@Override
	public int getRenderType()
	{
		return RIBlockRenderingHandler.ID;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		this.checkConflicts(world, x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5)
	{
		this.checkConflicts(world, x, y, z);
	}

	/**
	 * Checks for any conflicting directions with other grinders.
	 */
	private void checkConflicts(World world, int x, int y, int z)
	{
		ForgeDirection facing = this.getDirection(world, x, y, z);
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if (dir == facing || dir == facing.getOpposite())
			{
				VectorWorld checkPos = (VectorWorld) new VectorWorld(world, x, y, z).modifyPositionFromSide(dir);
				TileEntity tileEntity = checkPos.getTileEntity();

				if (tileEntity instanceof TileGrinderWheel)
				{
					if (this.getDirection(world, checkPos.intX(), checkPos.intY(), checkPos.intZ()) == facing)
					{
						this.dropBlockAsItem(world, x, y, z, 0, 0);
						world.setBlockToAir(x, y, z);
					}
				}
			}
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileGrinderWheel)
		{
			TileGrinderWheel tile = (TileGrinderWheel) tileEntity;

			if (tile.canWork())
			{
				if (entity instanceof EntityItem)
				{
					if (tile.canGrind(((EntityItem) entity).getEntityItem()))
					{
						if (tile.grindingItem == null)
						{
							tile.grindingItem = (EntityItem) entity;
						}

						if (!TileGrinderWheel.getTimer().containsKey(entity))
						{
							TileGrinderWheel.getTimer().put((EntityItem) entity, TileGrinderWheel.DEFAULT_TIME);
						}
					}
					else
					{
						entity.setPosition(entity.posX, entity.posY - 1.2, entity.posZ);
					}
				}
				else
				{
					entity.attackEntityFrom(DamageSource.cactus, 2);
				}

			}

			if (tile.getNetwork().getAngularVelocity() > 0)
			{
				// Move entity based on the direction of the block.
				ForgeDirection dir = this.getDirection(world, x, y, z);
				entity.motionX += dir.offsetX * tile.getNetwork().getAngularVelocity() / 20;
				entity.motionZ += dir.offsetZ * tile.getNetwork().getAngularVelocity() / 20;
				entity.motionY += Math.random() * tile.getNetwork().getAngularVelocity() / 20;
				entity.isAirBorne = true;
			}
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileGrinderWheel();
	}
}
