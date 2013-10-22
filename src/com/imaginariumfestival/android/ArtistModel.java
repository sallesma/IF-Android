package com.imaginariumfestival.android;

public class ArtistModel {
	private String photoUrl;
	private String name;
	private String genre;
	private String description;
	private ProgrammationModel programmation;

	public ArtistModel(String photoUrl, String name, String genre, String description,
			ProgrammationModel programmation) {
		super();
		this.photoUrl = photoUrl;
		this.name = name;
		this.genre = genre;
		this.description = description;
		this.programmation = programmation;
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
}
