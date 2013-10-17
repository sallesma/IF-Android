package com.imaginariumfestival.android;

public class ProgrammationModel {
	private String stage;
	private String day;
	private String hour;
	
	public ProgrammationModel(String stage, String day, String hour) {
		super();
		this.stage = stage;
		this.day = day;
		this.hour = hour;
	}
	
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}

	@Override
	public String toString() {
		return  stage + " - " + day + " " + hour;
	}
	
	
}
