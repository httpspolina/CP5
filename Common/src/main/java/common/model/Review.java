package common.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Review implements Serializable {
    private Integer id;
    private Integer filmId;
    private Integer clientId;
    private Integer rating;
    private String description;
    private Timestamp createdAt;

    private Client client;
}
