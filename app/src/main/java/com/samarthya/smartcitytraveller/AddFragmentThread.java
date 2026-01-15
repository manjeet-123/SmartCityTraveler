package com.samarthya.smartcitytraveller;

import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AddFragmentThread
{

	private static FragmentTransaction fragmentTransaction;
	private final String nameOfThread;
	private final MainActivity mainActivity;

	public AddFragmentThread(String nameOfThread, MainActivity mainActivity) {

		this.nameOfThread = nameOfThread;
		this.mainActivity = mainActivity;

	}

	public void init()
	{

		FragmentManager fragmentManager = mainActivity.fragmentManager;
		fragmentTransaction = fragmentManager.beginTransaction();

		switch (nameOfThread)
		{

			case "FetchLocationFragment":
				addFetchLocationFragment();
				break;

			case "VenueSelectionFragment":
				addVenueSelectionFragment();
				break;

		}

	}

	public void addFetchLocationFragment()
	{

		Log.d("CustomLog", "AddFragmentThread#addFetchLocationFragment");

		FetchLocationFragment fetchLocationFragment = new FetchLocationFragment();

		fragmentTransaction.replace(R.id.firstFragmentContainter, fetchLocationFragment);
		fragmentTransaction.addToBackStack("FirstScreenBackStack1");
		fragmentTransaction.commit();

	}

	public void addVenueSelectionFragment()
	{

		Log.d("CustomLog", "AddFragmentThread#addVenueSelectionFragment");

		VenueSelectionFragment venueSelectionFragment = new VenueSelectionFragment();

		fragmentTransaction.replace(R.id.firstFragmentContainter, venueSelectionFragment);
		fragmentTransaction.addToBackStack("FirstScreenBackStack1");
		fragmentTransaction.commit();

	}

}
