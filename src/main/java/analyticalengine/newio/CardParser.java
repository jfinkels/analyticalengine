package analyticalengine.newio;

import analyticalengine.components.cards.Card;
import analyticalengine.components.cards.CardType;

public class CardParser {
    public static Card toCard(String cardString) throws UnknownCard {
        char firstChar = cardString.charAt(0);
        String rest = cardString.substring(1).trim();

        switch (firstChar) {
        case '+':
            return new Card(CardType.ADD);
        case '*':
            return new Card(CardType.MULTIPLY);
        case '/':
            return new Card(CardType.DIVIDE);
        case '-':
            return new Card(CardType.SUBTRACT);
        case 'N':
            String[] args = rest.split("\\s");
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].trim();
            }
            return new Card(CardType.NUMBER, args);
        case 'L':
            if (rest.endsWith("'")) {
                return new Card(CardType.LOADPRIME,
                        new String[] { rest.substring(0, rest.length() - 1) });
            }
            return new Card(CardType.LOAD, new String[] { rest });
        case 'Z':
            if (rest.endsWith("'")) {
                return new Card(CardType.ZLOADPRIME,
                        new String[] { rest.substring(0, rest.length() - 1) });
            }
            return new Card(CardType.ZLOAD, new String[] { rest });
        case 'S':
            if (rest.endsWith("'")) {
                return new Card(CardType.STOREPRIME,
                        new String[] { rest.substring(0, rest.length() - 1) });
            }
            return new Card(CardType.STORE, new String[] { rest });
        case '<':
            if (!rest.isEmpty()) {
                return new Card(CardType.LSHIFTN, new String[] { rest });
            }
            return new Card(CardType.LSHIFT);
        case '>':
            if (!rest.isEmpty()) {
                return new Card(CardType.RSHIFTN, new String[] { rest });
            }
            return new Card(CardType.RSHIFT);
        case 'C':
            if (rest.charAt(0) == 'F') {
                if (rest.charAt(1) == '?') {
                    return new Card(CardType.CFORWARD,
                            new String[] { rest.substring(2) });
                } else if (rest.charAt(1) == '+') {
                    return new Card(CardType.FORWARD,
                            new String[] { rest.substring(2) });
                } else {
                    throw new UnknownCard(
                            "Forward card must be either CF+ or CF?, got: "
                                    + cardString);
                }
            } else if (rest.charAt(0) == 'B') {
                if (rest.charAt(1) == '?') {
                    return new Card(CardType.CBACKWARD,
                            new String[] { rest.substring(2) });
                } else if (rest.charAt(1) == '+') {
                    return new Card(CardType.BACKWARD,
                            new String[] { rest.substring(2) });
                } else {
                    throw new UnknownCard(
                            "Backward card must be either CB+ or CB?, got: "
                                    + cardString);
                }
            } else {
                throw new UnknownCard(
                        "Combinatorial card must be either CF or CB, got: "
                                + cardString);
            }
        case 'B':
            return new Card(CardType.BELL);
        case 'P':
            return new Card(CardType.PRINT);
        case 'H':
            return new Card(CardType.HALT);
        case 'D':
            if (rest.charAt(0) == '+') {
                return new Card(CardType.DRAW);
            } else if (rest.charAt(0) == '-') {
                return new Card(CardType.MOVE);
            } else if (rest.charAt(0) == 'X') {
                return new Card(CardType.SETX);
            } else if (rest.charAt(0) == 'Y') {
                return new Card(CardType.SETY);
            } else {
                throw new UnknownCard(
                        "Draw card must be one of DX, DY, D+, or D-, got: "
                                + cardString);
            }
        case 'A':
            if (rest.startsWith("include cards")) {
                return new Card(CardType.INCLUDE, new String[] {rest.substring(14)});   
            } else if (rest.startsWith("include from library cards for")) {
                return new Card(CardType.INCLUDELIB, new String[] {rest.substring(31)});   
            } else if (rest.startsWith("set decimal places to")) {
                return new Card(CardType.DECIMALEXPAND, new String[] {rest.substring(22)});   
            } else if (rest.startsWith("write numbers as")) {
                return new Card(CardType.WRITEPICTURE, new String[] {rest.substring(17)});   
            } else if (rest.startsWith("write numbers with decimal point")) {
                return new Card(CardType.WRITEDECIMAL);
            } else if (rest.startsWith("write in rows")) {
                return new Card(CardType.WRITEROWS);
            } else if (rest.startsWith("write in columns")) {
                return new Card(CardType.WRITECOLUMNS);
            } else if (rest.startsWith("write new line")) {
                return new Card(CardType.NEWLINE);
            } else if (rest.startsWith("write annotation")) {
                return new Card(CardType.ANNOTATE, new String[] {rest.substring(17)});
            }
        case '(':
            if (!rest.isEmpty() && rest.charAt(0) == '?') {
                return new Card(CardType.CBACKSTART);
            }
            return new Card(CardType.BACKSTART);
        case ')':
            return new Card(CardType.BACKEND);
        case '{':
            if (!rest.isEmpty() && rest.charAt(0) == '?') {
                return new Card(CardType.CFORWARDSTART);
            }
            return new Card(CardType.FORWARDSTART);
        case '}':
            if (!rest.isEmpty() && rest.charAt(0) == '{') {
                return new Card(CardType.ALTERNATION);
            }
            return new Card(CardType.FORWARDEND);
        case 'T':
            if (rest.charAt(0) == '1') {
                return new Card(CardType.TRACEON);
            } else if (rest.charAt(0) == '0') {
                return new Card(CardType.TRACEOFF);
            } else {
                throw new UnknownCard("Expected T0 or T1 but got: " + cardString);
            }
        case ' ':
        case '.':
            return Card.commentCard(rest);
        default:
            throw new UnknownCard("Unable to parse: " + cardString);
        }
    }
}
