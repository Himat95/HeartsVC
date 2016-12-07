
public class AIPlayer implements Player {
	
	private String playerName;
	private int playerScore;
	private int playerId;
	private Hand hand; 

	
	public AIPlayer(int id) {
		playerName = ""; 
		playerScore = 0; 
		playerId = id;
		hand = new Hand(); 
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
		playerScore = i; 
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
	public void setPlayerId(int i) {
		playerId = i; 
	}

	@Override
	public int getPlayerId() {
		return playerId; 
	}
	
	@Override 
	public String toString() {
		return "Player: " + playerName + "\t Player Score: " + playerScore; 
	}
	
	

}
