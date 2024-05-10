package models;

public enum TokenType {
    Keyword_Begin(false, -1, NodeType.nd_None),
    Keyword_End(false, -1, NodeType.nd_None),

    End_of_input(false, -1, NodeType.nd_None),
    Unknow(false, -1, NodeType.nd_None),

    Op_multiply(true, 5, NodeType.nd_Mul),
    Op_add(true, 4, NodeType.nd_Add),
    Op_greater(true, 3, NodeType.nd_Gtr),
    Op_greaterequal(true, 3, NodeType.nd_Geq),
    Op_equal(true, 2, NodeType.nd_Eql),
    Op_assign(false, -1, NodeType.nd_Assign),

    LeftParen(false, 6, NodeType.nd_None),
    RightParen(false, 6, NodeType.nd_None),

    LeftBrace(false, -1, NodeType.nd_None),
    RightBrace(false, -1, NodeType.nd_None),
    Semicolon(false, -1, NodeType.nd_None),

    Identifier(false, -1, NodeType.nd_Ident),
    Integer(false, -1, NodeType.nd_Integer),
    TrueConstant(false, -1, NodeType.nd_True),
    FalseConstant(false, -1, NodeType.nd_False),

    Keyword_if(false, -1, NodeType.nd_If),
    Keyword_else(false, -1, NodeType.nd_None),
    Keyword_then(false, -1, NodeType.nd_Then),
    Keyword_while(false, -1, NodeType.nd_Then),
    Keyword_do(false, -1, NodeType.nd_do),
    Keyword_print(false, -1, NodeType.nd_None),
    Keyword_int(false, -1, NodeType.nd_int),
    Keyword_bool(false, -1, NodeType.nd_Bool);

    /// `precedence` tỉ lệ thuận độ ưu tiên của phép toán trong biểu thức
    private final int precedence;
    /// Associativity áp dụng cho các toán tử như +, -, *, /
    /// để chỉ định nếu phép tính diễn ra từ trái sang phải hoặc từ phải sang trái.

    /// xác định xem token đó có phải là toán tử nhị phân không
    private final boolean is_binary;

    private final NodeType node_type;

    TokenType(boolean is_binary, int precedence, NodeType node) {

        this.is_binary = is_binary;

        this.precedence = precedence;
        this.node_type = node;
    }

    public boolean isBinary() {
        return this.is_binary;
    }

    public int getPrecedence() {
        return this.precedence;
    }

//    public NodeType getNodeType() {
//        return this.node_type;
//    }
}