import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

public class GreedyHeuristicTests {
	
	public static Comparator<Card> compareByValue = Comparator.comparingInt(Card::getValue);
	Deck d = new Deck(); 
	ArrayBlockingQueue<Player> queue = new ArrayBlockingQueue<Player>(2); 
	Trick t = new Trick(); 
	Table table = new Table(queue, t); 
	AIPlayer p = new AIPlayer("Test Dummy", 0, table, t); 
	
	
	/* Due to the difficulty of running threads only for certain instances, this
	 * test will simply take the algorithm's coding from each situation and test
	 * it with calculated trick input to predict desirable results. 
	 * 
	 * It must be noted that within this test environment, the AIPlayer is given an entire deck (52) 
	 * cards at its disposal. This will be the true test to understand how the AI can filter out cards
	 * Dependent on the trick conditions. 
	 */

	
	
	
//<-------------------------------------Empty Trick with Hearts State Off----------------------------------------------->
	
	/* This test is will examine what will the first card thrown by the Player, when the trick
	 * is empty and having the Heart state off (Allowing him to throw hearts).The predicted results is the
	 * player will throw the lowest card within the deck, therefore it will be less than the value of 3. 
	 */
	@Test
	public void test01() {
		queue.add(p);
		t.clearTrick();
		
		//Taken from:	trick.addtoTrick(this.getPlayerHand().getCards().stream().min(compareByValue).get(), this);
		t.addtoTrick(d.getCards().stream().min(compareByValue).get(), p);
		System.out.println("Test 01 |  " + t.getTrickCards());
		assertTrue("Testing when trick is empty with Hearts State off", t.getTrickCards().get(0).getValue() < 3);
	}
	
	
	
	/* Similar to test01 however this time 2 of Spades, 2 of Clubs and 2 of Diamonds is removed, to understand if the 
	 * player truly throws the 2 of hearts when the condition is off. 
	 * 
	 * //Taken from:	trick.addtoTrick(this.getPlayerHand().getCards().stream().min(compareByValue).get(), this);
	 */
	
	
	@Test
	public void test02() {
		queue.add(p);
		t.clearTrick();
		
		d.getCards().remove(0); 	// 2 of Spades
		d.getCards().remove(12);	// 2 of Clubs
		d.getCards().remove(24);	// 2 of Diamonds
		
		
		t.addtoTrick(d.getCards().stream().min(compareByValue).get(), p);
		System.out.println("Test 02 |  " + t.getTrickCards());
		assertTrue("Testing when trick is empty with Hearts State off", t.getTrickCards().get(0).getValue() < 3);
	}
	
	/* Same conditions as the previous tests but this time the lower values less than 7 have been removed, and this is
	 * to see if the AIPlayer acknowledges that 8 is the minimum value. 
	 */
	@Test
	public void test03() {
		queue.add(p);
		t.clearTrick();
		
		//the filter only returns cards that are above the value 7. Therefore it returns 8,9,10,jack,queen,king,ace. 
		t.addtoTrick(d.getCards().stream().filter(x -> x.getValue() > 7).min(compareByValue).get(), p);
		System.out.println("Test 03 |  " + t.getTrickCards());
		assertTrue("Testing the minimal card value thrown when trick is empty with Hearts State off", t.getTrickCards().get(0).getValue() == 8);
	}

	

	
	
	
	
	
	
	
	
	
//<-------------------------------------------Empty Trick with Hearts State On----------------------------------------------->
	
