
public class ExpeditionShip extends Field implements Startable {
	
	private Player player;
	public Inventory storage;
	public TeamMemberList teamMemberPool;
	
	public ExpeditionShip() {
	    super(FieldType.EXPEDITION_SHIP);
		storage = new Inventory(Inventory.UNLIMITED_SLOT);
		teamMemberPool = new TeamMemberList();
	}

	public void start(Player player) {
	    String userInput;

	    this.player = player;

        while (true) {
            System.out.println("The expedition ship");

            InputHandler.printOptions(new String[] {"[enter] go inside the ship for more options",
                    "[explore] leave your ship and explore the area",
                    "[home] sail home, to the port"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("enter")) {
                enter();
            } else if (userInput.equals("explore")) {
                break;
            } else if (userInput.equals("home")) {
                player.goHome = true;
                break;
            }
        }
	}

	private void enter() {
	    boolean exit = false;
	    String userInput;

        while (!exit) {
            System.out.println("Inside the expedition ship");

            InputHandler.printOptions(new String[]
                    {"[store] store items into the ship from your backpack",
                    "[take] take items with you from the ship", "[team] put together your team",
                    "[rest] take a nap", "[back] go back to the board"});

            userInput = InputHandler.getLineInput();

            switch(userInput) {
                case "store":
                    store();
                    break;
                case "take":
                    take();
                    break;
                case "team":
                    team();
                    break;
                case "rest":
                    rest();
                    break;
                case "back":
                    exit = true;
                    break;
            }
        }
    }

    private void store(){
	    String userInput;
	    TradeInput tradeInput;

	    while (true) {
            System.out.println("The storage room");
            System.out.println();
	        System.out.println("--- items in your backpack ---");
            System.out.println(player.backpack.toStringWithSlot());

            InputHandler.printOptions(new String[] {"[<item> <amount>] leave an amount of item",
                    "[back] go back"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("back")) {
                break;
            }

            tradeInput = InputHandler.getTradeInput(userInput);

            if (tradeInput != null
                    && player.backpack.amount(tradeInput.itemName) >= tradeInput.amount) {

                for (int i = 0; i < tradeInput.amount; i++) {
                    storage.add(player.backpack.take(tradeInput.itemName));
                }

                System.out.println("You added " + tradeInput.amount
                        + " " + tradeInput.itemName + " to the ship's storage.");
                InputHandler.printBlankInput();
            } else {
                InputHandler.printMessage("Invalid option, <item> or <amount>!");
            }
        }
    }

    private void take(){
        String userInput;
        TradeInput tradeInput;

        while (true) {
            System.out.println("The storage room");
            System.out.println();
            System.out.println("--- items on the ship ---");
            System.out.println(storage.toString());

            InputHandler.printOptions(new String[] {"[<item> <amount>] take an amount of item to your backpack"
                    , "[back] go back"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("back")) {
                break;
            }

            tradeInput = InputHandler.getTradeInput(userInput);

            if (tradeInput != null
                    && storage.amount(tradeInput.itemName) >= tradeInput.amount) {

                for (int i = 0; i < tradeInput.amount; i++) {
                    player.backpack.add(storage.take(tradeInput.itemName));
                }

                System.out.println("You added " + tradeInput.amount
                        + " " + tradeInput.itemName + " to your backpack.");
                InputHandler.printBlankInput();
            } else {
                InputHandler.printMessage("Invalid option, <item> or <amount>!");
            }
        }
    }

    private void team() {
	    String userInput;
	    String[] splittedInput;
	    String verb, memberName;
	    TeamMember teamMember;

	    while (true) {
	        System.out.println("The crew quarter");
	        System.out.println();

            System.out.println("--- people on your ship ---");
            teamMemberPool.listMembers();
            System.out.println();
            System.out.println("--- expedition team ---");
            player.team.listMembers();

            InputHandler.printOptions(new String[]
                    {"[bring <team member>] add team member to the expedition team",
                     "[leave <team member>] leave a team member on the ship",
                     "[back] go back"});

            userInput = InputHandler.getLineInput();
            if (userInput.equals("back")) {
                break;
            }

            splittedInput = userInput.split(" ");

            if (splittedInput.length == 2) {
                verb = splittedInput[0];
                memberName = splittedInput[1];
            } else {
                System.out.println("Invalid format or command!");
                InputHandler.printBlankInput();
                continue;
            }

            if (verb.equals("bring")) {

                if (player.team.size() <= 3) {
                    if (teamMemberPool.contains(memberName)) {

                        teamMember = teamMemberPool.get(memberName);
                        player.team.add(teamMember);
                        teamMemberPool.remove(teamMember);

                        InputHandler.printMessage(teamMember.getName()
                                + " has joined your team.");

                    } else {
                        InputHandler.printMessage("Invalid <team member>!");
                    }
                } else {
                    InputHandler.printMessage("You can't have more than 3 team member!");
                }

            } else if (verb.equals("leave")) {

                if (!player.team.isEmpty()) {
                    if (player.team.contains(memberName)) {

                        teamMember = player.team.get(memberName);
                        teamMemberPool.add(teamMember);
                        player.team.remove(teamMember);

                        InputHandler.printMessage(teamMember.getName()
                                + " will stay on the ship.");

                    } else {
                        InputHandler.printMessage("Invalid <team member>!");
                    }
                } else {
                    InputHandler.printMessage("You have nobody in your team!");
                }
            } else {
                InputHandler.printMessage("Invalid option!");
            }
	    }
	}

	private void rest() {
		player.restoreFullEnergy();
		for (TeamMember tm : player.team) {
		    tm.restoreFullEnergy();
        }

		InputHandler.printMessage("You and your team has rested!");
	}
}