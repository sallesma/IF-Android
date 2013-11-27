package com.imaginariumfestival.android.photos;

public class FilterModel {
	private long id;
	private String picture;
	
	public FilterModel() {
		super();
		this.id = 0;
		this.picture = "";
	}

	public FilterModel(long id, String picture) {
		super();
		this.id = id;
		this.picture = picture;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Override
	public String toString() {
		return "FilterModel [id=" + id + " - picture=" + picture + "]";
	}
}
