package popularmovies.udacity.com.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import popularmovies.udacity.com.popularmovies.data.MoviesContract;

public class CustomCursorAdapter
        extends MovieAdapter {
    private static final String TAG = CustomCursorAdapter.class.getSimpleName();

    // Class variables for Cursor that holds favorite movie data and the Context
    private Cursor mCursor;
    private Context mContext;

    // Constructor that initializes the Context
    public CustomCursorAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public void setID() {

    }


    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {


        int imageUrlIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_IMAGE_URL);
        int idIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID);
        Log.d(TAG, "onBindViewHolder: title index" + imageUrlIndex);

        // Get to the right location in the cursor
        mCursor.moveToPosition(position);

        // Get the wanted values
        String image_url = mCursor.getString(imageUrlIndex);
        String id = mCursor.getString(idIndex);
        Log.d(TAG, "onBindViewHolder: title" + image_url);
        Log.d(TAG, "onBindViewHolder: id" + id);

        // Set title text and id
        holder.bind(image_url);
        holder.setMovieId(id);

    }

    @Override
    public int getItemCount() {

        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // Check if this Cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            // Nothing has changed
            return null;
        }

        // New cursor value assigned
        Cursor temp = mCursor;
        this.mCursor = c;

        // Check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }

        return temp;
    }
}
