package client.models;

import java.io.Serializable;

public class Film implements Serializable {
    private static final long serialVersionUID = 1L; // Рекомендуется добавить serialVersionUID
    private String title;
    private String country;
    private int year;
    private String director;
    private String roles;
    private String genre;
    private String description;
    private String posterUrl;

    public Film(String title, String country, int year, String director, String roles, String genre, String description, String posterUrl) {
        this.title = title;
        this.country = country;
        this.year = year;
        this.director = director;
        this.roles = roles;
        this.genre = genre;
        this.description = description;
        this.posterUrl = posterUrl;
    }

    // Геттеры для каждого поля
    public String getTitle() {
        return title;
    }

    public String getCountry() {
        return country;
    }

    public int getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getRoles() {
        return roles;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", year=" + year +
                ", director='" + director + '\'' +
                ", roles='" + roles + '\'' +
                ", genre='" + genre + '\'' +
                ", description='" + description + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }
}
