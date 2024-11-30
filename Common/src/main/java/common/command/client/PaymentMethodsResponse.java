package common.command.client;

import common.command.SuccessResponse;
import common.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodsResponse implements SuccessResponse {
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
}
