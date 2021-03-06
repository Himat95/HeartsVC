
public class Card implements Comparable<Card>{

	//------------------fields---------------------
	private final Suit suit;
/*	private final static int JACK = 11,
							 QUEEN = 12,
							 KING = 13,
							 ACE = 14;
							 */
	private final int value;

	//---------------constructor-----------------
	public Card(Suit cSuit, int cValue) {
		suit = cSuit;
		value = cValue;
	}

	//------------------methods----------------------
	public Suit getSuit() {
		return suit;
	}

	public int getValue() {
		return value;
	}

	public String getValueAsString() {
		switch(value) {
		case 2: return "2";
		case 3: return "3";
		case 4: return "4";
		case 5: return "5";
		case 6: return "6";
		case 7: return "7";
		case 8: return "8";
		case 9: return "9";
		case 10: return "10";
		case 11: return "Jack";
		case 12: return "Queen";
		case 13: return "King";
		default: return "Ace";
		}
	}

	public String getSuitAsString() {
		switch(suit) {
		case HEARTS: return "Hearts";
		case DIAMONDS: return "Diamonds";
		case CLUBS: return "Clubs";
		case SPADES: return "Spades";
		default: return "it should never reach this...";
		}
	}

	@Override
	public String toString() {
		return getValueAsString() + " of " + getSuitAsString();
	}

	@Override
	public int compareTo(Card o) {
		if (Integer.compare(this.suit.getSuitValue(), o.suit.getSuitValue()) == 0) {
			return Integer.compare(this.value, o.value);
		}
		return Integer.compare(this.suit.getSuitValue(), o.suit.getSuitValue());
	}
	
	public boolean equals(Object o) {
		Card obj = (Card)o; 
		if (this.suit.getSuitValue() == obj.suit.getSuitValue()) {
			return this.value == obj.value; 
		}
		return false; 
	}


}
