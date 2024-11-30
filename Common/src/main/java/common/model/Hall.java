package common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Hall implements Serializable {
    private Integer id;
    private String name;
    private Integer seats;
    private Float price;
}
