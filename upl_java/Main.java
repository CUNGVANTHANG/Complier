package main;

import main.lexical.Lexical;
import main.parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static main.shared.ErrorHandler.error;

public class Main {
    private static final String defaultOutputLexFilePath = "upl_java/upl.lex";

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            try {
                File f = new File(args[0]);
                _lexicalAnalysis(f);

            } catch (FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            String defaultFilePath = "upl_java/main.upl";
            File f = new File(defaultFilePath);
            _lexicalAnalysis(f);
        }

        Parser.parse(defaultOutputLexFilePath);
    }


    private static void _lexicalAnalysis(File f) throws IOException {
        Scanner s = new Scanner(f);
        StringBuilder source = new StringBuilder(" ");
        while (s.hasNext()) {
            source.append(s.nextLine()).append("\n");
        }
        Lexical l = new Lexical(source.toString());
        l.printTokens(defaultOutputLexFilePath);
    }

}