package de.chaosdorf.dorfapp.longrunningio;

public interface LongRunningIOCallback
{
	public void displayErrorMessage(final String message);

	public void processIOResult(final LongRunningDorfMapItemIOTask task, final String json);
}
