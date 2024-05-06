package main.models;

//public enum TokenType {
//    Keyword_Begin,
//    Keyword_End,
//    End_of_input,
//    Semicolon, Identifier, Integer, Boolean,
//    Op_greater, Op_greaterequal, Op_equal, Op_assign, Op_multiply, Op_add,
//    Keyword_do, Keyword_while,
//    Keyword_if, Keyword_then, Keyword_else, Keyword_print,
//    LeftParen, RightParen, LeftBrace, RightBrace, Unknow, Comma,
//    TrueConstant, FalseConstant
//}

public enum TokenType {
    Keyword_Begin(false, false, -1, NodeType.nd_None),
    Keyword_End(false, false, -1, NodeType.nd_None),

    End_of_input(false, false, -1, NodeType.nd_None),
    Unknow(false, false, -1, NodeType.nd_None),

    Op_multiply(false, true, 5, NodeType.nd_Mul),
    Op_add(false, true, 4, NodeType.nd_Add),
    Op_greater(false, true, 3, NodeType.nd_Gtr),
    Op_greaterequal(false, true, 3, NodeType.nd_Geq),
    Op_equal(false, true, 2, NodeType.nd_Eql),
    Op_assign(false, false, -1, NodeType.nd_Assign),

    LeftParen(false, false, 6, NodeType.nd_None),
    RightParen(false, false, 6, NodeType.nd_None),
    LeftBrace(false, false, -1, NodeType.nd_None),
    RightBrace(false, false, -1, NodeType.nd_None),
    Semicolon(false, false, -1, NodeType.nd_None),

    Identifier(false, false, -1, NodeType.nd_Ident),
    Integer(false, false, -1, NodeType.nd_Integer),
    TrueConstant(false, false, -1, NodeType.nd_True),
    FalseConstant(false, false, -1, NodeType.nd_False),

    Keyword_if(false, false, -1, NodeType.nd_If),
    Keyword_else(false, false, -1, NodeType.nd_None),
    Keyword_then(false, false, -1, NodeType.nd_Then),
    Keyword_while(false, false, -1, NodeType.nd_Then),
    Keyword_do(false, false, -1, NodeType.nd_do),
    Keyword_print(false, false, -1, NodeType.nd_None),
    Keyword_int(false, false, -1, NodeType.nd_int),
    Keyword_bool(false, false, -1, NodeType.nd_Bool);


    private final int precedence;
    private final boolean right_assoc; //remove
    private final boolean is_binary;

    private final NodeType node_type;

    TokenType(boolean right_assoc, boolean is_binary, int precedence, NodeType node) {
        this.right_assoc = right_assoc;
        this.is_binary = is_binary;

        this.precedence = precedence;
        this.node_type = node;
    }

    public boolean isRightAssoc() {
        return this.right_assoc;
    }

    public boolean isBinary() {
        return this.is_binary;
    }
    
    public int getPrecedence() {
        return this.precedence;
    }

    public NodeType getNodeType() {
        return this.node_type;
    }
}