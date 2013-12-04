package com.imaginariumfestival.android.partners;

public class PartnerModel {
	private long id;
	private String name;
	private String picture;
	private String weblink;
	
	public PartnerModel() {
		super();
		this.id = 0;
		this.name = "";
		this.picture = "";
		this.weblink = "";
	}

	public PartnerModel(long id, String name, String picture, String weblink) {
		super();
		this.id = id;
		this.name = name;
		this.picture = picture;
		this.weblink = weblink;
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
	public String getWeblink() {
		return weblink;
	}
	public void setWeblink(String weblink) {
		this.weblink = weblink;
	}
}
