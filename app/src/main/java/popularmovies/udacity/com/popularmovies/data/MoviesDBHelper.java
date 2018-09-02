package popularmovies.udacity.com.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import popularmovies.udacity.com.popularmovies.data.MoviesContract.MovieEntry;

public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final String TAG = MoviesDBHelper.class.getSimpleName();

    // The name of the database
    private static final String DATABASE_NAME = "movies.db";

    // Increment the database version to change the database schema
    private static final int DATABASE_VERSION = 2;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Called when the movies database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create movies table carefully follow SQL formatting rules
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_IMAGE_URL + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    // This method discards the old table of data and calls onCreate to recreate a new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "onUpgrade: " + oldVersion + " to " + newVersion +
            ". OLD DATA WILL BE DESTROYED");

        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);

    }
}
