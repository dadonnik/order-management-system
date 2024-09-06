package out_of_scope_services.payment_systems.payment_provider;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Configuration
public class PaymentConfig {

    @Autowired
    private StripePaymentProvider stripePaymentProvider;

    @Autowired
    private PayPalPaymentProvider payPalPaymentProvider;

    @Bean
    public Map<PaymentMethod, PaymentProvider> paymentProviders() {
        return Map.of(
                PaymentMethod.STRIPE, stripePaymentProvider,
                PaymentMethod.PAYPAL, payPalPaymentProvider
        );
    }
}
