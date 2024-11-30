package common.command.client;

import common.command.Request;
import common.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentMethodRequest implements Request {
    private PaymentMethod paymentMethod;
}
