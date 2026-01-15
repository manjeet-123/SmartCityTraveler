package com.samarthya.smartcitytraveller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

class ImageToShow
{

	ImageView imageView;
	int image;
	int imageChecked;
	boolean isChecked;
	String typeOfPlace;

	public ImageToShow(ImageView imageView, int image, int imageChecked, boolean isChecked, String typeOfPlace)
	{

		this.imageView = imageView;
		this.image = image;
		this.imageChecked = imageChecked;
		this.isChecked = isChecked;
		this.typeOfPlace = typeOfPlace;

	}

}

public class WhereToVisitSelectionActivity extends AppCompatActivity
{

	private ImageToShow[] images;
	private ArrayList<String> typeOfPlaces;
	private int searchRadius;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_where_to_visit_selection);

		searchRadius = 30; // default search radius is 40kms
		Log.d("CustomLog", "WhereToVisitSelectionActivity#onCreate");
		MainActivity.progressDialog.hide();

		typeOfPlaces = new ArrayList<>();

		images = new ImageToShow[21];
		images[0] = new ImageToShow(findViewById(R.id.iv00), R.drawable.restaurant, R.drawable.restaurantchecked, false, "restaurant");
		images[1] = new ImageToShow(findViewById(R.id.iv01), R.drawable.bakery, R.drawable.bakerychecked, false, "bakery");
		images[2] = new ImageToShow(findViewById(R.id.iv02), R.drawable.coffee, R.drawable.coffeechecked, false, "coffee");
		images[3] = new ImageToShow(findViewById(R.id.iv10), R.drawable.taj_mahal, R.drawable.taj_mahalchecked, false, "monument");
		images[4] = new ImageToShow(findViewById(R.id.iv11), R.drawable.snacks, R.drawable.snackschecked, false, "snacks");
		images[5] = new ImageToShow(findViewById(R.id.iv12), R.drawable.shopping, R.drawable.shoppingchecked, false, "shopping");
		images[6] = new ImageToShow(findViewById(R.id.iv20), R.drawable.temple, R.drawable.templechecked, false, "temple");
		images[7] = new ImageToShow(findViewById(R.id.iv21), R.drawable.museum, R.drawable.museumchecked, false, "museum");
		images[8] = new ImageToShow(findViewById(R.id.iv22), R.drawable.view, R.drawable.viewchecked, false, "view");
		images[9] = new ImageToShow(findViewById(R.id.iv30), R.drawable.river, R.drawable.riverchecked, false, "river");
		images[10] = new ImageToShow(findViewById(R.id.iv31), R.drawable.zoo, R.drawable.zoochecked, false, "zoo");
		images[11] = new ImageToShow(findViewById(R.id.iv32), R.drawable.park, R.drawable.parkchecked, false, "park");
		images[12] = new ImageToShow(findViewById(R.id.iv40), R.drawable.movie, R.drawable.moviechecked, false, "movie");
		images[13] = new ImageToShow(findViewById(R.id.iv41), R.drawable.hotel, R.drawable.hotelchecked, false, "hotel");
		images[14] = new ImageToShow(findViewById(R.id.iv42), R.drawable.bar, R.drawable.barchecked, false, "bar");
		images[15] = new ImageToShow(findViewById(R.id.iv50), R.drawable.airport, R.drawable.airportchecked, false, "airport");
		images[16] = new ImageToShow(findViewById(R.id.iv51), R.drawable.railway, R.drawable.railwaychecked, false, "railway");
		images[17] = new ImageToShow(findViewById(R.id.iv52), R.drawable.bus, R.drawable.buschecked, false, "bus");
		images[18] = new ImageToShow(findViewById(R.id.iv60), R.drawable.tour, R.drawable.tourchecked, false, "tour");
		images[19] = new ImageToShow(findViewById(R.id.iv61), R.drawable.hospital, R.drawable.hospitalchecked, false, "hospital");

		// submit button
		images[20] = new ImageToShow(findViewById(R.id.ivSubmit), -1, -1, false, "submit");

		images[0].imageView.setOnClickListener(v -> setUnCheckedImages(0));
		images[1].imageView.setOnClickListener(v -> setUnCheckedImages(1));
		images[2].imageView.setOnClickListener(v -> setUnCheckedImages(2));
		images[3].imageView.setOnClickListener(v -> setUnCheckedImages(3));
		images[4].imageView.setOnClickListener(v -> setUnCheckedImages(4));
		images[5].imageView.setOnClickListener(v -> setUnCheckedImages(5));
		images[6].imageView.setOnClickListener(v -> setUnCheckedImages(6));
		images[7].imageView.setOnClickListener(v -> setUnCheckedImages(7));
		images[8].imageView.setOnClickListener(v -> setUnCheckedImages(8));
		images[9].imageView.setOnClickListener(v -> setUnCheckedImages(9));
		images[10].imageView.setOnClickListener(v -> setUnCheckedImages(10));
		images[11].imageView.setOnClickListener(v -> setUnCheckedImages(11));
		images[12].imageView.setOnClickListener(v -> setUnCheckedImages(12));
		images[13].imageView.setOnClickListener(v -> setUnCheckedImages(13));
		images[14].imageView.setOnClickListener(v -> setUnCheckedImages(14));
		images[15].imageView.setOnClickListener(v -> setUnCheckedImages(15));
		images[16].imageView.setOnClickListener(v -> setUnCheckedImages(16));
		images[17].imageView.setOnClickListener(v -> setUnCheckedImages(17));
		images[18].imageView.setOnClickListener(v -> setUnCheckedImages(18));
		images[19].imageView.setOnClickListener(v -> setUnCheckedImages(19));

		// submit button
		images[20].imageView.setOnClickListener(v ->
		{

			if (typeOfPlaces.size() == 0)
			{

				Toast.makeText(WhereToVisitSelectionActivity.this,
						"At least 1 Interest should be Selected!", Toast.LENGTH_SHORT).show();

				return;

			}

			// add the search radius as the last argument
			typeOfPlaces.add(searchRadius + "");

			// selected interests are stored in an ArrayList<String>
			Log.d("places, A2", String.valueOf(typeOfPlaces));

			Intent intent = new Intent();
			intent.putStringArrayListExtra("SelectedInterests", typeOfPlaces);

			// set the layout of progress bar here
			MainActivity.progressDialog.show();
			MainActivity.progressDialog.setContentView(R.layout.layout_progress_dialog);

			setResult(RESULT_OK, intent);
			WhereToVisitSelectionActivity.this.finish();

		});

		SeekBar radiusBar = findViewById(R.id.radiusBar);
		radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{

			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b)
			{

				String titleOfActionBarWithRadius = "Select Interests (" + i + "km)";
				searchRadius = i;
				Objects.requireNonNull(getSupportActionBar()).setTitle(titleOfActionBarWithRadius);

			}

			@Override public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override public void onStopTrackingTouch(SeekBar seekBar) {}

		});

	}

	public void setUnCheckedImages(int whichImage)
	{

		if ( !images[whichImage].isChecked)
		{

			if (typeOfPlaces.size() == 3)
				Toast.makeText(WhereToVisitSelectionActivity.this, "Max 3 Interests can be Selected!", Toast.LENGTH_SHORT).show();

			else
			{

				images[whichImage].imageView.setImageResource(images[whichImage].imageChecked);
				images[whichImage].isChecked = true;
				typeOfPlaces.add(images[whichImage].typeOfPlace);

			}

		}

		else
		{

			images[whichImage].imageView.setImageResource(images[whichImage].image);
			images[whichImage].isChecked = false;
			typeOfPlaces.remove(images[whichImage].typeOfPlace);

		}

	}

}