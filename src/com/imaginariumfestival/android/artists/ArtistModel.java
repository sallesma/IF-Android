package com.imaginariumfestival.android.artists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.imaginariumfestival.android.programmation.ProgrammationActivity;

public class ArtistModel {
	private static final int DAY_OF_MONTH = 30;
	private static final int MONTH = Calendar.MAY;
	private static final int YEAR = 2014;

	public static final String MAIN_STAGE = "principale";
	public static final String SECOND_STAGE = "chapiteau";
	
	private long id;
	private String name;
	private String picture;
	private String style;
	private String description;
	private String stage;
	private String day;
	private String beginHour;
	private String endHour;
	private String website;
	private String youtube;
	private String facebook;
	private String twitter;

	public ArtistModel(long id, String name, String picture, String style,
			String description, String stage, String day, String beginHour,
			String endHour, String website, String facebook,
			String twitter, String youtube) {
		super();
		this.id = id;
		this.name = name;
		this.picture = picture;
		this.style = style;
		this.description = description;
		this.stage = stage;
		this.day = day;
		this.beginHour = beginHour;
		this.endHour = endHour;
		this.website = website;
		this.youtube = youtube;
		this.facebook = facebook;
		this.twitter = twitter;
	}
	
	public ArtistModel() {
		this.id = 0;
		this.picture = "";
		this.name = "";
		this.style = "";
		this.description = "";
		this.day = "";
		this.stage = "";
		this.beginHour = "";
		this.endHour = "";
		this.website = "";
		this.youtube = "";
		this.facebook = "";
		this.twitter = "";
	}

	public String getProgrammation(){
		return  stage + " - " + day + " " + beginHour.substring(0, 5);
	}
	
	public Date getAbsoluteDate() {
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
		calendar.set(YEAR, MONTH, DAY_OF_MONTH, 0, 0, 0);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

		Calendar minuteAndHourCalendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
		try {
			minuteAndHourCalendar.setTime( dateFormat.parse(beginHour) );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendar.add(Calendar.HOUR_OF_DAY, minuteAndHourCalendar.get(Calendar.HOUR_OF_DAY));
		calendar.add(Calendar.MINUTE, minuteAndHourCalendar.get(Calendar.MINUTE));
		
		if (calendar.get(Calendar.HOUR_OF_DAY) < 9) //after midnight is considered as the same day
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + ProgrammationActivity.HOURS_IN_DAY );
		if ( day.equals(ProgrammationActivity.SECOND_DAY) ) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return calendar.getTime();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long l) {
		this.id = l;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getBeginHour() {
		return beginHour;
	}

	public void setBeginHour(String beginHour) {
		this.beginHour = beginHour;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endhour) {
		this.endHour = endhour;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getYoutube() {
		return youtube;
	}

	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	@Override
	public String toString() {
		return "ArtistModel [name=" + name + "]";
	}
}
