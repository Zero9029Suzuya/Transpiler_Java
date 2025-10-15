package sctg.sudocodetranspiler.Lexer;
import java.util.*;

public class LexicalAnalyzer {
    private List<Token> Tokens;
    private final String input;
    private int row = 1;
    private int col = 1;
    
    private int position = 0;
    private char current;
    private String text;
    
    public LexicalAnalyzer(String input){
        this.input = input;
        Tokens = new ArrayList<>();
    }
    
    public void tokenize(){
        while (position < input.length()){
            current = input.charAt(position);
            
            if (Character.isWhitespace(current)){
                handleWhitespace();
                continue;
            }
            
            if (current == '.'){
                Tokens.add(new Token(TokenType.PERIOD, ".", row, col));
                position++;
                col++;
                continue;
            }
            
            if (Character.isLetter(current)){
                handleLetter();
                continue;
            }
            
            if (Character.isDigit(current) || current == '-'){
                handleDigit();
                continue;
            }
            
            // Place Symbol Handler Here.
            if ("+-*/=<>!:(){},\"%".indexOf(current) != -1) {
                handleSymbols();
                continue;
            }
            
            throw new RuntimeException("Unexpected character: '" + current + "' at " + row + ":" + col);
        }
        Tokens.add(new Token(TokenType.EOF, "\0", row, col));
    }
    
    public void PrintTokens(){
        for(Token t : Tokens){
            System.out.println(t.toString());
        }
    }
    
    public List<Token> getTokens(){
        return Tokens;
    }
    
    private void handleWhitespace(){
        if(current == '\n'){
            row++;
            col = 1;
        } else {
            col++;
        }
        position++;
    }
    
    private void handleLetter(){
        int start = position;
        while (position < input.length() && Character.isLetterOrDigit(input.charAt(position))){
            position++;
        }

        
        text = input.substring(start, position);
        TokenType type = null; 
        switch(text.toLowerCase()){
            case "set" -> type = TokenType.SET;
            case "as" -> type =  TokenType.AS;
            case "then" -> type = TokenType.THEN;
            case "number" -> type = TokenType.NUMBER;
            case "decimal" -> type = TokenType.DECIMAL;
            case "sentence" -> type = TokenType.SENTENCE;
            case "boolean" -> type = TokenType.BOOLEAN;
            case "process" -> type = TokenType.PROCESS;
            case "using" -> type = TokenType.USING;
            case "true" -> type = TokenType.BOOLEAN_LITERAL;
            case "false" -> type = TokenType.BOOLEAN_LITERAL;
            
            case "do" -> type = TokenType.DO;
            case "is" -> type = TokenType.IS;
            case "halt" -> type = TokenType.HALT;
            case "skip" -> type = TokenType.SKIP;
            case "with" -> type = TokenType.WITH;
            case "print" -> type = TokenType.PRINT;
            case "increment" -> type = TokenType.INCREMENT;
            case "decrement" -> type = TokenType.DECREMENT;
            case "adding" -> type = TokenType.ADDING;
            case "subtracting" -> type = TokenType.SUBTRACTING;
            case "multiplying" -> type = TokenType.MULTIPLYING;
            case "dividing" -> type = TokenType.DIVIDING;
            case "to" -> type = TokenType.TO;
            case "until" -> type = TokenType.UNTIL;
            case "find" -> type = TokenType.FIND;
            case "if" -> type = TokenType.IF;
            case "absent" -> type = TokenType.ABSENT;
            case "gives" -> {
                if (lookAhead().equalsIgnoreCase("a")){
                    lookAhead(true);
                    type = TokenType.GIVES_A;
                } else {
                    type = TokenType.GIVES;
                }
            }
            
            case "input" -> {
                if (lookAhead().equalsIgnoreCase("from")){
                    lookAhead(true);
                    type =  TokenType.INPUT_FROM;
                } else {
                    throw new RuntimeException("Missing \"from\" keyword at: " + row + ":" + col);
                }
            }
            case "else" -> {
                if (lookAhead().equalsIgnoreCase("if")){
                    lookAhead(true);
                    type =  TokenType.ELSE_IF;
                } else {
                    type =  TokenType.ELSE;
                }
            }
            case "while" -> {
                if (lookAhead().equalsIgnoreCase("doing")){
                    lookAhead(true);
                    type =  TokenType.WHILE_DOING;
                } else {
                    type =  TokenType.WHILE;
                }
            }
            case "by" -> {
                if (lookAhead().equalsIgnoreCase("doing")){
                    lookAhead(true);
                    type =  TokenType.BY_DOING;
                } else {
                    type =  TokenType.BY;
                }
            }
            case "note" -> {
                if (input.charAt(position) == ':'){
                    while (input.charAt(position) != '\n'){
                        position++;
                    }
                    col = 1;
                }
            }
            default -> type = TokenType.IDENTIFIER;
        }
        
        if (type != null) Tokens.add(new Token(type, text, row, col));
        col += text.length();
    }
    
