/*
Szerző:         Pocsai Zsolt János
Neptun kód:     C5FVRU
Feladat:        The Peculiar Expedition
*/

public class Main {

    public static void main(String[] args) {
        String[] options = {"[new] start a new game", "[exit] quit the game"};
        String userInput;
        boolean exit = false;

        Game game;

        while (!exit) {
            System.out.println("THE PECULIAR EXPEDITION");
            System.out.println();
            for(String option : options) {
                System.out.println(option);
            }

            userInput = InputHandler.getLineInput();

            switch (userInput) {
                case "new":
                    game = new Game();
                    game.start();
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    InputHandler.printBlankInput();
            }
        }

    }
}