package com.imaginariumfestival.android.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.imaginariumfestival.android.photos.FilterModel;

public class FiltersDataSource {
	// Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_PICTURE };
 
    public FiltersDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }
 
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
 
    public void close() {
        dbHelper.close();
    }
    
	public FilterModel insertFilter(long id, String picture) {

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, id);
		values.put(MySQLiteHelper.COLUMN_PICTURE, picture);
		long insertId = database.insertOrThrow(MySQLiteHelper.TABLE_FILTERS,
				null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_FILTERS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		FilterModel newFilter = cursorToFilter(cursor);
		cursor.close();
		return newFilter;
    }
	
	public FilterModel updateInfo(Long id, FilterModel filter){
        ContentValues values = new ContentValues();
        
        values.put(MySQLiteHelper.COLUMN_PICTURE, filter.getPicture());
 
        database.update(MySQLiteHelper.TABLE_FILTERS, values, MySQLiteHelper.COLUMN_ID + " = " +filter.getId(), null);
 
        return getFilterFromId(filter.getId());
    }
	
	public FilterModel getFilterFromId(long id) {
		Cursor c = database.query(MySQLiteHelper.TABLE_FILTERS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = \"" + id + "\"", null, null,
				null, null);
		c.moveToFirst();
		FilterModel filter = cursorToFilter(c);
		c.close();
		return filter;
	}
	
	public List<FilterModel> getAllFilters() {
		List<FilterModel> filters = new ArrayList<FilterModel>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_FILTERS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FilterModel filter = cursorToFilter(cursor);
			filters.add(filter);
			cursor.moveToNext();
		}

		cursor.close();
		return filters;
	}
	
	public Map<String, String> getAllFilterPictures() {
		Map<String, String> picturesUrl = new HashMap<String, String>();
		
		String[] columns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_PICTURE};
		Cursor cursor = database.query(MySQLiteHelper.TABLE_FILTERS,
				columns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Long id = cursor.getLong(0);
			String picture = cursor.getString(1);
			picturesUrl.put(String.valueOf(id), picture);
			cursor.moveToNext();
		}

		cursor.close();
		return picturesUrl;
	}
	
	public void deleteFilter(FilterModel filter) {
        long id = filter.getId();
        database.delete(MySQLiteHelper.TABLE_FILTERS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }
	
	public void deleteAllFilters() {
		database.delete(MySQLiteHelper.TABLE_FILTERS, null, null);
	}
	
	private FilterModel cursorToFilter(Cursor cursor) {
        FilterModel filter = new FilterModel();
        if  ( cursor.getCount() > 0 ) {
        	filter.setId(cursor.getInt(0));
	        filter.setPicture(cursor.getString(1));
	        return filter;
        } else {
        	return null;
        }
    }
}