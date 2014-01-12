package com.imaginariumfestival.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "imaginarium.db";
    private static final int DATABASE_VERSION = 2;
    
    public static final String TABLE_ARTIST = "artists";
    public static final String TABLE_INFOS = "infos";
    public static final String TABLE_NEWS = "news";
    public static final String TABLE_FILTERS = "filters";
    public static final String TABLE_MAP_ITEMS = "mapItems";
    public static final String TABLE_PARTNERS = "partners";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_STYLE = "style";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_STAGE = "stage";
    public static final String COLUMN_BEGIN_HOUR = "beginHour";
    public static final String COLUMN_END_HOUR = "endHour";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_FACEBOOK = "facebook";
    public static final String COLUMN_TWITTER = "twitter";
    public static final String COLUMN_YOUTUBE = "youtube";
	public static final String COLUMN_IS_CATEGORY = "isCategory";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_PARENT_ID = "parent";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_LABEL = "label";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_INFO_ID = "infoId";

	public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_ARTIST + "(" +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_NAME + " text not null, " +
				COLUMN_PICTURE + " text not null, " +
				COLUMN_STYLE + " text not null, " +
				COLUMN_DESCRIPTION + " text not null, " +
				COLUMN_DAY + " text not null, " +
				COLUMN_STAGE + " text not null, " +
				COLUMN_BEGIN_HOUR + " INTEGER NOT NULL, " +
				COLUMN_END_HOUR + " INTEGER NOT NULL, " +
				COLUMN_WEBSITE + " text not null, " +
				COLUMN_FACEBOOK + " text not null, " +
				COLUMN_TWITTER + " text not null, " +
				COLUMN_YOUTUBE + " text not null)");
		db.execSQL("CREATE TABLE " + TABLE_INFOS + "(" +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_NAME + " text not null, " +
				COLUMN_PICTURE + " text not null, " +
				COLUMN_IS_CATEGORY + " INTEGER not null, " +
				COLUMN_CONTENT + " text not null, " +
				COLUMN_PARENT_ID + " INTEGER not null )");
		db.execSQL("CREATE TABLE " + TABLE_NEWS + "(" +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_TITLE + " text not null, " +
				COLUMN_CONTENT + " text not null, " +
				COLUMN_DATE + " text not null )");
		db.execSQL("CREATE TABLE " + TABLE_FILTERS + "(" +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_PICTURE + " text not null)");
		db.execSQL("CREATE TABLE " + TABLE_MAP_ITEMS + "(" +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_LABEL + " text not null, " +
				COLUMN_X + " INTEGER null, " +
				COLUMN_Y + " INTEGER null, " +
				COLUMN_INFO_ID + " INTEGER)");
		db.execSQL("CREATE TABLE " + TABLE_PARTNERS + "(" +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_NAME + " text not null, " +
				COLUMN_WEBSITE + " text not null, " +
				COLUMN_PICTURE + " text not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion ) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST +";");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFOS +";");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS +";");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTERS +";");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAP_ITEMS +";");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNERS +";");
			onCreate(db);
		}
	}
}