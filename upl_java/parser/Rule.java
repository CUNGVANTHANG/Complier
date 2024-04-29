package main.parser;

import java.util.List;

// Rule class to represent a grammar rule
class Rule {
    private String nonterminal;
    private List<String> expression;

    public Rule(String nonterminal, List<String> expression) {
        this.nonterminal = nonterminal;
        this.expression = expression;
    }

    public String getNonterminal() {
        return nonterminal;
    }

    public List<String> getExpression() {
        return expression;
    }
}
