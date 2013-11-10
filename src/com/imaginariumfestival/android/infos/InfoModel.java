package com.imaginariumfestival.android.infos;

public class InfoModel {
	private long id;
	private String name;
	private String picture;
	private Boolean isCategory;
	private String content;
	private long parentId;
	
	public InfoModel() {
		super();
		this.id = 0;
		this.name = "";
		this.picture = "";
		this.isCategory = false;
		this.content = "";
		this.parentId = 0;
	}

	public InfoModel(long id, String name, String picture, Boolean isCategory,
			String content, long parent) {
		super();
		this.id = id;
		this.name = name;
		this.picture = picture;
		this.isCategory = isCategory;
		this.content = content;
		this.parentId = parent;
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

	public Boolean getIsCategory() {
		return isCategory;
	}

	public void setIsCategory(Boolean isCategory) {
		this.isCategory = isCategory;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "ArtistModel [name=" + name + " - isCategory=" + isCategory + "]";
	}
}
