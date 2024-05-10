package parser;

import java.util.*;

public class LL1Parser {

    private static List<String> rules = new ArrayList<>();
    private static Set<String> termUserdef = new LinkedHashSet<>();
    private static Map<String, List<List<String>>> diction = new LinkedHashMap<>();
    private static final Map<String, Set<String>> firsts = new LinkedHashMap<>();
    private static final Map<String, Set<String>> follows = new LinkedHashMap<>();
    private static String startSymbol;

    public static void removeLeftRecursion(Map<String, List<List<String>>> rulesDict) {
        Map<String, List<List<String>>> store = new LinkedHashMap<>();

        for (String lhs : rulesDict.keySet()) {
            List<List<String>> alphaRules = new ArrayList<>();
            List<List<String>> betaRules = new ArrayList<>();

            List<List<String>> allrhs = rulesDict.get(lhs);

            for (List<String> subrhs : allrhs) {
                if (subrhs.get(0).equals(lhs)) {
                    alphaRules.add(subrhs.subList(1, subrhs.size()));
                } else {
                    betaRules.add(subrhs);
                }
            }

            if (!alphaRules.isEmpty()) {
                String lhs_ = lhs + "'";
                while (rulesDict.containsKey(lhs_) || store.containsKey(lhs_)) {
                    lhs_ += "'";
                }

                _processRule(betaRules, lhs_);
                rulesDict.put(lhs, betaRules);

                _processRule(alphaRules, lhs_);
                alphaRules.add(Collections.singletonList("#"));
                store.put(lhs_, alphaRules);
            }
        }

        for (String newLHS : store.keySet()) {
            rulesDict.put(newLHS, store.get(newLHS));
        }

    }

    private static void _processRule(List<List<String>> alphaRules, String lhs_) {
        for (int a = 0; a < alphaRules.size(); a++) {
            List<String> alphaRule = alphaRules.get(a);

            // Tạo một danh sách mới và sao chép các phần tử từ alphaRule
            List<String> newList = new ArrayList<>(alphaRule);

            newList.add(lhs_);

            alphaRules.set(a, newList);
        }
    }

    public static Map<String, List<List<String>>> leftFactoring(Map<String, List<List<String>>> rulesDict) {
        Map<String, List<List<String>>> newDict = new LinkedHashMap<>();

        for (String lhs : rulesDict.keySet()) {
            List<List<String>> allrhs = rulesDict.get(lhs);
            Map<String, List<List<String>>> temp = new LinkedHashMap<>();

            for (List<String> subrhs : allrhs) {
                String key = subrhs.get(0);
                temp.computeIfAbsent(key, k -> new ArrayList<>()).add(subrhs);
            }

            List<List<String>> newRule = new ArrayList<>();
            Map<String, List<List<String>>> tempoDict = new LinkedHashMap<>();

            for (String termKey : temp.keySet()) {
                List<List<String>> allStartingWithTermKey = temp.get(termKey);
                if (allStartingWithTermKey.size() > 1) {
                    String lhs_ = lhs + "'";
                    while (rulesDict.containsKey(lhs_) || tempoDict.containsKey(lhs_)) {
                        lhs_ += "'";
                    }

                    newRule.add(Arrays.asList(termKey, lhs_));

                    List<List<String>> exRules = new ArrayList<>();
                    for (List<String> g : allStartingWithTermKey) {
                        exRules.add(g.subList(1, g.size()));
                    }
                    tempoDict.put(lhs_, exRules);
                } else {
                    newRule.add(allStartingWithTermKey.get(0));
                }
            }

            newDict.put(lhs, newRule);
            newDict.putAll(tempoDict);
        }

        return newDict;
    }

    // First Function
    public static List<String> first(List<String> rule) {
        if (rule == null || rule.isEmpty()) {
            return Collections.emptyList();
        }

        String firstTerm = rule.get(0);
        if (termUserdef.contains(firstTerm)) {
            return Collections.singletonList(firstTerm);
        } else if (firstTerm.equals("#")) {
            return Collections.singletonList("#");
        }

        if (diction.containsKey(firstTerm)) {
            List<String> fres = new ArrayList<>();
            for (List<String> rhs : diction.get(firstTerm)) {
                List<String> indivRes = first(rhs);
                fres.addAll(indivRes);

                if (fres.contains("#")) {
                    fres.remove("#");
                    if (rule.size() > 1) {
                        List<String> remainingFirst = first(rule.subList(1, rule.size()));
                        fres.addAll(remainingFirst);
                    }
                    fres.add("#");
                }
                return fres;
            }
        }

        return Collections.emptyList();
    }

    // Follow Function
    public static List<String> follow(String nt) {
        Set<String> solset = new LinkedHashSet<>();
        if (nt.equals(startSymbol)) {
            solset.add("$");
        }

        for (String curNT : diction.keySet()) {
            List<List<String>> rhs = diction.get(curNT);
            for (List<String> subrule : rhs) {
                int index = subrule.indexOf(nt);
                if (index >= 0) {
                    List<String> rhsAfterNT = subrule.subList(index + 1, subrule.size());
                    List<String> firstRes = first(rhsAfterNT);

                    if (firstRes.contains("#")) {
                        firstRes.remove("#");
                        List<String> followRes = follow(curNT);
                        firstRes.addAll(followRes);
                    }

                    solset.addAll(firstRes);
                }
            }
        }

        return new ArrayList<>(solset);
    }

