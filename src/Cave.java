public class Cave extends Field implements Startable {

    private static final int CHANCE_OF_DISASTER = 65;

    private Player player;
    private Treasure treasure;
    private boolean isTorchUsed = false;
    private boolean empty;

    public Cave() {
        super(FieldType.CAVE);
        treasure = new Treasure();
        empty = false;
    }

    public void start(Player player) {
        this.player = player;
        String userInput;

        if (empty) {
            System.out.println("You have already taken the treasure from here!");
            InputHandler.printBlankInput();
            return;
        }

        System.out.println("You see a dark cave which leads inside the mountain.");
        System.out.println("The caves always have some treasure inside.");

        while (true) {
            InputHandler.printOptions(new String[] {"[enter] go inside to find the treasure",
                    "[leave] you rather pass by..."});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("enter")) {
                if (enter()) {

                    InputHandler.printMessage("You got a new treasure!"
                            + System.lineSeparator() + "+++ " + treasure.getName() + " +++");

                    player.backpack.add(treasure);
                    empty = true;

                    if (!isTorchUsed) {
                        new Disaster(player, CHANCE_OF_DISASTER);
                    }
                }
                break;
            } else if (userInput.equals("leave")) {
                break;
            }
        }
    }

    private boolean enter() {
        Tool torch = new Tool(Tool.TORCH);
        String userInput;

        System.out.println("It is very dangerous to go inside a cave without a torch.");

        if (player.backpack.amount(torch.getName()) > 0) {
            System.out.println("You have some torch in your backpack.");
            System.out.println("Do you want to use it?");

            while (true) {
                InputHandler.printOptions(new String[] {"[yes]", "[no]", "[leave] better turn back..."});

                userInput = InputHandler.getLineInput();

                if (userInput.equals("yes")) {
                    player.backpack.take(torch.getName());
                    isTorchUsed = true;
                    InputHandler.printMessage("You use up a torch and go inside...");
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
            System.out.println("You don't have any torch in your backpack.");
            System.out.println("You still want to go in?");

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
