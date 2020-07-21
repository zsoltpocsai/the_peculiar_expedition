
public class Expedition {

    private static final int BASE_COST_OF_MOVE = 1;

    private Map map;
    private Player player;
    private ExpeditionShip ship;

    private boolean exit = false;

    public Expedition(Map map, Player player, ExpeditionShip ship) {
        Position startingLocation;

        this.map = map;
        this.player = player;
        this.ship = ship;

        startingLocation = map.getStartingLocation();
        player.setPosition(startingLocation);
        ship.setPosition(startingLocation);
        map.changeField(startingLocation, ship);
    }

    private void printStatus() {

        System.out.println("--- your status ---");
        System.out.println("current location: "
                + map.getField(player.getPosition()).getName()
                + " (" + player.getPosition() + ")");
        System.out.println("energy: " + player.getEnergy() + ", status: " + player.getStatus());

        if (!player.team.isEmpty()) {
            System.out.println();
            System.out.println("--- your team ---");
            player.team.listMembers();
        }
    }

    private float calculateCostOfMove(Field nextField) {
        float costOfMove;
        int overload;

        costOfMove = BASE_COST_OF_MOVE * nextField.getCostOfMoveModifier();

        costOfMove *= 1 + (0.15 * player.team.size());

        overload = player.isOverLoaded();
        if (overload > 0) {
            costOfMove *= 1 + (0.2 * overload);
        }

        return costOfMove;
    }

    private float calculateDamage(Field nextField, Explorer explorer) {
        if (nextField.getType() == FieldType.LAVA) {
            return (float)(explorer.getEnergy() * 0.3);
        } else {
            return 0;
        }
    }

    private void movePlayer(Direction d) {
        Position nextPos = player.nextPosition(d);

        Field nextField = map.getField(nextPos);
        Field actualField;

        float costOfMove;
        float damage;


        if (nextField.getType() == FieldType.JUNGLE) {
            askIfRemoveJungle(nextPos);
        }

        if (nextField.isPassable()) {

            player.move(d);
            actualField = nextField;

            costOfMove = calculateCostOfMove(actualField);
            damage = calculateDamage(actualField, player);

            player.decreaseEnergy(costOfMove);
            player.decreaseEnergy(damage);

            for (TeamMember tm : player.team) {

                damage = calculateDamage(actualField, tm);

                tm.decreaseEnergy(costOfMove);
                tm.decreaseEnergy(damage);
            }

            events();
        } else {
            System.out.println("You can't move there!");
            InputHandler.printBlankInput();
        }
    }

    private void askIfRemoveJungle(Position junglePosition) {
        String[] options = {"[yes] use 1 machete", "[no]"};
        Tool machete = new Tool(Tool.MACHETE);
        String input;
        boolean exit = false;

        if (player.backpack.amount(machete.getName()) > 0) {
            System.out.println("Do you want to remove the jungle?");
            for (String option : options) {
                System.out.println(option);
            }
            while (!exit) {
                input = InputHandler.getLineInput();
                switch (input) {
                    case "yes":
                        player.backpack.take(machete.getName());
                        map.changeField(junglePosition, new Field(FieldType.GRASS));
                        System.out.println("You removed the jungle!");
                        exit = true;
                        break;
                    case "no":
                        System.out.println("You helped the planet breath!");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            }
        }
    }

    private void putPlayerToExitPosition() {
        for (Direction d : Direction.values()) {
            if (map.getField(player.nextPosition(d)).isPassable()) {
                player.move(d);
                revealMap();
                break;
            }
        }
    }

    private void sailHome() {
        Slot s;
        String itemName;
        TeamMember teamMember;

        while (!player.backpack.isEmpty()) {
            s = player.backpack.get(0);
            itemName = s.get(0).getName();
            for (int i = 0; i < s.size(); i++) {
                ship.storage.add(player.backpack.take(itemName));
            }
        }

        while (!player.team.isEmpty()) {
            teamMember = player.team.get(0);
            ship.teamMemberPool.add(teamMember);
            player.team.remove(teamMember);
        }

        System.out.println("You go back to your ship and sail home...");
        InputHandler.printBlankInput();
        exit = true;
    }

    private void interruptExpedition() {

        player.backpack.clear();

        player.team.clear();

        exit = true;
    }

    private void leavingOf(TeamMember tm) {
        player.team.remove(tm);
        System.out.println(tm.getName() + " has left your team...");
        InputHandler.printBlankInput();
    }

    private void revealMap() {
        map.revealMapAround(player.getPosition(),
                1 + player.team.numberOf(TeamMemberType.SCOUT));
    }

    private void events() {
        Field currentField;
        Startable startable;
        CurseCreator curseCreator;

        for (Volcano v : map.volcanos) {
            if (v.isActive()) {
                v.eruption();
            }
        }

        for (Geyser g : map.geysers) {
            if (g.isActive()) {
                g.eruption();
            }
        }

        currentField = map.getField(player.getPosition());

        revealMap();

        if (currentField instanceof Startable) {

            startable = (Startable) currentField;
            startable.start(player);
            putPlayerToExitPosition();

        } else if (currentField instanceof CurseCreator) {

            curseCreator = (CurseCreator) currentField;
            curseCreator.start(player, map);
            putPlayerToExitPosition();
        }

        if (player.goHome) {
            sailHome();
        }

        if (player.doWantToLeave()) {
            player.dead = true;
            interruptExpedition();
        } else if (player.isExhausted() && player.team.isEmpty()) {
            player.dead = true;
            interruptExpedition();
        }

        for (int i = (player.team.size() - 1); i >= 0; i--) {
            if (player.team.get(i).doWantToLeave() || player.team.get(i).isExhausted()) {
                leavingOf(player.team.get(i));
            }
        }
    }

    public void run() {
        String userInput;

        ship.start(player);
        putPlayerToExitPosition();

        if (player.goHome) {
            sailHome();
        }

        while (!exit) {

            printStatus();
            System.out.println();

            System.out.println("--- adjacent tiles ---");
            System.out.println(map.listSurroundings(player.getPosition()));

            InputHandler.printOptions(new String[] {"[<direction>] go to the next field",
                    "[map/m] check the map",
                    "[inventory/i] use food from your backpack to refresh your team",
                    "[quit] quit and go back to the port (every progress here will be lost)"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("quit")) {
                interruptExpedition();
                break;
            }

            search:
            for (MapAction action : MapAction.values()) {
                for (String alias : action.names) {

                    if (userInput.equals(alias)) {

                        switch (action) {
                            case MAP:
                                System.out.println(map.drawMap(player.getPosition()));
                                InputHandler.printBlankInput();
                                break;
                            case INVENTORY:
                                player.enterInventory();
                                break;
                        }
                        break search;
                    }
                }
            }

            search:
            for (Direction direction : Direction.values()) {
                for (String alias : direction.names) {

                    if (userInput.equals(alias)) {

                        switch (direction) {
                            case NORTH:
                                movePlayer(direction);
                                break;
                            case EAST:
                                movePlayer(direction);
                                break;
                            case WEST:
                                movePlayer(direction);
                                break;
                            case SOUTH:
                                movePlayer(direction);
                                break;
                        }
                        break search;
                    }
                }
            }
        }
    }
}