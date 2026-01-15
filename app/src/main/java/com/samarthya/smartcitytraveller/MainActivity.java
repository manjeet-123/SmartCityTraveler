package com.samarthya.smartcitytraveller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
{

	public FragmentManager fragmentManager;
	public static String coordinatesOfLocationAfterAllSteps;
	public FusedLocationProviderClient fusedLocationProviderClient;
	public String searchRadius;
	public static ArrayList<Interest> venues; // finally selected venue to be visited will be stored here
	public static ProgressDialog progressDialog;
	public ArrayList<String> selectedInterestsList;

	private ActivityResultLauncher<Intent> activityResultLauncher;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d("CustomLog", "MainActivity#onCreate");
		searchRadius = "";
		coordinatesOfLocationAfterAllSteps = "";
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

		progressDialog = new ProgressDialog(MainActivity.this);

		fragmentManager = getSupportFragmentManager();

		// the activity has to be registered before starting a thread
		activityResultLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(), result ->
			{

				Intent selectedInterestsReceivedIntent = result.getData();

				if (result.getResultCode() == RESULT_OK && selectedInterestsReceivedIntent != null)
					doAfterSelectedInterestsReceived(selectedInterestsReceivedIntent);

			});

		// thread to extract location automatically or manually
		AddFragmentThread addFetchLocationFragmentThread =
				new AddFragmentThread("FetchLocationFragment", this);

		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(addFetchLocationFragmentThread::init);

	}

	// this method is called when the location has been put into MainActivity.coordinatesOfLocationAfterAllSteps
	public void doAfterLocationHasBeenFetched()
	{

		Log.d("coordinates", coordinatesOfLocationAfterAllSteps);
		Log.d("CustomLog", "MainActivity#doAfterLocationHasBeenFetched");

		Intent intent = new Intent(com.samarthya.smartcitytraveller.MainActivity.this,
				com.samarthya.smartcitytraveller.WhereToVisitSelectionActivity.class);

		// start the activity
		activityResultLauncher.launch(intent);

	}

	private void doAfterSelectedInterestsReceived(Intent selectedInterestsReceivedIntent)
	{

		Log.d("CustomLog", "MainActivity#doAfterSelectedInterestsReceived");
		selectedInterestsList = selectedInterestsReceivedIntent.getStringArrayListExtra("SelectedInterests");
		venues = new ArrayList<>(selectedInterestsList.size()); // finally selected venues will be stored here

		// last argument is the search radius
		searchRadius = selectedInterestsList.get(selectedInterestsList.size() - 1);
		Log.d("places, A1", selectedInterestsList + " - " + searchRadius);

		// start a networking thread here (foursquare api)
		String nameOfThread = "ExtractSelectedInterestInformation" + selectedInterestsList.get(0);
		NetworkingThread networkingThread = new NetworkingThread(nameOfThread, this);
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(networkingThread::init);

	}

	public void startDestinationMapsActivity()
	{

		Log.d("CustomLog", "MainActivity#startDestinationMapsActivity");

		Intent startDestinationMapsActivity = new Intent(MainActivity.this,
				com.samarthya.smartcitytraveller.DestinationMapsActivity.class);

		startActivity(startDestinationMapsActivity);

	}

}