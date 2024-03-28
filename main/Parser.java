package main;

import java.util.ArrayList;
import java.util.HashSet;

public class Parser {
    private ArrayList<Token> tokens;
    private HashSet<String> localMemory;
    private HashSet<String> globalMemory;
    private ArrayList<String> lines;
    private int index;

    public Parser(ArrayList<Token> tokens, Lexer lexer) {
        this.tokens = tokens;
        this.index = 0;
        localMemory = new HashSet<>();
        globalMemory = new HashSet<>();
        this.lines = lexer.getLines();
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        lexer.tokenize("C:\\\\Users\\\\PC\\\\Downloads\\\\Lexical_Analysis\\\\src\\\\main.upl");

        // Tạo một đối tượng Parser và truyền vào lexer
        Parser parser = new Parser(null, lexer);

        // In ra các dòng từ Lexer
        for (String line : parser.lines) {
            System.out.println(line);
        }
    }

    public void parse() {
        program();
    }

    public Boolean nextToken(String token) {
        if (index < tokens.size()) {
            return tokens.get(index).getType().equals(token);
        } else {
            return false;
        }
    }

    public void match(String token) {
        if (index < tokens.size() && !nextToken(token)) {
            System.out.println("Invalid");
        }

        index++;
    }

    public void program() {
        match("BEGIN");
        statementList();
        match("END");
    }

    public void statementList() {
        while (index < tokens.size() && !nextToken("END")) {
            statement();
        }
    }

    public void statement() {
        if (nextToken("INTEGER") || nextToken("BOOLEAN")) {
            declaration();
        } else if (nextToken("IDENTIFIER")) {
            assignment();
        } else if (nextToken("IF")) {
            conditionalStatement();
        } else if (nextToken("DO")) {
            loopStatement();
        } else if (nextToken("PRINT")) {
            printStatement();
        } else if (nextToken("UNKNOWN")) {
            System.out.println("Error: Unknown token found '" + tokens.get(index).getValue() + "'");
            index++;
        } else {
            index++;
        }
    }

    public void declaration() {
        Type();
        String identifier = tokens.get(index).getValue();
        if (localMemory.contains(identifier)) {
            System.out
                    .println("Error: Identifier '" + identifier + "' has already been declared in the current scope.");
        } else {
            localMemory.add(identifier);
            globalMemory.add(identifier);
        }
        match("IDENTIFIER");
        if (nextToken("ASSIGN")) {
            match("ASSIGN");
            expression();
        }
        match("SEMICOLON");
    }

    public void Type() {
        if (nextToken("INTEGER")) {
            match("INTEGER");
        } else if (nextToken("BOOLEAN")) {
            match("BOOLEAN");
        }
    }

    public void assignment() {
        match("IDENTIFIER");
        match("ASSIGN");
        expression();
        match("SEMICOLON");
    }

    public void conditionalStatement() {
        match("IF");
        expression();
        match("THEN");
        match("L_BRACKET");
        while (index < tokens.size() && !nextToken("R_BRACKET")) {
            statement();
        }
        match("R_BRACKET");
        if (nextToken("ELSE")) {
            match("ELSE");
            match("L_BRACKET");
            while (index < tokens.size() && !nextToken("R_BRACKET")) {
                statement();
            }
            match("R_BRACKET");
        }
    }

    public void loopStatement() {
        match("DO");
        match("L_BRACKET");
        while (index < tokens.size() && !nextToken("R_BRACKET")) {
            statement();
        }
        match("R_BRACKET");
        match("WHILE");
        expression();
        match("SEMICOLON");
    }

    public void printStatement() {
        match("PRINT");
        match("L_PARENTHESES");
        expression();
        match("R_PARENTHESES");
        match("SEMICOLON");
    }

    public void expression() {
        term();
        while (index < tokens.size() && nextToken("ADD") || nextToken("MULTIPLY")) {
            if (nextToken("ADD")) {
                match("ADD");
                term();
            } else if (nextToken("MULTIPLY")) {
                match("MULTIPLY");
                term();
            }

        }
        if (nextToken("GREATER")) {
            match("GREATER");
            term();
        } else if (nextToken("GREATER OR EQUAL")) {
            match("GREATER OR EQUAL");
            term();
        } else if (nextToken("EQUALS")) {
            match("EQUALS");
            term();
        }
    }

    public void term() {
        if (nextToken("IDENTIFIER")
                || nextToken("INTEGER CONSTANT")) {
            factor();
        } else if (nextToken("L_PARENTHESES")) {
            match("L_PARENTHESES");
            expression();
            match("R_PARENTHESES");
        }
    }

    public void factor() {
        if (nextToken("IDENTIFIER")) {
            if (!globalMemory.contains(tokens.get(index).getValue())) {
                System.out.println("Error: Identifier '" + tokens.get(index).getValue() + "' is not declared.");
            }
            match("IDENTIFIER");
        } else if (nextToken("INTEGER CONSTANT")) {
            match("INTEGER CONSTANT");
        }
    }
}