package popularmovies.udacity.com.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import popularmovies.udacity.com.popularmovies.data.MoviesContract;
import popularmovies.udacity.com.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity
                          implements LoaderManager.LoaderCallbacks<Cursor> {
    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int MOVIE_LOADER_ID = 0;

    // Member variables for UI and Intent extras
    private MovieAdapter mMovieAdapter;
    private CustomCursorAdapter mCustomAdapter;
    private GridLayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mMoviesGrid;
    private String mJsonResults;
    private JSONArray mJsonArray;
    private JSONArray mJsonReviews;
    private JSONArray mJsonVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGrid = (RecyclerView) findViewById(R.id.rv_movies);

        mCustomAdapter = new CustomCursorAdapter(this);
        int numberOfColumns = 4;
        layoutManager = new GridLayoutManager(this, numberOfColumns);
        linearLayoutManager = new LinearLayoutManager(this);
        mMoviesGrid.setLayoutManager(layoutManager);

        mMoviesGrid.setHasFixedSize(true);

        if (isOnline()) {
            makeMoviedbSearchQuery("popular");
            mMovieAdapter = new MovieAdapter(mJsonArray);
            mMoviesGrid.setAdapter(mMovieAdapter);
        } else {
            Toast.makeText(this, "Please connect to the Internet", Toast.LENGTH_LONG).show();
        }

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Re-queries for all favorite movies
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    private void makeMoviedbSearchQuery(String movieQuery) {
        URL moviedbSearchUrl = NetworkUtils.buildUrl(movieQuery);
        try {
            mJsonResults = new MoviedbAPIQueryTask().execute(moviedbSearchUrl).get();
            JSONObject movieJson = new JSONObject(mJsonResults);
            mJsonArray = movieJson.getJSONArray("results");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            // Initialize a Cursor, this will hold all the favorite movies data
            Cursor mMovieData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Nullable @Override
            public Cursor loadInBackground() {
                // Query and load all favorite movie data in the background; sort by title
                try {
                    return getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE);
                } catch (Exception e) {
                    Log.e(TAG, "loadInBackground: Failed to asynchronously load data");
                    Log.d(TAG, "loadInBackground: " + e.getMessage());
                    return null;
                }

            }

            // deliverResult send the result of the load to the registered listener
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        Log.d(TAG, "onLoadFinished: Size of Cursor is " + data.getCount());
        mCustomAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCustomAdapter.swapCursor(null);

    }


    public class MoviedbAPIQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                // testMoviedbApi_tv.setText(s);
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String moviedbSearchResults = null;

            try {
                moviedbSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return moviedbSearchResults;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickedId = item.getItemId();

        if (itemClickedId == R.id.action_popular) {
            makeMoviedbSearchQuery("popular");
            mMoviesGrid.setLayoutManager(layoutManager);
            mMovieAdapter = new MovieAdapter(mJsonArray);
            mMoviesGrid.setAdapter(mMovieAdapter);
            return true;
        } else if (itemClickedId == R.id.action_top_rated) {
            makeMoviedbSearchQuery("top_rated");
            mMoviesGrid.setLayoutManager(layoutManager);
            mMovieAdapter = new MovieAdapter(mJsonArray);
            mMoviesGrid.setAdapter(mMovieAdapter);
            return true;
        } else if (itemClickedId == R.id.action_favorite) {
            mMoviesGrid.setLayoutManager(linearLayoutManager);
            // mCustomAdapter = new CustomCursorAdapter(this);
            mMoviesGrid.setAdapter(mCustomAdapter);
            return true;
        }
        if (super.onOptionsItemSelected(item)) return true;
        else
            return false;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
