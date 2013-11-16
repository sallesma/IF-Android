package com.imaginariumfestival.android.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.imaginariumfestival.android.artists.ArtistModel;

public class ArtistDataSource {
	// Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, 
			MySQLiteHelper.COLUMN_PICTURE,
			MySQLiteHelper.COLUMN_STYLE, 
			MySQLiteHelper.COLUMN_DESCRIPTION,
			MySQLiteHelper.COLUMN_DAY, 
			MySQLiteHelper.COLUMN_STAGE,
			MySQLiteHelper.COLUMN_BEGIN_HOUR, 
			MySQLiteHelper.COLUMN_END_HOUR, 
			MySQLiteHelper.COLUMN_WEBSITE,
			MySQLiteHelper.COLUMN_FACEBOOK, 
			MySQLiteHelper.COLUMN_TWITTER,
			MySQLiteHelper.COLUMN_YOUTUBE };
 
    public ArtistDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }
 
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
 
    public void close() {
        dbHelper.close();
    }
    
	public ArtistModel insertArtist(long id, String name, String picture,
			String style, String description, String day, String stage,
			String beginHour, String endHour, String website, String facebook,
			String twitter, String youtube) {
 
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, id);
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_PICTURE, picture);
		values.put(MySQLiteHelper.COLUMN_STYLE, style);
		values.put(MySQLiteHelper.COLUMN_DESCRIPTION, description);
		values.put(MySQLiteHelper.COLUMN_DAY, day);
		values.put(MySQLiteHelper.COLUMN_STAGE, stage);
		values.put(MySQLiteHelper.COLUMN_BEGIN_HOUR, beginHour);
		values.put(MySQLiteHelper.COLUMN_END_HOUR, endHour);
		values.put(MySQLiteHelper.COLUMN_WEBSITE, website);
		values.put(MySQLiteHelper.COLUMN_FACEBOOK, facebook);
		values.put(MySQLiteHelper.COLUMN_TWITTER, twitter);
		values.put(MySQLiteHelper.COLUMN_YOUTUBE, youtube);
		long insertId = database.insertOrThrow(MySQLiteHelper.TABLE_ARTIST,
				null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ARTIST, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		ArtistModel newArtist = cursorToArtist(cursor);
		cursor.close();
		return newArtist;
    }
	
	public ArtistModel updateArtist(Long id, ArtistModel artist){
        ContentValues values = new ContentValues();
 
        values.put(MySQLiteHelper.COLUMN_NAME, artist.getName());
        values.put(MySQLiteHelper.COLUMN_STYLE, artist.getStyle());
        values.put(MySQLiteHelper.COLUMN_DESCRIPTION, artist.getDescription());
        values.put(MySQLiteHelper.COLUMN_PICTURE, artist.getPicture());
        values.put(MySQLiteHelper.COLUMN_DAY, artist.getDay());
        values.put(MySQLiteHelper.COLUMN_STAGE, artist.getStage());
        values.put(MySQLiteHelper.COLUMN_BEGIN_HOUR, artist.getBeginHour());
        values.put(MySQLiteHelper.COLUMN_END_HOUR, artist.getEndHour());
        values.put(MySQLiteHelper.COLUMN_WEBSITE, artist.getWebsite());
        values.put(MySQLiteHelper.COLUMN_FACEBOOK, artist.getFacebook());
        values.put(MySQLiteHelper.COLUMN_TWITTER, artist.getTwitter());
        values.put(MySQLiteHelper.COLUMN_YOUTUBE, artist.getYoutube());
 
        database.update(MySQLiteHelper.TABLE_ARTIST, values, MySQLiteHelper.COLUMN_ID + " = " +artist.getId(), null);
 
        return getArtistFromId(artist.getId());
    }
	
	public ArtistModel getArtistFromId(long id) {
		Cursor c = database.query(MySQLiteHelper.TABLE_ARTIST, allColumns,
				MySQLiteHelper.COLUMN_ID + " = \"" + id + "\"", null, null,
				null, null);
		c.moveToFirst();
		ArtistModel artist = cursorToArtist(c);
		c.close();
		return artist;
	}
	
	public ArtistModel getArtistFromName(String name) {
		Cursor c = database.query(MySQLiteHelper.TABLE_ARTIST, allColumns,
				MySQLiteHelper.COLUMN_NAME + " = \"" + name + "\"", null, null,
				null, null);
		c.moveToFirst();
		ArtistModel artist = cursorToArtist(c);
		c.close();
		return artist;
	}
	
	public List<ArtistModel> getAllArtists() {
		List<ArtistModel> artists = new ArrayList<ArtistModel>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_ARTIST,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ArtistModel artist = cursorToArtist(cursor);
			artists.add(artist);
			cursor.moveToNext();
		}

		cursor.close();
		return artists;
	}
	
	public void deleteArtist(ArtistModel artist) {
        long id = artist.getId();
        database.delete(MySQLiteHelper.TABLE_ARTIST, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

	public void deleteAllArtists() {
		database.delete(MySQLiteHelper.TABLE_ARTIST, null, null);
	}
	
	private ArtistModel cursorToArtist(Cursor cursor) {
        ArtistModel artist = new ArtistModel();
        artist.setId(cursor.getInt(0));
        artist.setName(cursor.getString(1));
        artist.setPicture(cursor.getString(2));
        artist.setStyle(cursor.getString(3));
        artist.setDescription(cursor.getString(4));
        artist.setDay(cursor.getString(5));
        artist.setStage(cursor.getString(6));
        artist.setBeginHour(cursor.getString(7));
        artist.setEndHour(cursor.getString(8));
        artist.setWebsite(cursor.getString(9));
        artist.setFacebook(cursor.getString(10));
        artist.setTwitter(cursor.getString(11));
        artist.setYoutube(cursor.getString(12));
        return artist;
    }
}