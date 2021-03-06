import java.util.ArrayList;
import java.util.Collections;

public class Trick {
	
	private Player winner;
	private ArrayList<Card> trickCards;
	private Card queenSpade;
	private ArrayList<Card> heartCards; 
	private int scoreCount; 
	
	public Trick() {
		trickCards = new ArrayList<Card>();
		queenSpade = new Card(Suit.SPADES, 12); 
		heartCards = new ArrayList<Card>();
		scoreCount = 0; 
		
		for (int i = 2; i < 15; i++) {
			heartCards.add(new Card(Suit.HEARTS, i)); 
		}
	}
	
	
	public void setWinner(Player p) {
		winner = p;
	}
	
	public int getWinner() {
		int n = winner.getPlayerId(); 
		return n; 
	}
	
	public void sortTrick() {
		Collections.sort(trickCards);
	}
	
	public ArrayList<Card> getTrickCards() {
		return trickCards;
	}
	
	public void addtoTrick(Card c) {
		trickCards.add(c); 
	}
	
	public void calculateScore() {
		
		trickCards.iterator().forEachRemaining(x -> {
			if (x.equals(queenSpade)) {
				scoreCount += 13; 
			}
		}); 
		
		trickCards.iterator().forEachRemaining(x -> {
			if (x.getSuit().getSuitValue() == 3) {
				scoreCount += 1; 
			}
		}); 
	}
	
	public Card calculateWinner() {
		ArrayList<Card> winningCards = new ArrayList<>(); 
		
		trickCards.iterator().forEachRemaining(x -> {
			if (x.getSuit() == trickCards.get(0).getSuit()) {
				winningCards.add(x); 
			}
		});
		
		Collections.sort(winningCards);
		
		return winningCards.get(0); 
	}
	
	public int getScoreCount() {
		return scoreCount; 
	}
	
	public void setScoreCount(int i) {
		scoreCount = i; 
	}
	
	public void resetScore() {
		scoreCount = 0; 
	}
	
/*	public boolean hasPlayerShotOverTheMoon() { //This method is given to the player class now
		if (scoreCount == 26) {
			return true; 
		}
			return false; 
	}*/
	
	public void clearTrick() {
		trickCards.clear(); 
	}
	
	public String toString() {
		return "Current Trick has: " + trickCards.toString();
	}
		
}
