import java.util.Scanner;

public class Output {
<<<<<<< HEAD
    public static int addingNumber(int x, int y) {
=======
    public static int addingNumbers(int x, int y) {
>>>>>>> 7c44e15a6fbc1ce224cca797734434289b648b8f
        int c = x + y;
        System.out.println(c);
        return c;
    }
    public static String inferredInputString(String identifierName, String inputPrompt) {
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

        public static int inferredInputInt(String identifierName, String inputPrompt) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print(inputPrompt);
                return scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer.");
                scanner.nextLine(); // Clear invalid input
                return inferredInputInt(identifierName, inputPrompt);
            } finally {
                scanner.close();
            }
        }

        public static float inferredInputFloat(float ignored, String inputPrompt) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print(inputPrompt);
                return scanner.nextFloat();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter a valid float.");
                scanner.nextLine(); // Clear invalid input
                return inferredInputFloat(ignored, inputPrompt);
            } finally {
                scanner.close();
            }
        }

        public static boolean inferredInputBoolean(boolean ignored, String inputPrompt) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print(inputPrompt);
                return scanner.nextBoolean();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Please enter 'true' or 'false'.");
                scanner.nextLine(); // Clear invalid input
                return inferredInputBoolean(ignored, inputPrompt);
            } finally {
                scanner.close();
            }
        }

        public static Object inferredInput(String identifierName, String inputPrompt, String type) {
            return switch (type) {
                case "int" -> inferredInputInt(identifierName, inputPrompt);
                case "float" -> inferredInputFloat(0f, inputPrompt);
                case "boolean" -> inferredInputBoolean(false, inputPrompt);
                case "String" -> inferredInputString(identifierName, inputPrompt);
                default -> throw new RuntimeException("Unknown type for input: " + type);
            };
        }
    public static void main(String[] args) {
<<<<<<< HEAD
        int a = 140;
        int b = 20;
        String wordle = "Hello Worl";
        int d = 1;
        for (int i = 0; i <= 10; i+= 1) {
    System.out.println(wordle);
}
        addingNumber(a, b);
=======
        int a = 10;
        int b = 20;
        addingNumbers(a, b);
>>>>>>> 7c44e15a6fbc1ce224cca797734434289b648b8f
    }
}
