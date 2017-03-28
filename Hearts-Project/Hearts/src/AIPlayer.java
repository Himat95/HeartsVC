import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.stream.Collector;


public class AIPlayer extends Thread implements Player {

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



	public AIPlayer(String n, int id, Table table, Trick trick) {
		playerName = n;
		playerScore = 0;
		playerId = id;
		hand = new Hand();
		trickCardsWon = new ArrayList<Card>();
		this.table = table;
		isReady = false;
		this.trick = trick;


		for (int i = 2; i <= 14; i++) {
			sotm.add(new Card(Suit.HEARTS, i));
		}
		sotm.add(new Card(Suit.SPADES, 12));
		Collections.sort(sotm);
	}

	/*
	 * Don't forget the pass 3 cards to another AI at the start! This functionality is NEEDED!
	 */

	@Override
	public void run() {


		try {
			this.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.getPlayerHand().sortHand();

		System.out.println("Starting Game|  Player " + this.getPlayerId() + ": " + this.getPlayerHand().getCards());

		
		this.swap(); 
		
		this.clubCheck();


		//while (this.getPlayerHand().getCards().isEmpty() == false) {
			while (table.getIsGameFinished() == false) {
			//System.out.println(this.playerId + ": Phase 1");
				if (table.getTurn() == this.playerId && table.isPlay() == true && table.isResults() == false) {

					synchronized (trick) {

					//System.out.println(this.playerId + ": Phase 4");
					if (trick.getTrickCards().isEmpty() == true) {

						if(this.getPlayerHand().getCards().isEmpty()){
							break;
							}
						else{
						if (table.getHeartState() == true) { // Can't throw
																// hearts yet!
							System.out.println("Player: " + this.playerId + "\t Empty Trick with Hearts On");
							this.emptyTrickwithHeartStateOn();
						}

						else {
							System.out.println("Player: " + this.playerId + "\t Empty Trick with Hearts Off");
							this.emptyTrickwithHeartStateOff();
						}
						}

					}

					else {
						if(trick.isTrickFull()) {
							synchronized (trick) {
							System.out.println("Player: " + this.playerId + "\t Trick is full");
							table.setPlay(false);
							table.setResults(true);
							trick.calculateScore();
							trick.calculateWinner();
							table.setTurn(trick.getWinner());
							}
						}

						else {
							if(this.getPlayerHand().getCards().isEmpty()){
								break;
								}
							else {
							System.out.println("Player: " + this.playerId + "\t Throwing lowest suitable");
							this.throwLowestSuitable();
							}
						}
						//System.out.println(this.playerId + " Phase 5");
					} // greedy heuristic
					System.out.println("player: " + this.getPlayerId() + "\t Current Trick: " + trick.getTrickCards());
					this.heartCheck();
				}

				}// if table turn == this player

				
				if (table.isResults() == true) {
					System.out.println("Player: " + this.playerId + " Entered Results");
					//table.setPlay(false);
					while(table.isResults() == true) {
						try {
							System.out.println("Player: " + this.playerId + " Going to sleep");
							sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					if (this == trick.getWinner()) {
						System.out.println("Player: " + this.playerId + " is the trick winner... " + "Actual winner is... " + trick.getWinner().getPlayerId());
							synchronized (trick) {
						System.out.println("Player: " + this.playerId + " is getting Results...");
						this.setScore(trick.getScoreCount());
						System.out.println("Player: " + this.playerId + " score is... " + trick.getScoreCount());
						this.addToTrickCardsWon(trick.getTrickCards());
						System.out.println("Player: " + this.playerId + " score is... " + trick.getTrickCards());
						trick.clearTrick();
						table.incTrickNo();
						trick.resetScore();
						table.setResults(false);
						table.setPlay(true);
							}
						}
					else {
						try {
							System.out.println("Player: " + this.playerId + " Going to sleep using else");
							sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					}
					if(this.getPlayerHand().getCards().isEmpty())
					{break;}
				}


				//this.setReady(false);

//			}
				else {
			try {
				this.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
		} // Game is finished after this
			
			try {
				this.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


/*			if (this.ShotOverTheMoon()) {
				
			}*/
		this.printScore();

	}

	private void swap() {
		Card a, b, c;
		a = this.getPlayerHand().getCards().stream().max(compareByValue).get();
		b = this.getPlayerHand().getCards().stream().filter(x -> x !=a).max(compareByValue).get();
		c = this.getPlayerHand().getCards().stream().filter(x -> x !=b && x != a).max(compareByValue).get();
		
		System.out.println("Player: " + this.playerId + "swapping " + a + b + c);
		
		table.SwapCards(a, b, c, playerId);
				
		Iterator<Card> iter = this.getPlayerHand().getCards().iterator();
		while(iter.hasNext()) {
			Card x = iter.next();
			if (x.equals(a) || x.equals(b) || x.equals(c)) {
				iter.remove();
			}
		}
		
		try {
			this.sleep(1000*40);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		this.getPlayerHand().addCards(table.SwappedCards((this.playerId == 3) ? 0 : playerId+1));
		System.out.println("Player: " + this.playerId + "has " + this.getPlayerHand().getCards());
		
		try {
			this.sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	private void heartCheck() {
		trick.getTrickCards().forEach(x -> {
			if (x.getSuit() == Suit.HEARTS) {
				table.setHeartState(false);
			}
		});

	}
	private synchronized void printScore() {
		Collections.sort(this.getTrickCardsWon());
		System.out.println("Player " + this.playerId + ": has finished with the score of " + this.getScore());
		System.out.println("Player " + this.playerId + ": Trick Cards Won: " + this.getTrickCardsWon());
		System.out.println("Player " + this.playerId + ": Current Hand: " + this.getPlayerHand() + "\n");
	}

	private void throwLowestSuitable() {
		drop = trick.getTrickCards().stream()
				.filter(x -> trick.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		this.getPlayerHand().getCards().stream()
		.filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);



		if (suitableCards.isEmpty()) {
			//this.getThrownCard().putMVar(this.getPlayerHand().throwExactCard(this.getPlayerHand().getCards().stream().max(compareByValue).get()));
			//this.setReady(true);

			trick.addtoTrick(this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() == Suit.HEARTS).max(compareByValue).orElse(this.getPlayerHand().getCards().stream().max(compareByValue).get()), this);
			this.getPlayerHand().throwExactCard(this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() == Suit.HEARTS).max(compareByValue).orElse(this.getPlayerHand().getCards().stream().max(compareByValue).get()));
			table.incTurn();
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

				//this.getThrownCard().putMVar(this.getPlayerHand().throwExactCard(suitableCards.get(suitableCards.size() - 1)));
				//this.setReady(true);
				trick.addtoTrick(suitableCards.get(suitableCards.size() - 1), this);
				this.getPlayerHand().throwExactCard(suitableCards.get(suitableCards.size() - 1));
				table.incTurn();
			}

			else {
				Collections.sort(lessthandrop); // throw the biggest in lessthandrop

				//this.getThrownCard().putMVar(this.getPlayerHand().throwExactCard(lessthandrop.get(lessthandrop.size() - 1)));
				//this.setReady(true);
				trick.addtoTrick(lessthandrop.get(lessthandrop.size() - 1), this);
				this.getPlayerHand().throwExactCard(lessthandrop.get(lessthandrop.size() - 1));
				table.incTurn();
			}
			lessthandrop.clear(); // housekeeping!
		}

		suitableCards.clear(); // housekeeping!
		
	}


	private void emptyTrickwithHeartStateOff() {
		//this.getThrownCard().putMVar(this.getPlayerHand().throwExactCard(suitableCards.stream().min(compareByValue).get()));

		trick.addtoTrick(this.getPlayerHand().getCards().stream().min(compareByValue).get(), this);
		System.out.println(this.getPlayerId() + " " + this.getPlayerHand().getCards());
		this.getPlayerHand().throwExactCard(this.getPlayerHand().getCards().stream().min(compareByValue).get());
		//this.getPlayerHand().throwExactCard(suitableCards.stream().min(compareByValue).get());
		table.incTurn();

		suitableCards.clear();
		
	}


	private void emptyTrickwithHeartStateOn() {

		trick.addtoTrick(this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS).min(compareByValue).orElse(this.getPlayerHand().getCards().stream().min(compareByValue).get()), this);
		this.getPlayerHand().throwExactCard(this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS).min(compareByValue).get());
		//.forEach(suitableCards::add);

		//this.getThrownCard().putMVar(this.getPlayerHand().throwExactCard(suitableCards.stream().min(compareByValue).get()));
//		trick.addtoTrick(suitableCards.stream().min(compareByValue).get(), this);
//		this.getPlayerHand().throwExactCard(suitableCards.stream().min(compareByValue).get());
		table.incTurn();
//		suitableCards.clear();
		

	}

	private void clubCheck() {

		Iterator<Card> iter = this.getPlayerHand().getCards().iterator();

		while(iter.hasNext()) {

			Card c = iter.next();
			if (c.equals(new Card(Suit.CLUBS, 2))) {
				trick.addtoTrick(c, this);
				table.setTurn(this);
				table.incTurn();
				iter.remove();
				//this.getThrownCard().putMVar(c);
				System.out.println("Club check" + "Player" + this.getPlayerId() + ": " + this.getPlayerHand().getCards());
				System.out.println("Player : " + this.getPlayerId() + "\t Current Trick: " + trick.getTrickCards());
			}
		}
		/*this.getPlayerHand().getCards().forEach(x -> {
			if (x.equals(new Card(Suit.CLUBS, 2))) {
				//
				//this.getThrownCard().putMVar(x);

				trick.addtoTrick(x, this);
				table.setTurn(this);
				table.incTurn();
				//this.getPlayerHand().throwExactCard(x);
				System.out.println(trick.getTrickCards());

				}
			});
		*/


		//System.out.println("Club check" + "Player " + this.getPlayerId() + ": " + this.getPlayerHand().getCards());

/*		for(Card c :this.getPlayerHand().getCards())
		{
			if (c.equals(new Card(Suit.CLUBS, 2))) {
				iter.remove();
				this.getThrownCard().putMVar(c);
			}
		}*/
	}



	public String getPlayerName() {
		return playerName;
	}


	public void setScore(int i) {
		playerScore += i;
	}
	
	public void hardScoreReset(int i) {
		playerScore = i;
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
