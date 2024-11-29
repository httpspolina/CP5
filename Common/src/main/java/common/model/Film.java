package common.model;

import lombok.Data;

import java.util.List;

@Data
public class Film {
    private Integer id;
    private String title;
    private String country;
    private Integer year;
    private String director;
    private String roles;
    private String genre;
    private String description;
    private String posterUrl;

    private Double rating;
    private List<Review> reviews;
}
