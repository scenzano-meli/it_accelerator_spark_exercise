import java.util.Objects;

public class Currency {

    private String id;
    private String symbol;
    private String description;
    private int decimal_places;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDecimal_places() {
        return decimal_places;
    }

    public void setDecimal_places(int decimal_places) {
        this.decimal_places = decimal_places;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        Currency currency = (Currency) o;
        return decimal_places == currency.decimal_places &&
                Objects.equals (id, currency.id) &&
                Objects.equals (symbol, currency.symbol) &&
                Objects.equals (description, currency.description);
    }

}
