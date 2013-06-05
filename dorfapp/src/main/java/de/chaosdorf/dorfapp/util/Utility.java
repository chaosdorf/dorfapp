package de.chaosdorf.dorfapp.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Utility
{
	public static void displayToastMessage(final Activity activity, final CharSequence message)
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
			}
		});
	}

	public static void resetHostname(Activity activity)
	{
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		prefs.edit().remove("hostname").apply();
	}
}
