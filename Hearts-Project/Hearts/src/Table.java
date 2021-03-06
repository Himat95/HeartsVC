
public class Table {
	
	private int round;
	private int trickNo;
	private int turn; 
	private boolean heartStateOn;
	private Trick trick; 
	
	public Table() {
		round = 0; 
		trickNo = 0; 
		heartStateOn = true;
	}
	
	public int getRound() {
		return round; 
	}
	
	public void incRound() {
		round++; 
	}
	
	public Trick newTrick() {
		trick = new Trick(); 
		return trick; 
	}
	
	public void incTrickNo() {
		trickNo++; 
	}
	
	public int getTrickNo() {
		return trickNo; 
	}
	
	public void resetTrickNo() {
		trickNo = 0; 
	}
	
	public void resetRound() {
		round = 0; 
	}
	
	public void setTurn(Player p) {
		turn = p.getPlayerId(); 
	}
	
	public int getTurn() {
		return turn; 
	}
	
	public void incTurn() {
		turn = turn++ % 4; 
	}
	
	public void setHeartState(boolean b) {
		heartStateOn = b; 
	}
	
	public boolean getHeartState() {
		return heartStateOn; 
	}
	
}
