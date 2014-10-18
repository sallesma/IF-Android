package com.imaginariumfestival.android.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.imaginariumfestival.android.infos.InfoModel;

public class InfosDataSource {
	// Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, 
			MySQLiteHelper.COLUMN_PICTURE,
			MySQLiteHelper.COLUMN_IS_CATEGORY, 
			MySQLiteHelper.COLUMN_CONTENT,
			MySQLiteHelper.COLUMN_PARENT_ID };
 
    public InfosDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }
 
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
 
    public void close() {
        dbHelper.close();
    }
    
	public InfoModel insertInfo(long id, String name, String picture,
			String isCategory, String content, long parentId) {

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, id);
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_PICTURE, picture);
		values.put(MySQLiteHelper.COLUMN_IS_CATEGORY, isCategory);
		values.put(MySQLiteHelper.COLUMN_CONTENT, content);
		values.put(MySQLiteHelper.COLUMN_PARENT_ID, parentId);
		long insertId = database.insertOrThrow(MySQLiteHelper.TABLE_INFOS,
				null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_INFOS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		InfoModel newInfo = cursorToInfos(cursor);
		cursor.close();
		return newInfo;
    }
	
	public InfoModel updateInfo(Long id, InfoModel info){
        ContentValues values = new ContentValues();
        
        values.put(MySQLiteHelper.COLUMN_NAME, info.getName());
        values.put(MySQLiteHelper.COLUMN_PICTURE, info.getPicture());
        values.put(MySQLiteHelper.COLUMN_IS_CATEGORY, info.getIsCategory());
        values.put(MySQLiteHelper.COLUMN_CONTENT, info.getContent());
        values.put(MySQLiteHelper.COLUMN_PARENT_ID, info.getParentId());
 
        database.update(MySQLiteHelper.TABLE_INFOS, values, MySQLiteHelper.COLUMN_ID + " = " +info.getId(), null);
 
        return getInfoFromId(info.getId());
    }
	
	public InfoModel getInfoFromId(long id) {
		Cursor c = database.query(MySQLiteHelper.TABLE_INFOS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = \"" + id + "\"", null, null,
				null, null);
		c.moveToFirst();
		InfoModel info = cursorToInfos(c);
		c.close();
		return info;
	}
	
	public List<InfoModel> getAllInfos() {
		List<InfoModel> infos = new ArrayList<InfoModel>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_INFOS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			InfoModel info = cursorToInfos(cursor);
			infos.add(info);
			cursor.moveToNext();
		}

		cursor.close();
		return infos;
	}
	
	public Map<String, String> getAllInfosPictures() {
		Map<String, String> picturesUrl = new HashMap<String, String>();
		
		String[] columns = {MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_PICTURE};
		Cursor cursor = database.query(MySQLiteHelper.TABLE_INFOS,
				columns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String name = cursor.getString(0);
			String picture = cursor.getString(1);
			picturesUrl.put(name, picture);
			cursor.moveToNext();
		}

		cursor.close();
		return picturesUrl;
	}
	
	public void deleteInfo(InfoModel info) {
        long id = info.getId();
        database.delete(MySQLiteHelper.TABLE_INFOS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }
	
	public void deleteAllInfos() {
		database.delete(MySQLiteHelper.TABLE_INFOS, null, null);
	}
	
	private InfoModel cursorToInfos(Cursor cursor) {
        InfoModel info = new InfoModel();
        if  ( cursor.getCount() > 0 ) {
        	info.setId(cursor.getInt(0));
	        info.setName(cursor.getString(1));
	        info.setPicture(cursor.getString(2));
	        if (cursor.getString(3).equals("1")) {
	        	info.setIsCategory ( true );
	        } else {
	        	info.setIsCategory ( false );
	        }
	        info.setContent(cursor.getString(4));
	        info.setParentId(cursor.getLong(5));
	        return info;
        } else {
        	return null;
        }
    }
}