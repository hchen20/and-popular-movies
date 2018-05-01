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
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private static int viewHolderCount;

    private int mNumberItems;

    public MovieAdapter(int numberOfItems) {
        mNumberItems = numberOfItems;
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, attachParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        viewHolder.viewHolderIndex.setText("ViewHolder index: " + viewHolderCount);

        viewHolderCount++;

        Log.d(TAG, "onCreateViewHolder: number of view holders created: " + viewHolderCount);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: # " + position);
        holder.bind(position);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView listMovieNumberView;
        TextView viewHolderIndex;

        public MovieViewHolder(View itemView) {
            super(itemView);

            listMovieNumberView = (TextView) itemView.findViewById(R.id.tv_item_number);
            viewHolderIndex = (TextView) itemView.findViewById(R.id.tv_view_holder_instance);

        }

        void bind(int listIndex) {
            listMovieNumberView.setText(String.valueOf(listIndex));
        }
    }
}
