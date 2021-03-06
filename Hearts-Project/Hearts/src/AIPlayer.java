import java.util.ArrayList;
import java.util.Collections;

public class AIPlayer implements Player {
	
	private String playerName;
	private int playerScore;
	private int playerId;
	private Hand hand; 
	private ArrayList<Card> trickCardsWon; 

	
	public AIPlayer(int id) {
		playerName = ""; 
		playerScore = 0; 
		playerId = id;
		hand = new Hand(); 
		trickCardsWon = new ArrayList<Card>(); 
	}
	
	@Override
	public void setPlayerName(String s) {
		playerName = s; 
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}

	@Override
	public void setScore(int i) {
		playerScore += i; 
	}

	@Override
	public int getScore() {
		return playerScore; 
	}

	@Override
	public void incScore() {
		playerScore++; 
	}

	@Override
	public void decScore() {
		playerScore--; 
	}
	
	@Override
	public void resetScore() {
		playerScore = 0; 
	}

	@Override
	public void setPlayerId(int i) {
		playerId = i; 
	}

	@Override
	public int getPlayerId() {
		return playerId; 
	}
	
	@Override
	public Hand getPlayerHand() {
		return hand; 
	}
	
	@Override 
	public String toString() {
		return "Player ID: " + playerId + " \t " + "Player: " + playerName + "\t Player Score: " + playerScore; 
	}

	@Override
	public ArrayList<Card> getTrickCardsWon() {
		return trickCardsWon; 
	}
	
	@Override
	public void addToTrickCardsWon(ArrayList<Card> tc) {
		trickCardsWon.addAll(tc); 
	}

	@Override
	public boolean ShotOverTheMoon() {
		ArrayList<Card> sotm = new ArrayList<>(); 
		for (int i = 2; i <= 14; i++) {
			sotm.add(new Card(Suit.HEARTS, i)); 
		}
		sotm.add(new Card(Suit.SPADES, 12)); 
		
		return trickCardsWon.containsAll(sotm);
	}
	
	@Override
	public void sortTrickCardsWon() {
		Collections.sort(trickCardsWon);
	}
	
}
