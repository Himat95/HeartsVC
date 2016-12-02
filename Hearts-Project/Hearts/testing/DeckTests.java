import static org.junit.Assert.*;

import org.junit.Test;

public class DeckTests {

// <---------------------------------------Deck Tests------------------------------------------->
	
	@Test
	public void test01() {
		Deck d = new Deck();
		assertNotNull("Checking if the deck has been created properly", d);
	}

	@Test
	public void test02() {
		Deck d = new Deck();
		assertTrue("The deck has created 52 cards", d.cardsLeft() == 52);
	}

	@Test
	public void test03() {
		Deck d = new Deck();
		Boolean identical = null;

		for (int i = 1; i < 52; i++) {
			identical = d.getCard(0) == d.getCard(i);
		}

		assertTrue("There are 52 unique cards, none of them are identical", identical == false);
	}

	@Test
	public void test04() {
		Deck d = new Deck();
		Deck d1 = new Deck();
		Card c = d.getCard(0);
		System.out.println(c.toString());
		assertTrue("Getting correct values for cards, retrieving 2 of spades", d.getCard(0) == c);
	}

	@Test
	public void test05() {
		Deck d = new Deck();
		String s = d.toString();
		d.shuffle(); 
		String s1 = d.toString();
		System.out.println(s);
		System.out.println(s1);
		assertFalse("Deck is shuffled (changed from ordered cards to random)", s == s1); 
	}
	
	@Test
	public void test06() {
		Deck d = new Deck();
		int orignal = d.cardsLeft();
		d.dealCards();
		int altered = d.cardsLeft(); 
		assertFalse("Deck has dealt 13 cards, 52 - 13 = 39", orignal == altered); 
	}
	
	
//<------------------------------------------Hand tests---------------------------------------------->

	@Test
	public void test07() {
		Deck d = new Deck(); 
		Hand h = new Hand(); 
		h.addCards(d.dealCards());
		System.out.println("\t" + h.cardsRemaining());
		h.clearCards();
		System.out.println("\t" + h.cardsRemaining());
		assertTrue("Hand has successfully added cards from deck", h.cardsRemaining() != 0); 
	}
	
	@Test
	public void test08() {
		Deck d = new Deck(); 
		Deck d1 = new Deck(); 
		Hand h = new Hand(); 
		Card c = new Card(Suit.SPADES, 6);
		h.addCards(d.dealCards());
		
		//System.out.println(h.getCardByIndex(0)); 
		System.out.println("hand:" + h.getCards());
		h.removeCard(c);
		//h.removeCard(d1.getCard(0));
		//System.out.println(d1.getCard(0)); 
		//System.out.println(c); 
		//System.out.println("hand:" + h.getCardByIndex(0));
		System.out.println("handafter:" + h.getCards());
		System.out.println(d1.getCard(0).equals(c));
		assertTrue("The Card 2 of Spades is deleted from the hand", h.getCardByIndex(1).compareTo(c) == 0);
	}


}
