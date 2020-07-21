
public class Tool extends Carryable {

    public static final int ROPE = 21;
    public static final int MACHETE = 22;
    public static final int TORCH = 23;
    public static final int GLASSBALL = 24;

    public static final int[] values = {ROPE, MACHETE, TORCH, GLASSBALL};

    public Tool(int type) {
        super(type);
        setProperties(type);
    }

    private void setProperties(int toolType) {
        switch (toolType) {
            case Tool.ROPE:
                super.name = "rope";
                super.price = 40;
                super.tradeValue.sellingValue = TradeValue.LOW;
                super.tradeValue.buyingValue = TradeValue.MEDIUM;
                break;
            case Tool.MACHETE:
                super.name = "machete";
                super.price = 20;
                super.tradeValue.sellingValue = TradeValue.HIGH;
                break;
            case Tool.TORCH:
                super.name = "torch";
                super.price = 30;
                super.tradeValue.sellingValue = TradeValue.LOW;
                super.tradeValue.buyingValue = TradeValue.MEDIUM;
                break;
            case Tool.GLASSBALL:
                super.name = "glassball";
                super.price = 15;
                super.tradeValue.sellingValue = TradeValue.HIGH;
                break;
            default:
                super.name = "unknown tool";
                break;
        }
    }
}