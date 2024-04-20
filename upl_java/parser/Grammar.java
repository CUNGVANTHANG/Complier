package main.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grammar {
    private final List<String[]> rules;

    public Grammar(String... rules) {
        this.rules = new ArrayList<>();
        for (String rule : rules) {
            this.rules.add(parse(rule));
        }
    }

    private String[] parse(String rule) {
        return rule.replaceAll(" ", "").split("::=");
    }

    public Iterable<String[]> getRules(String nonterminal) {
        List<String[]> result = new ArrayList<>();
        for (String[] rule : rules) {
            if (rule[0].equals(nonterminal)) {
                result.add(rule);
            }
        }
        return result;
    }

    public boolean isNonTerminal(String symbol) {
        return symbol.matches("[A-Z]");
    }

    public Set<String> getNonTerminals() {
        Set<String> nonTerminals = new HashSet<>();
        for (String[] rule : rules) {
            nonTerminals.add(rule[0]);
        }
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        Set<String> terminals = new HashSet<>();
        for (String[] rule : rules) {
            for (int i = 1; i < rule.length; i++) {
                String symbol = rule[i];
                if (!isNonTerminal(symbol)) {
                    terminals.add(symbol);
                }
            }
        }
        return terminals;
    }
}