package com.imaginariumfestival.android.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.imaginariumfestival.android.news.NewsModel;

public class NewsDataSource {
	// Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TITLE,
			MySQLiteHelper.COLUMN_CONTENT,
			MySQLiteHelper.COLUMN_DATE };
 
    public NewsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }
 
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
 
    public void close() {
        dbHelper.close();
    }
    
	public NewsModel insertNews(long id, String title, String content, String date) {
 
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, id);
		values.put(MySQLiteHelper.COLUMN_TITLE, title);
		values.put(MySQLiteHelper.COLUMN_CONTENT, content);
		values.put(MySQLiteHelper.COLUMN_DATE, date);

		long insertId = database.insertOrThrow(MySQLiteHelper.TABLE_NEWS,
				null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NEWS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		NewsModel newNews = cursorToNews(cursor);
		cursor.close();
		return newNews;
    }
	
	public NewsModel updateNews(Long id, NewsModel news){
        ContentValues values = new ContentValues();
 
        values.put(MySQLiteHelper.COLUMN_TITLE, news.getTitle());
        values.put(MySQLiteHelper.COLUMN_CONTENT, news.getContent());
        values.put(MySQLiteHelper.COLUMN_DATE, news.getDate());
 
        database.update(MySQLiteHelper.TABLE_NEWS, values, MySQLiteHelper.COLUMN_ID + " = " +news.getId(), null);
 
        return getNewsFromId(news.getId());
    }
	
	public NewsModel getNewsFromId(long id) {
		Cursor c = database.query(MySQLiteHelper.TABLE_NEWS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = \"" + id + "\"", null, null,
				null, null);
		c.moveToFirst();
		NewsModel news = cursorToNews(c);
		c.close();
		return news;
	}
	
	public NewsModel getNewsFromTitle(String name) {
		Cursor c = database.query(MySQLiteHelper.TABLE_NEWS, allColumns,
				MySQLiteHelper.COLUMN_TITLE + " = \"" + name + "\"", null, null,
				null, null);
		c.moveToFirst();
		NewsModel news = cursorToNews(c);
		c.close();
		return news;
	}
	
	public NewsModel getLastNews() {
		Cursor c = database.query(MySQLiteHelper.TABLE_NEWS, allColumns, null, null, null,
				null, MySQLiteHelper.COLUMN_DATE + " DESC");
		c.moveToFirst();
		NewsModel news = cursorToNews(c);
		c.close();
		return news;
	}
	
	public List<NewsModel> getAllNews() {
		List<NewsModel> allNews = new ArrayList<NewsModel>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_NEWS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			NewsModel news = cursorToNews(cursor);
			allNews.add(news);
			cursor.moveToNext();
		}

		cursor.close();
		return allNews;
	}
	
	public void deleteNews(NewsModel news) {
        long id = news.getId();
        database.delete(MySQLiteHelper.TABLE_NEWS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

	public void deleteAllNews() {
		database.delete(MySQLiteHelper.TABLE_NEWS, null, null);
	}
	
	private NewsModel cursorToNews(Cursor cursor) {
        NewsModel news = new NewsModel();
        news.setId(cursor.getInt(0));
        news.setTitle(cursor.getString(1));
        news.setContent(cursor.getString(2));
        news.setDate(cursor.getString(3));

        return news;
    }
}