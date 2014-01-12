package com.imaginariumfestival.android.map;

public class MapItemModel {
	private long id;
	private String label;
	private int x;
	private int y;
	private int infoId;
	
	public MapItemModel(long id, String label, int x, int y, int infoId) {
		super();
		this.id = id;
		this.label = label;
		this.x = x;
		this.y = y;
		this.infoId = infoId;
	}
	
	public MapItemModel() {
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getInfoId() {
		return infoId;
	}
	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}
	
	@Override
	public String toString() {
		return "MapItemModel [id=" + id + ", label=" + label + ", x=" + x
				+ ", y=" + y + ", infoId=" + infoId + "]";
	}
}
