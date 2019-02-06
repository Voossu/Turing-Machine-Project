package TuringMachines;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TuringMachineTape {

    private String tape;
    private int currentPosition;

    public TuringMachineTape(String alphabet, String tape) {
        if (!tape.matches(String.format("[%s]*", alphabet))) throw new IllegalArgumentException("Incorrect input tape!");
        this.tape = tape;
        currentPosition = 0;
    }

    public char read() {
        if (currentPosition == -1 || currentPosition == tape.length()) return 'λ';
        return tape.charAt(currentPosition);
    }

    public void rewrite(char newSymbol) {
        if (newSymbol == 'λ' && (tape.length() == 0 || currentPosition == -1 || currentPosition == tape.length())) return;
        if (tape.length() == 0) tape = Character.toString(newSymbol);
        if (currentPosition == -1) { tape = newSymbol + tape; currentPosition = 0; return; }
        if (currentPosition == 0) { tape = newSymbol + tape.substring(1); return; }
        if (currentPosition == tape.length() - 1) { tape = tape.substring(0, tape.length() - 1) + newSymbol; return; }
        if (currentPosition == tape.length()) { tape = tape + newSymbol; return; }
        tape = tape.substring(0, currentPosition) + newSymbol + tape.substring(currentPosition + 1);
    }

    public int getCurrentPosition() {
        return currentPosition + 1;
    }

    public String getCurrent() {
        String temp = IntStream.range(0, currentPosition + 1).mapToObj(i -> " ").collect(Collectors.joining());
        return temp + '^';
    }

    public void move(char direction) {
        switch (direction) {
            case 'L':
                if (currentPosition == -1) tape = 'λ' + tape;
                else currentPosition--;
                break;
            case 'R':
                if (currentPosition == tape.length()) tape = tape + 'λ';
                currentPosition++;
                break;
        }
        if(tape.length() > 1 && currentPosition <= tape.length() - 1 && tape.charAt(tape.length() - 1) == 'λ')
            tape = tape.substring(0, tape.length() - 1);
        if(tape.length() > 1 && currentPosition >= 0 && tape.charAt(0) == 'λ') {
            tape = tape.substring(1);
            currentPosition--;
        }
    }

    @Override
    public String toString() {
        return 'λ' + ( (tape.length() == 0) ? "λ" : tape ) + 'λ';
    }
}