	/*	
	 * 	The tests within this section will demonstrate that the algorithm will not throw hearts until all other suits are
	 *  depleted from the deck. 
	 * 
	 * Taken from:	trick.addtoTrick(this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS)
	 * .min(compareByValue).orElse(this.getPlayerHand().getCards().stream().min(compareByValue).get()), this);
	 */
	
	
	/*	The first test will remove 2 of Spades, Clubs and Diamonds. Leaving only 2 of Hearts, but the algortihm will make sure to
	 *  skip this value and go for the next suitable card.
	 */
	@Test
	public void test04() {
		queue.add(p);
		t.clearTrick();

		d.getCards().remove(0); 	// 2 of Spades
		d.getCards().remove(12);	// 2 of Clubs
		d.getCards().remove(24);	// 2 of Diamonds

		t.addtoTrick(d.getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS).min(compareByValue).orElse(d.getCards().stream().min(compareByValue).get()), p);
		System.out.println("Test 04 |  " + t.getTrickCards());
		assertTrue("Testing the minimal card value thrown when trick is empty with Hearts State On", t.getTrickCards().get(0).getValue() == 3);
	}
	
	
	/*	The deck given only contains hearts and this checks if the AI will play hearts when no other option of suits is left. 
	 * 
	 */
	@Test
	public void test05() {
		queue.add(p);
		t.clearTrick();

		Deck d2 = new Deck(); 
		
		d2.getCards().clear();
		
		for (int i = 2; i <= 14; i++) {
			d2.getCards().add(new Card(Suit.HEARTS, i)); 
		}
		
		System.out.println("Test 05 |  " + "Deck has: " + d2.getCards());

		t.addtoTrick(d2.getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS).min(compareByValue).orElse(d2.getCards().stream().min(compareByValue).get()), p);
		System.out.println("Test 05 |  " + t.getTrickCards());
		assertTrue("Testing the minimal card value thrown when trick is empty with Hearts State On", t.getTrickCards().get(0).getValue() == 2);
	}
	
	
	/*	The deck given only contains hearts cards but one high card is given, this tests proves that even when multiple lower 
	 * 	cards of Hearts are available, the choice is still strict. 
	 */
	@Test
	public void test06() {
		queue.add(p);
		t.clearTrick();

		Deck d2 = new Deck(); 
		
		d2.getCards().clear();
		
		for (int i = 2; i <= 14; i++) {
			d2.getCards().add(new Card(Suit.HEARTS, i)); 
		}
		
		d2.getCards().add(new Card(Suit.SPADES, 14)); 
		
		System.out.println("Test 06 |  " + "Deck has: " + d2.getCards());

		t.addtoTrick(d2.getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS).min(compareByValue).orElse(d2.getCards().stream().min(compareByValue).get()), p);
		System.out.println("Test 06 |  " + t.getTrickCards());
		assertTrue("Testing the minimal card value thrown when trick is empty with Hearts State On", t.getTrickCards().get(0).getValue() == 14);
	}
	
	
	
	
	
	//<-------------------------------------------A single low value card in trick----------------------------------------------->
	
	
	/*	The "throwLowestSuitable" method implements the rest of the if and else conditions
	 * 	for the game, each condition will be tested in its own right with its respective coding
	 */
	
	
	
	/*	This tests sets an environment where available cards are present however
	 * 	the first trick card is too low for the algorithm to compete against. Another 
	 * 	condition is that due to the player not being the last player, it will try to
	 * 	throw low as it thinks there is still a possibility to avoid winning the trick. */
	@Test
	public void test07() {
		queue.add(p);
		t.clearTrick();
		Deck d2 = new Deck(); 

		t.addtoTrick(new Card(Suit.DIAMONDS, 2), p);
		d2.getCards().remove(new Card(Suit.DIAMONDS, 2)); //removing 2 of diamonds

		// Algorithm code
		ArrayList<Card> suitableCards = new ArrayList<Card>();
		Card drop = t.getTrickCards().stream().filter(x -> t.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		d2.getCards().stream().filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);

		Collections.sort(suitableCards);

		ArrayList<Card> lessthandrop = new ArrayList<Card>();

		suitableCards.forEach(x -> {
			if (x.getValue() < drop.getValue()) {
				lessthandrop.add(x);
			}
		});

		if (lessthandrop.isEmpty()) { 
			if (t.getTrickCards().size() < 3) {
				t.addtoTrick(suitableCards.get(0), p);
			}
			else {
				t.addtoTrick(suitableCards.get(suitableCards.size() - 1), p);
			}
		}
		else {
			Collections.sort(lessthandrop); 
			t.addtoTrick(lessthandrop.get(lessthandrop.size() - 1), p);
		}
		
		System.out.println("Test 07 | " + t.getTrickCards());
		assertTrue("When trick contains a card that is below anything the player has"
				+ "but the player isn't the last player to contribute to the trick",
				t.getTrickCards().get(1).getValue() == 3);
	}

	
	/*	This is the same as the previous test, but this the player will be the last
	 * 	to add to the trick and therefore the AI will throw its highest card to cut losses
	 * 	as he doesn't have card lower than 2 of Diamonds.
	 */
	@Test
	public void test08() {
		queue.add(p);
		t.clearTrick();
		Deck d2 = new Deck(); 

		t.addtoTrick(new Card(Suit.DIAMONDS, 2), p);
		t.addtoTrick(new Card(Suit.DIAMONDS, 2), p);
		t.addtoTrick(new Card(Suit.DIAMONDS, 2), p);
		d2.getCards().remove(new Card(Suit.DIAMONDS, 2)); //removing 2 of diamonds

		// Algorithm code
		ArrayList<Card> suitableCards = new ArrayList<Card>();
		Card drop = t.getTrickCards().stream().filter(x -> t.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		d2.getCards().stream().filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);

		Collections.sort(suitableCards);

		ArrayList<Card> lessthandrop = new ArrayList<Card>();

		suitableCards.forEach(x -> {
			if (x.getValue() < drop.getValue()) {
				lessthandrop.add(x);
			}
		});

		if (lessthandrop.isEmpty()) { 
			if (t.getTrickCards().size() < 3) {
				t.addtoTrick(suitableCards.get(0), p);
			}
			else {
				t.addtoTrick(suitableCards.get(suitableCards.size() - 1), p);
			}
		}
		else {
			Collections.sort(lessthandrop); 
			t.addtoTrick(lessthandrop.get(lessthandrop.size() - 1), p);
		}
		
		System.out.println("Test 08 | " + t.getTrickCards());
		assertTrue("When trick contains a card that is below anything the player has,"
				+ "however the player is the last one to throw", 
				t.getTrickCards().get(3).getValue() == 14);
	}
	

	
	
	
