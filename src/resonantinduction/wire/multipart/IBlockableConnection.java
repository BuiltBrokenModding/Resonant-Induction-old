package resonantinduction.wire.multipart;

import net.minecraftforge.common.ForgeDirection;

public interface IBlockableConnection
{
	public boolean isBlockedOnSide(ForgeDirection side);
}
