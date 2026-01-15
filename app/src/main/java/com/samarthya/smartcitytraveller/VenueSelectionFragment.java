package com.samarthya.smartcitytraveller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class VenueSelectionFragment extends Fragment {

	public VenueSelectionFragment() {
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
		View view = inflater.inflate(R.layout.fragment_venue_selection, container, false);

		MainActivity.progressDialog.hide();

		// get the ActionBar and set what to select currently
		MainActivity mainActivity = (MainActivity) view.getContext();

		if (mainActivity.selectedInterestsList.size() != 0) {
			Objects.requireNonNull(mainActivity.getSupportActionBar()).setTitle(
					"Select venue: " + mainActivity.selectedInterestsList.get(0));
		}

		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		VenueSelectionAdapter venueSelectionAdapter =
				new VenueSelectionAdapter(NetworkingThread.interests, mainActivity);
		recyclerView.setAdapter(venueSelectionAdapter);

		return view;

	}
}