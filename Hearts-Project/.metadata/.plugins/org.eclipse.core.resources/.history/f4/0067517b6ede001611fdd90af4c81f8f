import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.stream.Stream;

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
		
		if (startstate == true) {
		this.getPlayerHand().getCards().forEach(x -> {
			if (x.equals(new Card(Suit.CLUBS, 2))) {
				this.getPlayerHand().throwExactCard(new Card(Suit.CLUBS, 2));
		}
			else { try {
				wait();
			} catch (Exception e) {
				System.err.println("Failed to wait (Clubs 2)");
				e.printStackTrace();
			} }
				});
		}
		
		else {
			

			if (trick.getTrickCards().isEmpty() == true) {
				//Write throwing code when its empty
			}
			else {
				
		switch (trick.getTrickCards().get(0).getSuit()) {
			case SPADES: suitableCards.addAll(Arrays.stream(this.hand.getCards().iterator().next().getSuit().getSuitValue()); break;
			case CLUBS: suitableCards.addAll(this.getPlayerHand().getCards()); break;
			case DIAMONDS: suitableCards.addAll(this.getPlayerHand().getCards()); break;
			default: suitableCards.addAll(this.getPlayerHand().getCards());
		}

		if (suitableCards.isEmpty()) {
			//THROW ERRORS!
		}

		else {
		Collections.sort(suitableCards);
		suitableCards.forEach(x -> {
			if (x -> x.getValue() < Table.this.getTrickNo()) //table card thrown){
					ArrayList<Card> sizeable = new ArrayList<Card>();
			Collections.sort(sizeable);
			sizeable.stream.forEachRemaining(x -> {
				.Filter(x < )
			}
		}



			}

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
