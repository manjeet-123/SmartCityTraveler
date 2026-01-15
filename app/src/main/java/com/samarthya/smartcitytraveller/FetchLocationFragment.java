package com.samarthya.smartcitytraveller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchLocationFragment extends Fragment {

	private MainActivity mainActivity;
	private FusedLocationProviderClient fusedLocationProviderClient;
	public static String currentLocationCoordinates;
	private boolean isEnterLocationManuallyBtnClickedOnce;

	public FetchLocationFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_fetch_location, container, false);
		mainActivity = (MainActivity) view.getContext();

		fusedLocationProviderClient = mainActivity.fusedLocationProviderClient;
		currentLocationCoordinates = "";

		// when the location is detected automatically
		CardView cardHoldDetectedLocation = view.findViewById(R.id.cardHoldDetectedLocation);
		cardHoldDetectedLocation.setOnClickListener(v -> getLocationAutomatic());

		EditText etEnterLocationManually = view.findViewById(R.id.etEnterLocationManually);
		etEnterLocationManually.setVisibility(View.GONE);
		isEnterLocationManuallyBtnClickedOnce = false;

		TextView tvEnterLocationManually = view.findViewById(R.id.tvEnterLocationManually);

		// when the location is entered manually
		CardView cardHoldManualLocation = view.findViewById(R.id.cardHoldManualLocation);
		cardHoldManualLocation.setOnClickListener(v ->
		{

			if ( !isEnterLocationManuallyBtnClickedOnce)
			{

				isEnterLocationManuallyBtnClickedOnce = true;
				etEnterLocationManually.setVisibility(View.VISIBLE);

				tvEnterLocationManually.setText(R.string.click_again);

			}

			else
			{

				if (etEnterLocationManually.getText().toString().trim().isEmpty())
					Toast.makeText(mainActivity, "Enter a Location!", Toast.LENGTH_SHORT).show();

				else
				{

					MainActivity.progressDialog.show();
					MainActivity.progressDialog.setContentView(R.layout.layout_progress_dialog);

					currentLocationCoordinates = etEnterLocationManually.getText().toString().trim();

					NetworkingThread findCoordinatesFromLocation =
							new NetworkingThread("FindCoordinatesFromLocation", mainActivity);
					ExecutorService exec = Executors.newSingleThreadExecutor();
					Handler handler = new Handler(Looper.getMainLooper());
					exec.execute(() -> {

						findCoordinatesFromLocation.init();

						handler.post(() -> {

							MainActivity.coordinatesOfLocationAfterAllSteps = currentLocationCoordinates;
							mainActivity.doAfterLocationHasBeenFetched();

						});

					});

				}

			}

		});

		return view;
	}

	public void getLocationAutomatic()
	{

		Log.d("CustomLog", "FetchLocationFragment#getLocationAutomatic");

		if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{

			String[] permissions = new String[] {Manifest.permission.ACCESS_FINE_LOCATION};
			ActivityCompat.requestPermissions(mainActivity, permissions, 1);

			return;

		}

		fusedLocationProviderClient.getLastLocation().addOnSuccessListener(mainActivity, location ->
		{

			if (location != null)
			{

				currentLocationCoordinates = location.getLatitude() + "," + location.getLongitude();

				MainActivity.coordinatesOfLocationAfterAllSteps = currentLocationCoordinates;
				mainActivity.doAfterLocationHasBeenFetched();

			}

			else
				Toast.makeText(mainActivity, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();

		});

	}

}