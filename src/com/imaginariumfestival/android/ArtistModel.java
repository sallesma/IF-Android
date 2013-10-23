package com.imaginariumfestival.android;

public class ArtistModel {
	private String photoUrl;
	private String name;
	private String genre;
	private String description;
	private ProgrammationModel programmation;
	private String website;
	private String youtube;
	private String facebook;
	private String twitter;

	public ArtistModel(String photoUrl, String name, String genre, String description,
			ProgrammationModel programmation, String youtube, String facebook, String twitter, String website) {
		super();
		this.photoUrl = photoUrl;
		this.name = name;
		this.genre = genre;
		this.description = description;
		this.programmation = programmation;
		this.website = website;
		this.youtube = youtube;
		this.facebook = facebook;
		this.twitter = twitter;
		
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

	public ProgrammationModel getProgrammation() {
		return programmation;
	}

	public void setProgrammation(ProgrammationModel programmation) {
		this.programmation = programmation;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
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
	
}
