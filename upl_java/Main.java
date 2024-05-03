package main;

import main.lexical.Lexer;

public class Main {
    public static void main(String[] args) {
        // File Path
        String filePath = "upl_java/main.upl";

        Lexer lexer = new Lexer();
        lexer.tokenize(filePath);
        lexer.printTokens();


//        Parser parser = new Parser(lexer.getTokensList());
//        parser.parse();
    }

}