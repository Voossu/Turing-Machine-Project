package TuringMachines;

import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultiTapesTuringMachine implements TuringMachinesInterface {

    private int amountTape;
    private String alphabet;
    private String currentRule;
    private TuringMachineTape[] tapes;
    private Vector<TuringMachinesRule> rules;

    public MultiTapesTuringMachine(int amountTape, String alphabet) {
        this.amountTape = amountTape;
        this.alphabet = alphabet;
        this.currentRule = "q1";
        this.rules = new Vector<>();
    }


    public MultiTapesTuringMachine(int amountTape, String alphabet, String[] tapes, String[] rules) {
        this.amountTape = amountTape;
        this.alphabet = alphabet;
        this.currentRule = "q1";
        this.setTapes(tapes);
        this.rules = new Vector<>();
        this.addAllRule(rules);
    }

    @Override
    public boolean run() {
        if (currentRule.equals("qz")) return false;
        int currentRulePositionInRules = -1;
        char[] initialValues = new char[amountTape];
        for (int i = 0; i < amountTape; i++) {
            initialValues[i] = tapes[i].read();
        }
        for (int i = 0; i < this.rules.size(); i++) {
            if (this.rules.elementAt(i).matchRuleNameAndInitialValues(currentRule, initialValues)){
                currentRulePositionInRules = i;
                break;
            }
        }
        if (currentRulePositionInRules == -1) throw new UnsupportedOperationException("Typically not found!");
        char[] finalValues = rules.elementAt(currentRulePositionInRules).getFinalValues();
        char[] moveCarriage = rules.elementAt(currentRulePositionInRules).getMoveCarriage();
        for (int i = 0; i < amountTape; i++) {
            tapes[i].rewrite(finalValues[i]);
            tapes[i].move(moveCarriage[i]);
        }
        currentRule = rules.elementAt(currentRulePositionInRules).getNextRuleName();
        return true;
    }

    @Override
    public void setDefaultCurrentRule() {
        this.currentRule = "q1";
    }

    @Override
    public void setTapes(String[] tapes) {
        this.tapes = new TuringMachineTape[amountTape];
        for (int i = 0; i < tapes.length; i++) {
            this.tapes[i] = new TuringMachineTape(alphabet, tapes[i]);
        }
        for (int i = tapes.length; i < amountTape; i++) {
            this.tapes[i] = new TuringMachineTape(alphabet, "");
        }
    }

    @Override
    public String[] getTapes() {
        String[] temp = new String[tapes.length];
        for (int i = 0; i < tapes.length; i++) {
            temp[i] = tapes[i].toString();
        }
        return temp;
    }

    @Override
    public String getTapes(int i) {
        return tapes[i].toString() + '\n' + tapes[i].getCurrent() + '\n';
    }

    @Override
    public String[] getCurrentPosition() {
        String[] temp = new String[tapes.length];
        for (int i = 0; i < tapes.length; i++) {
            temp[i] = tapes[i].getCurrent();
        }
        return temp;
    }

    @Override
    public String nextRule() {
        return currentRule;
    }

    @Override
    public void addRule(String rule) {
        TuringMachinesRule temp = new TuringMachinesRule(amountTape, alphabet, rule);
        for (int i = 0; i < this.rules.size(); i++) {
            if (this.rules.elementAt(i).matchRuleNameAndInitialValues(temp.getRuleName(), temp.getInitialValues()))
                throw new IllegalArgumentException("Usually repeated. There was uncertainty.");
        }
        rules.add(temp);
    }

    @Override
    public void addAllRule(String[] rules) {
        for(String i : rules) addRule(i);
    }

    @Override
    public String getRules() {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < rules.size(); i++) {
            temp.append(rules.elementAt(i)).append("\n");
        }
        return temp.toString();
    }

    @Override
    public void clearRules() {
        rules.clear();
    }

    @Override
    public String toString() {
        String temp = IntStream.range(0, amountTape).mapToObj(i -> tapes[i].toString() + '\n' + tapes[i].getCurrent() + '\n').collect(Collectors.joining());
        return temp + "\n\n" + "Rule : " + currentRule + '\n';
    }
}