//<-------------------------------------------A single high value card in trick----------------------------------------------->
	
	/*	This test will calculate what happens when the algorithm notices there is a high
	 * 	value card in the trick. 
	 */
	
	@Test
	public void test09() {
		queue.add(p);
		t.clearTrick();

		t.addtoTrick(new Card(Suit.HEARTS, 12), p);

		// Algorithm code
		ArrayList<Card> suitableCards = new ArrayList<Card>();
		Card drop = t.getTrickCards().stream().filter(x -> t.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		d.getCards().stream().filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);

		Collections.sort(suitableCards);

		ArrayList<Card> lessthandrop = new ArrayList<Card>();

		suitableCards.forEach(x -> {
			if (x.getValue() < drop.getValue()) {
				lessthandrop.add(x);
			}
		});

		if (lessthandrop.isEmpty()) { 
			if (t.getTrickCards().size() < 3) {
				t.addtoTrick(suitableCards.get(0), p);
			}
			else {
				t.addtoTrick(suitableCards.get(suitableCards.size() - 1), p);
			}
		}
		else {
			Collections.sort(lessthandrop); 
			t.addtoTrick(lessthandrop.get(lessthandrop.size() - 1), p);
		}
		
		System.out.println("Test 09 | " + t.getTrickCards());
		assertTrue("The algorthim will try to throw one lower than the card put into the trick",
				t.getTrickCards().get(1).getValue() == 11);
	}
	
	
	
	
	
//<---------------------------------------Multiple Cards in the trick--------------------------------------------->

	/*	This is the most important test as the algorithm is introduced to many cards
	 * 	within the trick and it uses the provided conditions to filter the best choice
	 * 	card to throw dependent on the situation.
	 */
	
	@Test
	public void test10() {
		queue.add(p);
		t.clearTrick();

		t.addtoTrick(new Card(Suit.HEARTS, 10), p);
		t.addtoTrick(new Card(Suit.HEARTS, 5), p);
		t.addtoTrick(new Card(Suit.HEARTS, 7), p);
		
		// Algorithm code
		ArrayList<Card> suitableCards = new ArrayList<Card>();
		Card drop = t.getTrickCards().stream().filter(x -> t.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		d.getCards().stream().filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);

		Collections.sort(suitableCards);

		ArrayList<Card> lessthandrop = new ArrayList<Card>();

		suitableCards.forEach(x -> {
			if (x.getValue() < drop.getValue()) {
				lessthandrop.add(x);
			}
		});

		if (lessthandrop.isEmpty()) { 
			if (t.getTrickCards().size() < 3) {
				t.addtoTrick(suitableCards.get(0), p);
			}
			else {
				t.addtoTrick(suitableCards.get(suitableCards.size() - 1), p);
			}
		}
		else {
			Collections.sort(lessthandrop); 
			t.addtoTrick(lessthandrop.get(lessthandrop.size() - 1), p);
		}
		
		System.out.println("Test 10 | " + t.getTrickCards());
		assertTrue("The algorthim will try to throw one lower than the card put into the trick",
				t.getTrickCards().get(3).getValue() == 9);
	}
	
	
	
	/*	This test is same as the previous one however the cards are not the same suit, 
	 * 	this test will show that the algorithm is smart enough to calculate which card
	 * 	is worth calculating. 
	 */
	
	@Test
	public void test11() {
		queue.add(p);
		t.clearTrick();

		t.addtoTrick(new Card(Suit.HEARTS, 6), p);
		t.addtoTrick(new Card(Suit.DIAMONDS, 10), p);
		t.addtoTrick(new Card(Suit.SPADES, 4), p);
		
		// Algorithm code
		ArrayList<Card> suitableCards = new ArrayList<Card>();
		Card drop = t.getTrickCards().stream().filter(x -> t.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		d.getCards().stream().filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);

		Collections.sort(suitableCards);

		ArrayList<Card> lessthandrop = new ArrayList<Card>();

		suitableCards.forEach(x -> {
			if (x.getValue() < drop.getValue()) {
				lessthandrop.add(x);
			}
		});

		if (lessthandrop.isEmpty()) { 
			if (t.getTrickCards().size() < 3) {
				t.addtoTrick(suitableCards.get(0), p);
			}
			else {
				t.addtoTrick(suitableCards.get(suitableCards.size() - 1), p);
			}
		}
		else {
			Collections.sort(lessthandrop); 
			t.addtoTrick(lessthandrop.get(lessthandrop.size() - 1), p);
		}
		
		System.out.println("Test 11 | " + t.getTrickCards());
		assertTrue("The algorthim will try to throw one lower than the card put into the trick",
				t.getTrickCards().get(3).getValue() == 5);
	}
	
	
	
