import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.stream.Stream;
import java.util.function.*;

public class AIPlayer extends Thread implements Player{

	private String playerName;
	private int playerScore;
	private int playerId;
	private Hand hand;
	private ArrayList<Card> trickCardsWon;
	private ArrayList<Card> suitableCards = new ArrayList<Card>();;
	private ArrayList<Card> sotm = new ArrayList<>();
	private boolean startstate = false; 
	private Trick trick;
	private Table table;
	private Card drop;


	public AIPlayer(int id) {
		playerName = "";
		playerScore = 0;
		playerId = id;
		hand = new Hand();
		trickCardsWon = new ArrayList<Card>();


		for (int i = 2; i <= 14; i++) {
			sotm.add(new Card(Suit.HEARTS, i));
		}
		sotm.add(new Card(Suit.SPADES, 12));
		Collections.sort(sotm);
	}

	
	public void run() {

		while (table.getIsGameFinished() == false) {

			if (startstate == true) {
				this.getPlayerHand().getCards().forEach(x -> {
					if (x.equals(new Card(Suit.CLUBS, 2))) {
						this.getPlayerHand().throwExactCard(new Card(Suit.CLUBS, 2));
					} else {
						try {
							wait();
						} catch (Exception e) {
							System.err.println("Failed to wait (Clubs 2)");
							e.printStackTrace();
						}
					}
				});
			}

			else {

				if (table.getTurn() == this.playerId) {

					if (trick.getTrickCards().isEmpty() == true) {
						// Write throwing code when its empty
					}

					else {
						drop = trick.getTrickCards().get(0);

						switch (drop.getSuit()) {
						case SPADES:
							hand.getCards().forEach(x -> {
								if (x.getSuit() == Suit.SPADES)
									suitableCards.add(x);
							});
							break;

						case CLUBS:
							hand.getCards().forEach(x -> {
								if (x.getSuit() == Suit.CLUBS)
									suitableCards.add(x);
							});
							break;
						case DIAMONDS:
							hand.getCards().forEach(x -> {
								if (x.getSuit() == Suit.DIAMONDS)
									suitableCards.add(x);
							});
							break;
						default:
							hand.getCards().forEach(x -> {
								if (x.getSuit() == Suit.HEARTS)
									suitableCards.add(x);
							});
							break;

						}

						// Highest value from the trick hard which is equal to
						// the suit
						for (int i = 0; i < trick.getTrickCards().size(); i++) {
							if (trick.getTrickCards().get(i).getValue() >= drop.getValue()
									&& drop.getSuit() == trick.getTrickCards().get(i).getSuit()) {
								drop = trick.getTrickCards().get(i);
							}
						}

						if (suitableCards.isEmpty()) {
							// Provide alternative!
						}

						else {
							Collections.sort(suitableCards);

							ArrayList<Card> sizeable = new ArrayList<Card>();

							suitableCards.forEach(x -> {
								if (x.getValue() < drop.getValue()) {
									sizeable.add(x);
								}
							});

							Collections.sort(sizeable);

							this.getPlayerHand().throwExactCard(sizeable.get(sizeable.size() - 1));
						}

					}

				}

				else {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			
			//write collecting results here
		}
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
		playerScore += i;
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
	public void resetScore() {
		playerScore = 0;
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
	public Hand getPlayerHand() {
		return hand;
	}

	@Override
	public String toString() {
		return "Player ID: " + playerId + " \t " + "Player: " + playerName + "\t Player Score: " + playerScore;
	}

	@Override
	public ArrayList<Card> getTrickCardsWon() {
		return trickCardsWon;
	}

	@Override
	public void addToTrickCardsWon(ArrayList<Card> tc) {
		trickCardsWon.addAll(tc);
	}

	@Override
	public boolean ShotOverTheMoon() {
		return trickCardsWon.containsAll(sotm);
	}

	public ArrayList<Card> getsotm() {
		return sotm;
	}

	@Override
	public void sortTrickCardsWon() {
		Collections.sort(trickCardsWon);
	}



}