    private void handleDigit(){
        boolean isDecimal = false;
        boolean isNegative = false;
        
        if (current == '-') {
            if (position + 1 < input.length() && Character.isDigit(input.charAt(position + 1))) {
                isNegative = true;
                position++;
            } else if (position + 1 < input.length() && input.charAt(position + 1) != ' '){
                throw new RuntimeException("Unexpected '-' at " + row + ":" + col);
            }
        }
        
        int start = position;
        while (position < input.length() && (Character.isDigit(input.charAt(position)) || input.charAt(position) == ',')){
            if (input.charAt(position) == ',' && !isDecimal){
                isDecimal = true;
            } else if (input.charAt(position) == ',' && isDecimal) {
                throw new RuntimeException("Unexpected character in number '" + current + "' at " + row + ":" + col);
            }
            position++;
        }
        if(position < input.length() && input.charAt(position) == '-' && isNegative){
            throw new RuntimeException("Unexpected '-' at" + row + ":" + col);
        } else if (start == position && !isNegative){
            Tokens.add(new Token(TokenType.MINUS, "-", row, col));
            position++;
            col++;
            return;
        }
        
        text = isNegative ? ("-" + input.substring(start, position)): input.substring(start, position);
        
        TokenType type = isDecimal ? TokenType.DECIMAL_LITERAL : TokenType.NUMBER_LITERAL;
        
        Tokens.add(new Token(type, text, row, col));
        col += text.length();
    }
    
    private void handleSymbols(){
        char next = (position + 1 < input.length() ? input.charAt(position + 1) : '\0');
        TokenType type = null;
        
        switch(current){
            //Negative is at Digits
            case ':' -> type = TokenType.COLON;
            case '+' -> type = TokenType.PLUS;
            case '/' -> type = TokenType.DIVIDE;
            case '*' -> type = TokenType.MULTIPLY;
            case '=' -> type = TokenType.EQ;
            case '(' -> type = TokenType.LPAREN;
            case ')' -> type = TokenType.RPAREN;
            case '{' -> type = TokenType.LBRACE;
            case '}' -> type = TokenType.RBRACE;
            case ',' -> type = TokenType.COMMA;
            case '%' -> type = TokenType.MODULO;
            case '!' -> {
                switch(next) {
                    case '=' -> type = TokenType.NE;
                    default -> throw new RuntimeException("Unknown operator: '" + current + "' at " + row + ":" + col);
                }
            }
            case '>' -> {
                switch(next) {
                    case ' ' -> type = TokenType.GT;
                    case '=' -> type = TokenType.GE;
                    default -> throw new RuntimeException("Unknown operator: '" + current + "' at " + row + ":" + col);
                }
            }
            case '<' -> {
                switch(next) {
                    case ' ' -> type = TokenType.LT;
                    case '=' -> type = TokenType.LE;
                    default -> throw new RuntimeException("Unknown operator: '" + current + "' at " + row + ":" + col);
                }
            }
            case '"' -> {
                type = handleStringLiteral();
                Tokens.add(new Token(type, text, row, col));
                col++;
                position++;
                return;
            }
            default -> throw new RuntimeException("Unknown operator: '" + current + "' at " + row + ":" + col);
        }
        
        if (next == '\0' || Character.isWhitespace(next)){
            text = Character.toString(current);
        } else if (!Character.isLetterOrDigit(next) && next != '.'){
            text = Character.toString(current) + Character.toString(next);
            col++;
            position++;
        }
        
        Tokens.add(new Token(type, text, row, col));
        col++;
        position++;
    }
    
    private String lookAhead(){
        return lookAhead(false);
    }
    
    private String lookAhead(boolean consume){
        int index = position + 1;
        
        while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
        index++;
        }
        
        if (index >= input.length()){
            return null;
        }
        
        int start = index;
        while (index < input.length() && Character.isLetterOrDigit(input.charAt(index))){
            index++;
        }
        
        String nextWord = input.substring(start, index);
        if (consume){
            position = index;
            text = text + " " + nextWord;
        }
        return nextWord;
    }
    
    private TokenType handleStringLiteral(){
        int index = position;
        
        while (index < input.length()){
            index++;
            if (input.charAt(index) == '"'){
                if(input.charAt(index - 1) == '\\'){
                    index++;
                    continue;
                } else {
                    text = input.substring(position + 1, index);
                    position = index;
                    col += index - position;
                    return TokenType.STRING_LITERAL;
                }
            }
            if (input.charAt(index) == '\n'){
                row++;
                col = 1;
            }
        }
        throw new RuntimeException("Missing Closer '\"' at: " + row + ":" + col);
    }
}
