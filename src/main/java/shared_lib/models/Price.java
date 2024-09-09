package shared_lib.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;

import java.util.Objects;

@Embeddable
public final class Price {

    private String price;
    private String currency;
    private int fraction;

    @Transient
    private double amount;

    @Transient
    private String formatted;

    public Price() {
    }

    public Price(String price, String currency, int fraction) {
        this.price = price;
        this.currency = currency;
        this.fraction = fraction;
        this.amount = converted();
        this.formatted = toString();
    }

    public Price(String price) {
        this(price, "AED", 2);
    }

    @PostLoad
    public void postLoad() {
        this.amount = converted();
        this.formatted = toString();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getFraction() {
        return fraction;
    }

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }

    public int toInt() {
        return Integer.parseInt(price);
    }

    public double converted() {
        return toInt() / Math.pow(10, fraction);
    }

    @Override
    public String toString() {
        return String.format("%." + fraction + "f %s", converted(), currency);
    }

    public double getAmount() {
        return amount;
    }

    public String getFormatted() {
        return formatted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return fraction == price1.fraction &&
                Objects.equals(price, price1.price) &&
                Objects.equals(currency, price1.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, currency, fraction);
    }
}