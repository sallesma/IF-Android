package com.imaginariumfestival.android.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.imaginariumfestival.android.map.MapItemModel;

public class MapItemsDataSource {
	private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_LABEL,
			MySQLiteHelper.COLUMN_X,
			MySQLiteHelper.COLUMN_Y,
			MySQLiteHelper.COLUMN_INFO_ID };
 
    public MapItemsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }
 
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
 
    public void close() {
        dbHelper.close();
    }
    
	public MapItemModel insertMapItems(long id, String label, int x, int y, int infoId) {
 
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, id);
		values.put(MySQLiteHelper.COLUMN_LABEL, label);
		values.put(MySQLiteHelper.COLUMN_X, x);
		values.put(MySQLiteHelper.COLUMN_Y, y);
		values.put(MySQLiteHelper.COLUMN_INFO_ID, infoId);

		long insertId = database.insertOrThrow(MySQLiteHelper.TABLE_MAP_ITEMS,
				null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_MAP_ITEMS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		MapItemModel newMapItem = cursorToMapItem(cursor);
		cursor.close();
		return newMapItem;
    }
	
	public MapItemModel updateMapItem(Long id, MapItemModel mapItem){
        ContentValues values = new ContentValues();
 
        values.put(MySQLiteHelper.COLUMN_LABEL, mapItem.getLabel());
        values.put(MySQLiteHelper.COLUMN_X, mapItem.getX());
        values.put(MySQLiteHelper.COLUMN_Y, mapItem.getY());
        values.put(MySQLiteHelper.COLUMN_INFO_ID, mapItem.getInfoId());
 
        database.update(MySQLiteHelper.TABLE_MAP_ITEMS, values, MySQLiteHelper.COLUMN_ID + " = " +mapItem.getId(), null);
 
        return getMapItemFromId(mapItem.getId());
    }
	
	public MapItemModel getMapItemFromId(long id) {
		Cursor c = database.query(MySQLiteHelper.TABLE_MAP_ITEMS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = \"" + id + "\"", null, null,
				null, null);
		c.moveToFirst();
		MapItemModel mapItem = cursorToMapItem(c);
		c.close();
		return mapItem;
	}
	
	public MapItemModel getMapItemFromTitle(String name) {
		Cursor c = database.query(MySQLiteHelper.TABLE_MAP_ITEMS, allColumns,
				MySQLiteHelper.COLUMN_TITLE + " = \"" + name + "\"", null, null,
				null, null);
		c.moveToFirst();
		MapItemModel mapItem = cursorToMapItem(c);
		c.close();
		return mapItem;
	}

	public List<MapItemModel> getAllMapItems() {
		List<MapItemModel> allMapItems = new ArrayList<MapItemModel>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_MAP_ITEMS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MapItemModel mapItem = cursorToMapItem(cursor);
			allMapItems.add(mapItem);
			cursor.moveToNext();
		}

		cursor.close();
		return allMapItems;
	}
	
	public void deleteMapItem(MapItemModel mapItem) {
        long id = mapItem.getId();
        database.delete(MySQLiteHelper.TABLE_MAP_ITEMS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

	public void deleteAllMapItems() {
		database.delete(MySQLiteHelper.TABLE_MAP_ITEMS, null, null);
	}
	
	private MapItemModel cursorToMapItem(Cursor cursor) {
		MapItemModel mapItem = new MapItemModel();
        mapItem.setId(cursor.getInt(0));
        mapItem.setLabel(cursor.getString(1));
        mapItem.setX(cursor.getInt(2));
        mapItem.setY(cursor.getInt(3));
        mapItem.setInfoId(cursor.getInt(4));

        return mapItem;
    }
}