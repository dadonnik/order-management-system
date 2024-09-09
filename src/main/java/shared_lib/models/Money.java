package shared_lib.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public final class Money {

    private String price;
    private String currency;
    private int fraction;

    @Transient
    private BigDecimal amount;

    @Transient
    private String formatted;

    public Money() {
    }

    public Money(String price, String currency, int fraction) {
        this.price = price;
        this.currency = currency;
        this.fraction = fraction;
        this.amount = new BigDecimal(price).movePointLeft(fraction);
        this.formatted = toString();
    }

    public Money(String price) {
        this(price, "AED", 2);
    }

    @PostLoad
    public void postLoad() {
        this.amount = new BigDecimal(price).movePointLeft(fraction);
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
        return new BigDecimal(price).intValue();  // Integer representation of the price
    }

    @Override
    public String toString() {
        return String.format("%." + fraction + "f %s", amount, currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getFormatted() {
        return formatted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money1 = (Money) o;
        return fraction == money1.fraction &&
                Objects.equals(price, money1.price) &&
                Objects.equals(currency, money1.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, currency, fraction);
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must match for addition.");
        }
        BigDecimal newAmount = this.amount.add(other.amount);
        return new Money(newAmount.movePointRight(fraction).toPlainString(), this.currency, this.fraction);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must match for subtraction.");
        }
        BigDecimal newAmount = this.amount.subtract(other.amount);
        return new Money(newAmount.movePointRight(fraction).toPlainString(), this.currency, this.fraction);
    }

    public Money multiply(int factor) {
        BigDecimal newAmount = this.amount.multiply(BigDecimal.valueOf(factor));
        return new Money(newAmount.movePointRight(fraction).toPlainString(), this.currency, this.fraction);
    }
}