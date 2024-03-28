package main;

import java.io.*;
import java.util.regex.*;
import java.util.*;

public class Lexer {
    private ArrayList<Token> tokensList = new ArrayList<>();
    private boolean isInMultiLineComment = false;

    public ArrayList<String> lines = new ArrayList<>();

    private static String[] TOKEN_REGEX = {
            // NOTE: Keyword higher priority
            "^begin$", "BEGIN",
            "^end$", "END",
            "^if$", "IF",
            "^then$", "THEN",
            "^else$", "ELSE",
            "^int$", "INTEGER",
            "^bool$", "BOOLEAN",
            "^do$", "DO",
            "^while$", "WHILE",
            "^true$|^false$", "BOOLEAN CONSTANT",
            "^[0]|[1-9][0-9]*$", "INTEGER CONSTANT",
            "^print\\b", "PRINT",

            // NOTE: Lower priority
            "^[a-zA-Z]+[0-9]*$", "IDENTIFIER",
            "^[=]$", "ASSIGN",
            // "^>$|^>=$|^==$", "ROP",
            "^>$", "GREATER",
            "^>=$", "GREATER OR EQUAL",
            "^==$", "EQUALS",
            "^[;]$", "SEMICOLON",
            "^[(]$", "L_PARENTHESES",
            "^[)]$", "R_PARENTHESES",
            "^[{]$", "L_BRACKET",
            "^[}]$", "R_BRACKET",
            "^\\+$", "ADD",
            "^\\*$", "MULTIPLY"
    };

    public String removeSingleLineComment(String line) {
        int index = line.indexOf("//");
        if (index != -1) {
            line = line.substring(0, index);
        }
        return line;
    }
    
    public String removeMultiLineComment(String line) {
        int startIndex = line.indexOf("/*");
        if (startIndex != -1) {
            int endIndex = line.indexOf("*/");
            if (endIndex != -1) {
                line = line.substring(0, startIndex) + line.substring(endIndex + 2);
            } else {
                line = line.substring(0, startIndex);
                isInMultiLineComment = true;
            }
        } else if (isInMultiLineComment) {
            int endIndex = line.indexOf("*/");
            if (endIndex != -1) {
                line = line.substring(endIndex + 2);
                isInMultiLineComment = false;
            } else {
                line = "";
            }
        }
        return line;
    }
    
    public void tokenize(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
                line= removeSingleLineComment(line);
                line = removeMultiLineComment(line);
                if (!line.trim().isEmpty()) {
                    String convertedLine = convertCode(line);
                    tokenizeLine(convertedLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tokenizeLine(String line) {
        Matcher matcher = Pattern.compile("\\S+").matcher(line.trim());
        while (matcher.find()) {
            String token = matcher.group();
            String tokenType = matched(token);
            tokensList.add(new Token(token, tokenType));
        }
    }

    public String matched(String input) {
        for (int i = 0; i < TOKEN_REGEX.length; i += 2) {
            String regex = TOKEN_REGEX[i];
            String tokenType = TOKEN_REGEX[i + 1];
            if (input.matches(regex)) {
                return tokenType;
            }
        }
        return "Unknown";
    }

    public String convertCode(String code) {
        Pattern pattern = Pattern.compile("(/\\*|\\*/|//|>=|<=|==|!=|[+\\-*/;()><{}=])");
        Matcher matcher = pattern.matcher(code);
        String spacedCode = matcher.replaceAll(" $1 ");
        return spacedCode;
    }

    public void printTokens() {
        for (Token token : tokensList) {
            System.out.println(token);
        }
    }

    public ArrayList<Token> getTokensList() {
        return tokensList;
    }

    public ArrayList<String> getLines() {
        return lines;
    }
}