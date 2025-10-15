import java.util.Scanner;

public class Output {

    public static String inferredInput(String identifierName, String inputPrompt) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print(inputPrompt + " ");
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
                System.out.print(inputPrompt + " ");
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
                System.out.print(inputPrompt + " ");
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
                System.out.print(inputPrompt + " ");
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
        String a = """
 T
Added"""
;
        System.out.println(a);
        int b= 0;;
        b = inferredInput( b, """
Pick a choice:
1. Addition:
2. Subtraction:
Choice: """
);
        System.out.println(b);
    }
}
