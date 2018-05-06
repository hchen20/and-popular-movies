package popularmovies.udacity.com.popularmovies;

import android.app.Activity;
import android.content.Context;
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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    JSONArray mJsonArray;

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
        TextView mMoviePoster;
        Context mContext;

        public MovieViewHolder(View itemView, Context context) {
            super(itemView);

            mMoviePoster = (TextView) itemView.findViewById(R.id.iv_item_movie);
            mContext = context;
        }

        void bind(String imageUrl) {
            mMoviePoster.setText(imageUrl);
        }
    }
}
