package main.model;

import main.Token;

import java.util.List;

public class Parsec {
    private int n = 0;
    private int pos = 0;
    private String input = "";
    private List<Token> tokens;
    private boolean validity = true;

    // Default constructor
    public Parsec() {
    }

    // Constructor with input string
    public Parsec(String input) {
        this.input = input;
    }

    // Getters and setters (optional)
    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean isValidity() {
        return validity;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    // Additional methods for parsing logic (depending on your implementation)
    // ...
}
