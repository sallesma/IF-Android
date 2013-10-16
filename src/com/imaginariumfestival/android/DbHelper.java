package com.imaginariumfestival.android;

import java.util.ArrayList;
import java.util.List;

public class DbHelper {

	List<ArtistModel> artists = null;

	private DbHelper() {
		populate();
	}

	public static DbHelper getInstance() {
		return new DbHelper();
	}

	private void populate() {
		ProgrammationModel prog1 = new ProgrammationModel("Main Stage","31 mai", "22h");
		ArtistModel artist1 = new ArtistModel("","Justice",
				"Justice est un duo français de synthpop, musique électronique, composé de Gaspard Augé et de Xavier de Rosnay.",
				prog1);
		ProgrammationModel prog2 = new ProgrammationModel("Main Stage","31 mai", "20h");
		ArtistModel artist2 = new ArtistModel("","Manu Chao",
				"Manu Chao (né José-Manuel Thomas Arthur Chao le 21 juin 1961 à Paris1) est un chanteur auteur-compositeur-interprète et musicien français d'origine espagnole et bilingue hispanophone, devenu une figure majeure du rock français et de la musique latine avec son groupe la Mano Negra.",
				prog2);
		ProgrammationModel prog3 = new ProgrammationModel("Main Stage","31 mai", "18h");
		ArtistModel artist3 = new ArtistModel("","Wej",
				"Wej (né MartinBouvier) est un auteur-compositeur étudiant vraiment très sympa.",
				prog3);
		ProgrammationModel prog4 = new ProgrammationModel("Main Stage", "31 mai", "16h");
		ArtistModel artist4 = new ArtistModel("", "Daft Punk",
				"Daft Punk est un groupe français de musique électronique, originaire de Paris. Actifs depuis 1992, Thomas Bangalter et Guy-Manuel de Homem-Christo, les deux membres, ont allié à leurs sons electro, house et techno des tonalités rock, groove et disco.",
				prog4);
		
		artists = new ArrayList<ArtistModel>();
		artists.add(artist1);
		artists.add(artist2);
		artists.add(artist3);
		artists.add(artist4);
	}

	public List<ArtistModel> getArtists() {
		return artists;
	}

	public void setArtists(List<ArtistModel> artists) {
		this.artists = artists;
	}
	
	
}
