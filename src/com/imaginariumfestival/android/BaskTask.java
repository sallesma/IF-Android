package com.imaginariumfestival.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

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
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.imaginariumfestival.android.data.ArtistDataSource;
import com.imaginariumfestival.android.data.MySQLiteHelper;

class BackTask extends AsyncTask<Void, Integer, Void> {
	private Context context;

	public BackTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Toast.makeText(context, "Mise à jour des données...", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		String textArtist = getArtistsFromWebService();
		JSONArray jsonArtists = parseArtists(textArtist);
		Boolean result  = recordArtists(jsonArtists);
		
        if(result == true) {
            SharedPreferences pref = context.getSharedPreferences("lastSync", 0); // 0 - for private mode
            Editor editor = pref.edit();
            Date currentDate = new Date();
            editor.putLong("lastSync", currentDate.getTime());
            editor.commit();
        }
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
	
	public String getArtistsFromWebService() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://titouanrossier.com/ifM/api/api.php?request=artists");
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
	
	public JSONArray parseArtists(String artists) {
		JSONArray jArray = null;
		try {
			jArray = new JSONArray(artists);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return jArray;
	}
	
	public Boolean recordArtists(JSONArray jsonArtistsArray) {
		ArtistDataSource datasource = new ArtistDataSource(context);
		datasource.open();
		try {
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

				ArtistModel artist = null;
				artist = datasource.createArtist(id, name, picture, genre,
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
}