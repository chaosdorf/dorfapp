package de.chaosdorf.dorfapp.model;

public enum BlinkenControlOpMode
{
	STEADY("steady", "steady"),
	BLINK_RGB("blinkrgb", "RGB (hard transition)"),
	BLINK_RANDOM("blinkrand", "random (hard transition)"),
	BLINK_ON_OFF("blinkonoff", "blink on/off"),
	FADE_TO_STEADY("fadetosteady", "fade to new color"),
	FADE_RGB("fadergb", "RGB (fading)"),
	FADE_RANDOM("faderand", "random (fading)"),
	FADE_ON_OFF("fadeonoff", "fade on/off");

	private final String controlName;
	private final String translation;

	BlinkenControlOpMode(final String controlName, final String translation)
	{
		this.controlName = controlName;
		this.translation = translation;
	}

	public static BlinkenControlOpMode fromString(String opModeName)
	{
		if (opModeName != null)
		{
			for (BlinkenControlOpMode blinkenControlOpMode : BlinkenControlOpMode.values())
			{
				if (opModeName.equalsIgnoreCase(blinkenControlOpMode.getControlName()))
				{
					return blinkenControlOpMode;
				}
			}
		}
		return STEADY;
	}

	public String getControlName()
	{
		return controlName;
	}

	public String getTranslation()
	{
		return translation;
	}
}
