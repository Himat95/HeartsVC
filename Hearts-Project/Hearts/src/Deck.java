import java.util.ArrayList;
import java.util.Collections;

public class Deck {

/*	private Card[] deck;

	public Deck() {
		deck = new Card[52];
		int cardCt = 0; //Cards created so far

		for (Suit s : Suit.values()) {
			for (int value = 2; value <=14; value++) {
				deck[cardCt] = new Card(s, value);
				cardCt++;
			}
		}
	}


	public String cardsMade() {
		int i = 0;
		while (i != 52) {
			System.out.println(deck[i].toString());
			i++;
		}
		return null;
	}


	*/

	private ArrayList<Card> deck = new ArrayList<Card>();

	public Deck() {
		for (Suit s : Suit.values()) {
			for(int value = 2; value <=14; value++) {
				deck.add(new Card(s, value));
			}
		}
	}


	public void shuffle() {
		Collections.shuffle(deck);
	}

	public ArrayList<Card> dealCards() {
		ArrayList<Card> dealt = new ArrayList<>();
		/*
		for (int i = 0; i < 13; i++) {
		dealt.add(deck.remove(i));
		} 
		*/
		deck.stream().limit(13).forEach(x -> dealt.add(x));
		deck.removeAll(dealt); 
		return dealt;
	}

	public int cardsLeft() {
		return deck.size();
	}

	public ArrayList<Card> getCards() {
		return deck;
	}

	public Card getCard(int i) {
		return deck.get(i);
	}

	@Override
	public String toString() {
		return deck.toString();
	}

}