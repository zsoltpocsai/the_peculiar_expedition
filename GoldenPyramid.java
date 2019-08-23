public class GoldenPyramid extends Field implements Startable {

    public GoldenPyramid() {
        super(FieldType.GOLDEN_PYRAMID);
    }

    public void start(Player player) {
        String userInput;

        System.out.println("You have found the Golden Pyramid!");
        System.out.println("Now you can go home and celebrate yourself "
                + "or continue to hunt for more treasures.");

        player.foundGoldenPyramid = true;

        while (true) {
            InputHandler.printOptions(new String[] {"[home] go home",
                    "[continue] continue the expedition"});

            userInput = InputHandler.getLineInput();

            if (userInput.equals("home")) {
                player.goHome = true;
                break;
            } else if (userInput.equals("continue")) {
                break;
            }
        }
    }
}
