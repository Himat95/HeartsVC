import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Hand {

	private ArrayList<Card> hand;
	private Card thrown;
	private ArrayList<Card> used = new ArrayList<Card>();


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
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			Card temp = it.next(); 
			if (temp.equals(c)) {
				it.remove();
			}
		}
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

	
	public Card getCardByIndex(int i) {
		return hand.get(i);
	}

	public ArrayList<Card> getCards() {
			return hand;
	}
	
	public Card getCard(Card c) {
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			Card temp = it.next(); 
			if (temp.equals(c)) {
				return temp; 
			}
		}
		return c;
	}
	

	public void sortHand() {
		Collections.sort(hand);
	}

	public Card throwCard() {
		thrown = hand.stream().parallel().findAny().get();
		hand.remove(thrown); 
		return thrown;  
	}
	
	public Card throwExactCard(Card c) {
		thrown = c; 
		this.removeCard(c); 
		return thrown;
	}

	public Card lastThrownCard() {
		return this.thrown;
	}

	public ArrayList<Card> getUsedCards() {
		return used;
	}
	
	public boolean contains(Card c) {
		return hand.contains(c);
	}

	@Override
	public String toString() {
		return hand.toString(); 
	}


}



