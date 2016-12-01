
public enum Suit {

	SPADES(0), 
	CLUBS(1), 
	DIAMONDS(2), 
	HEARTS(3);
	
	private final int suits; //Create int value for enums
	Suit(int suits) {
		this.suits = suits; 
		}
	
	public int getSuitValue() {
		return suits; 
	}
		
}
