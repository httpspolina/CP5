package common.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class PaymentMethod implements Serializable {
    private Integer id;
    private Integer clientId;
    private String cardNumber;
    private Date expiresAt;
    private String cardholder;
    private String cvv;
}
