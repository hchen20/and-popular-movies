package popularmovies.udacity.com.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import popularmovies.udacity.com.popularmovies.data.MoviesContract;

public class MovieDetail extends AppCompatActivity {
    private static final String TAG = MovieDetail.class.getSimpleName();
    private static final String MOVIE_DETAILS = "movie";
    private static final String MOVIE_REVIEWS = "reviews";
    private static final String MOVIE_VIEDOS = "videos";
    private final String moviedbBaseUrl = "http://image.tmdb.org/t/p/w185";
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mPlotSynopsis;
    private TextView mVoteAverage;
    private ImageView mPoster;
    private Button mFavorite;
    private JSONObject mMovieDetails;
    private JSONArray mMovieReviews;
    private JSONArray mMovieTrailers;
    private ArrayList<String> mTrailerNames;
    private ArrayList<String> mTrailerKeys;


    private ListView mTrailers;
    private TextView mReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitle = (TextView) findViewById(R.id.movie_title_tv);
        mReleaseDate = (TextView) findViewById(R.id.movie_release_date_tv);
        mPlotSynopsis = (TextView) findViewById(R.id.movie_plot_synopsis_tv);
        mVoteAverage = (TextView) findViewById(R.id.movie_vote_average_tv);
        mPoster = (ImageView) findViewById(R.id.movie_poster_iv);
        mFavorite = (Button) findViewById(R.id.movie_favorite_button);
        mReviews = (TextView) findViewById(R.id.movie_reviews_tv);
        mTrailers = (ListView) findViewById(R.id.movie_videos_lv);

        mTrailerNames = new ArrayList<String>();
        mTrailerKeys = new ArrayList<String>();

        try {
            mMovieDetails = new JSONObject(getIntent().getStringExtra(MOVIE_DETAILS));
            mMovieReviews = new JSONArray(getIntent().getStringExtra(MOVIE_REVIEWS));
            mMovieTrailers = new JSONArray(getIntent().getStringExtra(MOVIE_VIEDOS));
            String imageUrl = mMovieDetails.getString("poster_path");

            Picasso.with(this)
                    .load(moviedbBaseUrl + imageUrl)
                    .into(mPoster);
            mTitle.setText(mMovieDetails.getString("title") +"\n");
            mPlotSynopsis.setText(mMovieDetails.getString("overview") + "\n");
            mReleaseDate.setText(mMovieDetails.getString("release_date") + "\n");
            mVoteAverage.setText(mMovieDetails.getString("vote_average") + "\n");
            setMovieReviews();
            setMovieTrailers();


            ArrayAdapter<String> listviewAdpter = new ArrayAdapter<String>(
                                this,
                                android.R.layout.simple_list_item_1,
                                android.R.id.text1,
                                mTrailerNames);
            mTrailers.setAdapter(listviewAdpter);

            setListViewHeightBasedOnChildren(mTrailers);
            mTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MovieDetail.this, "Playing trailer", Toast.LENGTH_SHORT).show();
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(YOUTUBE_URL+mTrailerKeys.get(position)));
                    getApplicationContext().startActivity(webIntent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        View.OnClickListener favoriteButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean alreadyFavorite = true;
                if (alreadyFavorite) {
                    return;
                } else {
                    try {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, mMovieDetails.getString("id"));
                        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, mMovieDetails.getString("title"));

                        // Insert the content values via a ContentResolver
                        Uri uri = getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, contentValues);

                        // Display the URI that's returned with a Toast

                    } catch (Exception e) {
                        Log.d(TAG, "onClick: " + e.getMessage());
                    }
                }
                mFavorite.setText("Already Favorited");

            }
        };

        mFavorite.setOnClickListener(favoriteButtonListener);
    }

    private void setMovieReviews() {
        try {
            for (int i = 0; i < mMovieReviews.length(); i++) {
                JSONObject row = mMovieReviews.getJSONObject(i);
                String reviewContent = row.getString("content");
                mReviews.append(reviewContent + "\n\n" );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMovieTrailers() {
        try {
            for (int i = 0; i < mMovieTrailers.length(); i++) {
                JSONObject row = mMovieTrailers.getJSONObject(i);
                Log.d(TAG, "setMovieTrailers: " + row.toString());
                mTrailerNames.add(row.getString("name"));
                mTrailerKeys.add(row.getString("key"));
            }
        } catch (Exception e) {
            Log.d(TAG, "setMovieTrailers: " + e.getMessage());
        }
    }


    // Source: https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
