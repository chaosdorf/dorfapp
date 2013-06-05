package de.chaosdorf.dorfapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final String hostname = prefs.getString("hostname", null);

		if (hostname == null)
		{
			// Set hostname if not done yet
			Intent intent = new Intent(this, SetHostname.class);
			startActivity(intent);
			finish();
		}
		else
		{
			// Ready to buy some mate
			Intent intent = new Intent(this, DorfMap.class);
			startActivity(intent);
			finish();
		}
	}
}
