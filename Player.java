
public class Player extends Explorer implements Famous, Comparable<Famous> {

    private static final int STARTING_GOLD = 250;
    private static final int DEFAULT_LOAD_CAPACITY = 8;

    private int fame;
    private int gold;
    public boolean foundGoldenPyramid = false;
    public boolean dead = false;
    public Inventory backpack;
    public TeamMemberList team;
    private Position position;
    public boolean goHome = false;

    public Player(String name) {
        super(name);
        fame = 0;
        gold = STARTING_GOLD;
        backpack = new Inventory(Inventory.LIMITED_SLOT);
        position = new Position();
        team = new TeamMemberList();
    }

    public int getFame() {
        return fame;
    }

    public void gainFame(int fame) {
        this.fame += fame;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public void spendGold(int amount) {
        gold -= amount;
        if (gold < 0) {
            gold = 0;
        }
    }

    public int isOverLoaded() {
        int numberOfDonkeys;
        int overloadedLimit = DEFAULT_LOAD_CAPACITY;

        numberOfDonkeys = team.numberOf(TeamMemberType.DONKEY);

        overloadedLimit += (TeamMemberType.DONKEY.bonus * numberOfDonkeys);

        if (overloadedLimit < backpack.size()) {
            return backpack.size() - overloadedLimit;
        } else {
            return 0;
        }
    }

    public Position getPosition() {

        return position;
    }

    public void setPosition(Position p) {
        position.x = p.x;
        position.y = p.y;
    }

    public Position nextPosition(Direction d) {

        switch (d) {
            case NORTH:
                return new Position(position.x, position.y - 1);
            case SOUTH:
                return new Position(position.x, position.y + 1);
            case EAST:
                return new Position(position.x + 1, position.y);
            case WEST:
                return new Position(position.x - 1, position.y);
            default:
                return position;
        }
    }

    public void enterInventory() {
        String userInput;
        String[] splittedInput;
        String verb, itemName, consumer;
        TeamMember teamMember = null;
        Food item;

        while (true) {
            System.out.println("Inventory");
            System.out.println();
            System.out.println("Your energy: " + getEnergy() + ", status: "
                    + getStatus());
            System.out.println();
            System.out.println("--- your items ---");
            System.out.println(backpack.toStringWithSlot());

            System.out.println("--- team members ---");
            team.listMembers();

            InputHandler.printOptions(new String[]
                    {"[eat/drink/use] [<item>] [me/<team member>] restore energy "
                            + "for you (me) or one of your team member",
                            "[exit] exit inventory"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("exit")) {
                break;
            }

            splittedInput = userInput.split(" ");

            if (splittedInput.length != 3) {
                InputHandler.printMessage("Invalid format!");
                continue;
            } else {
                verb = splittedInput[0];
                itemName = splittedInput[1];
                consumer = splittedInput[2];
            }

            item = (Food)backpack.take(itemName);
            if (item == null) {
                InputHandler.printMessage("Invalid <item>");
                continue;
            }

            if (!consumer.equals("me")) {
                teamMember = team.get(consumer);
                if (teamMember == null) {
                    InputHandler.printMessage("Invalid <team member>");
                    continue;
                }
            }

            switch (verb) {
                case "eat":
                    if (Food.isEdible(item.type)) {

                        if (consumer.equals("me")) {
                            eat(item);
                            InputHandler.printMessage("You ate a " + item.getName());
                            break;
                        } else if (teamMember != null) {
                            teamMember.eat(item);
                            InputHandler.printMessage( teamMember.getName()
                                    + " ate a " + item.getName());
                            break;
                        }
                    } else {
                        InputHandler.printMessage("It's not edible!");
                        backpack.add(item);
                    }
                    break;
                case "drink":
                    if (Food.isDrinkable(item.type)) {

                        if (team.numberOf(TeamMemberType.SOLDIER) > 0) {
                            item.multipleEnergy(1 + (TeamMemberType.SOLDIER.bonus
                                    * team.numberOf(TeamMemberType.SOLDIER)));
                        }

                        if (consumer.equals("me")) {
                            drink(item);
                            InputHandler.printMessage("You drank a " + item.getName());
                            break;
                        } else if (teamMember != null) {
                            teamMember.drink(item);
                            InputHandler.printMessage( teamMember.getName()
                                    + " drank a " + item.getName());
                            break;
                        }
                    } else {
                        InputHandler.printMessage("It's not drinkable!");
                        backpack.add(item);
                    }
                    break;
                case "use":
                    if (Food.isUsable(item.type)) {

                        if (team.numberOf(TeamMemberType.SHAMAN) > 0) {
                            item.multipleEnergy(1 + (TeamMemberType.SHAMAN.bonus
                                    * team.numberOf(TeamMemberType.SOLDIER)));
                        }

                        if (consumer.equals("me")) {
                            use(item);
                            InputHandler.printMessage("You used a " + item.getName());
                            break;
                        } else if (teamMember != null) {
                            teamMember.use(item);
                            InputHandler.printMessage( teamMember.getName()
                                    + " used a " + item.getName());
                        }
                    } else {
                        InputHandler.printMessage("It's not usable!");
                        backpack.add(item);
                    }
                    break;
                default:
                    InputHandler.printMessage("Invalid option!");
                    break;
            }
        }
    }

    public void move(Direction d) {
        setPosition(nextPosition(d));

        numberOfSteps++;
        if (!team.isEmpty()) {
            for (TeamMember tm : team) {
                tm.numberOfSteps++;
            }
        }
    }

    public int compareTo(Famous player) {
        return Famous.super.compareTo(player);
    }
}
