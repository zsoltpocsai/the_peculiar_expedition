import java.util.Random;

public class Village extends Field implements Startable {

    private static final int MIN_GOODS = 5;
    private static final int MAX_GOODS = 15;

    public static int relation = 3;

    private Player player;
    private Inventory storage;

    public Village() {
        super(FieldType.VILLAGE);
        storage = new Inventory(Inventory.UNLIMITED_SLOT);
        int[] goodsToStore = {Food.FRUIT, Food.MEAT, Food.DROG, Tool.ROPE, Tool.TORCH};

        Random rand = new Random();
        int randBound = MAX_GOODS - MIN_GOODS;
        int randAmount;

        for (int type : goodsToStore) {
            for (int value : Food.values) {
                if (type == value) {
                    randAmount = rand.nextInt(randBound) + MIN_GOODS;
                    for (int i = 0; i < randAmount; i++) {
                        storage.add(new Food(type));
                    }
                    break;
                }
            }
            for (int value : Tool.values) {
                if (type == value) {
                    randAmount = rand.nextInt(randBound) + MIN_GOODS;
                    for (int i = 0; i < randAmount; i++) {
                        storage.add(new Tool(type));
                    }
                    break;
                }
            }
        }
    }

    public void start(Player player){
        this.player = player;
        int numberOfSages;
        int relationBonus;
        String userInput;
        boolean exit = false;

        numberOfSages = player.team.numberOf(TeamMemberType.SAGE);
        relationBonus = (int)TeamMemberType.SAGE.bonus * numberOfSages;

        relation += relationBonus;

        System.out.println("You see a village in a short distance...");

        while (!exit) {
            InputHandler.printOptions(new String[] {"[enter] try to enter the village",
                    "[pass] pass by"});

            userInput = InputHandler.getLineInput();

            switch (userInput) {
                case "pass":
                    exit = true;
                    break;
                case "enter":
                    enter();
                    exit = true;
                    relation -= relationBonus;
                    break;
                default:
                    InputHandler.printMessage("Invalid option!");
                    break;
            }
        }
    }

    private void enter() {
        boolean exit = false;
        String userInput;

        if (relation < 1) {
            InputHandler.printMessage("You are not welcome here! You can't enter!");
            return;
        }

        while (!exit){
            System.out.println("Village");

            InputHandler.printOptions(new String[] {"[trade] trade with the village",
                    "[rest] take a nap and restore the energy of your team",
                    "[hire] try to hire someone into your team",
                    "[exit] leave the village"});

            userInput = InputHandler.getLineInput();

            switch (userInput) {
                case "trade":
                    trade();
                    break;
                case "rest":
                    rest();
                    break;
                case "hire":
                    hire();
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    InputHandler.printMessage("Invalid option!");
                    break;
            }
        }
    }

    private void trade() {
        Inventory itemsToBuy = new Inventory(Inventory.UNLIMITED_SLOT);
        Inventory itemsToSell = new Inventory(Inventory.UNLIMITED_SLOT);
        boolean exit = false;
        String userInput;

        while (!exit) {
            System.out.println("Trade with the village");
            System.out.println("The people here are willing to trade for anything "
                    + "but they value most the items they don't have.");
            System.out.println("And for some reason they are carzy about glassballs...");
            System.out.println();

            System.out.println("--- items to trade for ---");
            System.out.println(itemsToBuy.toString());

            System.out.println("--- items to give in trade ---");
            System.out.println(itemsToSell.toString());

            InputHandler.printOptions(new String[] {"[trade] select items to trade for",
                    "[give] select items to offer in trade",
                    "[offer] offer the deal",
                    "[exit]"});

            userInput = InputHandler.getLineInput();

            switch (userInput) {
                case "trade":
                    editBuyingList(itemsToBuy);
                    break;
                case "give":
                    editSellingList(itemsToSell);
                    break;
                case "offer":
                    offer(itemsToSell, itemsToBuy);
                    break;
                case "exit":
                    exit = true;
                    transferItems(itemsToBuy, storage);
                    transferItems(itemsToSell, player.backpack);
                    break;
                default:
                    InputHandler.printMessage("Invalid option!");
            }
        }
    }

