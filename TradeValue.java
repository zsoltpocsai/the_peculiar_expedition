public class TradeValue {

    public static final int NO_VALUE = 0;
    public static final int LOW = 1;
    public static final int MEDIUM = 2;
    public static final int HIGH = 4;

    public int sellingValue;
    public int buyingValue;

    public TradeValue() {
        sellingValue = NO_VALUE;
        buyingValue = NO_VALUE;
    }
}
