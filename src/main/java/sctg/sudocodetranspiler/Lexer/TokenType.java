package sctg.sudocodetranspiler.Lexer;

public enum TokenType {
    // Keywords
    SET, AS, PROCESS, GIVES, GIVES_A, USING,
    TO, UNTIL, WHILE, WHILE_DOING, BY_DOING,
    FIND, IS, SKIP, HALT, THEN, DO, WITH, INPUT_FROM, PRINT, BY, NOTE, ABSENT,
    
    //Conditioals
    IF, ELSE_IF, ELSE, 
    
    //Data Types
    NUMBER, DECIMAL, SENTENCE, BOOLEAN,

    // Operators
    PLUS, MINUS, MULTIPLY, DIVIDE, MODULO,
    ASSIGN, GT, LT, GE, LE, EQ, NE,
    INCREMENT, DECREMENT,
    
    //Asignment Operator
    ADDING, SUBTRACTING, MULTIPLYING, DIVIDING,
    
    //ADD, SUBTRACT, DIVIDE, MULTIPLYING
    OP,

    // Identifiers and literals
    IDENTIFIER, NUMBER_LITERAL, DECIMAL_LITERAL, STRING_LITERAL, BOOLEAN_LITERAL,

    // Symbols
    COMMA, COLON, LPAREN, RPAREN, LBRACE, RBRACE, PERIOD,

    // Misc
    EOF
}
