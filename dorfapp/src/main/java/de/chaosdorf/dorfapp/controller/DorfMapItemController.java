package de.chaosdorf.dorfapp.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.chaosdorf.dorfapp.model.DorfMapItem;
import de.chaosdorf.dorfapp.model.Group;
import de.chaosdorf.dorfapp.model.Type;

public class DorfMapItemController
{
	public static List<DorfMapItem> parseAllDorfMapItemsFromJSON(String json)
	{
		final HashSet<String> areas = new HashSet<String>();
		final List<DorfMapItem> list = new ArrayList<DorfMapItem>();
		try
		{
			final JSONObject dorfMapItems = new JSONObject(json);
			final JSONArray dorfMapItemsNames = dorfMapItems.names();
			for (int i = 0; i < dorfMapItemsNames.length(); i++)
			{
				final String dorfMapItemname = dorfMapItemsNames.getString(i);
				final DorfMapItem dorfMapItem = parseDorfMapItemFromJSONObject(dorfMapItemname, dorfMapItems.getJSONObject(dorfMapItemname));
				// Add all existing items which are not in group NOTHING and are not an amplifier which ends with "b"
				if (dorfMapItem != null && dorfMapItem.getGroup() != Group.NOTHING && (dorfMapItem.getGroup() != Group.AMPLIFIER || !dorfMapItemname.endsWith("b")))
				{
					list.add(dorfMapItem);
				}
				// Check if we already have an area entry for a light
				final String area = dorfMapItem.getArea();
				if (dorfMapItem.getGroup() == Group.LIGHT && area != null && !areas.contains(area))
				{
					areas.add(dorfMapItem.getArea());
					list.add(new DorfMapItem("", "", area, false, false, "-1", Type.AREA));
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
					jsonObject.getString("desc"),
					jsonObject.getString("area"),
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
