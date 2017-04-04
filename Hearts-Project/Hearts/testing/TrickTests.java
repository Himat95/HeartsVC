import static org.junit.Assert.*;

import org.junit.Test;

public class TrickTests {

	@Test
	public void test01() {
		Trick t = new Trick(); 
		assertTrue("There are no cards in the newly created trick", t.getTrickCards().isEmpty()); 
	}
	
	@Test
	public void test02() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		assertTrue("The trick is not empty", !t.getTrickCards().isEmpty()); 
	}

	@Test
	public void test03() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		assertTrue("The trick is now full", t.isTrickFull()); 
	}
	
	@Test
	public void test04() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.clearTrick();
		assertTrue("The trick is now empty", t.getTrickCards().isEmpty()); 
	}
	
	@Test
	public void test05() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		Card c = new Card(Suit.HEARTS, 12);
		
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.SPADES, 12), p);
		t.getTrickCards();
		
		assertTrue("The trick gives back the appropriate cards and this is tested with"
				+ "queen of hearts and checking if the last card which is queen of spades is not equal to hearts", 
				t.getTrickCards().get(0).equals(c) && t.getTrickCards().get(1).equals(c) && 
				t.getTrickCards().get(2).equals(c) && !t.getTrickCards().get(3).equals(c)); 
	}
	
	
	@Test
	public void test06() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.calculateScore();
		assertTrue("The trick can successfully calculate the score of 4 heart cards", t.getScoreCount() == 4); 
	}
	
	@Test
	public void test07() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		t.addtoTrick(new Card(Suit.HEARTS, 12), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p);
		t.calculateScore();
		assertTrue("The trick can successfully calculate the score of 1 heart card", t.getScoreCount() == 1); 
	}
	
	@Test
	public void test08() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		t.addtoTrick(new Card(Suit.DIAMONDS, 12), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p);
		t.addtoTrick(new Card(Suit.SPADES, 11), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p);
		t.calculateScore();
		assertTrue("The trick can successfully calculate the score without any hearts", t.getScoreCount() == 0); 
	}
	
	@Test
	public void test09() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		t.addtoTrick(new Card(Suit.SPADES, 12), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p);
		t.calculateScore();
		assertTrue("The trick can identify the Queen of Spades which is 13 points", t.getScoreCount() == 13); 
	}
	
	@Test
	public void test10() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		Player p2 = new AIPlayer("Test Dummy2", 2, null, t);
		
		t.addtoTrick(new Card(Suit.SPADES, 12), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p2);

		t.calculateWinner();
		assertTrue("Calculating the winning card, should be spade as it was thrown first", t.getWinner() == p); 
	}
	
	@Test
	public void test11() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		Player p2 = new AIPlayer("Test Dummy2", 2, null, t);
		
		t.addtoTrick(new Card(Suit.SPADES, 12), p);
		t.addtoTrick(new Card(Suit.CLUBS, 12), p2);

		t.calculateWinner();
		assertFalse("The opposite of test 10, but asserting false", t.getWinner() == p2); 
	}
	
	@Test
	public void test12() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		Player p2 = new AIPlayer("Test Dummy2", 2, null, t);
		
		t.addtoTrick(new Card(Suit.SPADES, 12), p);
		t.addtoTrick(new Card(Suit.SPADES, 13), p2);

		t.calculateWinner();
		assertTrue("Calculating the winning card, Queen of Spades verses King of Spades", t.getWinner() == p2); 
	}
	
	@Test
	public void test13() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		Player p2 = new AIPlayer("Test Dummy2", 2, null, t);
		Player p3 = new AIPlayer("Test Dummy3", 3, null, t);
		Player p4 = new AIPlayer("Test Dummy4", 4, null, t);
		
		t.addtoTrick(new Card(Suit.HEARTS, 4), p);
		t.addtoTrick(new Card(Suit.HEARTS, 2), p2);
		t.addtoTrick(new Card(Suit.HEARTS, 7), p3);
		t.addtoTrick(new Card(Suit.HEARTS, 10), p4);

		t.calculateWinner();
		assertTrue("Calculating winner for low value cards, winner should be 10", t.getWinner() == p4); 
	}
	
	@Test
	public void test14() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		Player p2 = new AIPlayer("Test Dummy2", 2, null, t);
		Player p3 = new AIPlayer("Test Dummy3", 3, null, t);
		Player p4 = new AIPlayer("Test Dummy4", 4, null, t);
		
		t.addtoTrick(new Card(Suit.HEARTS, 14), p);
		t.addtoTrick(new Card(Suit.HEARTS, 13), p2);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p3);
		t.addtoTrick(new Card(Suit.HEARTS, 11), p4);

		t.calculateWinner();
		assertTrue("Calculating winner for high value cards, winner should be 14 (Ace of Hearts)", t.getWinner() == p); 
	}
	
	@Test
	public void test15() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		Player p2 = new AIPlayer("Test Dummy2", 2, null, t);
		Player p3 = new AIPlayer("Test Dummy3", 3, null, t);
		Player p4 = new AIPlayer("Test Dummy4", 4, null, t);
		
		t.addtoTrick(new Card(Suit.HEARTS, 14), p);
		t.addtoTrick(new Card(Suit.HEARTS, 13), p2);
		t.addtoTrick(new Card(Suit.HEARTS, 12), p3);
		t.addtoTrick(new Card(Suit.HEARTS, 11), p4);

		t.calculateWinner();
		t.setWinner(p3);
		assertTrue("Setting a new winner, same as previous test but overriding the current winner", t.getWinner() == p3); 
	}
	
	@Test
	public void test16() {
		Trick t = new Trick(); 
		Player p = new AIPlayer("Test Dummy", 1, null, t); 
		Player p2 = new AIPlayer("Test Dummy2", 2, null, t);
		Player p3 = new AIPlayer("Test Dummy3", 3, null, t);
		Player p4 = new AIPlayer("Test Dummy4", 4, null, t);
		
		t.addtoTrick(new Card(Suit.HEARTS, 7), p);
		t.addtoTrick(new Card(Suit.HEARTS, 13), p2);
		t.addtoTrick(new Card(Suit.HEARTS, 2), p3);
		t.addtoTrick(new Card(Suit.HEARTS, 10), p4);

		System.out.println(t.getTrickCards());
		t.sortTrick();
		System.out.println(t.getTrickCards());
		//Sorting a trick, This can't be asserted true but it can be displayed
	}
}
