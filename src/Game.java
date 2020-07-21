import java.util.ArrayList;

public class Game {

    private static final String[] aiPlayerNamesPool = {"Edgar A. Poe", "Robinson Crusoe", "Elon Musk"};

    private Port port;
    private ArrayList<AIPlayer> aiPlayers;

    public Game() {
        Player player;
        ExpeditionShip expeditionShip;

        player = new Player(askName());
        expeditionShip = new ExpeditionShip();
        aiPlayers = new ArrayList<>();

        generateAIPlayers();
        port = new Port(expeditionShip, player, aiPlayers);
    }

    private String askName() {
        String name;

        do {
            System.out.println("Choose a name for your explorer!");
            name = InputHandler.getLineInput();
        } while (name.isEmpty());

        return name;
    }

    private void generateAIPlayers() {
        for (String name : aiPlayerNamesPool) {
            aiPlayers.add(new AIPlayer(name));
        }
    }

    public void start() {
        port.enter();
    }
}
