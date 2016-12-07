
public interface Player {

	void setPlayerName(String s); 
	String getPlayerName(); 
	
	void setScore(int i); 
	int getScore(); 
	void incScore();
	void decScore(); 
	
	void setPlayerId(int i);
	int getPlayerId();
	Hand getPlayerHand(); 
	
}