package com.imaginariumfestival.android.partners;

public class PartnerModel {
	private long id;
	private String name;
	private String picture;
	private String website;
	private int priority;

	public PartnerModel() {
		super();
		this.id = 0;
		this.name = "";
		this.picture = "";
		this.website = "";
	}

	public PartnerModel(long id, String name, String picture, String website, int priority) {
		super();
		this.id = id;
		this.name = name;
		this.picture = picture;
		this.website = website;
		this.priority = priority;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String weblink) {
		this.website = weblink;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
}
