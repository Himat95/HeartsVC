import java.util.ArrayList;

public class CardTesting {

	public static void main(String[] args) {

		Deck d = new Deck();
		ArrayList<Card> s = new ArrayList<Card>(); 
		//System.out.println(d.cardsMade()); 
		
		d.shuffle();
		s = d.dealCards(); 
		System.out.println(d.toString()); 
		
		System.out.println(s.toString());
		
		System.out.println(d.cardsLeft());
		
	}

}