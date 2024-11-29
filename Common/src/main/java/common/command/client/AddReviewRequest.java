package common.command.client;

import common.command.Request;
import lombok.Data;

@Data
public class AddReviewRequest implements Request {
    private Integer filmId;
    private Integer rating;
    private String description;
}
