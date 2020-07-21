import java.util.Random;

public class Treasure extends Carryable{

    public static final int TREASURE = 30;

    private static String[] treasureNames = {"golden monkey", "golden dagger",
        "silver dragon", "silver sword", "crystal skull"};

    private static int[] prices = {200, 150, 350, 200, 300};
    private static int[] fame = {350, 100, 450, 300, 400};

    public int fameValue;

    public Treasure() {
        super(TREASURE);
        generateName();
    }

    private void generateName() {
        Random rand = new Random();
        int randIndex = rand.nextInt(treasureNames.length);

        super.name = treasureNames[randIndex];
        super.price = prices[randIndex];
        fameValue = fame[randIndex];
    }
}
