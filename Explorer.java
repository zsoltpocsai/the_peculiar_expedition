import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Explorer {

    private static final int MAX_ENERGY = 100;

    private String name;
    private float energy;
    protected ArrayList<Illness> illnesses;
    private ArrayList<Integer> consumedItems;
    protected int numberOfSteps;
    private HashMap<Integer, Integer> lastTimeOfGettingAdditiveItem;
    private boolean injured;

    public Explorer(String name) {
        this.name = name;
        energy = MAX_ENERGY;
        consumedItems = new ArrayList<>();
        illnesses = new ArrayList<>();
        numberOfSteps = 0;
        lastTimeOfGettingAdditiveItem = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public float getEnergy() {

        return (float)(Math.round(energy * 10))/10;
    }

    public void restoreEnergy(float f) {

        energy += f;

        if (energy > MAX_ENERGY) {
            energy = MAX_ENERGY;
        }
    }

    public void restoreFullEnergy() {

        energy = MAX_ENERGY;
    }

    public void decreaseEnergy(float f) {

        energy -= f;

        if (energy < 0) {
            energy = 0;
        }
    }

    public void eat(Food edible) {
        restoreEnergy(edible.getEnergy());
        consumedItems.add(edible.getType());
        System.out.println(name + " ate a " + edible.getName());
    }

    public void drink(Food drinkable) {
        restoreEnergy(drinkable.getEnergy());
        consumedItems.add(drinkable.getType());
        createPossibleAddiction();
        lastTimeOfGettingAdditiveItem.replace(Food.WHISKEY, numberOfSteps);
    }

    public void use(Food usable) {
        restoreEnergy(usable.getEnergy());
        consumedItems.add(usable.getType());
        createPossibleAddiction();
        lastTimeOfGettingAdditiveItem.replace(Food.DROG, numberOfSteps);
    }

    private void createPossibleAddiction(){
        Random rand = new Random();
        int lastIndex;
        int[] lastTwoType = new int[2];

        lastIndex = consumedItems.size() - 1;
        if (lastIndex >= 1) {
            lastTwoType[0] = consumedItems.get(lastIndex);
            lastTwoType[1] = consumedItems.get(lastIndex - 1);
        } else {
            lastTwoType[0] = consumedItems.get(lastIndex);
            lastTwoType[1] = -1;
        }

        if (lastTwoType[0] == Food.WHISKEY && lastTwoType[1] == Food.WHISKEY) {
            if (rand.nextInt(100) < 15) {
                if (!illnesses.contains(Illness.ALCOHOLISM)) {
                    illnesses.add(Illness.ALCOHOLISM);
                    lastTimeOfGettingAdditiveItem.put(Food.WHISKEY, numberOfSteps);
                    System.out.println(name + " got a new illness: "
                            + Illness.ALCOHOLISM.name);
                    InputHandler.printBlankInput();
                }
            }
        } else if (lastTwoType[0] == Food.DROG && lastTwoType[1] == Food.DROG) {
            if (rand.nextInt(100) < 15) {
                if (!illnesses.contains(Illness.DROG_ADDICTION)) {
                    illnesses.add(Illness.DROG_ADDICTION);
                    lastTimeOfGettingAdditiveItem.put(Food.DROG, numberOfSteps);
                    System.out.println(name + " got a new illness: "
                            + Illness.DROG_ADDICTION.name);
                    InputHandler.printBlankInput();
                }
            }
        }
    }

    public boolean doWantToLeave() {
        Random rand = new Random();

        if (illnesses.contains(Illness.ALCOHOLISM)) {
            if (numberOfSteps - lastTimeOfGettingAdditiveItem.get(Food.WHISKEY) > 30) {

                if (rand.nextInt(100) < 10) {
                    InputHandler.printMessage(name + " can't deal with his alcoholism!");
                    return true;
                }
            }
        }

        if (illnesses.contains(Illness.DROG_ADDICTION)) {
            if (numberOfSteps - lastTimeOfGettingAdditiveItem.get(Food.DROG) > 30) {

                if (rand.nextInt(100) < 10) {
                    InputHandler.printMessage(name + " can't deal with his drog addiction!");
                    return true;
                }
            }
        }

        if (injured) {
            if (rand.nextInt(100) < 5) {
                InputHandler.printMessage(name + "'s injury got too serious!");
                return true;
            }
        }

        return false;
    }

    public boolean isExhausted() {
        Random rand = new Random();

        if (this.getEnergy() == 0) {
            if (rand.nextInt(100) < 8) {
                InputHandler.printMessage(name + " is too exhausted to continue!");
                return true;
            }
        }

        return false;
    }

    public void getsInjured() {
        injured = true;
    }

    public void healInjury() {
        injured = false;
    }

    public String getStatus() {
        String status;

        if (illnesses.isEmpty() && !injured) {
            status = "healthy";
        } else if (illnesses.size() == 1) {
            status = illnesses.get(0).name;
        } else if (illnesses.size() == 2) {
            status = illnesses.get(0).name + ", "
                    + illnesses.get(1).name;
        } else {
            status = "";
        }

        if (injured && !illnesses.isEmpty()) {
            status = status.concat(", injured");
        } else if (injured) {
            status = status.concat("injured");
        }

        return status;
    }
}