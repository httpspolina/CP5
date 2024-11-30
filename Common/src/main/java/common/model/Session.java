package common.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Session implements Serializable {
    private Integer id;
    private Integer filmId;
    private Integer hallId;
    private Timestamp date;
}
