package main.lexical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Lexical {
    private int line;
    private int pos;
    private int position;
    private char chr;
    private final String s;

    Map<String, TokenType> keywords = new HashMap<>();
    Map<String, TokenType> constant = new HashMap<>();

    static class Token {
        public TokenType tokentype;
        public String value;
        public int line;
        public int pos;

        Token(TokenType token, String value, int line, int pos) {
            this.tokentype = token;
            this.value = value;
            this.line = line;
            this.pos = pos;
        }

        @Override
        public String toString() {
            String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokentype);

            switch (this.tokentype) {
                case Integer, Boolean:
                    result += String.format("  %4s", value);
                    break;
                case Identifier:
                    result += String.format(" %s", value);
                    break;
            }
            return result;
        }
    }

    static enum TokenType {
        Keyword_Begin, Keyword_End,
        End_of_input, Semicolon, Identifier, Integer, Boolean,
        Op_greater, Op_greaterequal, Op_equal, Op_assign, Op_multiply, Op_add,
        Keyword_if, Keyword_then, Keyword_else, Keyword_while, Keyword_print,
        LeftParen, RightParen, LeftBrace, RightBrace, Unknow, Comma,
        TrueConstant, FalseContant
    }

    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

    Lexical(String source) {
        this.line = 1;
        this.pos = 0;
        this.position = 0;
        this.s = source;
        this.chr = this.s.charAt(0);
        this.keywords.put("if", TokenType.Keyword_if);
        this.keywords.put("else", TokenType.Keyword_else);
        this.keywords.put("print", TokenType.Keyword_print);
        this.keywords.put("while", TokenType.Keyword_while);
        this.keywords.put("then", TokenType.Keyword_then);
        this.keywords.put("begin", TokenType.Keyword_Begin);
        this.keywords.put("end", TokenType.Keyword_End);
        this.constant.put("true", TokenType.TrueConstant);
        this.constant.put("false", TokenType.FalseContant);
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

    Token char_lit(int line, int pos) {
        char c = getNextChar(); // skip opening quote
        int n = (int) c;
        if (c == '\'') {
            error(line, pos, "empty character constant");
        } else if (c == '\\') {
            c = getNextChar();
            if (c == 'n') {
                n = 10;
            } else {
                error(line, pos, String.format("unknown escape sequence \\%c", c));
            }
        }
        if (getNextChar() != '\'') {
            error(line, pos, "multi-character constant");
        }
        getNextChar();
        return new Token(TokenType.Integer, "" + n, line, pos);
    }

    Token commentDetector(int line, int pos) {
        if (getNextChar() != '*') {
            return new Token(TokenType.Unknow, "", line, pos);
        }
        getNextChar();
        while (true) {
            if (this.chr == '\u0000') {
                error(line, pos, "EOF in comment");
            } else if (this.chr == '*') {
                if (getNextChar() == '/') {
                    getNextChar();
                    return getToken();
                }
            } else {
                getNextChar();
            }
        }
    }

    Token identifier_or_integer(int line, int pos) {
        StringBuilder text = new StringBuilder();
        boolean has_number_in_middle = false;

        // Collect characters that are valid in identifiers
        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr) || this.chr == '_') {
            text.append(this.chr);
            getNextChar();
        }

        String content = text.toString();

        // Validate that an identifier starts with a letter
        if (content.isEmpty() || !Character.isAlphabetic(content.charAt(0))) {
            error(line, pos, String.format("Identifier must start with a letter, but got: %s", content));
        }

        // Check if there are numbers in the middle (not at the end)
        for (int i = 0; i < content.length() - 1; i++) {
            char c = content.charAt(i);
            if (Character.isDigit(content.charAt(i)) && Character.isAlphabetic(content.charAt(i + 1))) {
                has_number_in_middle = true;
                break;
            }
        }

        if (has_number_in_middle) {
            error(line, pos, String.format("Identifier has numbers in an invalid position: %s", content));
        }

        // If identifier contains numbers only at the end, treat it as a valid identifier
        if (content.chars().allMatch(Character::isDigit)) {
            return new Token(TokenType.Integer, content, line, pos);
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
            case '\'' -> char_lit(line, pos);
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
            case ',' -> {
                getNextChar();
                yield new Token(TokenType.Comma, "", line, pos);
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

    void printTokens() {
        Token t;
        while ((t = getToken()).tokentype != TokenType.End_of_input) {
            System.out.println(t);
        }
        System.out.println(t);
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length > 0) {
            try {
                File f = new File(args[0]);
                _analyzeFile(f);
            } catch (FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            String filePath = "upl_java/main.upl";
            File f = new File(filePath);
            _analyzeFile(f);
        }
    }

    private static void _analyzeFile(File f) throws FileNotFoundException {
        Scanner s = new Scanner(f);
        StringBuilder source = new StringBuilder(" ");
        while (s.hasNext()) {
            source.append(s.nextLine()).append("\n");
        }
        Lexical l = new Lexical(source.toString());
        l.printTokens();
    }
}
