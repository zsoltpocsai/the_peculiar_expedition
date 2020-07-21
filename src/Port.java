import java.util.ArrayList;
import java.util.Random;

public class Port {

    private static final int MIN_GOODS = 20;
    private static final int MAX_GOODS = 35;

    private int numberOfExpeditions = 0;
    private boolean endOfGame = false;

    private ArrayList<AIPlayer> aiPlayers;
    private ExpeditionShip ship;
    private Player player;
    private Inventory market;

    public Port(ExpeditionShip ship, Player player, ArrayList<AIPlayer> aiPlayers) {
        this.aiPlayers = aiPlayers;
        this.ship = ship;
        this.player = player;
        market = new Inventory(Inventory.UNLIMITED_SLOT);
        generateGoods();
    }

    private void generateGoods(){
        Random rand = new Random();
        int randBound = MAX_GOODS - MIN_GOODS;
        int forLimit;

        for (int k = 0; k < 8; k++) {
            forLimit = rand.nextInt(randBound) + MIN_GOODS;
            for (int i = 0; i < forLimit; i++) {
                switch (k) {
                    case 0:
                        market.add(new Food(Food.FRUIT));
                        break;
                    case 1:
                        market.add(new Food(Food.CHOCOLATE));
                        break;
                    case 2:
                        market.add(new Food(Food.MEAT));
                        break;
                    case 3:
                        market.add(new Food(Food.WHISKEY));
                        break;
                    case 4:
                        market.add(new Tool(Tool.TORCH));
                        break;
                    case 5:
                        market.add(new Tool(Tool.MACHETE));
                        break;
                    case 6:
                        market.add(new Tool(Tool.ROPE));
                        break;
                    case 7:
                        market.add(new Tool(Tool.GLASSBALL));
                        break;
                }
            }
        }
    }

    private void startExpedition() {
        Map map = new Map();
        Expedition expedition = new Expedition(map, player, ship);

        Village.relation = 3;

        System.out.println("You start sailing to new lands...");
        InputHandler.printBlankInput();

        startExpeditionOfAIPlayers();

        expedition.run();

        arrivingFromExpedition();
    }

    private void arrivingFromExpedition() {

        numberOfExpeditions++;

        if (player.dead) {
            deadPlayer();
            return;
        }

        restoreEnergies();
        removeInjuries();

        if (player.foundGoldenPyramid) {
            celebrateGoldenPyramid();
        }

        player.foundGoldenPyramid = false;
        player.goHome = false;

        if (numberOfExpeditions == 5) {
            congratulations();
        }
    }

    private void startExpeditionOfAIPlayers() {
        Random rand = new Random();

        for (AIPlayer aiPlayer : aiPlayers) {
            aiPlayer.gainFame(rand.nextInt(1500));
        }
    }

    private void celebrateGoldenPyramid() {
        System.out.println("People are impressed that you have found the Golden Pyramid!");
        System.out.println("You are awesome! You have gained 1000 fame!");
        player.gainFame(1000);
        InputHandler.printBlankInput();
    }

    private void restoreEnergies() {
        player.restoreFullEnergy();
        for (TeamMember tm : ship.teamMemberPool) {
            tm.restoreFullEnergy();
        }
    }

    private void removeInjuries() {
        for (TeamMember tm : player.team) {
            tm.healInjury();
        }
    }

    public void enter(){
        String userInput;

        while (!endOfGame) {
            System.out.println("The Port");
            InputHandler.printOptions(new String[] {"[start] start a new expedition",
                    "[market] buy or sell items", "[museum] give treasures to museums",
                    "[hall] enter the Hall Of Fame",
                    "[exit] finish the game and exit to the main menu"});

            userInput = InputHandler.getLineInput();

            switch (userInput) {
                case "start":
                    offerTeamMember();
                    startExpedition();
                    break;
                case "market":
                    enterMarket();
                    break;
                case "museum":
                    museum();
                    break;
                case "hall":
                    enterHallOfFame();
                    break;
                case "exit":
                    endOfGame = true;
                    break;
                default:
                    InputHandler.printMessage("Invalid option!");
                    break;
            }
        }
    }

