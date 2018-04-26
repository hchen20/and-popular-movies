package popularmovies.udacity.com.popularmovies;

public class Movie {

    private String title;
    private String thumbnail;
    private String synopsis;
    private int rating;
    private String releaseDate;

    public Movie() {

    }

    public Movie(String title, String thumbnail, String synopsis, int rating, String releaseDate) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
