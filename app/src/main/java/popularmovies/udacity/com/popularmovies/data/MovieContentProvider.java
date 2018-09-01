package popularmovies.udacity.com.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static popularmovies.udacity.com.popularmovies.data.MoviesContract.MovieEntry.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {
    private static final String TAG = MovieContentProvider.class.getSimpleName();

    // Recommend to use 100, 200, 300, etc for directories and related ints (101, ..) for items
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Adding paths to the UriMatcher with corresponding ints
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return  uriMatcher;
    }

    private MoviesDBHelper mMoviesDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDBHelper = new MoviesDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying read-only database
        final SQLiteDatabase db = mMoviesDBHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for movies and movie directories and write a default case
        switch (match) {
            // Query for the movies directory
            case MOVIES:
                retCursor = db.query(TABLE_NAME,
                                projection,
                                selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Query for the movie directory
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                retCursor = db.query(TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                            null,
                            null,
                            sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Get access to the movie database to write new data
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();

        // URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                // Inserting values into the database
                long id = Long.parseLong(values.get(MoviesContract.MovieEntry.COLUMN_MOVIE_ID).toString());
                long something = db.insert(TABLE_NAME, null, values);
                Log.d(TAG, "insert: id " + id);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI, id);
                    Log.d(TAG, "insert: returnUri " + returnUri.toString());
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted uri
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the writable database and write URI matching
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        // Keep track of number of deleted tasks
        int moviesDeleted;

        // Delete a single row of data
        switch (match) {
            case MOVIE_WITH_ID:
                // Get the movie ID from the URI path
                String id = uri.getPathSegments().get(1);

                // Use selections/selectionArgs to filter this ID
                moviesDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a movie is deleted
        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of movie deleted
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
