package popularmovies.udacity.com.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    /* Add content provider constants to the Contract
        1) Content authority
        2) Base content uri
        3) Path(s) to the movies directory
        4) Content uri for data in the MovieEntry class
     */

    // The authority for Content Provider to access
    public static final String AUTHORITY = "popularmovies.udacity.com.popularmovies";

    // The base content uri = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Path for the "movies" directory
    public static final String PATH_MOVIES = "movies";

    // MovieEntry is an inner class that defines the contents of the movie table
    public static final class MovieEntry implements BaseColumns {

        // MovieEntry content uri = base content uri + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                                                .appendPath(PATH_MOVIES)
                                                .build();

        // Table name
        public static final String TABLE_NAME = "movie";

        // Columns
        public static final String _ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_IMAGE_URL = "movie_image_url";

        /*

        Movies:

        _id     movie_id    title        movie url
         1      123         Iron Man     asd1293ijd
         2      332         Thor         1238123jfse
         .
         .
         .
         */
    }

}
