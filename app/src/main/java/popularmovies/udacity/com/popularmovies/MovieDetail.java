package popularmovies.udacity.com.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MovieDetail extends AppCompatActivity {

    private static final String MOVIE_DETAILS = "movie";
    private static final String MOVIE_REVIEWS= "reviews";
    private final String moviedbBaseUrl = "http://image.tmdb.org/t/p/w185";

    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mPlotSynopsis;
    private TextView mVoteAverage;
    private ImageView mPoster;
    private Button mFavorite;
    private JSONObject mMovieDetails;
    private JSONArray mMovieReviews;


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

        try {
            mMovieDetails = new JSONObject(getIntent().getStringExtra(MOVIE_DETAILS));
            mMovieReviews = new JSONArray(getIntent().getStringExtra(MOVIE_REVIEWS));
            String imageUrl = mMovieDetails.getString("poster_path");

            Picasso.with(this)
                    .load(moviedbBaseUrl + imageUrl)
                    .into(mPoster);
            mTitle.setText(mMovieDetails.getString("title") +"\n");
            mPlotSynopsis.setText(mMovieDetails.getString("overview") + "\n");
            mReleaseDate.setText(mMovieDetails.getString("release_date") + "\n");
            mVoteAverage.setText(mMovieDetails.getString("vote_average") + "\n");
            setMovieReviews();
        } catch (Exception e) {
            e.printStackTrace();
        }

        View.OnClickListener favoriteButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
