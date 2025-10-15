import java.util.Scanner;

public class Output {

    public static String inferredInput(String identifierName, String inputPrompt) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print(inputPrompt);
                return scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Error reading String input: " + e.getMessage());
                return null;
            } finally {
                scanner.close();
            }
        }

        public static int inferredInput(int identifierName, String inputPrompt) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print(inputPrompt);
                return scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer.");
                scanner.nextLine(); // Clear invalid input
                return inferredInput(identifierName, inputPrompt);
            } finally {
                scanner.close();
            }
        }

        public static float inferredInput(float ignored, String inputPrompt) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print(inputPrompt);
                return scanner.nextFloat();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid float.");
                scanner.nextLine(); // Clear invalid input
                return inferredInput(ignored, inputPrompt);
            } finally {
                scanner.close();
            }
        }

        public static boolean inferredInput(boolean ignored, String inputPrompt) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print(inputPrompt);
                return scanner.nextBoolean();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter 'true' or 'false'.");
                scanner.nextLine(); // Clear invalid input
                return inferredInput(ignored, inputPrompt);
            } finally {
                scanner.close();
            }
        }

    public static void main(String[] args) {
        int op = 10;
        switch (op) {
    case 5 -> {
        System.out.println("It is 5");
    }
    case 10 -> {
        System.out.println("It is 10");
    }
    default -> {
        System.out.println("Not Found");
    }
}
    }
}
