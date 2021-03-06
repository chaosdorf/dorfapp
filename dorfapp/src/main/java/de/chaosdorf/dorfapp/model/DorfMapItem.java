package de.chaosdorf.dorfapp.model;

public class DorfMapItem
{
	final String id;
	final String desc;
	final String area;
	final boolean isReadable;
	final boolean isWriteable;
	String status;
	final Type type;
	boolean pending;

	public DorfMapItem(final String id, final String desc, final String area, final boolean readable, final boolean writeable, final String status, final Type type)
	{
		this.id = id;
		this.desc = desc;
		this.area = area;
		this.isReadable = readable;
		this.isWriteable = writeable;
		this.status = status;
		this.type = type;
		this.pending = false;
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		if (type == Type.AREA)
		{
			return area;
		}
		if (desc != null && desc.length() > 0)
		{
			return desc;
		}
		return id;
	}

	public String getArea()
	{
		return area;
	}

	public boolean isWriteable()
	{
		return isWriteable;
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

	public boolean isPending()
	{
		return pending;
	}

	public void setPending(boolean pending)
	{
		this.pending = pending;
	}
}
