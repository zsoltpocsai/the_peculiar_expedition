public class MountainSanctuary extends Field implements CurseCreator {

    private static final int CHANCE_OF_DISASTER = 50;
    private static final int CHANCE_OF_CURSE = 20;

    private Player player;
    private Treasure treasure;
    private boolean isRopeUsed = false;
    private boolean empty;

    public MountainSanctuary() {
        super(FieldType.MOUNTAIN_SANCTUARY);
        treasure = new Treasure();
        empty = false;
    }

    public void start(Player player, Map map) {
        this.player = player;
        String userInput;

        if (empty) {
            System.out.println("You have already taken the treasure from here!");
            InputHandler.printBlankInput();
            return;
        }

        System.out.println("You see a sanctuary high up on the mountain.");
        System.out.println("Sanctuaries always have some treasure inside.");

        while (true) {
            InputHandler.printOptions(new String[] {"[climb] climb up",
                    "[leave] you rather pass by..."});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("climb")) {
                if (enter()) {

                    InputHandler.printMessage("You got a new treasure!"
                            + System.lineSeparator() + "+++ " + treasure.getName() + " +++");

                    player.backpack.add(treasure);
                    empty = true;

                    Village.relation -= 1;

                    InputHandler.printMessage("Your relation with the native people has worsen!");

                    if (!isRopeUsed) {
                        new Disaster(player, CHANCE_OF_DISASTER);
                        new Curse(player, map, CHANCE_OF_CURSE);
                    }
                }
                break;
            } else if (userInput.equals("leave")) {
                break;
            }
        }
    }

    private boolean enter() {
        Tool rope = new Tool(Tool.ROPE);
        String userInput;

        System.out.println("It is very dangerous to climb up without a rope.");

        if (player.backpack.amount(rope.getName()) > 0) {
            System.out.println("You have some rope in your backpack.");
            System.out.println("Do you want to use it?");

            while (true) {
                InputHandler.printOptions(new String[] {"[yes]", "[no]", "[leave] better turn back..."});

                userInput = InputHandler.getLineInput();

                if (userInput.equals("yes")) {
                    player.backpack.take(rope.getName());
                    isRopeUsed = true;
                    InputHandler.printMessage("You use up a rope and start climbing...");
                    return true;
                } else if (userInput.equals("no")) {
                    return true;
                } else if (userInput.equals("leave")) {
                    return false;
                } else {
                    InputHandler.printMessage("Invalid option!");
                }
            }
        } else {
            System.out.println("You don't have any rope in your backpack.");
            System.out.println("You still want to go up?");

            while (true) {
                InputHandler.printOptions(new String[] {"[yes]", "[no] better turn back..."});

                userInput = InputHandler.getLineInput();

                if (userInput.equals("yes")) {
                    return true;
                } else if (userInput.equals("no")) {
                    return false;
                } else {
                    InputHandler.printMessage("Invalid option!");
                }
            }
        }
    }
}
