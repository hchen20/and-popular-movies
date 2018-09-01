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

import popularmovies.udacity.com.popularmovies.data.MoviesContract;

public class CustomCursorAdapter
        extends RecyclerView.Adapter<CustomCursorAdapter.MovieViewHolder> {
    private static final String TAG = CustomCursorAdapter.class.getSimpleName();

    // Class variables for Cursor that holds favorite movie data and the Context
    private Cursor mCursor;
    private Context mContext;

    // Constructor that initializes the Context
    public CustomCursorAdapter(Context context) {
        this.mContext = context;
    }

    // Called when ViewHolders are created to fill a RecyclerView
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the favorite_movie_layout to a view
        View view = LayoutInflater.from(mContext)
                        .inflate(R.layout.favorite_movie_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {


        int titleIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE);
        Log.d(TAG, "onBindViewHolder: title index" + titleIndex);

        // Get to the right location in the cursor
        mCursor.moveToPosition(position);

        // Get the wanted values
        String title = mCursor.getString(titleIndex);
        Log.d(TAG, "onBindViewHolder: title" + title);

        // Set title text
        holder.favoriteMovieTitleView.setText(title);

    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in
     */
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

    // Returns the number of items to display
    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    // Inner class for creating ViewHolders
    class MovieViewHolder extends RecyclerView.ViewHolder {
        // Class variable for favorite movie titles
        TextView favoriteMovieTitleView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            favoriteMovieTitleView = (TextView) itemView.findViewById(R.id.favorite_movie_title);
        }
    }
}
