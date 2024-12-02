package common.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class Order implements Serializable {
    private Integer id;
    private Integer clientId;
    private Integer paymentMethodId;
    private Integer sessionId;
    private Integer seatIndex;
    private OrderStatus status;
    private Timestamp createdAt;
}