//<------------------------------------Missing appropriate Suit------------------------------------------->

	
	/*	This test shows what happens when the missing the correct suit */
	
	@Test
	public void test12() {
		queue.add(p);
		t.clearTrick();
		Deck d2 = new Deck();

		t.addtoTrick(new Card(Suit.SPADES, 8), p);
		t.addtoTrick(new Card(Suit.SPADES, 12), p);
		t.addtoTrick(new Card(Suit.SPADES, 4), p);
		
		for (int i = 2; i <= 14; i++) {
			d2.getCards().remove(new Card(Suit.SPADES, i)); 
		}
		
		// Algorithm code
		ArrayList<Card> suitableCards = new ArrayList<Card>();
		Card drop = t.getTrickCards().stream().filter(x -> t.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		d2.getCards().stream().filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);

		if (suitableCards.isEmpty()) {
			t.addtoTrick(d2.getCards().stream().filter(x -> x.getSuit() == Suit.HEARTS).max(compareByValue).
					orElse(d2.getCards().stream().max(compareByValue).get()), p);
		}

		else { 
				
		Collections.sort(suitableCards);

		ArrayList<Card> lessthandrop = new ArrayList<Card>();

		suitableCards.forEach(x -> {
			if (x.getValue() < drop.getValue()) {
				lessthandrop.add(x);
			}
		});

		if (lessthandrop.isEmpty()) { 
			if (t.getTrickCards().size() < 3) {
				t.addtoTrick(suitableCards.get(0), p);
			}
			else {
				t.addtoTrick(suitableCards.get(suitableCards.size() - 1), p);
			}
		}
		else {
			Collections.sort(lessthandrop); 
			t.addtoTrick(lessthandrop.get(lessthandrop.size() - 1), p);
		}
		}
		
		System.out.println("Test 12 | " + t.getTrickCards());
		assertTrue("When no approriate suit cards are found, the algorthim will throw"
				+ "the highest Heart card it has to make others gain points",
				t.getTrickCards().get(3).getValue() == 14);
	}
	
	
	
	/*	This test shows what happens when the missing the correct suit, however
	 * 	previously it is demonstrated that the algorithm will choose hearts, but 
	 * 	Hearts is missing, it will throw the highest available card (regardless of the
	 * 	3 suits) */
	
	@Test
	public void test13() {
		queue.add(p);
		t.clearTrick();
		Deck d2 = new Deck();

		t.addtoTrick(new Card(Suit.SPADES, 8), p);
		t.addtoTrick(new Card(Suit.SPADES, 12), p);
		t.addtoTrick(new Card(Suit.SPADES, 4), p);
		
		for (int i = 2; i <= 14; i++) {
			d2.getCards().remove(new Card(Suit.SPADES, i)); 
			d2.getCards().remove(new Card(Suit.HEARTS, i)); 
		}
		
		
		
		// Algorithm code
		ArrayList<Card> suitableCards = new ArrayList<Card>();
		Card drop = t.getTrickCards().stream().filter(x -> t.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		d2.getCards().stream().filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);

		if (suitableCards.isEmpty()) {
			t.addtoTrick(d2.getCards().stream().filter(x -> x.getSuit() == Suit.HEARTS).max(compareByValue).
					orElse(d2.getCards().stream().max(compareByValue).get()), p);
		}

		else { 
				
		Collections.sort(suitableCards);

		ArrayList<Card> lessthandrop = new ArrayList<Card>();

		suitableCards.forEach(x -> {
			if (x.getValue() < drop.getValue()) {
				lessthandrop.add(x);
			}
		});

		if (lessthandrop.isEmpty()) { 
			if (t.getTrickCards().size() < 3) {
				t.addtoTrick(suitableCards.get(0), p);
			}
			else {
				t.addtoTrick(suitableCards.get(suitableCards.size() - 1), p);
			}
		}
		else {
			Collections.sort(lessthandrop); 
			t.addtoTrick(lessthandrop.get(lessthandrop.size() - 1), p);
		}
		}
		
		System.out.println("Test 12 | " + t.getTrickCards());
		assertTrue("When no approriate suit cards are found for Spades and the"
				+ "algorithm has no Hearts, it will throw the highest available"
				+ "card it has within the hand.",
				t.getTrickCards().get(3).getValue() == 14);
	}
	
	
	
	
	/*	The commented code below is the actual algorithm that has been broken
	 * 	apart to understand each feature within the coding is doing and this is
	 * 	displayed within the tests.  
	 */
	
	
	
	
	
	
	
	
	
	
