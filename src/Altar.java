public class Altar extends Field implements CurseCreator {

    private static final int CHANCE_OF_CURSE = 80;

    private Treasure treasure;
    private Player player;
    private Map map;
    private boolean empty;

    public Altar() {
        super(FieldType.ALTAR);
        treasure = new Treasure();
        empty = false;
    }

    public void start(Player player, Map map) {
        this.player = player;
        this.map = map;
        String userInput;

        if (empty) {
            System.out.println("You have already taken the treasure from here.");
            InputHandler.printBlankInput();
            return;
        }

        System.out.println("You see a strange place in front of you, which seems like an altar.");
        System.out.println("It has a rare item inside. Looks valuable...");

        while (true) {
            InputHandler.printOptions(new String[] {"[enter] enter the altar",
                    "[leave] you rather pass by..."});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("enter")) {
                enter();
                break;
            } else if (userInput.equals("leave")) {
                break;
            }
        }
    }

    private void enter() {
        String userInput;

        System.out.println("You step inside and see a treasure in the middle.");
        System.out.println("It is very dangerous take a trasure from an altar "
                + "but it might worth the risk.");

        InputHandler.printOptions(new String[] {"[take] Who cares? Take the treasure!",
                "[back] better not take it..."});

        while (true) {

            userInput = InputHandler.getLineInput();

            if (userInput.equals("take")) {
                takeTreasure();
                break;
            } else if (userInput.equals("back")) {
                break;
            } else {
                InputHandler.printMessage("Invalid option!");
            }
        }
    }

    private void takeTreasure() {
        player.backpack.add(treasure);
        empty = true;

        InputHandler.printMessage("You got a new treasure!"
                + System.lineSeparator() + "+++ " + treasure.getName() + " +++");

        Village.relation -= 2;

        InputHandler.printMessage("Your relation with the native people has worsen.");

        new Curse(player, map, CHANCE_OF_CURSE);
    }
}
