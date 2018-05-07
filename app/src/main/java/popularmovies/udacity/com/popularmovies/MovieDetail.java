package popularmovies.udacity.com.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class MovieDetail extends AppCompatActivity {

    private static final String MOVIE_DETAILS = "movie";
    private final String moviedbBaseUrl = "http://image.tmdb.org/t/p/w185";

    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mPlotSynopsis;
    private TextView mVoteAverage;
    private ImageView mPoster;
    private JSONObject mMovieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitle = (TextView) findViewById(R.id.movie_title_tv);
        mReleaseDate = (TextView) findViewById(R.id.movie_release_date_tv);
        mPlotSynopsis = (TextView) findViewById(R.id.movie_plot_synopsis_tv);
        mVoteAverage = (TextView) findViewById(R.id.movie_vote_average_tv);
        mPoster = (ImageView) findViewById(R.id.movie_poster_iv);

        try {
            mMovieDetails = new JSONObject(getIntent().getStringExtra(MOVIE_DETAILS));
            String imageUrl = mMovieDetails.getString("poster_path");

            Picasso.with(this)
                    .load(moviedbBaseUrl + imageUrl)
                    .into(mPoster);
            mTitle.setText(mMovieDetails.getString("title") +"\n");
            mPlotSynopsis.setText(mMovieDetails.getString("overview") + "\n");
            mReleaseDate.setText(mMovieDetails.getString("release_date") + "\n");
            mVoteAverage.setText(mMovieDetails.getString("vote_average") + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
