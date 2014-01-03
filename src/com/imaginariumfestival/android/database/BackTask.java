package com.imaginariumfestival.android.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.imaginariumfestival.android.MainMenuActivity;

public class BackTask extends AsyncTask<Void, Integer, Void> {
	public final static String LAST_UPDATE_FROM_DISTANT_DATABASE = "lastUpdateFromDistantDatabase";
	
	public final static String LAST_ARTIST_UPDATE_FROM_DISTANT_DATABASE = "lastArtistUpdateFromDistantDatabase";
	public final static String LAST_INFO_UPDATE_FROM_DISTANT_DATABASE = "lastInfoUpdateFromDistantDatabase";
	public final static String LAST_NEWS_UPDATE_FROM_DISTANT_DATABASE = "lastNewsUpdateFromDistantDatabase";
	public final static String LAST_FILTERS_UPDATE_FROM_DISTANT_DATABASE = "lastFilterUpdateFromDistantDatabase";
	public final static String LAST_PARTNERS_UPDATE_FROM_DISTANT_DATABASE = "lastPartnerUpdateFromDistantDatabase";
	
	private final String BASE_URL = "http://titouanrossier.com/if/";
	private final String ARTISTS_WEB_SERVICE_URL = BASE_URL + "api/api.php?request=artists";
	private final String INFOS_WEB_SERVICE_URL = BASE_URL + "api/api.php?request=infos";
	private final String NEWS_WEB_SERVICE_URL = BASE_URL + "api/api.php?request=news";
	private final String FILTERS_WEB_SERVICE_URL = BASE_URL + "api/api.php?request=filters";
	private final String PARTNERS_WEB_SERVICE_URL = BASE_URL + "api/api.php?request=partners";
	private Context context; 

