
public class Carryable {

    protected int type;
    protected String name;
    protected TradeValue tradeValue;
    protected int price;

    protected Carryable(int type) {
        this.type = type;
        tradeValue = new TradeValue();
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public TradeValue getTradeValue() {
        return tradeValue;
    }
}