    // Compute All Firsts
    public static void computeAllFirsts() {
        for (String rule : rules) {
            String[] k = rule.split("->");
            // remove un-necessary spaces
            k[0] = k[0].trim();
            k[1] = k[1].trim();

            String rhs = k[1];
            String[] multirhs = rhs.split("\\|");
            List<List<String>> rhsList = new ArrayList<>();

            // remove un-necessary spaces and convert to list
            for (String item : multirhs) {
                List<String> subList = Arrays.asList(item.trim().split("\\s+"));
                rhsList.add(subList);
            }
            diction.put(k[0], rhsList);
        }

        System.out.println("\nRules:");

        for (Map.Entry<String, List<List<String>>> entry : diction.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("\nAfter elimination of left recursion:");
        removeLeftRecursion(diction);

        for (Map.Entry<String, List<List<String>>> entry : diction.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("\nAfter left factoring:");
        diction = leftFactoring(diction);
        for (Map.Entry<String, List<List<String>>> entry : diction.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        // Calculate first for each rule
        for (Map.Entry<String, List<List<String>>> entry : diction.entrySet()) {


            Set<String> firstSet = new LinkedHashSet<>();


            for (List<String> sub : entry.getValue()) {


                List<String> results = first(sub);


                Set<String> res = new LinkedHashSet<>(results);


                firstSet.addAll(res);
            }
            firsts.put(entry.getKey(), firstSet);
        }

        System.out.println("\nCalculated firsts:");
        for (Map.Entry<String, Set<String>> entry : firsts.entrySet()) {
            System.out.println("first(" + entry.getKey() + ") => " + entry.getValue());
        }
    }

    // Compute All Follows
    public static void computeAllFollows() {
        for (String nt : diction.keySet()) {
            List<String> res = follow(nt);
            Set<String> solset = new LinkedHashSet<>(res);
            follows.put(nt, solset);
        }

        System.out.println("\nCalculated Follows:");
        for (Map.Entry<String, Set<String>> entry : follows.entrySet()) {
            System.out.println("Follow(" + entry.getKey() + ") => " + entry.getValue());
        }
    }

    // Create Parse Table
    public static ParseTable createParseTable() {
        System.out.println("\nFirsts and Follow Result table\n");

        // find space size
        int mx_len_first = 0;
        int mx_len_fol = 0;
        for (String u : diction.keySet()) {
            int k1 = String.valueOf(firsts.get(u)).length();
            int k2 = String.valueOf(follows.get(u)).length();
            mx_len_first = Math.max(mx_len_first, k1);
            mx_len_fol = Math.max(mx_len_fol, k2);
        }

        System.out.printf("%-10s %-15s %-15s%n", "Non-T", "FIRST", "FOLLOW");
        for (String u : diction.keySet()) {
            System.out.printf("%-10s %-15s %-15s%n",
                    u, firsts.get(u), follows.get(u));
        }

        // create matrix of row(NT) x [col(T) + 1($)]
        // create list of non-terminals
        List<String> ntlist = new ArrayList<>(diction.keySet());
        List<String> terminals = new ArrayList<>(termUserdef);
        terminals.add("$");

        // create the initial empty state of matrix
        List<List<String>> mat = new ArrayList<>();
        for (String x : diction.keySet()) {
            List<String> row = new ArrayList<>();
            for (String y : terminals) {
                row.add("");
            }
            mat.add(row);
        }

        // Classifying grammar as LL(1) or not LL(1)
        boolean grammar_is_LL = true;

        // rules implementation
        for (String lhs : diction.keySet()) {
            List<List<String>> rhs = diction.get(lhs);
            for (List<String> y : rhs) {
                List<String> res = first(y);
                // epsilon is present,
                // - take union with follow
                if (res.contains("#")) {
                    if (res.size() == 1) {
                        Set<String> fol_op = follows.get(lhs);

                        res = new ArrayList<>(fol_op);
                    } else {
                        res.remove("#");
                        res.addAll(follows.get(lhs));
                    }
                }
                // add rules to table
                List<String> ttemp = new ArrayList<>();
                if (res.size() == 1) {
                    ttemp.add(res.get(0));
                    res = new ArrayList<>(ttemp);
                }
                for (String c : res) {
                    int xnt = ntlist.indexOf(lhs);
                    int yt = terminals.indexOf(c);
                    if (mat.get(xnt).get(yt).isEmpty()) {
                        mat.get(xnt).set(yt, lhs + " -> " + y);
                    } else {
                        // if rule already present
                        if (!mat.get(xnt).get(yt).contains(lhs + " -> " + y)) {
                            grammar_is_LL = false;
                            mat.get(xnt).set(yt, mat.get(xnt).get(yt) + ", " + lhs + " -> " + y);
                        }
                    }
                }
            }
        }

        // final state of parse table
        System.out.println("\nGenerated parsing table:\n");
        System.out.printf("%-20s".repeat(terminals.size()), terminals.toArray());
        System.out.println();
        for (int i = 0; i < ntlist.size(); i++) {
            System.out.printf("%-20s".repeat(mat.get(i).size()), mat.get(i).toArray());
            System.out.println();
        }

        // Create the ParseTable object
        String[][] parseTableArray = new String[ntlist.size()][terminals.size()];
        for (int i = 0; i < ntlist.size(); i++) {
            for (int j = 0; j < terminals.size(); j++) {
                parseTableArray[i][j] = mat.get(i).get(j);
            }
        }

        return new ParseTable(parseTableArray, grammar_is_LL, terminals);
    }

    // Validate String Using Stack Buffer
    public static String validateStringUsingStackBuffer(ParseTable parseTable, String inputString) {
        if (!parseTable.isGrammarLL1()) {
            return "Grammar is not LL(1)";
        }

        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push(startSymbol);

        List<String> buffer = Arrays.asList(inputString.split(" "));
        Collections.reverse(buffer);
        buffer.add(0, "$");

        System.out.format("%20s %20s %20s\n", "Buffer", "Stack", "Action");

        while (true) {
            if (stack.peek().equals("$") && buffer.get(0).equals("$")) {
                System.out.format("%20s %20s %20s\n", String.join(" ", buffer), String.join(" ", stack), "Valid");
                return "Valid String!";
            }

            String stackTop = stack.peek();
            String bufferTop = buffer.get(0);

            if (!termUserdef.contains(stackTop)) {
                int rowIndex = new ArrayList<>(diction.keySet()).indexOf(stackTop);
                int colIndex = parseTable.getTerminalList().indexOf(bufferTop);
                String tableEntry = parseTable.getParseTable()[rowIndex][colIndex];

                if (tableEntry.isEmpty()) {
                    return "Invalid String! No rule at " +
                            "Table[" + stackTop + "][" + bufferTop + "].";
                }

                String action = "T[" + stackTop + "][" + bufferTop + "] = " + tableEntry;
                System.out.format("%20s %20s %20s\n", String.join(" ", buffer), String.join(" ", stack), action);

                stack.pop();
                List<String> production = Arrays.asList(tableEntry.split("->")[1].split(" "));
                Collections.reverse(production);
                stack.addAll(production);

            } else {
                if (stackTop.equals(bufferTop)) {
                    System.out.format("%20s %20s %20s\n", String.join(" ", buffer), String.join(" ", stack), "Matched:" + stackTop);
                    buffer.remove(0);
                    stack.pop();
                } else {
                    return "Invalid String! Unmatched terminal symbols.";
                }
            }
        }
    }

    public static class ParseTable {
        private final String[][] parseTable;
        private final boolean grammarIsLL1;
        private final List<String> terminalList;

        public ParseTable(String[][] parseTable, boolean grammarIsLL1, List<String> terminalList) {
            this.parseTable = parseTable;
            this.grammarIsLL1 = grammarIsLL1;
            this.terminalList = terminalList;
        }

        public String[][] getParseTable() {
            return parseTable;
        }

        public boolean isGrammarLL1() {
            return grammarIsLL1;
        }

        public List<String> getTerminalList() {
            return terminalList;
        }
    }


    public static void main(String[] args) {
        // # đại diện cho epsilon
        rules = Arrays.asList(
                "Program -> begin StatementList end",
                "StatementList -> Statement ; StatementList | #",
                "Statement -> Declaration | Assignment | ConditionalStatement | LoopStatement | PrintStatement",
                "Declaration -> Type Identifier | Type Identifier = Expression",
                "Assignment -> Identifier '=' Expression",
                "Expression -> Term | Expression '+' Term | Expression ROP Term",
                "Term -> Factor | Term '*' Factor",
                "Factor -> Identifier | Number | ( Expression )",
                "ConditionalStatement -> if Expression then { Statement } | if Expression then { Statement } else { Statement }",
                "LoopStatement -> do { Statement } while Expression",
                "PrintStatement -> print ( Expression )",
                "Type -> int | bool"
        );


        termUserdef = new LinkedHashSet<>(
                Arrays.asList("begin", ";", "end", "=", "+", "*", "if", "then", "{", "}",
                        "int", "bool", "else", "do", "while", "print", "(", ")", "Identifier", "Number", "ROP"
                )
        );

        String sampleInputString = "a r k O";

        computeAllFirsts();

        // Lấy danh sách các khóa từ Map
        List<String> keys = new ArrayList<>(diction.keySet());

        // Lấy khóa đầu tiên
        startSymbol = keys.get(0);

        computeAllFollows();

//        final ParseTable parseTable = createParseTable();

//        String validity = validateStringUsingStackBuffer(
//                parseTable,
//                sampleInputString
//        );

//        System.out.println(validity);
    }

}


