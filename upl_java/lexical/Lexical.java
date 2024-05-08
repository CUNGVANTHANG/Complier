package main.lexical;

import main.models.Token;
import main.models.TokenType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static main.shared.ErrorHandler.error;

public class Lexical {
    private int line;
    private int pos;
    private int position;
    private char chr;
    private final String s;

    Map<String, TokenType> keywords = new HashMap<>();
    Map<String, TokenType> constant = new HashMap<>();


    public Lexical(String source) {
        this.line = 1;
        this.pos = 0;
        this.position = 0;
        this.s = source;
        this.chr = this.s.charAt(0);
        this.keywords.put("if", TokenType.Keyword_if);
        this.keywords.put("else", TokenType.Keyword_else);
        this.keywords.put("print", TokenType.Keyword_print);
        this.keywords.put("then", TokenType.Keyword_then);
        this.keywords.put("begin", TokenType.Keyword_Begin);
        this.keywords.put("end", TokenType.Keyword_End);
        this.keywords.put("while", TokenType.Keyword_while);

        this.keywords.put("bool", TokenType.Keyword_bool);
        this.keywords.put("int", TokenType.Keyword_int);
        this.keywords.put("do", TokenType.Keyword_do);
        this.constant.put("true", TokenType.TrueConstant);
        this.constant.put("false", TokenType.FalseConstant);
    }

    Token follow(TokenType ifyes, TokenType ifno, int line, int pos) {
        if (getNextChar() == '=') {
            getNextChar();
            return new Token(ifyes, "", line, pos);
        }
        if (ifno == TokenType.End_of_input) {
            error(line, pos, String.format("follow: unrecognized character: (%d) '%c'", (int) this.chr, this.chr));
        }
        return new Token(ifno, "", line, pos);
    }

    Token commentDetector(int line, int pos) {
        char firstChar = getNextChar();

        if (firstChar == '*') {
            // Xử lý comment nhiều dòng kiểu /* ... */
            getNextChar(); // Di chuyển qua '*'
            while (true) {
                if (this.chr == '\u0000') {
                    error(line, pos, "EOF trong comment nhiều dòng");
                    return null; // Nếu lỗi, có thể trả về null hoặc token đặc biệt
                } else if (this.chr == '*') {
                    if (getNextChar() == '/') {
                        getNextChar(); // Di chuyển qua '/'
                        return getToken();
                    }
                } else {
                    getNextChar();
                }
            }
        } else if (firstChar == '/') {
            // Xử lý comment một dòng kiểu //
            while (this.chr != '\n' && this.chr != '\u0000') {
                getNextChar(); // Bỏ qua cho đến khi gặp newline hoặc EOF
            }
            return getToken();
        } else {
            error(line, pos, String.format("Kí tự khong phù hợp: %s", this.chr));
            return null;
        }
    }

    Token identifier_or_integer(int line, int pos) {
        StringBuilder text = new StringBuilder();
        boolean has_number_in_middle = false;

        // Collect characters that are valid in identifiers
        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr) || this.chr == '-') {
            text.append(this.chr);
            getNextChar();
        }

        String content = text.toString();

        // If identifier contains numbers only at the end, treat it as a valid identifier
        if (content.matches("^-?\\d+$")) {
            return new Token(TokenType.Integer, content, line, pos);
        }

        // Validate that an identifier starts with a letter
        if (content.isEmpty() || !Character.isAlphabetic(content.charAt(0))) {
            error(line, pos, String.format("Identifier must start with a letter, but got: %s", content));
        }

        // Check if there are numbers in the middle (not at the end)
        for (int i = 0; i < content.length() - 1; i++) {
            if (Character.isDigit(content.charAt(i)) && Character.isAlphabetic(content.charAt(i + 1))) {
                has_number_in_middle = true;
                break;
            }
        }

        if (has_number_in_middle) {
            error(line, pos, String.format("Identifier has numbers in an invalid position: %s", content));
        }


        if (this.constant.containsKey(content)) {
            return new Token(this.constant.get(content), "", line, pos);
        }

        if (this.keywords.containsKey(content)) {
            return new Token(this.keywords.get(content), "", line, pos);
        }

        return new Token(TokenType.Identifier, content, line, pos);
    }


    Token getToken() {
        int line, pos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line = this.line;
        pos = this.pos;

        return switch (this.chr) {
            case '\u0000' -> new Token(TokenType.End_of_input, "", this.line, this.pos);
            case '/' -> commentDetector(line, pos);
            case '>' -> follow(TokenType.Op_greaterequal, TokenType.Op_greater, line, pos);
            case '=' -> follow(TokenType.Op_equal, TokenType.Op_assign, line, pos);
            case '{' -> {
                getNextChar();
                yield new Token(TokenType.LeftBrace, "", line, pos);
            }
            case '}' -> {
                getNextChar();
                yield new Token(TokenType.RightBrace, "", line, pos);
            }
            case '(' -> {
                getNextChar();
                yield new Token(TokenType.LeftParen, "", line, pos);
            }
            case ')' -> {
                getNextChar();
                yield new Token(TokenType.RightParen, "", line, pos);
            }
            case '+' -> {
                getNextChar();
                yield new Token(TokenType.Op_add, "", line, pos);
            }
            case '*' -> {
                getNextChar();
                yield new Token(TokenType.Op_multiply, "", line, pos);
            }
            case ';' -> {
                getNextChar();
                yield new Token(TokenType.Semicolon, "", line, pos);
            }

            default -> identifier_or_integer(line, pos);
        };
    }

    char getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.s.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = this.s.charAt(this.position);
        if (this.chr == '\n') {
            this.line++;
            this.pos = 0;
        }
        return this.chr;
    }

    public void printTokens(String outputLexFilePath) throws IOException {

        Token token;

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputLexFilePath))) {
            while ((token = getToken()).tokentype != TokenType.End_of_input) {
//                System.out.println(token);
                /// in ra file
                writer.println(token);  // Ghi
            }
//            System.out.println(token);
            writer.println(token);
        }
    }
}
