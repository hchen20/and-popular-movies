package popularmovies.udacity.com.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import popularmovies.udacity.com.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter mMovieAdapter;
    private RecyclerView mMoviesGrid;
    private String mJsonResults;
    private JSONArray mJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGrid = (RecyclerView) findViewById(R.id.rv_movies);

        int numberOfColumns = 4;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mMoviesGrid.setLayoutManager(layoutManager);

        mMoviesGrid.setHasFixedSize(true);

        makeMoviedbSearchQuery("popular");

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        mMovieAdapter = new MovieAdapter(mJsonArray);
        mMoviesGrid.setAdapter(mMovieAdapter);
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
            mMovieAdapter = new MovieAdapter(mJsonArray);
            mMoviesGrid.setAdapter(mMovieAdapter);
            return true;
        } else if (itemClickedId == R.id.action_top_rated) {
            makeMoviedbSearchQuery("top_rated");
            mMovieAdapter = new MovieAdapter(mJsonArray);
            mMoviesGrid.setAdapter(mMovieAdapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
