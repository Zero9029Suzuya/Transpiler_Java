import java.util.Scanner;

public class Output {
    public static int Calculator(int choice) {
        int b = 0;
        System.out.println("Choices: \n1. Add\n2.Subtract");
        choice = inferredInput( choice, "Please Set Your Choice: ");
        return b;
    }

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
        int choice = 0;
        int result = 0;
        result = Calculator(choice);
        System.out.println(result);
    }
}
