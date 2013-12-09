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

import com.imaginariumfestival.android.partners.PartnerModel;

public class PartnersDataSource {
	// Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, 
			MySQLiteHelper.COLUMN_PICTURE,
			MySQLiteHelper.COLUMN_WEBSITE };
 
    public PartnersDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }
 
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
 
    public void close() {
        dbHelper.close();
    }
    
	public PartnerModel insertPartner(long id, String name, String picture,
			String website) {

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, id);
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_PICTURE, picture);
		values.put(MySQLiteHelper.COLUMN_WEBSITE, website);
		long insertId = database.insertOrThrow(MySQLiteHelper.TABLE_PARTNERS,
				null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PARTNERS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		PartnerModel newPartner = cursorToPartner(cursor);
		cursor.close();
		return newPartner;
    }
	
	public PartnerModel updateInfo(Long id, PartnerModel partner){
        ContentValues values = new ContentValues();
        
        values.put(MySQLiteHelper.COLUMN_NAME, partner.getName());
        values.put(MySQLiteHelper.COLUMN_PICTURE, partner.getPicture());
        values.put(MySQLiteHelper.COLUMN_WEBSITE, partner.getWebsite());
 
        database.update(MySQLiteHelper.TABLE_PARTNERS, values, MySQLiteHelper.COLUMN_ID + " = " + partner.getId(), null);
 
        return getPartnerFromId(partner.getId());
    }
	
	public PartnerModel getPartnerFromId(long id) {
		Cursor c = database.query(MySQLiteHelper.TABLE_PARTNERS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = \"" + id + "\"", null, null,
				null, null);
		c.moveToFirst();
		PartnerModel partner = cursorToPartner(c);
		c.close();
		return partner;
	}

	public List<PartnerModel> getAllPartners() {
		List<PartnerModel> partners = new ArrayList<PartnerModel>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PARTNERS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PartnerModel partner = cursorToPartner(cursor);
			partners.add(partner);
			cursor.moveToNext();
		}

		cursor.close();
		return partners;
	}
	
	public Map<String, String> getAllPartnersPictures() {
		Map<String, String> picturesUrl = new HashMap<String, String>();
		
		String[] columns = {MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_PICTURE};
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PARTNERS,
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
	
	public void deletePartner(PartnerModel partner) {
        long id = partner.getId();
        database.delete(MySQLiteHelper.TABLE_PARTNERS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }
	
	public void deleteAllPartners() {
		database.delete(MySQLiteHelper.TABLE_PARTNERS, null, null);
	}
	
	private PartnerModel cursorToPartner(Cursor cursor) {
        PartnerModel partner = new PartnerModel();
        if  ( cursor.getCount() > 0 ) {
        	partner.setId(cursor.getInt(0));
	        partner.setName(cursor.getString(1));
	        partner.setPicture(cursor.getString(2));
	        partner.setWebsite(cursor.getString(3));
	        return partner;
        } else {
        	return null;
        }
    }
}