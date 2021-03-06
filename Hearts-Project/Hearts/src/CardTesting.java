import java.util.ArrayList;

public class CardTesting {

	public static void main(String[] args) {

		// <------------------------This is a personal testing class where i test out some methods and coding before putting it in game-------------------->
		//<--------------------------Also used for debugging and error checkings ---------------------------------------->
		
		Deck d = new Deck();
		Hand h = new Hand();
		//System.out.println(d.cardsMade());

		d.shuffle();
		h.addCards(d.dealCards());

		System.out.println(d.toString());

		System.out.println(h.toString());

		System.out.println(d.cardsLeft());

		h.sortHand();

		System.out.println(h.toString());

		System.out.println(h.cardsRemaining());

		System.out.println(h.getCards());

		h.clearCards();

		System.out.println(h.toString());

		System.out.println("<-----------Break------------>");

		h.addCards(d.dealCards());

		System.out.println(d.toString());
		System.out.println(d.cardsLeft());


		System.out.println(h.toString());
		h.sortHand();
		System.out.println(h.toString());
		
		System.out.println(h.getCardByIndex(0).getSuit());
		
		
		int i = 0; 
		int m = 0;
		for (int j = 0; j < 10; j++) {
		i = m++ % 4; 
		System.out.println(i); 
		}
		
		ArrayList<Card> s = new ArrayList<>(); 
		
		s.add(new Card(Suit.CLUBS, 2));
		s.add(new Card(Suit.DIAMONDS, 2));
		s.add(new Card(Suit.HEARTS, 2));
		s.add(new Card(Suit.SPADES, 2)); 
		
		System.out.println("\n");
		
		s.iterator().forEachRemaining(x -> System.out.println(x));  
		


	}

}
