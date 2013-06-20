package de.chaosdorf.dorfapp.model;

public enum Type
{
	UNKOWN(null, "", Group.NOTHING),
	LIGHT("light", "light", Group.LIGHT),
	LIGHT_AUTOMATIC("light_au", "light_auto", Group.LIGHT),
	LIGHT_READONLY("light_ro", "light", Group.LIGHT),
	SERVER("server", "server", Group.SERVER),
	AMPLIFIER("amp", "amp", Group.AMPLIFIER),
	BLINKENLIGHT("blinkenlight", "blinkenlight", Group.BLINKENLIGHT),
	INFOAREA("infoarea", "", Group.NOTHING),
	PRINTER("printer", "printer", Group.PRINTER),
	TEXT("rtext", "", Group.NOTHING),
	WIFI("wifi", "wifi", Group.WIFI),
	DOOR("door", "door", Group.DOOR),
	PHONE("phone", "phone", Group.PHONE),
	AREA("area", "area", Group.LIGHT);

	final String type;
	final String iconName;
	final Group group;

	private Type(final String type, final String iconName, final Group group)
	{
		this.type = type;
		this.iconName = iconName;
		this.group = group;
	}

	public static Type fromString(String typeName)
	{
		if (typeName != null)
		{
			for (Type type : Type.values())
			{
				if (typeName.equalsIgnoreCase(type.getType()))
				{
					return type;
				}
			}
		}
		return UNKOWN;
	}

	public String getType()
	{
		return type;
	}

	public Group getGroup()
	{
		return group;
	}

	public String getIconName(final String status)
	{
		if (this == BLINKENLIGHT || this == AREA)
		{
			return iconName;
		}
		return iconName + (status.equals("1") ? "_on" : "_off");
	}
}
