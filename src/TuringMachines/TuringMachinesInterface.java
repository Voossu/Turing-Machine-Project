package TuringMachines;

public interface TuringMachinesInterface {

    boolean run();
    void setDefaultCurrentRule();
    void setTapes(String[] tapes);
    String[] getTapes();
    String getTapes(int i);
    String[] getCurrentPosition();
    String nextRule();
    void addRule(String rule);
    void addAllRule(String[] rules);
    String getRules();
    void clearRules();
    String toString();

}

