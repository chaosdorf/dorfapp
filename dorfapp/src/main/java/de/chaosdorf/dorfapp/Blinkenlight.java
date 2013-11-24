package de.chaosdorf.dorfapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import de.chaosdorf.dorfapp.colorpicker.ColorPickerView;
import de.chaosdorf.dorfapp.controller.BlinkenControlController;
import de.chaosdorf.dorfapp.longrunningio.LongRunningDorfMapItemIOTask;
import de.chaosdorf.dorfapp.longrunningio.LongRunningIOCallback;
import de.chaosdorf.dorfapp.longrunningio.LongRunningIOGet;
import de.chaosdorf.dorfapp.longrunningio.LongRunningIOTask;
import de.chaosdorf.dorfapp.model.BlinkenControl;
import de.chaosdorf.dorfapp.util.ColorPickerDialog;
import de.chaosdorf.dorfapp.util.Utility;

public class Blinkenlight extends Activity implements LongRunningIOCallback, ColorPickerDialog.OnColorChangedListener
{
	private Activity activity;
	private String hostname;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_blinkenlight);

		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		hostname = prefs.getString("hostname", null);

		new LongRunningIOGet(this, new LongRunningDorfMapItemIOTask(LongRunningIOTask.GET_LOUNGE_RGB, null), hostname + "blinkencontrol/lounge_rgb.json").execute();
	}

	@Override
	public void displayErrorMessage(String message)
	{
		Utility.displayToastMessage(activity, message);
	}

	@Override
	public void processIOResult(final LongRunningDorfMapItemIOTask task, final String json)
	{
		if (json != null)
		{
			switch (task.getLongRunningIOTask())
			{
				// Parse rgb data
				case GET_LOUNGE_RGB:
					BlinkenControl blinkenControl = BlinkenControlController.parseDorfMapItemFromJSONObject(json);
					int color = Color.rgb(blinkenControl.getRed(), blinkenControl.getGreen(), blinkenControl.getBlue());
					//ColorPickerDialog dialog = new ColorPickerDialog(activity, this, "foobar", color, Color.WHITE);
					//dialog.show();
					break;
			}
		}
	}

	@Override
	public void colorChanged(String key, int color)
	{
		Utility.displayToastMessage(activity, key + " R:" + Color.red(color) + " G:" + Color.green(color) + " B:" + Color.blue(color));
		final String param = "?red=" + Color.red(color) + "&green=" + Color.green(color) + "&blue=" + Color.blue(color) + "&speed=16&opmode=steady";
		new LongRunningIOGet(this, new LongRunningDorfMapItemIOTask(LongRunningIOTask.SET_LOUNGE_RGB, null), hostname + "blinkencontrol/lounge_rgb" + param).execute();
	}
}