	public BackTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Toast.makeText(context, "Mise à jour des données...", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		getArtistsFromWebService();
		getInfosFromWebService();
		getNewsFromWebService();
		getFiltersFromWebService();
		getPartnersFromWebService();
        return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

	@Override
	protected void onPostExecute(Void result) {
		Toast.makeText(context, "...la mise à jour est terminée",
				Toast.LENGTH_SHORT).show();
	}
	
	private void getArtistsFromWebService() {
		final SharedPreferences pref = context.getSharedPreferences(BackTask.LAST_ARTIST_UPDATE_FROM_DISTANT_DATABASE, Context.MODE_PRIVATE);
        final String lastRetrieve = pref.getString(BackTask.LAST_ARTIST_UPDATE_FROM_DISTANT_DATABASE, "");
		final String url = ARTISTS_WEB_SERVICE_URL + "&lastRetrieve=" + lastRetrieve;
		String artistsFromDistantDatabase = getDataFromDistantDatabase(url);
		
		Boolean result = false;
		if (artistsFromDistantDatabase != null && artistsFromDistantDatabase != "") {
			JSONArray jsonArtists = new JSONArray();
			try {
				jsonArtists = new JSONArray(artistsFromDistantDatabase);
				result = recordArtists(jsonArtists);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		}
		
		if (result) {
			updatePicturesFromRemoteServer(MySQLiteHelper.TABLE_ARTIST);
			updateLastUpdateFromDatabase(LAST_ARTIST_UPDATE_FROM_DISTANT_DATABASE);
		}
	}
	
	private Boolean recordArtists(JSONArray jsonArtistsArray) {
		ArtistDataSource datasource = new ArtistDataSource(context);
		datasource.open();
		try {
			datasource.deleteAllArtists();
			for (int i = 0; i < jsonArtistsArray.length(); i++) {
				JSONObject c = jsonArtistsArray.getJSONObject(i);

				Long id = c.getLong(MySQLiteHelper.COLUMN_ID);
				String name = c.getString(MySQLiteHelper.COLUMN_NAME);
				String picture = c.getString(MySQLiteHelper.COLUMN_PICTURE);
				String genre = c.getString(MySQLiteHelper.COLUMN_STYLE);
				String description = c.getString(MySQLiteHelper.COLUMN_DESCRIPTION);
				String jour = c.getString(MySQLiteHelper.COLUMN_DAY);
				String scene = c.getString(MySQLiteHelper.COLUMN_STAGE);
				String debut = c.getString(MySQLiteHelper.COLUMN_BEGIN_HOUR);
				String fin = c.getString(MySQLiteHelper.COLUMN_END_HOUR);
				String website = c.getString(MySQLiteHelper.COLUMN_WEBSITE);
				String facebook = c.getString(MySQLiteHelper.COLUMN_FACEBOOK);
				String twitter = c.getString(MySQLiteHelper.COLUMN_TWITTER);
				String youtube = c.getString(MySQLiteHelper.COLUMN_YOUTUBE);

				datasource.insertArtist(id, name, picture, genre,
						description, jour, scene, debut, fin, website,
						facebook, twitter, youtube);
			}
			datasource.close();
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			datasource.close();
			return false;
		}
	}
	
	private void getInfosFromWebService() {
		final SharedPreferences pref = context.getSharedPreferences(BackTask.LAST_INFO_UPDATE_FROM_DISTANT_DATABASE, Context.MODE_PRIVATE);
        final String lastRetrieve = pref.getString(BackTask.LAST_INFO_UPDATE_FROM_DISTANT_DATABASE, "");
		final String url = INFOS_WEB_SERVICE_URL + "&lastRetrieve=" + lastRetrieve;
		String infosFromDistantDatabase = getDataFromDistantDatabase(url);
		
		Boolean result = false;
		if (infosFromDistantDatabase != null && infosFromDistantDatabase != "") {
			JSONArray jsonInfos = new JSONArray();
			try {
				jsonInfos = new JSONArray(infosFromDistantDatabase);
				result = recordInfos(jsonInfos);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		}

		if (result) {
			updatePicturesFromRemoteServer(MySQLiteHelper.TABLE_INFOS);
			updateLastUpdateFromDatabase(LAST_INFO_UPDATE_FROM_DISTANT_DATABASE);
		}
	}
	
	private Boolean recordInfos(JSONArray jsonInfosArray) {
		InfosDataSource datasource = new InfosDataSource(context);
		datasource.open();
		try {
			datasource.deleteAllInfos();
			for (int i = 0; i < jsonInfosArray.length(); i++) {
				JSONObject c = jsonInfosArray.getJSONObject(i);
				
				Long id = c.getLong(MySQLiteHelper.COLUMN_ID);
				String name = c.getString(MySQLiteHelper.COLUMN_NAME);
				String picture = c.getString(MySQLiteHelper.COLUMN_PICTURE);
				String isCategory = c.getString(MySQLiteHelper.COLUMN_IS_CATEGORY);
				String content = c.getString(MySQLiteHelper.COLUMN_CONTENT);
				Long parent = Long.valueOf( c.getString(MySQLiteHelper.COLUMN_PARENT_ID) );
				
				datasource.insertInfo(id, name, picture, isCategory, content, parent);
			}
			datasource.close();
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			datasource.close();
			return false;
		}
	}
	
	private void getNewsFromWebService() {
		final SharedPreferences pref = context.getSharedPreferences(BackTask.LAST_NEWS_UPDATE_FROM_DISTANT_DATABASE, Context.MODE_PRIVATE);
        final String lastRetrieve = pref.getString(BackTask.LAST_NEWS_UPDATE_FROM_DISTANT_DATABASE, "");
		final String url = NEWS_WEB_SERVICE_URL + "&lastRetrieve=" + lastRetrieve;
		String newsFromDistantDatabase = getDataFromDistantDatabase(url);
		
		Boolean result = false;
		if (newsFromDistantDatabase != null && newsFromDistantDatabase != "") {
			JSONArray jsonNews = new JSONArray();
			try {
				jsonNews = new JSONArray(newsFromDistantDatabase);
				result = recordNews(jsonNews);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		}
		
		if (result) {
			updateLastUpdateFromDatabase(LAST_NEWS_UPDATE_FROM_DISTANT_DATABASE);
		}
	}
	
	private Boolean recordNews(JSONArray jsonNewsArray) {
		NewsDataSource datasource = new NewsDataSource(context);
		datasource.open();
		try {
			datasource.deleteAllNews();
			for (int i = 0; i < jsonNewsArray.length(); i++) {
				JSONObject c = jsonNewsArray.getJSONObject(i);

				Long id = c.getLong(MySQLiteHelper.COLUMN_ID);
				String title = c.getString(MySQLiteHelper.COLUMN_TITLE);
				String content = c.getString(MySQLiteHelper.COLUMN_CONTENT);
				String date = c.getString(MySQLiteHelper.COLUMN_DATE);


				datasource.insertNews(id, title, content, date);
			}
			datasource.close();
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			datasource.close();
			return false;
		}
	}
	
	private void getFiltersFromWebService() {
		final SharedPreferences pref = context.getSharedPreferences(BackTask.LAST_FILTERS_UPDATE_FROM_DISTANT_DATABASE, Context.MODE_PRIVATE);
        final String lastRetrieve = pref.getString(BackTask.LAST_FILTERS_UPDATE_FROM_DISTANT_DATABASE, "");
		final String url = FILTERS_WEB_SERVICE_URL + "&lastRetrieve=" + lastRetrieve;
		String filtersFromDistantDatabase = getDataFromDistantDatabase(url);
		
		Boolean result = false;
		if (filtersFromDistantDatabase != null && filtersFromDistantDatabase != "") {
			JSONArray jsonFilters = new JSONArray();
			try {
				jsonFilters = new JSONArray(filtersFromDistantDatabase);
				result = recordFilters(jsonFilters);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		}
		
		if (result) {
			updatePicturesFromRemoteServer(MySQLiteHelper.TABLE_FILTERS);
			updateLastUpdateFromDatabase(LAST_FILTERS_UPDATE_FROM_DISTANT_DATABASE);
		}
	}
	
	private Boolean recordFilters(JSONArray jsonFiltersArray) {
		FiltersDataSource datasource = new FiltersDataSource(context);
		datasource.open();
		try {
			datasource.deleteAllFilters();
			for (int i = 0; i < jsonFiltersArray.length(); i++) {
				JSONObject c = jsonFiltersArray.getJSONObject(i);

				Long id = c.getLong(MySQLiteHelper.COLUMN_ID);
				String picture = c.getString(MySQLiteHelper.COLUMN_PICTURE);

				datasource.insertFilter(id, picture);
			}
			datasource.close();
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			datasource.close();
			return false;
		}
	}
	
	private void getPartnersFromWebService() {
		final SharedPreferences pref = context.getSharedPreferences(BackTask.LAST_PARTNERS_UPDATE_FROM_DISTANT_DATABASE, Context.MODE_PRIVATE);
        final String lastRetrieve = pref.getString(BackTask.LAST_PARTNERS_UPDATE_FROM_DISTANT_DATABASE, "");
		final String url = PARTNERS_WEB_SERVICE_URL + "&lastRetrieve=" + lastRetrieve;
		String partnersFromDistantDatabase = getDataFromDistantDatabase(url);
		
		Boolean result = false;
		if (partnersFromDistantDatabase != null && partnersFromDistantDatabase != "") {
			JSONArray jsonPartners = new JSONArray();
			try {
				jsonPartners = new JSONArray(partnersFromDistantDatabase);
				result = recordPartners(jsonPartners);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		}
		
		if (result) {
			updatePicturesFromRemoteServer(MySQLiteHelper.TABLE_PARTNERS);
			updateLastUpdateFromDatabase(LAST_PARTNERS_UPDATE_FROM_DISTANT_DATABASE);
		}
	}
	
	private Boolean recordPartners(JSONArray jsonPartnersArray) {
		PartnersDataSource datasource = new PartnersDataSource(context);
		datasource.open();
		try {
			datasource.deleteAllPartners();
			for (int i = 0; i < jsonPartnersArray.length(); i++) {
				JSONObject c = jsonPartnersArray.getJSONObject(i);

				Long id = c.getLong(MySQLiteHelper.COLUMN_ID);
				String name = c.getString(MySQLiteHelper.COLUMN_NAME);
				String picture = c.getString(MySQLiteHelper.COLUMN_PICTURE);
				String website = c.getString(MySQLiteHelper.COLUMN_WEBSITE);

				datasource.insertPartner(id, name, picture, website);
			}
			datasource.close();
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			datasource.close();
			return false;
		}
	}
	
	private String getDataFromDistantDatabase(String URL) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line + "\n");
				}
			} else {
				Log.e(MainMenuActivity.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	private void updateLastUpdateFromDatabase(final String prefToUpdate) {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd%20hh:mm:ss");
		String formattedDate = dateFormat.format(currentDate);
		
		SharedPreferences pref = context.getSharedPreferences(prefToUpdate, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(prefToUpdate, formattedDate);
		editor.commit();
	}

	private void updatePicturesFromRemoteServer( String tableName ) {
		Map<String, String> urlList = new HashMap<String, String>();
		
		if (tableName.equals(MySQLiteHelper.TABLE_ARTIST)) {
			ArtistDataSource artistDataSource = new ArtistDataSource(context);
			artistDataSource.open();
			urlList.putAll(artistDataSource.getAllArtistPictures());
			artistDataSource.close();
		} else if (tableName.equals(MySQLiteHelper.TABLE_INFOS)) {
			InfosDataSource infosDataSource = new InfosDataSource(context);
			infosDataSource.open();
			urlList.putAll(infosDataSource.getAllInfosPictures());
			infosDataSource.close();
		} else if (tableName.equals(MySQLiteHelper.TABLE_FILTERS)) {
			FiltersDataSource filtersDataSource = new FiltersDataSource(context);
			filtersDataSource.open();
			urlList.putAll(filtersDataSource.getAllFilterPictures());
			filtersDataSource.close();
		}

		String path = context.getFilesDir() + "/" + tableName + "/";
		new File(path).mkdirs();
		
		for (Entry<String, String> entry : urlList.entrySet()) {
			if (entry != null && entry.getValue() != null && !entry.getValue().equals("")) {
				Bitmap bitmap = getBitmap(entry.getValue());
				
				try {
			    	FileOutputStream fos = new FileOutputStream(path + entry.getKey(),false);
		            if (fos != null && bitmap != null) {
		            	bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		            }
		            fos.close();
		        } catch (FileNotFoundException e) {
		            Log.d("DEBUG", "File not found: " + e.getMessage());
		        } catch (IOException e) {
		            Log.d("DEBUG", "Error accessing file: " + e.getMessage());
		        }
			}
		}
	}
	
	public Bitmap getBitmap(String bitmapUrl) {
		try {
			URL url = new URL(bitmapUrl);
			return BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (Exception ex) {
			return null;
		}
	}
}

