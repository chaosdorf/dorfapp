package de.chaosdorf.dorfapp.longrunningio;

import de.chaosdorf.dorfapp.model.DorfMapItem;

public class LongRunningDorfMapItemIOTask
{
	final LongRunningIOTask longRunningIOTask;
	final DorfMapItem dorfMapItem;

	public LongRunningDorfMapItemIOTask(LongRunningIOTask longRunningIOTask, DorfMapItem dorfMapItem)
	{
		this.longRunningIOTask = longRunningIOTask;
		this.dorfMapItem = dorfMapItem;
	}

	public LongRunningIOTask getLongRunningIOTask()
	{
		return longRunningIOTask;
	}

	public DorfMapItem getDorfMapItem()
	{
		return dorfMapItem;
	}
}
