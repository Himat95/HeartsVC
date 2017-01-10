import java.util.ArrayList;

public interface Player { //Abstract class to allow me create different types of players: Greedy heuristic AI, randmon AI and human player

	void setPlayerName(String s); 
	String getPlayerName(); 
	
	void setScore(int i); 
	int getScore(); 
	void incScore();
	void decScore(); 
	void resetScore(); 
	
	void setPlayerId(int i);
	int getPlayerId();
	Hand getPlayerHand(); 
	ArrayList<Card> getTrickCardsWon(); 
	void addToTrickCardsWon(ArrayList<Card> tc); 
	boolean ShotOverTheMoon();
	void sortTrickCardsWon(); 
	
}
