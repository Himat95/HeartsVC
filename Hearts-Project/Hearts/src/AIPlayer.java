import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.stream.Collector;


public class AIPlayer extends Thread {

	private String playerName;
	private int playerScore;
	private int playerId;
	private Hand hand;
	private ArrayList<Card> trickCardsWon;
	private ArrayList<Card> suitableCards = new ArrayList<Card>();;
	private ArrayList<Card> sotm = new ArrayList<>();
	private boolean isReady; 
	private Trick trick;
	private Table table;
	private Card drop;

	public static Comparator<Card> compareByValue = Comparator.comparingInt(Card::getValue);


	public AIPlayer(String n, int id, Table table) {
		playerName = n;
		playerScore = 0;
		playerId = id;
		hand = new Hand();
		trickCardsWon = new ArrayList<Card>();
		this.table = table; 
		isReady = false; 


		for (int i = 2; i <= 14; i++) {
			sotm.add(new Card(Suit.HEARTS, i));
		}
		sotm.add(new Card(Suit.SPADES, 12));
		Collections.sort(sotm);
	}


	@Override
	public void run() {

/*		try {
			table.getPlayerQueue().put(this);
			notifyAll();
		} catch (InterruptedException e1) {
			System.err.println("Failed to put myself in queue " + this.playerId);
			e1.printStackTrace();
		}*/

		while(this.getPlayerHand().getCards().isEmpty()) {
		try {
			wait();
		} catch (InterruptedException e1) {
			System.err.println("I dont wait for no table...");
			e1.printStackTrace();
			}
		}
		
		System.out.println("Player " + this.getPlayerId() + ": " + this.getPlayerHand().getCards());
		
		this.clubCheck(); 
		
		while (table.getIsGameFinished() == false) {
/*			
			//System.out.println(this.playerId + ": Phase 1");
			if (table.getTrickNo() == 1) {
				this.clubCheck();
				//System.out.println(this.playerId + ": Phase 2");
			}

			else {
*/
				System.out.println(this.playerId + ": Phase 3");
				if (table.getTurn() == this.playerId && table.isPlay() == true) {
					System.out.println(this.playerId + ": Phase 4");
					if (trick.getTrickCards().isEmpty() == true) {

						if (table.getHeartState() == true) { // Can't throw
																// hearts yet!

							this.emptyTrickwithHeartStateOn();
						}

						else {
							this.emptyTrickwithHeartStateOff();
						}

					}

					else {
						this.throwLowestSuitable();
						System.out.println(this.playerId + " Phase 5");
					} // greedy heuristic

				} // if table turn == this player

				
/*				if (table.isResults() == true) {
					System.out.println(this.playerId + ": Phase 6");
					if (this == trick.getWinner()) {
						this.setScore(trick.getScoreCount());
					}
				}
*/
				else { // wait for your turn silly AI
					this.setReady(false);
					try {
						wait();
					} catch (InterruptedException e) {
						System.err.println("Wait error, was interrupted.");
						e.printStackTrace();
					}
				}
				this.setReady(false);

//			}
			
		} // Game is finished after this

		System.out.println("Player " + this.playerId + ": has finished with the score of " + this.getScore());
		System.out.println(this.getTrickCardsWon() + " + ");
		System.out.print(this.getPlayerHand());
	}
	
	
	private synchronized void throwLowestSuitable() {
		drop = trick.getTrickCards().stream()
				.filter(x -> trick.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		this.getPlayerHand().getCards().stream()
		.filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);


		if (suitableCards.isEmpty()) {
			this.getPlayerHand().throwExactCard(this.getPlayerHand().getCards().stream().max(compareByValue).get());
			this.setReady(true);
		}

		else { // There are cards in suitable Cards, we want to
				// throw lower than the drop value
			Collections.sort(suitableCards);

			ArrayList<Card> lessthandrop = new ArrayList<Card>();

			//suitableCards.stream().filter(x -> x.getValue() < drop.getValue()).map(lessthandrop::add); 
			suitableCards.forEach(x -> {
				if (x.getValue() < drop.getValue()) {
					lessthandrop.add(x);
				}
			});

			if (lessthandrop.isEmpty()) { // no card is smaller than the drop value, throw highest in suitableCards
	
				this.getPlayerHand().throwExactCard(suitableCards.get(suitableCards.size() - 1));
				this.setReady(true);
			}

			else {
				Collections.sort(lessthandrop); // throw the biggest in lessthandrop
												
				this.getPlayerHand().throwExactCard(lessthandrop.get(lessthandrop.size() - 1));
				this.setReady(true);
			}
			lessthandrop.clear(); // housekeeping!
		}
		
		suitableCards.clear(); // housekeeping!

	}


	private synchronized void emptyTrickwithHeartStateOff() {
		this.getPlayerHand().throwExactCard(suitableCards.stream().min(compareByValue).get());
		
		suitableCards.clear();

	}
	
	
	private synchronized void emptyTrickwithHeartStateOn() {
		this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS)
		.forEach(suitableCards::add);

		this.getPlayerHand().throwExactCard(suitableCards.stream().min(compareByValue).get());
		
		suitableCards.clear();
		

	}

	private synchronized void clubCheck() {
		this.getPlayerHand().getCards().forEach(x -> {
			if (x.equals(new Card(Suit.CLUBS, 2))) {
				this.getPlayerHand().throwExactCard(x);
				}
			});
	}
	
	

	public String getPlayerName() {
		return playerName;
	}


	public void setScore(int i) {
		playerScore += i;
	}


	public int getScore() {
		return playerScore;
	}


	public void incScore() {
		playerScore++;
	}


	public void decScore() {
		playerScore--;
	}

	public void resetScore() {
		playerScore = 0;
	}


	public int getPlayerId() {
		return playerId;
	}


	public Hand getPlayerHand() {
		return hand;
	}


	public String toString() {
		return "Player ID: " + playerId + " \t " + "Player: " + playerName + "\t Player Score: " + playerScore;
	}


	public ArrayList<Card> getTrickCardsWon() {
		return trickCardsWon;
	}


	public void addToTrickCardsWon(ArrayList<Card> tc) {
		trickCardsWon.addAll(tc);
	}


	public boolean ShotOverTheMoon() {
		return trickCardsWon.containsAll(sotm);
	}

	public ArrayList<Card> getsotm() {
		return sotm;
	}


	public void sortTrickCardsWon() {
		Collections.sort(trickCardsWon);
	}


	public boolean isReady() {
		return isReady;
	}


	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}



}
