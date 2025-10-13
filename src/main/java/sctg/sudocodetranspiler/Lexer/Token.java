package sctg.sudocodetranspiler.Lexer;

public class Token {
    private final TokenType type;
    private final String value;
    private final int row;
    private final int column;

    public Token(TokenType type, String value, int row, int column) {
        this.type = type;
        this.value = value;
        this.row = row;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
    public int getRow() {
        return row;
    }
    

    public int getLine() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("%s('%s') @ %d:%d", type, value, row, column);
    }
}
