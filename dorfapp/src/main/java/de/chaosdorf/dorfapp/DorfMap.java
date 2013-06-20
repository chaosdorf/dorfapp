package de.chaosdorf.dorfapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.chaosdorf.dorfapp.controller.DorfMapItemController;
import de.chaosdorf.dorfapp.longrunningio.LongRunningDorfMapItemIOTask;
import de.chaosdorf.dorfapp.longrunningio.LongRunningIOCallback;
import de.chaosdorf.dorfapp.longrunningio.LongRunningIOGet;
import de.chaosdorf.dorfapp.longrunningio.LongRunningIOTask;
import de.chaosdorf.dorfapp.model.DorfMapItem;
import de.chaosdorf.dorfapp.model.Group;
import de.chaosdorf.dorfapp.util.Utility;

public class DorfMap extends Activity implements LongRunningIOCallback, AdapterView.OnItemClickListener
{
	private final Set<DorfMapItem> pendingDorfMapItems = Collections.synchronizedSet(new HashSet<DorfMapItem>());

	private Activity activity;
	private String hostname;

	private ListView listView;
	private DorfMapItemAdapter dorfMapItemAdapter;

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_dorfmap);

		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		hostname = prefs.getString("hostname", null);

		new LongRunningIOGet(this, new LongRunningDorfMapItemIOTask(LongRunningIOTask.GET_LIST_ALL, null), hostname + "list/all.json").execute();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		getMenuInflater().inflate(R.menu.dorfmap, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.reset_hostname:
				Utility.resetHostname(activity);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
		return true;
	}

	@Override
	public void onDestroy()
	{
		if (listView != null)
		{
			listView.setAdapter(null);
		}
		super.onDestroy();
	}

	@Override
	public void displayErrorMessage(final String message)
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
				// Parse user data
				case GET_LIST_ALL:
					final List<DorfMapItem> dorfMapItems = DorfMapItemController.parseAllDorfMapItemsFromJSON(json);
					Collections.sort(dorfMapItems, new DorfMapItemComparator());

					dorfMapItemAdapter = new DorfMapItemAdapter(dorfMapItems);

					listView = (ListView) findViewById(R.id.list_view);
					listView.setAdapter(dorfMapItemAdapter);
					listView.setOnItemClickListener(this);
					break;

				// Update toggled dorfMapItem
				case DORFMAP_TOGGLE:
					new LongRunningIOGet(this, new LongRunningDorfMapItemIOTask(LongRunningIOTask.DORFMAP_UPDATE_UI, task.getDorfMapItem()), hostname + "get/" + task.getDorfMapItem().getId()).execute();
					break;

				// Update icon of toggled dorfMapItem
				case DORFMAP_UPDATE_UI:
					task.getDorfMapItem().setPending(false);
					task.getDorfMapItem().setStatus(json);
					dorfMapItemAdapter.notifyDataSetChanged();
					pendingDorfMapItems.remove(task.getDorfMapItem());
					break;
			}
		}
	}

	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int index, final long l)
	{
		DorfMapItem dorfMapItem = null;
		boolean showErrorMessage = (index < 0);
		if (!showErrorMessage)
		{
			dorfMapItem = (DorfMapItem) listView.getAdapter().getItem(index);
			if (pendingDorfMapItems.contains(dorfMapItem))
			{
				showErrorMessage = true;
			}
		}
		if (showErrorMessage)
		{
			Utility.displayToastMessage(activity, getResources().getString(R.string.dorfmap_pending));
			return;
		}
		if (dorfMapItem != null)
		{
			// Read-only dorfMapItem
			if (!dorfMapItem.isWriteable())
			{
				Utility.displayToastMessage(activity, getResources().getString(R.string.dorfmap_read_only_device).replace("%s", dorfMapItem.getName()));
				return;
			}
			// Special activity for blinkenlight
			if (dorfMapItem.getGroup() == Group.BLINKENLIGHT)
			{
				Intent intent = new Intent(view.getContext(), Blinkenlight.class);
				startActivity(intent);
				finish();
				return;
			}
			// Normal writable dorfMapItem
			if (pendingDorfMapItems.add(dorfMapItem))
			{
				dorfMapItem.setPending(true);
				dorfMapItemAdapter.notifyDataSetChanged();
				new LongRunningIOGet(this, new LongRunningDorfMapItemIOTask(LongRunningIOTask.DORFMAP_TOGGLE, dorfMapItem), hostname + "toggle/" + dorfMapItem.getId()).execute();
			}
		}
	}

	private class DorfMapItemAdapter extends ArrayAdapter<DorfMapItem>
	{
		private final List<DorfMapItem> dorfMapItemList;
		private final LayoutInflater inflater;

		DorfMapItemAdapter(final List<DorfMapItem> dorfMapItemList)
		{
			super(activity, R.layout.activity_dorfmap, dorfMapItemList);
			this.dorfMapItemList = dorfMapItemList;
			this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(final int position, final View convertView, final ViewGroup parent)
		{
			View view = convertView;
			if (view == null)
			{
				view = inflater.inflate(R.layout.activity_dorfmap_item, parent, false);
			}
			if (view == null)
			{
				return null;
			}

			final DorfMapItem dorfMapItem = dorfMapItemList.get(position);
			final ImageView icon = (ImageView) view.findViewById(R.id.icon);
			final TextView label = (TextView) view.findViewById(R.id.label);
			int dorfMapItemIconID = getResources().getIdentifier(dorfMapItem.getIconName(), "drawable", getPackageName());
			if (dorfMapItem.isPending())
			{
				dorfMapItemIconID = android.R.drawable.stat_notify_sync;
			}
			else if (dorfMapItemIconID == 0)
			{
				dorfMapItemIconID = android.R.drawable.ic_menu_search;
			}

			icon.setContentDescription(dorfMapItem.getName());
			icon.setImageResource(dorfMapItemIconID);
			label.setText(dorfMapItem.getName());

			return view;
		}
	}

	private class DorfMapItemComparator implements Comparator<DorfMapItem>
	{
		@Override
		public int compare(DorfMapItem dorfMapItem, DorfMapItem dorfMapItem2)
		{
			int compare = dorfMapItem.getGroup().compareTo(dorfMapItem2.getGroup());
			if (compare != 0)
			{
				return compare;
			}
			if (dorfMapItem.getArea() != null)
			{
				compare = dorfMapItem.getArea().compareToIgnoreCase(dorfMapItem2.getArea());
				if (compare != 0)
				{
					return compare;
				}
			}
			return dorfMapItem.getId().compareToIgnoreCase(dorfMapItem2.getId());
		}
	}
}
