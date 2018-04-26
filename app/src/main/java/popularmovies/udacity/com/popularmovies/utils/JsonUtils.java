package popularmovies.udacity.com.popularmovies.utils;

import org.json.JSONObject;

import popularmovies.udacity.com.popularmovies.Movie;

public class JsonUtils {

    public static Movie parseMovieJson(String json) {

        Movie sMovie = new Movie();

        try {
            JSONObject movie = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sMovie;

    }
}
