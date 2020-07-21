import java.util.ArrayList;

public class Inventory extends ArrayList<Slot> {

    public static final int LIMITED_SLOT = 0;
    public static final int UNLIMITED_SLOT = 1;

    private final int type;

    public Inventory(int type) {
        this.type = type;
    }

    public boolean add(Carryable ca) {
        for (Slot slot : this) {
            if (slot.add(ca)) {
                return true;
            }
        }
        if (type == LIMITED_SLOT) {
            super.add(new Slot(Slot.LIMITED));
        } else {
            super.add(new Slot(Slot.UNLIMITED));
        }
        return add(ca);
    }

    public Carryable take(String name) {
        Carryable ca;

        for (Slot slot : this) {
            if (slot.get(0).getName().equals(name)) {
                ca = slot.take();
                if (slot.isEmpty()) {
                    remove(slot);
                }
                return ca;
            }
        }
        return null;
    }

    public Carryable get(String name) {
        for (Slot s : this) {
            if (s.get(0).getName().equals(name)) {
                return s.get(0);
            }
        }
        return null;
    }

    public int amount(String name) {
        int sum = 0;

        for (Slot s : this) {
            if (s.get(0).getName().equals(name)) {
                sum += s.size();
            }
        }
        return sum;
    }

    public String toString() {
        String output = "";

        if (isEmpty()) {
            return "No items";
        } else {
            for (Slot slot : this) {
                output = output.concat(slot.toString()
                        + System.lineSeparator());
            }
        }

        return output;
    }

    public String toStringWithSlot() {
        String output = "";

        if (isEmpty()) {
            return "No items";
        } else {
            for (int i = 0; i < this.size(); i++) {
                output = output.concat(String.format("%-9s%s",
                        "slot " + (i + 1) + ":",
                        this.get(i).toString() + System.lineSeparator()));
            }
        }

        return output;
    }
}
