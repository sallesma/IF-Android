package com.imaginariumfestival.android.news;

public class NewsModel {
	private long id;
	private String title;
	private String content;
	private String date;

	public NewsModel(long id, String title, String content, String date) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.date = date;
	}
	
	public NewsModel() {
		super();
		this.id = 0;
		this.title = "";
		this.content = "";
		this.date = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "NEwsModel [title=" + title + "]";
	}
}
