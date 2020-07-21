import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class InputHandler {
    private static BufferedReader in;

    static {
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    private static String getInput() {
        System.out.print("> ");
        try {
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Hiba a beolvasasnal!");
        }
        return "";
    }

    public static void printBlankInput() {
        System.out.println();
        System.out.print("<type to continue>");
        try {
            in.readLine();
            System.out.println();
        } catch (IOException e) {
            System.err.println("Hiba a beolvasásnál!");
        }
    }

    public static String getLineInput() {
        String input;
        input = getInput();
        System.out.println();
        return input;
    }

    public static TradeInput getTradeInput(String input) {
        String [] splittedInput;
        TradeInput tradeInput = new TradeInput();

        splittedInput = input.split(" ");
        if (splittedInput.length == 2) {
            try {
                tradeInput.itemName = splittedInput[0];
                tradeInput.amount = Integer.parseInt(splittedInput[1]);
            } catch (NumberFormatException e){
                return null;
            }
        } else if (splittedInput.length == 3) {     // a treasure
            try {
                tradeInput.itemName = splittedInput[0] + " " + splittedInput[1];
                tradeInput.amount = Integer.parseInt(splittedInput[2]);
            } catch (NumberFormatException e){
                return null;
            }
        } else {
            return null;
        }

        return tradeInput;
    }

    public static void printOptions(String[] options) {
        System.out.println();
        System.out.println("--- options ---");
        for (String option : options) {
            System.out.println(option);
        }
    }

    public static void printMessage(String text) {
        System.out.println(text);
        printBlankInput();
    }
}
