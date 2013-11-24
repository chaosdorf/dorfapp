package de.chaosdorf.dorfapp.controller;

import org.json.JSONException;
import org.json.JSONObject;

import de.chaosdorf.dorfapp.model.BlinkenControl;
import de.chaosdorf.dorfapp.model.BlinkenControlOpMode;

public class BlinkenControlController
{
	public static BlinkenControl parseDorfMapItemFromJSONObject(final String json)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(json);
			return new BlinkenControl(
					jsonObject.getInt("red"),
					jsonObject.getInt("green"),
					jsonObject.getInt("blue"),
					jsonObject.getInt("speed"),
					BlinkenControlOpMode.fromString(jsonObject.getString("opmode"))
			);
		}
		catch (JSONException ignored)
		{
			return null;
		}
	}
}
