package de.chaosdorf.dorfapp.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.chaosdorf.dorfapp.model.DorfMapItem;
import de.chaosdorf.dorfapp.model.Group;
import de.chaosdorf.dorfapp.model.Type;

public class DorfMapItemController
{
	public static List<DorfMapItem> parseAllDorfMapItemsFromJSON(String json)
	{
		final List<DorfMapItem> list = new ArrayList<DorfMapItem>();
		try
		{
			final JSONObject dorfMapItems = new JSONObject(json);
			final JSONArray dorfMapItemsNames = dorfMapItems.names();
			for (int i = 0; i < dorfMapItemsNames.length(); i++)
			{
				final String dorfMapItemname = dorfMapItemsNames.getString(i);
				final DorfMapItem dorfMapItem = parseDorfMapItemFromJSONObject(dorfMapItemname, dorfMapItems.getJSONObject(dorfMapItemname));
				if (dorfMapItem != null && dorfMapItem.getGroup() != Group.NOTHING)
				{
					list.add(dorfMapItem);
				}
			}
			return list;
		}
		catch (JSONException ignored)
		{
			return null;
		}
	}

	private static DorfMapItem parseDorfMapItemFromJSONObject(final String dorfMapItemname, final JSONObject jsonObject)
	{
		try
		{
			return new DorfMapItem(
					dorfMapItemname,
					jsonObject.getInt("is_readable") > 0,
					jsonObject.getInt("is_writable") > 0,
					jsonObject.getString("status"),
					Type.fromString(jsonObject.getString("type"))
			);
		}
		catch (JSONException ignored)
		{
			return null;
		}
	}
}
