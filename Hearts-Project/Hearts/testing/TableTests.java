import static org.junit.Assert.*;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

public class TableTests {

	@Test
	public void test01() {
		Table t = new Table(null, null); 
		assertTrue("Testing Heart State, its always on at start of a game", t.getHeartState() == true);
	}
	
	@Test
	public void test02() {
		Table t = new Table(null, null); 
		t.setHeartState(false);
		assertTrue("Testing Heart State, turning it off (Hearts is broken)", !t.getHeartState() == true);
	}
	
	@Test
	public void test03() {
		Table t = new Table(null, null); 
		assertTrue("Testing starting round", t.getRound() == 0);
	}
	
	@Test
	public void test04() {
		Table t = new Table(null, null); 
		t.incRound();
		assertTrue("Testing increment", t.getRound() == 1);
	}
	
	@Test
	public void test05() {
		Table t = new Table(null, null); 
		assertTrue("Trick starts from 0", t.getTrickNo() == 0);
	}
	
	@Test
	public void test06() {
		Table t = new Table(null, null); 
		t.incTrickNo();
		assertTrue("incrementing trick number", t.getTrickNo() == 1);
	}
	
	@Test
	public void test07() {
		Table t = new Table(null, null); 
		assertTrue("Turn is not equal to 0,1,2,3 at start", t.getTurn() != 0 && t.getTurn() != 1
				&& t.getTurn() != 2 && t.getTurn() != 3);
		
		/* This design decision was taken due to threads having errors of starting the algorithm as they thought it was their
		 * turn before the Clubs check even took place, therefore the value of turn is 5 at the start of the game to stop
		 * any threads from jumping ahead from the Clubs check and diving right into the game. 
		 */
	}

	@Test
	public void test08() {
		Table t = new Table(null, null); 
		Player p = new AIPlayer("Test", 2, t, null); 
		t.setTurn(p);
		assertTrue("Setting the turn to p's playerId number (2)", t.getTurn() == p.getPlayerId());
	}
	
	@Test
	public void test09() {
		Table t = new Table(null, null); 
		Player p = new AIPlayer("Test", 2, t, null); 
		Player p2 = new AIPlayer("Test", 3, t, null);
		t.setTurn(p);
		t.incTurn();
		assertTrue("incrementing the turn from player p to player p2 (2 to 3)", t.getTurn() == p2.getPlayerId());
	}
	
	@Test
	public void test10() {
		Table t = new Table(null, null); 
		Player p = new AIPlayer("Test", 0, t, null); 
		Player p2 = new AIPlayer("Test", 3, t, null);
		t.setTurn(p2);
		t.incTurn();
		assertTrue("Incrementing turn from 3 to 0", t.getTurn() == p.getPlayerId());
	}
	/*	This is a special case within the design of the game, due to the game only having up to 4 players.
	 *	When the turn value goes up to 3, it understands that 4 will go beyond any of the player thread's 
	 *	ID values, therefore it "wraps" around back to 0 and goes 0-1-2-3-0-1-2-3-0. To make sure there is a 
	 *	loop of players. This is also how clockwise rotation is achieved. 
	 */
	
	
	@Test
	public void test11() {
		ArrayBlockingQueue<Player> s = new ArrayBlockingQueue<Player>(2); 
		Table t = new Table(s, null); 
		Player p = new AIPlayer("Test", 0, t, null); 
		Player p2 = new AIPlayer("Test", 3, t, null);
		s.add(p);
		s.add(p2);
		assertTrue("2 players have been added to the queue", t.getPlayerQueue().size() == 2);
	}
	
	@Test
	public void test12() {
		ArrayBlockingQueue<Player> s = new ArrayBlockingQueue<Player>(2); 
		Table t = new Table(s, null); 
		Player p = new AIPlayer("Test", 0, t, null); 
		Player p2 = new AIPlayer("Test", 3, t, null);
		assertTrue("No players in the queue", t.getPlayerQueue().size() == 0);
	}
	
	
	@Test
	public void test13() {
		Table t = new Table(null, null); 
		assertTrue("At the start of the game, this value should always be false", t.getIsGameFinished() == false);
	}
	
	@Test
	public void test14() {
		Table t = new Table(null, null); 
		t.setIsGameFinished(true);
		assertTrue("Setting game finish to true", t.getIsGameFinished() == true);
	}
	
	@Test
	public void test15() {
		Table t = new Table(null, null); 
		t.setPlay(true);
		assertTrue("Setting play game to true to indicate players can throw", t.isPlay()== true);
	}
	
	@Test
	public void test16() {
		Table t = new Table(null, null); 
		t.setPlay(false);
		assertTrue("Setting play game to false", t.isPlay()== false);
	}
	
	@Test
	public void test17() {
		Table t = new Table(null, null); 
		t.setResults(true);
		assertTrue("Setting play game to true to indicate players can throw", t.isResults() == true);
	}
	
	@Test
	public void test18() {
		Table t = new Table(null, null); 
		t.setResults(false);
		assertTrue("Setting play game to false", t.isResults() == false);
	}
	
	@Test
	public void test19() {
		Table t = new Table(null, null); 
		Player p = new AIPlayer("test", 2, null, null);
		Card a = new Card(Suit.HEARTS, 12); 
		Card b = new Card(Suit.SPADES, 12); 
		Card c = new Card(Suit.DIAMONDS, 12); 
		
		t.SwapCards(a, b, c, 2);
		assertTrue("Adding 3 cards into the cards that need to be swapped, along with the player's id"
				+ "and retrieving the swapped cards that were given by the player with id '2'"
				, t.SwappedCards(2).contains(a) && t.SwappedCards(2).contains(b) && t.SwappedCards(2).contains(b));
	}
	/*	In the game this is used very effectively as each player would put in cards with their id, however
	 * 	the person who will retrieve the cards will subtract 1 from their ID and get the previous player's card.
	 * 	If the player id is 0, then the player will take cards from the player with ID equivalent to 3. 
	 */

	@Test
	public void test20() {
		Table t = new Table(null, null); 
		Player p = new AIPlayer("test", 2, null, null);
		Card a = new Card(Suit.HEARTS, 12); 
		Card b = new Card(Suit.SPADES, 12); 
		Card c = new Card(Suit.CLUBS, 12); 
		
		t.SwapCards(a, b, c, 2);
		p.getPlayerHand().addCards(t.SwappedCards(2));
		assertTrue("The player will add the swapped card to his own hand from the table", p.getPlayerHand().contains(a)
				&& p.getPlayerHand().contains(b) && p.getPlayerHand().contains(c));
	}

}