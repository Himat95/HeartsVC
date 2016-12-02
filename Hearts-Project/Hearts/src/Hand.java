import java.util.ArrayList;
import java.util.Collections;

public class Hand {

	private ArrayList<Card> hand;
	private Card thrown;
	private ArrayList<Card> used = new ArrayList<Card>();

// I HAVE NO CONSTRUCTOR?!

	public Hand() {
		this.hand  = new ArrayList<Card>(); 
	}

	public void clearCards() {
		hand.clear();
		}

	public void addCard(Card c) {
		hand.add(c);
	}

	public void removeCard(Card c) {
		hand.remove(this.getCard(c)); 
		used.add(c);
	}

	public void addCards(ArrayList<Card> ac) {
		hand.addAll(ac);
	}


	public void removeCards(ArrayList<Card> ac) {
		hand.removeAll(ac);
		used.addAll(ac);
	}


	public void swapCards(ArrayList<Card> ac2, ArrayList<Card> ac) {
		this.addCards(ac2);
		this.removeCards(ac);

	}

	public int cardsRemaining() {
		return hand.size();
	}

	// <------------------might wanna change these later----------->
	public Card getCardByIndex(int i) {
		return hand.get(i);
	}

	public ArrayList<Card> getCards() {
			return hand;
	}
	
	public Card getCard(Card c) {
		int i = hand.indexOf(c);
		return hand.get(i+1); 
	}
	// <------------------might wanna change these later----------->


	public void sortHand() {
		Collections.sort(hand);
	}

	public Card throwCard(Card c) {
		if (hand.contains(c)) {
			hand.remove(c);
			this.thrown = c;
			return c;
		}
		else
			return null;
	}

	public Card lastThrownCard() {
		return this.thrown;
	}

	public ArrayList<Card> getUsedCards() {
		return used;
	}

	@Override
	public String toString() {
		return hand.toString(); 
	}


}