    private void editBuyingList(Inventory buyingList) {
        String userInput;
        TradeInput tradeInput;
        String itemName;
        int amount;
        boolean exit = false;

        while (!exit) {
            System.out.println("Selecting items to trade for");
            System.out.println();
            System.out.println("--- items of the village ---");
            System.out.println(storage.toString());

            InputHandler.printOptions(new String[]
                    {"[<item> <amount>] an amount of item you want from the village (can be negative to decrease)",
                            "[back]"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("back")) {
                exit = true;
            } else {

                tradeInput = InputHandler.getTradeInput(userInput);

                if (tradeInput != null) {

                    itemName = tradeInput.itemName;
                    amount = tradeInput.amount;

                    if (amount > 0 && storage.amount(itemName) >= amount) {

                        for (int i = 0; i < amount; i++) {
                            buyingList.add(storage.take(itemName));
                        }
                        InputHandler.printMessage(amount + " " + itemName
                                + " has been added");

                    } else if (amount < 0 && buyingList.amount(itemName) >= Math.abs(amount)) {

                        for (int i = 0; i < Math.abs(amount); i++) {
                            storage.add(buyingList.take(itemName));
                        }
                        InputHandler.printMessage(amount + " " + itemName
                                + " has been removed");
                    } else {
                        InputHandler.printMessage("Invalid <item> or <amount>!");
                    }

                } else {
                    InputHandler.printMessage("Invalid format!");
                }
            }
        }
    }

    private void editSellingList(Inventory sellingList) {
        String userInput;
        TradeInput tradeInput;
        String itemName;
        int amount;
        boolean exit = false;

        while (!exit) {
            System.out.println("Selecting items to offer in trade");
            System.out.println();
            System.out.println("--- your items ---");
            System.out.println(player.backpack.toString());

            InputHandler.printOptions(new String[]
                    {"[<item> <amount>] an amount of item you want to offer (can be negative to decrease)",
                            "[back]"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("back")) {
                exit = true;
            } else {

                tradeInput = InputHandler.getTradeInput(userInput);

                if (tradeInput != null) {

                    itemName = tradeInput.itemName;
                    amount = tradeInput.amount;

                    if (amount > 0 && player.backpack.amount(itemName) >= amount) {

                        for (int i = 0; i < amount; i++) {
                            sellingList.add(player.backpack.take(itemName));
                        }
                        InputHandler.printMessage(amount + " " + itemName
                                + " has been added");

                    } else if (amount < 0 && sellingList.amount(itemName) >= Math.abs(amount)) {

                        for (int i = 0; i < Math.abs(amount); i++) {
                            storage.add(sellingList.take(itemName));
                        }
                        InputHandler.printMessage(amount + " " + itemName
                                + " has been removed");
                    } else {
                        InputHandler.printMessage("Invalid <item> or <amount>!");
                    }

                } else {
                    InputHandler.printMessage("Invalid format!");
                }
            }
        }
    }

    private void transferItems(Inventory source, Inventory dest) {
        Slot s;
        String itemName;

        while (!source.isEmpty()) {
            s = source.get(0);
            itemName = s.get(0).getName();
            for (int i = 0; i < s.size(); i++) {
                dest.add(source.take(itemName));
            }
        }
    }

    private void offer(Inventory sellingList, Inventory buyingList) {
        int valueOfOfferedItems = 0;
        int valueOfNeededItems = 0;
        int numberOfMerchants;

        numberOfMerchants = player.team.numberOf(TeamMemberType.MERCHANT);

        for (Slot s : sellingList) {
            valueOfOfferedItems += s.get(0).getTradeValue().sellingValue * s.size();
        }
        valueOfOfferedItems *= (1 + TeamMemberType.MERCHANT.bonus * numberOfMerchants);

        for (Slot s : buyingList) {
            valueOfNeededItems += s.get(0).getTradeValue().buyingValue * s.size();
        }
        valueOfNeededItems *= (1 - TeamMemberType.MERCHANT.bonus * numberOfMerchants);

        if (valueOfOfferedItems >= valueOfNeededItems) {

            InputHandler.printMessage("The village accepts your offer!");
            transferItems(buyingList, player.backpack);
            transferItems(sellingList, storage);

        } else {
            InputHandler.printMessage("The village refuses your offer.");
        }
    }

    private void hire() {
        Random rand = new Random();

        if (player.team.size() >= 3) {
            InputHandler.printMessage("Your team is full! You can't take more people with you.");
        } else if (relation >= 2 && rand.nextInt(100) < 20) {
            offerTeamMember();
        } else {
            InputHandler.printMessage("Nobody is offering his service");
        }
    }

    private void offerTeamMember(){
        String userInput;
        Random rand = new Random();
        int randomMember = rand.nextInt(3);
        TeamMember offeredMember;

        switch (randomMember) {
            case 0:
                offeredMember = new TeamMember(TeamMemberType.SHAMAN);
                break;
            case 1:
                offeredMember = new TeamMember(TeamMemberType.SCOUT);
                break;
            default:
                offeredMember = new TeamMember(TeamMemberType.SAGE);
                break;
        }

        System.out.println("There is a " + offeredMember.type.getName()
                + ", who is offering his service for you." + System.lineSeparator()
                + "You can hire him for 150 gold.");

        while (true) {
            InputHandler.printOptions(new String[]
                    {"[hire] take him with you", "[refuse] rather no"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("hire")) {
                if (player.getGold() >= 150) {

                    player.spendGold(150);
                    player.team.add(offeredMember);

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

    private void rest() {
        String userInput, itemName;
        boolean exit = false;

        if (relation == 1 || relation == 2) {

            while(!exit) {
                System.out.println("The village gives you a place to rest only for an item in trade!");
                System.out.println();
                System.out.println("--- your items ---");
                System.out.println(player.backpack.toString());

                InputHandler.printOptions(new String[] {"[<item>] to give", "[exit] rather no"});

                userInput = InputHandler.getLineInput();

                if (!userInput.equals("exit")) {
                    itemName = userInput;

                    if (player.backpack.amount(itemName) > 0) {
                        storage.add(player.backpack.take(itemName));

                        InputHandler.printMessage("The village accepts your offer."
                                + System.lineSeparator() + "You and your team got back its energy!");

                        player.restoreFullEnergy();
                        for (TeamMember tm : player.team) {
                            tm.restoreFullEnergy();
                        }

                    } else {
                        InputHandler.printMessage("Invalid <item>");
                    }
                } else {
                    exit = true;
                }
            }

        } else {
            InputHandler.printMessage("You and your team got back its energy!");

            player.restoreFullEnergy();
            for (TeamMember tm : player.team) {
                tm.restoreFullEnergy();
            }
        }

    }
}