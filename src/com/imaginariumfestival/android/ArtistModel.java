package com.imaginariumfestival.android;

public class ArtistModel implements Comparable<ArtistModel>{
	private String photoUrl;
	private String name;
	private String description;
	private ProgrammationModel programmation;

	public ArtistModel(String photoUrl, String name, String description,
			ProgrammationModel programmation) {
		super();
		this.photoUrl = photoUrl;
		this.name = name;
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

	@Override
	public int compareTo(ArtistModel another) {
		return this.name.compareTo(another.getName());
	}

}
