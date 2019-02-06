package TuringMachines;

import java.util.stream.IntStream;

public class TuringMachinesRule {

    private String ruleName, nextRuleName;
    private char[] initialValues, finalValues, moveCarriage;

    // lexical parser
    public TuringMachinesRule(int amountTape, String alphabet, String inputRule) {
        if(!isRule(amountTape, alphabet, inputRule)) {
            throw new IllegalArgumentException("Incorrect input rule!");
        }
        String[] rule = inputRule.split(" (->)? ?");
        ruleName = rule[0];
        nextRuleName = rule[amountTape + 1];
        initialValues = new char[amountTape];
        finalValues = new char[amountTape];
        moveCarriage = new char[amountTape];
        IntStream.range(0, amountTape).forEach(i -> {
            initialValues[i] = rule[i + 1].charAt(0);
            finalValues[i] = rule[i + amountTape + 2].charAt(0);
            moveCarriage[i] = rule[i + 2 * amountTape + 2].charAt(0);
        });
    }

    public boolean matchRuleNameAndInitialValues(String ruleName, char[] initialValues) {
        if (!this.ruleName.equals(ruleName) || this.initialValues.length != initialValues.length) return false;
        for (int i = 0; i < initialValues.length; i++) {
            if (this.initialValues[i] != initialValues[i]) return false;
        }
        return true;
    }

    // lexical analysis and parser
    public static boolean isRule(int amountTape, String alphabet, String inputRule) {
        // parse
        String[] rule = inputRule.split(" (->)? ?");
        // check
        if (rule.length != 2 + 3 * amountTape) return false;

        if (rule[0].charAt(0) != 'q' || rule[amountTape + 1].charAt(0) != 'q') return false;
        return IntStream.range(0, amountTape).noneMatch(i -> !rule[i + 1].matches(String.format("[%sλ]", alphabet))
                || !rule[i + amountTape + 2].matches(String.format("[%sλ]", alphabet))
                || !rule[i + 2 * amountTape + 2].matches("[LNR]"));
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getNextRuleName() {
        return nextRuleName;
    }

    public char[] getInitialValues() {
        return initialValues;
    }

    public char[] getFinalValues() {
        return finalValues;
    }

    public char[] getMoveCarriage() {
        return moveCarriage;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder(ruleName);
        for (char i : initialValues) temp.append(" ").append(i);
        temp.append(" ->");
        for (char i : finalValues) temp.append(" ").append(i);
        for (char i : moveCarriage) temp.append(" ").append(i);
        return temp.toString();
    }

}
