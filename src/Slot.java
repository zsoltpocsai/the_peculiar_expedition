import java.util.ArrayList;

public class Slot extends ArrayList<Carryable> {

    public static final int LIMITED = 7;
    public static final int UNLIMITED = -1;

    private final int limit;

    public Slot(int limit){
        this.limit = limit;
    }

    public int getStoredType() {
        if (isEmpty()) {
            return -1;
        } else {
            return get(0).getType();
        }
    }

    public boolean add(Carryable ca) {
        if (isEmpty()) {
            super.add(ca);
            return true;
        } else if (!isFull() && getStoredType() == ca.getType()) {
            super.add(ca);
            return true;
        } else {
            return false;
        }
    }

    public Carryable take() {
        Carryable ca;

        if (isEmpty()) {
            return null;
        } else {
            ca = get(0);
            super.remove(ca);
            return ca;
        }
    }

    public boolean isFull() {
        return (this.size() == limit || getStoredType() == Treasure.TREASURE);
    }

    public String toString() {
        //return "[" + get(0).getName() + "], amount: " + size();
        return String.format("%-18s%-15s", "[" + get(0).getName() + "]", "amount: " + size());
    }

    public String toStringWithPrice() {
        return this.toString() + "price each: " + get(0).getPrice() + " gold";
    }
}
