package sctg.sudocodetranspiler.Parser;

import sctg.sudocodetranspiler.Lexer.Token;
import sctg.sudocodetranspiler.Lexer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomParser {
    private final List<Token> tokens;
    private int pos = 0;
    private final StringBuilder javaCode = new StringBuilder();
    private final Map<String, String> variableTypes = new HashMap<>(); // Tracks variable types for input

    public CustomParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public String transpile() {
        javaCode.append("import java.util.Scanner;\n\n");
        javaCode.append("public class Main {\n");
        List<String> methods = new ArrayList<>();
        StringBuilder mainCode = new StringBuilder();
        mainCode.append("    public static void main(String[] args) {\n");

        while (!isEOF()) {
            if (current().getType() == TokenType.PROCESS) {
                methods.add(parseFunctionDef());
            } else {
                mainCode.append("\t\t").append(parseStatement());
            }
        }

        mainCode.append("    }\n");
        javaCode.append(String.join("\n", methods));
        javaCode.append(generateInferredInputMethods());
        javaCode.append(mainCode);
        javaCode.append("}\n");
        return javaCode.toString();
    }

    private String parseStatement() {
        Token t = current();
        switch (t.getType()) {
            case SET -> {
                advance();
                return parseSet();
            }
            case USING -> {
                advance();
                String call = parseFunctionCall();
                expect(TokenType.PERIOD);
                return call + ";\n";
            }
            case IF -> {
                advance();
                return parseIf();
            }
            case WHILE -> {
                advance();
                return parseWhile();
            }
            case FIND -> {
                advance();
                return parseSwitch();
            }
            case SKIP -> {
                advance();
                expect(TokenType.PERIOD);
                return "continue;\n";
            }
            case HALT -> {
                advance();
                expect(TokenType.PERIOD);
                return "break;\n";
            }
            case PRINT -> {
                advance();
                return parsePrint();
            }
            default -> throw new RuntimeException("Invalid statement starting with: " + t);
        }
    }

    private String parseSet() {
        Token next = current();
        if (next.getType() == TokenType.NUMBER || next.getType() == TokenType.DECIMAL ||
            next.getType() == TokenType.WORDS || next.getType() == TokenType.BOOLEAN) {
            // Declarationq
            String type = mapType(next.getValue());
            advance();
            Token id = expect(TokenType.IDENTIFIER);
            variableTypes.put(id.getValue(), type); // Store variable type
            expect(TokenType.AS);
            if (current().getType() == TokenType.INPUT_FROM) {
                advance();
                String prompt = parseExpression();
                expect(TokenType.PERIOD);
                return id.getValue() + " = inferredInput(" + id.getValue() + ", " + prompt + ");\n";
            }
            String expr = parseExpression();
            expect(TokenType.PERIOD);
            return type + " " + id.getValue() + " = " + expr + ";\n";
        } else if (next.getType() == TokenType.IDENTIFIER) {
            Token id = expect(TokenType.IDENTIFIER); // Use expect to advance
            Token look = current();
            if (look.getType() == TokenType.AS) {
                // Assignment or Input
                advance();
                if (current().getType() == TokenType.INPUT_FROM) {
                    advance();
                    String prompt = parseExpression();
                    expect(TokenType.PERIOD);
                    if (!variableTypes.containsKey(id.getValue())) {
                        throw new RuntimeException("Variable " + id.getValue() + " not declared before input at " + id.getLine() + ":" + id.getColumn());
                    }
                    return id.getValue() + " = inferredInput(" + id.getValue() + ", " + prompt + ");\n";
                }
                String expr = parseExpression();
                expect(TokenType.PERIOD);
                return id.getValue() + " = " + expr + ";\n";
            } else if (look.getType() == TokenType.TO) {
                // For loop
                advance();
                String initValue = parseExpression();
                expect(TokenType.THEN);
                Token opToken = expect(TokenType.IDENTIFIER);
                String opWord = opToken.getValue().toLowerCase();
                if (opWord.equals("multiply") || opWord.equals("divide")) {
                    expect(TokenType.BY);
                }
                String stepValue = parseExpression();
                String stepOp;
                switch (opWord) {
                    case "add" -> stepOp = "+=";
                    case "subtract" -> stepOp = "-=";
                    case "multiply" -> stepOp = "*=";
                    case "divide" -> stepOp = "/=";
                    default -> throw new RuntimeException("Unknown operation: " + opWord);
                }
                expect(TokenType.UNTIL);
                Token condId = expect(TokenType.IDENTIFIER);
                if (!condId.getValue().equals(id.getValue())) {
                    throw new RuntimeException("Until must reference the same identifier: " + id.getValue());
                }
                String compOp = parseCompOp();
                String rangeValue = parseExpression();
                expect(TokenType.WHILE_DOING);
                expect(TokenType.COLON);
                expect(TokenType.LBRACE);
                StringBuilder body = new StringBuilder();
                while (!match(TokenType.RBRACE)) {
                    body.append("    ").append(parseStatement());
                }
                return "for (int " + id.getValue() + " = " + initValue + "; " +
                       id.getValue() + " " + compOp + " " + rangeValue + "; " +
                       id.getValue() + stepOp + " " + stepValue + ") {\n" +
                       body + "}\n";
            }
        }
        throw new RuntimeException("Invalid set statement starting with: " + next);
    }

    private String parseFunctionDef() {
        Token name = expect(TokenType.IDENTIFIER);
        expect(TokenType.GIVES);
        Token retType = expectDatatype();
        String javaRet = mapType(retType.getValue());
        expect(TokenType.USING);
        List<String> params = new ArrayList<>();
        if (current().getType() != TokenType.BY_DOING) {
            do {
                Token pType = expectDatatype();
                Token pName = expect(TokenType.IDENTIFIER);
                params.add(mapType(pType.getValue()) + " " + pName.getValue());
            } while (match(TokenType.COMMA));
        }
        expect(TokenType.BY_DOING);
        expect(TokenType.COLON);
        expect(TokenType.LBRACE);
        StringBuilder body = new StringBuilder();
        while (!match(TokenType.RBRACE)) {
            body.append("        ").append(parseStatement());
        }
        return "    public static " + javaRet + " " + name.getValue() + "(" +
               String.join(", ", params) + ") {\n" + body + "    }\n";
    }

    private String parseFunctionCall() {
        Token name = expect(TokenType.IDENTIFIER);
        expect(TokenType.WITH);
        StringBuilder sb = new StringBuilder(name.getValue() + "(");
        if (current().getType() != TokenType.PERIOD) {
            do {
                sb.append(parseExpression());
                if (!match(TokenType.COMMA)) {
                    break;
                }
                sb.append(", ");
            } while (true);
        }
        sb.append(")");
        return sb.toString();
    }

    private String parseIf() {
        StringBuilder sb = new StringBuilder("if (" + parseCondition() + ") ");
        expect(TokenType.DO);
        expect(TokenType.COLON);
        expect(TokenType.LBRACE);
        StringBuilder body = new StringBuilder();
        while (!match(TokenType.RBRACE)) {
            body.append("    ").append(parseStatement());
        }
        sb.append("{\n").append(body).append("}\n");
        while (match(TokenType.ELSE_IF)) {
            sb.append("else if (").append(parseCondition()).append(") ");
            expect(TokenType.DO);
            expect(TokenType.COLON);
            expect(TokenType.LBRACE);
            body = new StringBuilder();
            while (!match(TokenType.RBRACE)) {
                body.append("    ").append(parseStatement());
            }
            sb.append("{\n").append(body).append("}\n");
        }
        if (match(TokenType.ELSE)) {
            expect(TokenType.DO);
            expect(TokenType.COLON);
            expect(TokenType.LBRACE);
            body = new StringBuilder();
            while (!match(TokenType.RBRACE)) {
                body.append("    ").append(parseStatement());
            }
            sb.append("else {\n").append(body).append("}\n");
        }
        return sb.toString();
    }

    private String parseWhile() {
        StringBuilder sb = new StringBuilder("while (" + parseCondition() + ") ");
        expect(TokenType.DO);
        expect(TokenType.COLON);
        expect(TokenType.LBRACE);
        StringBuilder body = new StringBuilder();
        while (!match(TokenType.RBRACE)) {
            body.append("    ").append(parseStatement());
        }
        sb.append("{\n").append(body).append("}\n");
        return sb.toString();
    }

    private String parseSwitch() {
        Token var = expect(TokenType.IDENTIFIER);
        expect(TokenType.THEN);
        expect(TokenType.DO);
        expect(TokenType.COLON);
        expect(TokenType.LBRACE);
        StringBuilder sb = new StringBuilder("switch (" + var.getValue() + ") {\n");
        while (!match(TokenType.RBRACE)) {
            expect(TokenType.IS);
            String cond = parseExpression();
            expect(TokenType.COLON);
            expect(TokenType.LBRACE);
            StringBuilder caseBody = new StringBuilder();
            while (!match(TokenType.RBRACE)) {
                caseBody.append("        ").append(parseStatement());
            }
            sb.append("    case ").append(cond).append(" -> {\n")
              .append(caseBody)
              .append("    }\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    private String parsePrint() {
        StringBuilder sb = new StringBuilder("System.out.println(");
        expect(TokenType.LPAREN);
        do {
            sb.append(parseExpression());
            if (!match(TokenType.COMMA)) {
                break;
            }
            sb.append(" + ");
        } while (true);
        expect(TokenType.RPAREN);
        expect(TokenType.PERIOD);
        sb.append(");\n");
        return sb.toString();
    }

    private String parseCondition() {
        String left = parseExpression();
        String op = parseCompOp();
        if (op.isEmpty()) {
            return left;
        }
        String right = parseExpression();
        return left + " " + op + " " + right;
    }

    private String parseCompOp() {
        if (match(TokenType.GT)) return ">";
        if (match(TokenType.LT)) return "<";
        if (match(TokenType.GE)) return ">=";
        if (match(TokenType.LE)) return "<=";
        if (match(TokenType.EQ)) return "==";
        if (match(TokenType.NE)) return "!=";
        return "";
    }

    private String parseExpression() {
        return parseAdditive();
    }

    private String parseAdditive() {
        String left = parseMultiplicative();
        while (true) {
            if (match(TokenType.PLUS)) {
                left += " + " + parseMultiplicative();
            } else if (match(TokenType.MINUS)) {
                left += " - " + parseMultiplicative();
            } else {
                break;
            }
        }
        return left;
    }

    private String parseMultiplicative() {
        String left = parseUnary();
        while (true) {
            if (match(TokenType.MULTIPLY)) {
                left += " * " + parseUnary();
            } else if (match(TokenType.DIVIDE)) {
                left += " / " + parseUnary();
            } else {
                break;
            }
        }
        return left;
    }

    private String parseUnary() {
        if (match(TokenType.MINUS)) {
            return "-" + parsePrimary();
        }
        return parsePrimary();
    }

    private String parsePrimary() {
        Token t = current();
        if (t.getType() == TokenType.NUMBER_LITERAL) {
            advance();
            return t.getValue();
        } else if (t.getType() == TokenType.DECIMAL_LITERAL) {
            advance();
            return t.getValue().replace(',', '.');
        } else if (t.getType() == TokenType.STRING_LITERAL) {
            advance();
            return "\"" + t.getValue() + "\"";
        } else if (t.getType() == TokenType.BOOLEAN_LITERAL) {
            advance();
            return t.getValue();
        } else if (t.getType() == TokenType.IDENTIFIER) {
            advance();
            if (current().getType() == TokenType.WITH) {
                pos--;
                return parseFunctionCall();
            }
            return t.getValue();
        } else if (match(TokenType.LPAREN)) {
            String expr = parseExpression();
            expect(TokenType.RPAREN);
            return "(" + expr + ")";
        } else {
            throw new RuntimeException("Unexpected token in expression: " + t);
        }
    }

    private String mapType(String sucoType) {
        return switch (sucoType.toLowerCase()) {
            case "number" -> "int";
            case "decimal" -> "float";
            case "words" -> "String";
            case "boolean" -> "boolean";
            default -> throw new RuntimeException("Unknown data type: " + sucoType);
        };
    }

    private Token expectDatatype() {
        Token t = current();
        if (t.getType() == TokenType.NUMBER || t.getType() == TokenType.DECIMAL ||
            t.getType() == TokenType.WORDS || t.getType() == TokenType.BOOLEAN) {
            advance();
            return t;
        }
        throw new RuntimeException("Expected data type but got: " + t.getType());
    }

    private String generateInferredInputMethods() {
        return """
                public static String inferredInput(String identifier, String inputPrompt) {
                    try (Scanner scanner = new Scanner(System.in)) {
                        System.out.print(inputPrompt);
                        return scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Error reading String input: " + e.getMessage());
                        return null;
                    }
                }

                public static int inferredInput(int identifier, String inputPrompt) {
                    try (Scanner scanner = new Scanner(System.in)) {
                        System.out.print(inputPrompt);
                        return scanner.nextInt();
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Error: Please enter a valid integer.");
                        return inferredInput(identifier, inputPrompt);
                    }
                }

                public static float inferredInput(float identifier, String inputPrompt) {
                    try (Scanner scanner = new Scanner(System.in)) {
                        System.out.print(inputPrompt);
                        return scanner.nextFloat();
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Error: Please enter a valid float.");
                        return inferredInput(identifier, inputPrompt);
                    }
                }

                public static boolean inferredInput(boolean identifier, String inputPrompt) {
                    try (Scanner scanner = new Scanner(System.in)) {
                        System.out.print(inputPrompt);
                        return scanner.nextBoolean();
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Error: Please enter 'true' or 'false'.");
                        return inferredInput(identifier, inputPrompt);
                    }
                }
        """;
    }

    private Token current() {
        if (pos >= tokens.size()) {
            return new Token(TokenType.EOF, "", -1, -1);
        }
        return tokens.get(pos);
    }

    private void advance() {
        pos++;
    }

    private boolean match(TokenType type) {
        if (current().getType() == type) {
            advance();
            return true;
        }
        return false;
    }

    private Token expect(TokenType type) {
        if (current().getType() == type) {
            Token t = current();
            advance();
            return t;
        }
        throw new RuntimeException("Expected " + type + " but got " + current().getType());
    }

    private boolean isEOF() {
        return pos >= tokens.size() || current().getType() == TokenType.EOF;
    }
}