package main.parser;

import java.util.*;


public class FirstAndFollow {
    private final Grammar grammar;
    private final Map<String, Set<String>> first;
    private final Map<String, Set<String>> follow;
    private final Set<String> epsilon;

    public FirstAndFollow(Grammar grammar) {
        this.grammar = grammar;
        this.first = new HashMap<>();
        this.follow = new HashMap<>();
        this.epsilon = new HashSet<>();
    }

    public void calculateFirstAndFollow() {
        for (String nonterminal : grammar.getNonTerminals()) {
            first.put(nonterminal, new HashSet<>());
            follow.put(nonterminal, new HashSet<>());
        }

        for (String nonterminal : grammar.getNonTerminals()) {
            for (String[] rule : grammar.getRules(nonterminal)) {
                calculateFirst(rule);
            }
        }

        while (true) {
            boolean updated = false;

            for (String nonterminal : grammar.getNonTerminals()) {
                for (String[] rule : grammar.getRules(nonterminal)) {
                    updated |= calculateFollow(rule);
                }
            }

            if (!updated) {
                return;
            }
        }
    }

    private void calculateFirst(String[] expression) {
        String nonterminal = expression[0];
        Set<String> currentFirst = first.get(nonterminal);

        for (int i = 1; i < expression.length; i++) {
            String symbol = expression[i];

            if (grammar.isNonTerminal(symbol)) {
                currentFirst.addAll(first.get(symbol));
                if (!epsilon.contains(symbol)) {
                    break;
                }
            } else {
                currentFirst.add(symbol);
                break;
            }
        }
    }

    private boolean calculateFollow(String[] expression) {
        boolean updated = false;

        for (int i = 1; i < expression.length; i++) {
            String symbol = expression[i];

            if (grammar.isNonTerminal(symbol)) {
                Set<String> aux = follow.get(symbol);
                updated |= follow.get(expression[0]).addAll(aux);
                if (epsilon.contains(symbol)) {
                    aux.addAll(first.get(expression[i + 1]));
                } else {
                    aux = new HashSet<>(first.get(expression[i + 1]));
                }
            }
        }

        return updated;
    }

    public Map<String, Set<String>> getFirst() {
        return first;
    }

    public Map<String, Set<String>> getFollow() {
        return follow;
    }

    public Set<String> getEpsilon() {
        return epsilon;
    }
}
