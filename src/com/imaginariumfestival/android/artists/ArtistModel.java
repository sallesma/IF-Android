package com.imaginariumfestival.android.artists;

public class ArtistModel {
	private long id;
	private String name;
	private String photoUrl;
	private String style;
	private String description;
	private String scene;
	private String jour;
	private String debut;
	private String fin;
	private String website;
	private String youtube;
	private String facebook;
	private String twitter;

	public ArtistModel(long id, String photoUrl, String name, String style,
			String description, String youtube, String facebook,
			String twitter, String website, String jour, String debut,
			String fin, String scene) {
		super();
		this.id = id;
		this.photoUrl = photoUrl;
		this.name = name;
		this.style = style;
		this.description = description;
		this.jour = jour;
		this.scene = scene;
		this.debut = debut;
		this.fin = fin;
		this.website = website;
		this.youtube = youtube;
		this.facebook = facebook;
		this.twitter = twitter;
	}
	
	public ArtistModel() {
		this.id = 0;
		this.photoUrl = "";
		this.name = "";
		this.style = "";
		this.description = "";
		this.jour = "";
		this.scene = "";
		this.debut = "";
		this.fin = "";
		this.website = "";
		this.youtube = "";
		this.facebook = "";
		this.twitter = "";
	}

	public String getProgrammation(){
		return  scene + " - " + jour + " " + debut;
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

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getJour() {
		return jour;
	}

	public void setJour(String jour) {
		this.jour = jour;
	}

	public String getDebut() {
		return debut;
	}

	public void setDebut(String debut) {
		this.debut = debut;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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
