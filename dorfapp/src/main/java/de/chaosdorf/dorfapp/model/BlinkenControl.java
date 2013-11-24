package de.chaosdorf.dorfapp.model;

public class BlinkenControl
{
	private final int red;
	private final int green;
	private final int blue;

	private final int speed;

	private final BlinkenControlOpMode opmode;

	public BlinkenControl(final int red, final int green, final int blue, final int speed, final BlinkenControlOpMode opmode)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.speed = speed;
		this.opmode = opmode;
	}

	public int getRed()
	{
		return red;
	}

	public int getGreen()
	{
		return green;
	}

	public int getBlue()
	{
		return blue;
	}

	public int getSpeed()
	{
		return speed;
	}

	public BlinkenControlOpMode getOpmode()
	{
		return opmode;
	}
}
