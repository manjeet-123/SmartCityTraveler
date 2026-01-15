package com.samarthya.smartcitytraveller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VenueSelectionAdapter extends RecyclerView.Adapter<VenueSelectionAdapter.ViewHolder>
{

	private final ArrayList<Interest> localDataSet;
	private static MainActivity mainActivity;

	public static class ViewHolder extends RecyclerView.ViewHolder
	{

		private final TextView tvName;
		private final TextView tvType;
		private final TextView tvDistance;

		public ViewHolder(View view)
		{

			super(view);

			// Define click listener for the ViewHolder's View
			view.setOnClickListener(v ->
			{

				int index = getAdapterPosition();
				Toast.makeText(view.getContext(), NetworkingThread.interests.get(index).name, Toast.LENGTH_SHORT).show();

				// put the selected Interest into MainActivity.venues
				MainActivity.venues.add(NetworkingThread.interests.get(index));

				// start the next thread if more items present in MainActivity.selectedInterestsList
				if (mainActivity.selectedInterestsList.size() > 1) // because last element is the search radius
				{

					mainActivity.selectedInterestsList.remove(0);
					String nameOfThread;
					nameOfThread = "ExtractSelectedInterestInformation" + mainActivity.selectedInterestsList.get(0);
					NetworkingThread networkingThread = new NetworkingThread(nameOfThread, mainActivity);
					ExecutorService exec = Executors.newSingleThreadExecutor();
					exec.execute(networkingThread::init);

					Log.d("places", mainActivity.selectedInterestsList +
							" " + MainActivity.venues.get(MainActivity.venues.size() - 1).name);

					MainActivity.progressDialog.show();

					if (mainActivity.selectedInterestsList.size() == 1)
					{

						Log.d("places", "start the maps activity now");
						MainActivity mainActivity = (MainActivity) view.getContext();
						mainActivity.startDestinationMapsActivity();

					}

				}

			});

			tvName = view.findViewById(R.id.tvName);
			tvType = view.findViewById(R.id.tvType);
			tvDistance = view.findViewById(R.id.tvDistance);

		}

		public TextView getTvName() {
			return tvName;
		}

		public TextView getTvType() {
			return tvType;
		}

		public TextView getTvDistance() {
			return tvDistance;
		}

	}

	public VenueSelectionAdapter(ArrayList<Interest> dataSet, MainActivity mainActivity) {
		localDataSet = dataSet;
		VenueSelectionAdapter.mainActivity = mainActivity;
	}

	// Create new views (invoked by the layout manager)
	@NonNull @Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
	{

		// Create a new view, which defines the UI of the list item
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.venue_selection_adapter_item, viewGroup, false);

		return new ViewHolder(view);

	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position)
	{

		// Get element from your dataset at this position and replace the
		// contents of the view with that element

		String nameToDisplay;
		if (localDataSet.get(position).name.length() >= 20) {
			nameToDisplay = localDataSet.get(position).name.substring(0, 19) + "...";
		} else {
			nameToDisplay = localDataSet.get(position).name;
		}

		String distanceToDisplay = localDataSet.get(position).distance + "km";

		viewHolder.getTvName().setText(nameToDisplay);
		viewHolder.getTvType().setText(localDataSet.get(position).type);
		viewHolder.getTvDistance().setText(distanceToDisplay);

	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return localDataSet.size();
	}

}
