package main.parser;

import main.models.Token;
import main.models.TokenType;
import main.shared.ErrorHandler;

import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Parser {
    private final List<Token> source;
    private Token token;
    private int position;


    Parser(List<Token> source) {
        this.source = source;
        this.token = null;
        this.position = 0;
    }

    void getNextToken() {
        this.token = this.source.get(this.position++);
    }

    void expr(int p) {
        TokenType op;
        int q;

        if (this.token.tokentype == TokenType.LeftParen) {
            paren_expr();
        } else if (this.token.tokentype == TokenType.Identifier) {
            getNextToken();
        } else if (this.token.tokentype == TokenType.Integer) {
            getNextToken();
        } else {
            ErrorHandler.error(this.token.line, this.token.pos, "Expecting a term, found: " + this.token.tokentype);
        }

        while (this.token.tokentype.isBinary() && this.token.tokentype.getPrecedence() >= p) {
            op = this.token.tokentype;
            getNextToken();
            q = op.getPrecedence();
            if (!op.isRightAssoc()) {
                q++;
            }
            expr(q);
        }
    }

    void paren_expr() {
        expect("paren_expr", TokenType.LeftParen);
        expr(0);
        expect("paren_expr", TokenType.RightParen);
    }

    void expect(String msg, TokenType s) {
        if (this.token.tokentype == s) {
            getNextToken();
            return;
        }
        ErrorHandler.error(this.token.line, this.token.pos, msg + ": Expecting '" + s + "', found: '" + this.token.tokentype + "'");
    }

    void stmt() {
//        Node s = null, s2 = null, t = null, e, v;

        switch (this.token.tokentype) {
            case Keyword_if -> {
                getNextToken();
                // expect expression
                expr(0);


                if (this.token.tokentype == TokenType.Keyword_then) {
                    getNextToken();
                    stmt();

                    // handle thêm trường hợp có else, không có thì thoát hàm stmt()
                    if (this.token.tokentype == TokenType.Keyword_else) {
                        getNextToken();
                        stmt();
                    }
                }
//                t = Node.make_node(NodeType.nd_If, e, Node.make_node(NodeType.nd_If, s, s2));
            }

            case Keyword_do -> {
                getNextToken();
//                s =
                stmt();
                if (this.token.tokentype == TokenType.Keyword_while) {
                    getNextToken();
//                    e =
                    paren_expr();
                    expect("While", TokenType.Semicolon);
                }
//                t = Node.make_node(NodeType.nd_do, null, Node.make_node(NodeType.nd_do, s, t));
            }

            case Keyword_print -> {
                getNextToken();
                expect("Print", TokenType.LeftParen);
                expr(0);
//                e = Node.make_node(NodeType.nd_Prti, expr(0), null);
//                t = Node.make_node(NodeType.nd_Statement, t, e);

                expect("Print", TokenType.RightParen);
                expect("Print", TokenType.Semicolon);
            }
            case Keyword_int, Keyword_bool -> {
                getNextToken();
                stmt();
            }

            case Semicolon -> getNextToken();

            case Identifier -> {
//                v = Node.make_leaf(NodeType.nd_Ident, this.token.value);
                getNextToken();

                // Kiểm tra xem token tiếp theo là gì
                if (this.token.tokentype == TokenType.Semicolon) {
                    // Trường hợp chỉ khai báo, không gán giá trị
//                    t = Node.make_node(NodeType.nd_Declaration, v); // Nút khai báo
                    getNextToken(); // Di chuyển qua dấu chấm phẩy
                } else if (this.token.tokentype == TokenType.Op_assign) {
                    // Trường hợp gán giá trị
                    getNextToken(); // Di chuyển qua dấu gán
//                    e =
                    expr(0); // Đánh giá biểu thức để lấy giá trị gán
//                    t = Node.make_node(NodeType.nd_Assign, v, e); // Tạo nút gán giá trị
                    expect("assign", TokenType.Semicolon); // Kiểm tra dấu chấm phẩy
                } else {
                    // Trường hợp token không được mong đợi
                    ErrorHandler.error(this.token.line, this.token.pos, "Expected ';' or '=' but got: " + this.token.tokentype);
                }
            }

            case LeftBrace -> {
                getNextToken();
                while (this.token.tokentype != TokenType.RightBrace && this.token.tokentype != TokenType.End_of_input) {
                    stmt();
//                    t = Node.make_node(NodeType.nd_Statement, t, stmt());
                }
                expect("RBrace", TokenType.RightBrace);
            }

            default ->
                    ErrorHandler.error(this.token.line, this.token.pos, "Expecting start of statement, found: " + this.token.tokentype);
        }
    }


    public void parse() {
//        Node t = null;

        getNextToken();
        // Check for "begin" keyword
        if (this.token.tokentype != TokenType.Keyword_Begin) {
            ErrorHandler.error(this.token.line, this.token.pos, "Expected 'begin' keyword at the beginning of the program");
            return;
        }
        getNextToken();

        // Parse the statement
        while (this.token.tokentype != TokenType.Keyword_End) {
            stmt();
        }

        expect("End of statement", TokenType.Keyword_End);

        System.out.println("Accepted");
    }

//    void printAST(Node t) {
//        int i = 0;
//        if (t == null) {
//            System.out.println(";");
//        } else {
//            System.out.printf("%-14s", t.nt);
//            if (t.nt == NodeType.nd_Ident || t.nt == NodeType.nd_Integer) {
//                System.out.println(" " + t.value);
//            } else {
//                System.out.println();
//                printAST(t.left);
//                printAST(t.right);
//            }
//        }
//    }


    public static void parse(String lexFilePath) {

        try {
            StringBuilder value;
            String token;
            int line, pos;
            Token t;
            boolean found;

            List<Token> tokens = new ArrayList<>();
            Map<String, TokenType> str_to_tokens = new HashMap<>();

            str_to_tokens.put("Keyword_Begin", TokenType.Keyword_Begin);
            str_to_tokens.put("Keyword_End", TokenType.Keyword_End);

            str_to_tokens.put("End_of_input", TokenType.End_of_input);
            str_to_tokens.put("Op_multiply", TokenType.Op_multiply);
            str_to_tokens.put("Op_add", TokenType.Op_add);
            str_to_tokens.put("Op_greater", TokenType.Op_greater);
            str_to_tokens.put("Op_greaterequal", TokenType.Op_greaterequal);
            str_to_tokens.put("Op_equal", TokenType.Op_equal);
            str_to_tokens.put("Op_assign", TokenType.Op_assign);

            str_to_tokens.put("Keyword_if", TokenType.Keyword_if);
            str_to_tokens.put("Keyword_else", TokenType.Keyword_else);
            str_to_tokens.put("Keyword_then", TokenType.Keyword_then);
            str_to_tokens.put("Keyword_do", TokenType.Keyword_do);
            str_to_tokens.put("Keyword_while", TokenType.Keyword_while);
            str_to_tokens.put("Keyword_int", TokenType.Keyword_int);
            str_to_tokens.put("Keyword_bool", TokenType.Keyword_bool);

            str_to_tokens.put("Keyword_print", TokenType.Keyword_print);
            str_to_tokens.put("LeftParen", TokenType.LeftParen);
            str_to_tokens.put("RightParen", TokenType.RightParen);
            str_to_tokens.put("LeftBrace", TokenType.LeftBrace);
            str_to_tokens.put("RightBrace", TokenType.RightBrace);
            str_to_tokens.put("Semicolon", TokenType.Semicolon);

            str_to_tokens.put("Identifier", TokenType.Identifier);
            str_to_tokens.put("Integer", TokenType.Integer);
            str_to_tokens.put("True", TokenType.TrueConstant);
            str_to_tokens.put("False", TokenType.FalseConstant);


            Scanner s = new Scanner(new File(lexFilePath));
            String source = " ";
            while (s.hasNext()) {
                String str = s.nextLine();
                StringTokenizer st = new StringTokenizer(str);
                line = Integer.parseInt(st.nextToken());
                pos = Integer.parseInt(st.nextToken());
                token = st.nextToken();
                value = new StringBuilder();
                while (st.hasMoreTokens()) {
                    value.append(st.nextToken()).append(" ");
                }
                found = false;
                if (str_to_tokens.containsKey(token)) {
                    found = true;
                    tokens.add(new Token(str_to_tokens.get(token), value.toString(), line, pos));
                }

                if (!found) {
                    throw new Exception("Token not found: '" + token + "'");
                }
            }
            Parser p = new Parser(tokens);
            p.parse();
//            p.printAST(p.parseV2());
        } catch (Exception e) {
            ErrorHandler.error(-1, -1, "Exception: " + e.getMessage());
        }


    }
}
