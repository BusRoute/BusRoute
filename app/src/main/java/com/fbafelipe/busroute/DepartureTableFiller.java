package com.fbafelipe.busroute;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fbafelipe.busroute.busroute.Calendar;
import com.fbafelipe.busroute.busroute.Departure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe on 5/31/15.
 */
public class DepartureTableFiller {
	private Context mContext;
	private TableLayout mDeparturesTable;
	
	public DepartureTableFiller(Context context, TableLayout departuresTable) {
		mContext = context;
		mDeparturesTable = departuresTable;
	}
	
	public void populateTable(List<Departure> departures) {
		mDeparturesTable.removeAllViews();

		List<List<Departure> > departureTable = organizeDepartureTable(departures);
		createTableHeader();
		createTableContent(departureTable);
	}

	private List<List<Departure> > organizeDepartureTable(List<Departure> departures) {
		List<List<Departure> > departureTable = new ArrayList<>();
		for (Calendar calendar : Calendar.values())
			departureTable.add(new ArrayList<Departure>());

		for (Departure departure : departures) {
			int index = departure.getCalendar().ordinal();
			departureTable.get(index).add(departure);
		}

		return departureTable;
	}

	private void createTableHeader() {
		TableRow tableRow = createTableRow(mContext);
		for (Calendar calendar : Calendar.values()) {
			TextView textView = new TextView(mContext);
			textView.setText(calendar.getName(mContext));
			addViewToTableRow(tableRow, textView);
		}
		mDeparturesTable.addView(tableRow);
	}

	private void createTableContent(List<List<Departure>> departureTable) {
		int maxSize = 0;
		for (List<Departure> departureList : departureTable)
			maxSize = Math.max(maxSize, departureList.size());

		for (int i = 0; i < maxSize; ++i) {
			TableRow tableRow = createTableRow(mContext);

			for (List<Departure> departureList : departureTable) {
				if (i < departureList.size()) {
					Departure departure = departureList.get(i);
					
					TextView textView = new TextView(mContext);
					textView.setText(departure.getTime());
					
					addViewToTableRow(tableRow, textView);
				}
				else {
					// empty entry
					addViewToTableRow(tableRow, new View(mContext));
				}
			}

			mDeparturesTable.addView(tableRow);
		}
	}
	
	private void addViewToTableRow(TableRow tableRow, View view) {
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.weight = 1;
		layoutParams.gravity = Gravity.CENTER;
		view.setLayoutParams(layoutParams);
		
		tableRow.addView(view);
	}

	private TableRow createTableRow(Context context) {
		TableRow tableRow = new TableRow(context);
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		tableRow.setLayoutParams(layoutParams);
		return tableRow;
	}
}
