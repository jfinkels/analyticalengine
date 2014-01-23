package analyticalengine.newio;

import analyticalengine.components.cards.Card;

public class CardParser {
    public static Card toCard(String cardString) throws UnknownCard {
        char firstChar = cardString.charAt(0);
        switch (firstChar) {
        case '+':
            break;
        case '*':
            break;
        case '/':
            break;
        case '-':
            break;
        case 'N':
            break;
        case 'L':
            break;
        case 'Z':
            break;
        case 'S':
            break;
        case '<':
            break;
        case '>':
            break;
        case 'C':
            break;
        case 'B':
            break;
        case 'P':
            break;
        case 'H':
            break;
        case 'D':
            break;
        case 'A':
            break;
        case 'T':
            break;
        case ' ':
            break;
        case '.':
            break;
        default:
            throw new UnknownCard("Unable to parse: " + cardString);
        }
        return null;
    }
}
