package resonantinduction.core.nei;

import resonantinduction.api.recipe.MachineRecipes.RecipeType;
import calclavia.lib.utility.LanguageUtility;

public class RIMixerRecipeHandler extends RITemplateRecipeHandler
{

	@Override
	public String getRecipeName()
	{
		return LanguageUtility.getLocal("resonantinduction.machine.mixer");
	}

	@Override
	public RecipeType getMachine()
	{
		return RecipeType.MIXER;
	}
}