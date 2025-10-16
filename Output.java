import java.util.Scanner;

public class Output {
    public static final Scanner SCANNER_INPUT = new Scanner(System.in);

    public static String inferredInput(String identifierName, String inputPrompt) {
            try {
                System.out.print(inputPrompt + " ");
                String input = SCANNER_INPUT.nextLine();
                return input;
            } catch (Exception e) {
                System.out.println("Error reading String input: " + e.getMessage());
                return null;
            }
        }

        public static int inferredInput(int identifierName, String inputPrompt) {
            try {
                System.out.print(inputPrompt + " ");
                int input = SCANNER_INPUT.nextInt();
                SCANNER_INPUT.nextLine();
                return input;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer.");
                SCANNER_INPUT.nextLine();
                return inferredInput(identifierName, inputPrompt);
            }
        }

        public static float inferredInput(float ignored, String inputPrompt) {
            try {
                System.out.print(inputPrompt + " ");
                float input =  SCANNER_INPUT.nextFloat();
                SCANNER_INPUT.nextLine();
                return input;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid float.");
                SCANNER_INPUT.nextLine();
                return inferredInput(ignored, inputPrompt);
            }
        }

        public static boolean inferredInput(boolean ignored, String inputPrompt) {
            try {
                System.out.print(inputPrompt + " ");
                boolean input = SCANNER_INPUT.nextBoolean();
                SCANNER_INPUT.nextLine();
                return input;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter 'true' or 'false'.");
                SCANNER_INPUT.nextLine();
                return inferredInput(ignored, inputPrompt);
            }
        }

    public static void main(String[] args) {
        for (int counter = 1; counter <= 10; counter+= 1) {
    System.out.println(counter);
}
    }
}
