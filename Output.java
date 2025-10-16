import java.util.Scanner;

public class Output {
    public static float plus(float a, float b) {
        float total = a + b;
        return total;
    }

    public static float minus(float a, float b) {
        float total = a - b;
        return total;
    }

    public static float multi(float a, float b) {
        float total = a * b;
        return total;
    }

    public static float divi(float a, float b) {
        float total = a / b;
        return total;
    }
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
        boolean auth = true;
        while (auth == true) {
    float a = 0.0f;
        a = inferredInput( a, """
Enter 1st Number: """
);
    int op= 0;
        op = inferredInput( op, """
Enter Operator:
1. Add
2. Subtract	
3. Multiply
4. Divide
Choice: """
);
    float b = 0.0f;
        b = inferredInput( b, """
Enter 2nd Number: """
);
    float result = 0;
    switch (op) {
    case 1 -> {
        result = plus(a, b);
    }
    case 2 -> {
        result = minus(a, b);
    }
    case 3 -> {
        result = multi(a, b);
    }
    case 4 -> {
        result = divi(a, b);
    }
    default -> {
        System.out.println("""
 Invalid """
);
    }
}
    System.out.println(result);
}
    }
}
