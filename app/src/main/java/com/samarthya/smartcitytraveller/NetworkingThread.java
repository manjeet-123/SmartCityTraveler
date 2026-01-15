package com.samarthya.smartcitytraveller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkingThread
{

	private String selectedInterest;
	public static ArrayList<Interest> interests;
	private String nameOfThread;
	private final MainActivity mainActivity;

	public NetworkingThread(String nameOfThread, MainActivity mainActivity) {
		this.nameOfThread = nameOfThread;
		this.mainActivity = mainActivity;
	}

	public void init()
	{

		if (nameOfThread.contains("ExtractSelectedInterestInformation"))
		{

			selectedInterest = nameOfThread.substring(34);
			nameOfThread = nameOfThread.substring(0, 34);

		}

		switch (nameOfThread)
		{

			case "FindCoordinatesFromLocation":
				try {
					findCoordinatesFromLocation();
				} catch (IOException | JSONException e) {
					e.printStackTrace();
				}
				break;

			case "ExtractSelectedInterestInformation":
				try {
					extractSelectedInterestInformation();
				} catch (IOException | JSONException e) {
					e.printStackTrace();
				}
				break;

		}

	}

	// geocoding api
	private void findCoordinatesFromLocation() throws IOException, JSONException
	{

		Log.d("CustomLog", "NetworkingThread#findCoordinatesFromLocation");

		// here, FetchLocationFragment.currentLocationCoordinates is the location name
		// it will be updated to coordinates at the end of this thread
		String locationCoordinates = FetchLocationFragment.currentLocationCoordinates;

		URL url = new URL("https://app.geocodeapi.io/api/v1/search?"
				+ "apikey=" + mainActivity.getString(R.string.geocodeApiKey) + "&"
				+ "text=" + locationCoordinates);

		HttpURLConnection httpURLConnection;
		InputStream inputStream;
		Scanner readFromInputStream;
		String jsonResponse = "";

		httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(10000);
		httpURLConnection.setReadTimeout(10000);
		httpURLConnection.connect();

		inputStream = httpURLConnection.getInputStream();
		readFromInputStream = new Scanner(inputStream);//, Charset.forName("UTF-8"));

		while (readFromInputStream.hasNext()) {
			jsonResponse = jsonResponse.concat(readFromInputStream.nextLine());
		}

		readFromInputStream.close();
		inputStream.close();
		httpURLConnection.disconnect();

		JSONObject root = new JSONObject(jsonResponse);
		JSONArray features = root.getJSONArray("features");
		JSONObject firstElement = features.getJSONObject(0);
		JSONObject geometry = firstElement.getJSONObject("geometry");
		JSONArray coordinates = geometry.getJSONArray("coordinates");

		locationCoordinates = coordinates.getString(1) + "," + coordinates.getString(0);
		FetchLocationFragment.currentLocationCoordinates = locationCoordinates;

	}

	// foursquare api
	private void extractSelectedInterestInformation() throws IOException, JSONException
	{

		Log.d("CustomLog", "NetworkingThread#extractSelectedInterestInformation");

		String urlString = "https://api.foursquare.com/v3/places/search?"
				+ "ll=" + MainActivity.coordinatesOfLocationAfterAllSteps + "&"
				+ "llacc=100&"
				+ "sortByPopularity=true&"
				+ "radius=";

		// concatenate the radius parameter
		urlString = urlString.concat(mainActivity.searchRadius + "000&");

		// selected interest will be concatenated here
		urlString = urlString.concat("query=" + selectedInterest);

		URL url = new URL(urlString);
		HttpURLConnection httpURLConnection;
		InputStream inputStream;
		Scanner readFromInputStream;
		String jsonResponse = "";

		httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setConnectTimeout(10000);
		httpURLConnection.setReadTimeout(10000);
		httpURLConnection.setRequestProperty("Authorization", mainActivity.getString(R.string.foursquareApiKey1));
		httpURLConnection.connect();

		inputStream = httpURLConnection.getInputStream();
		readFromInputStream = new Scanner(inputStream);

		while (readFromInputStream.hasNext()) {
			jsonResponse = jsonResponse.concat(readFromInputStream.nextLine());
		}

		readFromInputStream.close();
		inputStream.close();
		httpURLConnection.disconnect();

		// extract the required information from the received JSON file
		interests = new ArrayList<>();
		String name, type, distance, lat, lng;
		DecimalFormat latFormat = new DecimalFormat("#00.000000");
		DecimalFormat longFormat = new DecimalFormat("#000.000000");
		DecimalFormat distFormat = new DecimalFormat("#00.00");

		JSONObject root = new JSONObject(jsonResponse);//.body());
		JSONArray results = root.getJSONArray("results");

		for (int i = 0; i < results.length(); i++)
		{

			JSONObject eachResult = results.getJSONObject(i); // whole data of each object

			name = eachResult.getString("name");

			JSONArray categories = eachResult.getJSONArray("categories");
			type = categories.getJSONObject(0).getString("name");

			distance = distFormat.format((float) Integer.parseInt(eachResult.getString("distance")) / 1000);

			JSONObject geocodes = eachResult.getJSONObject("geocodes");
			lat = latFormat.format(geocodes.getJSONObject("main").getDouble("latitude"));
			lng = longFormat.format(geocodes.getJSONObject("main").getDouble("longitude"));

			interests.add(new Interest(name, type, distance, lat, lng));

		}

		Log.d("xxx", String.valueOf(interests.size()));

		AddFragmentThread addVenueSelectionFragment =
				new AddFragmentThread("VenueSelectionFragment", mainActivity);
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(addVenueSelectionFragment::init);

	}

}
