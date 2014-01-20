package resonantinduction.mechanical;

import resonantinduction.mechanical.fluid.pipe.PartPipe;
import resonantinduction.mechanical.gear.PartGear;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.MultipartGenerator;
import codechicken.multipart.TMultiPart;

public class MultipartMechanical implements IPartFactory
{
	public static MultipartMechanical INSTANCE;

	public static final String[] PART_TYPES = { "resonant_induction_gear", "resonant_induction_pipe" };

	public MultipartMechanical()
	{
		MultiPartRegistry.registerParts(this, PART_TYPES);
		MultipartGenerator.registerPassThroughInterface("resonantinduction.api.fluid.IFluidPipe");
		MultipartGenerator.registerTrait("resonantinduction.mechanical.network.IMechanical", "resonantinduction.mechanical.trait.TraitMechanical");
	}

	@Override
	public TMultiPart createPart(String name, boolean client)
	{
		switch (name)
		{
			case "resonant_induction_gear":
				return new PartGear();
			case "resonant_induction_pipe":
				return new PartPipe();
		}

		return null;
	}
}