    private void offerTeamMember(){
        String userInput;
        Random rand = new Random();
        int randomMember = rand.nextInt(3);
        TeamMember offeredMember;

        switch (randomMember) {
            case 0:
                offeredMember = new TeamMember(TeamMemberType.MERCHANT);
                break;
            case 1:
                offeredMember = new TeamMember(TeamMemberType.SOLDIER);
                break;
            default:
                offeredMember = new TeamMember(TeamMemberType.DONKEY);
                break;
        }

        System.out.println("There is a " + offeredMember.type.getName()
                + ", who is offering his service for you." + System.lineSeparator()
                + "You can hire him for 150 gold.");

        while (true) {
            InputHandler.printOptions(new String[] {"[hire] you accept his service",
                    "[refuse] you go without him"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("hire")) {
                if (player.getGold() >= 150) {

                    player.spendGold(150);
                    ship.teamMemberPool.add(offeredMember);

                    System.out.println("You hired the " + offeredMember.type.getName());
                    System.out.println("His name is: " + offeredMember.getName());
                    InputHandler.printBlankInput();

                    break;
                } else {
                    InputHandler.printMessage("You have not enough gold!");
                    break;
                }
            } else if (userInput.equals("refuse")) {
                InputHandler.printMessage("You politely refuse his service.");
                break;
            } else {
                InputHandler.printMessage("Invalid option!");
            }
        }
    }

    private void enterMarket() {
        String userInput;
        boolean exit = false;

        while (!exit) {
            System.out.println("Market");
            InputHandler.printOptions(new String[] {"[buy] buy new items",
                    "[sell] sell items or treasures", "[back] go back to the port"});
            userInput = InputHandler.getLineInput();
            switch(userInput) {
                case "buy":
                    buyItems();
                    break;
                case "sell":
                    sellItems();
                    break;
                case "back":
                    exit = true;
                    break;
                default:
                    InputHandler.printMessage("Invalid option!");
            }
        }
    }

    private void buyItems() {
        String userInput;
        TradeInput tradeInput;
        String itemName;
        int amount;
        int finalPrice;
        int numberOfMerchants;
        Carryable itemToBuy;

        while (true) {
            System.out.println("--- items on the market ---");

            for (int i = 0; i < market.size(); i++) {
                System.out.println(market.get(i).toStringWithPrice());
            }

            System.out.println(System.lineSeparator()
                    + "Your gold: " + player.getGold());

            InputHandler.printOptions(new String[] {"[<item> <amount>] buy an amount of item",
                    "[back] go back to the market"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("back")) {
                break;
            }

            tradeInput = InputHandler.getTradeInput(userInput);

            if (tradeInput != null) {
                itemName = tradeInput.itemName;
                amount = tradeInput.amount;

                if (market.amount(itemName) >= amount) {
                    itemToBuy = market.get(itemName);
                    numberOfMerchants = ship.teamMemberPool.numberOf(TeamMemberType.MERCHANT);

                    finalPrice = Math.round(itemToBuy.getPrice()
                            * (1 - TeamMemberType.MERCHANT.bonus * numberOfMerchants)
                            * amount);

                    if (finalPrice <= player.getGold()) {

                        for (int i = 0; i < amount; i++) {
                            ship.storage.add(market.take(itemName));
                        }

                        player.spendGold(finalPrice);
                        System.out.println("You added " + amount + " " + itemName
                                + " to your ship's storage." );
                        InputHandler.printBlankInput();

                    } else {
                        InputHandler.printMessage("You have not enough gold!");
                    }
                } else {
                    InputHandler.printMessage("Invalid <item> or <amount>!");
                }
            } else {
                InputHandler.printMessage("Invalid input format!");
            }
        }
    }

    private void sellItems() {
        String userInput;
        TradeInput tradeInput;
        String itemName;
        int amount;
        int finalPrice;
        int numberOfMerchants;
        Carryable itemToSell;

        while (true) {
            System.out.println("--- your items ---");

            for (int i = 0; i < ship.storage.size(); i++) {
                System.out.println(ship.storage.get(i).toStringWithPrice());
            }

            System.out.println(System.lineSeparator()
                    + "Your gold: " + player.getGold());

            InputHandler.printOptions(new String[] {"[<item> <amount>] sell an amount of item",
                    "[back] go back to the market"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("back")) {
                break;
            }

            tradeInput = InputHandler.getTradeInput(userInput);

            if (tradeInput != null) {
                itemName = tradeInput.itemName;
                amount = tradeInput.amount;

                if (ship.storage.amount(itemName) >= amount) {
                    itemToSell = ship.storage.get(itemName);
                    numberOfMerchants = ship.teamMemberPool.numberOf(TeamMemberType.MERCHANT);

                    finalPrice = Math.round(itemToSell.getPrice() * amount
                            * (1 + TeamMemberType.MERCHANT.bonus * numberOfMerchants));

                    for (int i = 0; i < amount; i++) {
                        market.add(ship.storage.take(itemName));
                    }

                    player.addGold(finalPrice);
                    System.out.println("You sold " + amount + " " + itemName);
                    InputHandler.printBlankInput();

                } else {
                    InputHandler.printMessage("Invalid <item> or <amount>!");
                }
            } else {
                InputHandler.printMessage("Invalid input format!");
            }
        }
    }

    private void museum() {
        String userInput;
        Treasure treasure;
        boolean noTreasures = true;

        while (true) {
            System.out.println("The Museum");
            System.out.println();
            System.out.println("--- your treasures ---");
            for (Slot s : ship.storage) {
                if (s.get(0) instanceof Treasure) {
                    System.out.println("[" + s.get(0).name + "]");
                    noTreasures = false;
                }
            }
            if (noTreasures) {
                System.out.println("You have no treasures.");
            }

            InputHandler.printOptions(new String[] {"[<trasure>] offer treasure to gain fame",
                    "[back] go back to the port"});

            userInput = InputHandler.getLineInput();

            if (ship.storage.get(userInput) instanceof Treasure) {

                treasure = (Treasure) ship.storage.take(userInput);
                player.gainFame(treasure.fameValue);
                InputHandler.printMessage("You got " + treasure.fameValue + " fame!");

            } else if (userInput.equals("back")) {
                break;
            } else {
                InputHandler.printMessage("There is no such treasure.");
            }
        }
    }

    private void enterHallOfFame() {
        String userInput;
        ArrayList<Famous> players = new ArrayList<>();

        players.add(player);
        players.addAll(aiPlayers);

        players.sort(null);

        while (true){
            System.out.println("Hall Of Fame");
            System.out.println();
            System.out.println(String.format("%-18s%s", "Player", "Fame"));
            System.out.println();
            for (Famous player : players) {
                System.out.println(String.format("%-18s%d", player.getName(), player.getFame()));
            }

            InputHandler.printOptions(new String[] {"[exit] go back"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("exit")) {
                break;
            } else {
                InputHandler.printMessage("Invalid option!");
            }
        }
    }

    private void deadPlayer() {
        InputHandler.printMessage("Your adventure has ended here...");
        endOfGame = true;
    }

    private void congratulations() {
        InputHandler.printMessage("Congratulations! You have completed 5 expedition!");
        InputHandler.printMessage("Now go inside the Hall Of Fame of great "
                + "explorers and see who got the most fame!");

        enterHallOfFame();

        endOfGame = true;
    }
}
