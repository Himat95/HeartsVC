import java.util.ArrayList;

public class BasicGame {

	public static void main(String[] args) {
		
		Deck deck = new Deck();  	//Create a deck of 52 cards
		Table table = new Table(); 	//Create a table for the players to play on
		ArrayList<Player> players = new ArrayList<>(); 
		//An arraylist of players so I can use lambda expressions rather than loops
		//and we are able to use collection's methods. Lowering coding needed excessively.
		
		
		//Fill the players arraylist with 4 players, each with different IDs
		for(int i = 0; i < 4; i++) {
			players.add(new AIPlayer(i)); 
		}
		
		
		System.out.println("The Current Players and their scores: \n");
		players.forEach(x -> System.out.println(x.toString())); 
		
		
		
		deck.shuffle(); //Shuffle the deck 
		
		
		players.forEach(x -> x.getPlayerHand().addCards(deck.dealCards()));
		//Give each player 13 cards from the deck
		
		
		System.out.println("\n"); 
		System.out.println("The starting cards for each player, 13 in total: \n");
		players.forEach(x -> System.out.println(x.getPlayerHand().getCards()));
		// Print out the current cards each players have in their hand
		
		
		
		players.forEach(x -> x.getPlayerHand().sortHand());
		//Organise every player's hand so its in order by suit and then value
		
		
		
		
		

		
		// --------------------------------Game Play--------------------------------------
		
		Trick trick = table.newTrick(); //initialise a trick (takes a card each from players) 
		
		
		System.out.println("\n\n <--------------------------------------Game is about to Begin----------------------------------------->");  
		
		while (table.getTrickNo() != 13) { // We run 13 tricks (which uses up 52 cards)

			
			for (int i = 0; i < 4; i++) { // Each player throws a card into the trick
										
				trick.addtoTrick(players.get(i).getPlayerHand().throwCard());
			}

			System.out.println("\n \n \t" + trick.toString() + " \n\n"); // Show what cards are in the trick
																	

			trick.calculateScore(); // Adds up the score of the current trick

			
			players.forEach(x -> {
				if (x.getPlayerHand().lastThrownCard() == trick.calculateWinner()) {
					trick.setWinner(x);
					x.setScore(trick.getScoreCount());
					x.addToTrickCardsWon(trick.getTrickCards());
				}
			});
			// Check every player's last thrown card and see if it equals to the winning card
			// once found whose card it is, set that player as the winner of the trick and give
			// that player the score and give the winner all the trick cards

			
			players.forEach(x -> x.sortTrickCardsWon()); //Organise the trick cards
			
			players.iterator().forEachRemaining(x -> {
				if (x.ShotOverTheMoon() == true) {
					players.iterator().forEachRemaining(x1 -> x1.setScore(26));
					x.resetScore();
				}
			});
			
			// Print out the current state of the player objects (Their cards, scores and won cards)
			players.forEach(x -> System.out.println("Current Cards: " + x.getPlayerHand().getCards() + "\n \n"));
			players.forEach(x -> System.out.println(x.toString() + "\n"));
			players.forEach(x -> System.out.println("\n Held Trick Cards: " + x.getTrickCardsWon() + "\n"));

			trick.resetScore();
			trick.clearTrick();
			table.incTrickNo();
			// increment the loop and clear the trick for the next set of cards
		}
	}
}
