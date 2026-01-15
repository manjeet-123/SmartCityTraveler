package com.samarthya.smartcitytraveller;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samarthya.smartcitytraveller.databinding.ActivityDestinationMapsBinding;

public class DestinationMapsActivity extends FragmentActivity implements OnMapReadyCallback
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		Log.d("CustomLog", "DestinationMapsActivity#onCreate");

		com.samarthya.smartcitytraveller.databinding.ActivityDestinationMapsBinding binding = ActivityDestinationMapsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		assert mapFragment != null;
		mapFragment.getMapAsync(this);

	}

	@Override
	public void onMapReady(@NonNull GoogleMap googleMap)
	{

		Log.d("CustomLog", "DestinationMapsActivity#onMapReady");

		// store all the destinations in the LatLng array
		LatLng[] destinations = new LatLng[MainActivity.venues.size() + 1];
		for (int i = 0; i < destinations.length - 1; i++)
		{

			destinations[i] = new LatLng
					(Double.parseDouble(MainActivity.venues.get(i).lat),
							Double.parseDouble(MainActivity.venues.get(i).lng));

			googleMap.addMarker(new MarkerOptions()
					.position(destinations[i])
					.title(i + 1 + ". " + MainActivity.venues.get(i).name));

		}

		// last element is the current position
		String[] currLatLng = MainActivity.coordinatesOfLocationAfterAllSteps.split(",");
		destinations[destinations.length - 1] = new LatLng
				(Double.parseDouble(currLatLng[0]), Double.parseDouble(currLatLng[1]));

		// current position marker will be blue in color
		googleMap.addMarker(new MarkerOptions()
				.position(destinations[destinations.length - 1])
				.title("Current Position")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

		// auto zoom animation to current position on the map, the zoom level can vary from 0 - 21
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinations[destinations.length - 1], 10.0f));

	}

}