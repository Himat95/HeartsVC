import java.util.ArrayList;

public class CardTesting {

	public static void main(String[] args) {

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


	}

}
