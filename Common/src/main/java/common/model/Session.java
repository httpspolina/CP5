package common.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
public class Session implements Serializable {
    private Integer id;
    private Integer filmId;
    private Integer hallId;
    private Timestamp date;

    private Integer seats; // вместимость зала
    private Set<Integer> occupiedSeats = new HashSet<>(); // номера занятых мест
}
