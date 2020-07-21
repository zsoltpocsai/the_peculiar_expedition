
public class Food extends Carryable {

    public static final int FRUIT = 11;
    public static final int MEAT = 12;
    public static final int CHOCOLATE = 13;
    public static final int WHISKEY = 14;
    public static final int DROG = 15;

    public static final int[] values = {FRUIT, MEAT, CHOCOLATE, WHISKEY, DROG};
    public static final int[] edibles = {FRUIT, MEAT, CHOCOLATE};
    public static final int[] drinkables = {WHISKEY};
    public static final int[] usables = {DROG};

    private int energy;

    public Food(int type) {
        super(type);
        setProperties(type);
    }

    public static boolean isEdible(int type) {
        for (int i : edibles) {
            if (i == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDrinkable(int type) {
        for (int i : drinkables) {
            if (i == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUsable(int type) {
        for (int i : usables) {
            if (i == type) {
                return true;
            }
        }
        return false;
    }

    private void setProperties(int foodType) {
        switch (foodType) {
            case Food.FRUIT:
                super.name = "fruit";
                super.tradeValue.buyingValue = TradeValue.LOW;
                super.tradeValue.sellingValue = TradeValue.LOW;
                super.price = 5;
                energy = 15;
                break;
            case Food.MEAT:
                super.name = "meat";
                super.tradeValue.buyingValue = TradeValue.MEDIUM;
                super.tradeValue.sellingValue = TradeValue.LOW;
                super.price = 10;
                energy = 25;
                break;
            case Food.CHOCOLATE:
                super.name = "chocolate";
                super.tradeValue.sellingValue = TradeValue.HIGH;
                super.price = 13;
                energy = 20;
                break;
            case Food.WHISKEY:
                super.name = "whiskey";
                super.tradeValue.sellingValue = TradeValue.HIGH;
                super.price = 8;
                energy = 20;
                break;
            case Food.DROG:
                super.name = "drog";
                super.tradeValue.buyingValue = TradeValue.MEDIUM;
                super.tradeValue.sellingValue = TradeValue.LOW;
                super.price = 30;
                energy = 20;
                break;
            default:
                super.name = "unknown food";
                energy = 0;
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void multipleEnergy(float f) {
        energy *= f;
    }
}