/*	drop = trick.getTrickCards().stream()
			.filter(x -> trick.getTrickCards().get(0).getSuit() == x.getSuit())
			.max(compareByValue).get();

	this.getPlayerHand().getCards().stream()
	.filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);



	if (suitableCards.isEmpty()) {

		trick.addtoTrick(this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() == Suit.HEARTS).max(compareByValue).orElse(this.getPlayerHand().getCards().stream().max(compareByValue).get()), this);
		this.getPlayerHand().removeCard(this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() == Suit.HEARTS).max(compareByValue).orElse(this.getPlayerHand().getCards().stream().max(compareByValue).get()));
		table.incTurn();
	}

	else { // There are cards in suitable Cards, we want to
			// throw lower than the drop value
		Collections.sort(suitableCards);

		ArrayList<Card> lessthandrop = new ArrayList<Card>();

		//suitableCards.stream().filter(x -> x.getValue() < drop.getValue()).map(lessthandrop::add);
		suitableCards.forEach(x -> {
			if (x.getValue() < drop.getValue()) {
				lessthandrop.add(x);
			}
		});

		if (lessthandrop.isEmpty()) { // no card is smaller than the drop value, throw highest in suitableCards

			if (trick.getTrickCards().size() < 3) {
				trick.addtoTrick(suitableCards.get(0), this);
				this.getPlayerHand().removeCard(suitableCards.get(0)); 
				table.incTurn();
			}
			
			else {
			trick.addtoTrick(suitableCards.get(suitableCards.size() - 1), this);
			this.getPlayerHand().removeCard(suitableCards.get(suitableCards.size() - 1));
			table.incTurn();
			}
		}

		else {
			Collections.sort(lessthandrop); // throw the biggest in lessthandrop

			trick.addtoTrick(lessthandrop.get(lessthandrop.size() - 1), this);
			this.getPlayerHand().removeCard(lessthandrop.get(lessthandrop.size() - 1));
			table.incTurn();
		}
		lessthandrop.clear(); // housekeeping!
	}

	suitableCards.clear(); // housekeeping!
	*/
	
	
	
	/*
	 * 	private void emptyTrickwithHeartStateOff() {

		trick.addtoTrick(this.getPlayerHand().getCards().stream().min(compareByValue).get(), this);
		System.out.println(this.getPlayerId() + " " + this.getPlayerHand().getCards());
		this.getPlayerHand().removeCard(this.getPlayerHand().getCards().stream().min(compareByValue).get());
		table.incTurn();
		
	}


	private void emptyTrickwithHeartStateOn() {

		trick.addtoTrick(this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS).min(compareByValue).orElse(this.getPlayerHand().getCards().stream().min(compareByValue).get()), this);
		this.getPlayerHand().removeCard(this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS).min(compareByValue).get());
		table.incTurn();

	}

	private void clubCheck() {

		Iterator<Card> iter = this.getPlayerHand().getCards().iterator();

		while(iter.hasNext()) {

			Card c = iter.next();
			if (c.equals(new Card(Suit.CLUBS, 2))) {
				trick.addtoTrick(c, this);
				table.setTurn(this);
				table.incTurn();
				iter.remove();
				System.out.println("Club check" + "Player" + this.getPlayerId() + ": " + this.getPlayerHand().getCards());
				System.out.println("Player : " + this.getPlayerId() + "\t Current Trick: " + trick.getTrickCards());
			}
		}
	 */
	
}

