package de.chaosdorf.dorfapp.model;

public class DorfMapItem
{
	final String name;
	final boolean isReadable;
	final boolean isWriteable;
	String status;
	final Type type;

	public DorfMapItem(final String name, final boolean readable, final boolean writeable, final String status, final Type type)
	{
		this.name = name;
		this.isReadable = readable;
		this.isWriteable = writeable;
		this.status = status;
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public boolean isReadable()
	{
		return isReadable;
	}

	public boolean isWriteable()
	{
		return isWriteable;
	}

	public String getStatus()
	{
		return status;
	}

	public Type getType()
	{
		return type;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getIconName()
	{
		return type.getIconName(status);
	}

	public Group getGroup()
	{
		return type.getGroup();
	}
}
