package out_of_scope_services.payment_systems.payment_provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentProviderFactory {
    private final Map<String, PaymentProvider> paymentProviders;

    @Autowired
    public PaymentProviderFactory(Map<String, PaymentProvider> paymentProviders) {
        this.paymentProviders = paymentProviders;
    }

    public PaymentProvider getProvider(String paymentMethod) {
        PaymentProvider provider = paymentProviders.get(paymentMethod.toLowerCase());
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }
        return provider;
    }
}