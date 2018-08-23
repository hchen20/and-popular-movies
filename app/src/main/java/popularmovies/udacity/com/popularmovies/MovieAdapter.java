package popularmovies.udacity.com.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.List;

import popularmovies.udacity.com.popularmovies.utils.NetworkUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private JSONArray mJsonArray;
    private String mMovieReviews;
    private JSONArray mMoveVideos;

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        try {
            JSONObject imageUrlJson = mJsonArray.getJSONObject(position);
            String imageUrl = imageUrlJson.getString("poster_path");
            holder.bind(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MovieAdapter(JSONArray jsonArray) {
        mJsonArray = jsonArray;
    }


    @Override
    public int getItemCount() {

        if (mJsonArray == null) {
            return 0;
        }
        return mJsonArray.length();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, attachParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view, context);

        return viewHolder;
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {
        // private static final String TAG = MovieViewHolder.class.getSimpleName();
        private static final String MOVIE_DETAILS = "movie";
        private static final String MOVIE_REVIEWS= "reviews";
        private static final String MOVIE_VIDEOS= "videos";
        ImageView mMoviePoster;
        Context mContext;

        public MovieViewHolder(View itemView, Context context) {
            super(itemView);

            mMoviePoster = (ImageView) itemView.findViewById(R.id.iv_item_movie);
            mContext = context;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        JSONObject detailedMovieInfo = mJsonArray.getJSONObject(position);

                        String movieId = detailedMovieInfo.getString("id");

                        makeMovieReviewsQuery(movieId);
                        Class detailedMovieActivity = MovieDetail.class;

                        Intent startDetailedMovieIntent = new Intent(mContext, detailedMovieActivity);
                        startDetailedMovieIntent.putExtra(MOVIE_DETAILS ,mJsonArray.optString(position));
                        startDetailedMovieIntent.putExtra(MOVIE_REVIEWS, mMovieReviews);
                        mContext.startActivity(startDetailedMovieIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
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


        private void makeMovieReviewsQuery(String movieQuery) {
            String reviewsPath = "reviews";
            URL moviedbSearchUrl = NetworkUtils.buildUrl(movieQuery, reviewsPath);
            Log.d(TAG, "makeMovieReviewsQuery: "+ moviedbSearchUrl.toString());

            try {
                String reviews = new MoviedbAPIQueryTask().execute(moviedbSearchUrl).get();
                JSONObject reviewJson = new JSONObject(reviews);
                mMovieReviews = reviewJson.getJSONArray("results").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void bind(String imageUrl) {
            String moviedbBaseUrl = "http://image.tmdb.org/t/p/w185";
            String fullImageUrl = moviedbBaseUrl + imageUrl;

            Picasso.with(mContext)
                    .load(fullImageUrl)
                    .into(mMoviePoster);

        }
    }
}
