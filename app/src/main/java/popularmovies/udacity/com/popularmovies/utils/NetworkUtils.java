package popularmovies.udacity.com.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import popularmovies.udacity.com.popularmovies.R;

public class NetworkUtils {

    final static String MOVIEBD_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String PARAM_MOVIEDB_API = "api_key";
    final static String moviedb_api = "fb3ae32b98663668e866118ececfba72";

    public static URL buildUrl(String moviedbSearchQuery) {
        Uri builtUri = Uri.parse(MOVIEBD_BASE_URL).buildUpon()
                .appendPath(moviedbSearchQuery)
                .appendQueryParameter(PARAM_MOVIEDB_API, moviedb_api)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
