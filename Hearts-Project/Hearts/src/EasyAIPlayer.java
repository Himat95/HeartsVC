import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class EasyAIPlayer extends Thread implements Player {
	
	private String playerName;
	private int playerScore;
	private int playerId;
	private Hand hand;
	private ArrayList<Card> trickCardsWon;
	private ArrayList<Card> suitableCards = new ArrayList<Card>();;
	private ArrayList<Card> sotm = new ArrayList<>();
	private Trick trick;
	private Table table;
	private Card drop;

	public static Comparator<Card> compareByValue = Comparator.comparingInt(Card::getValue);


	public EasyAIPlayer(String n, int id, Table table, Trick trick) {
		playerName = n;
		playerScore = 0;
		playerId = id;
		hand = new Hand();
		trickCardsWon = new ArrayList<Card>();
		this.table = table;
		this.trick = trick;


		for (int i = 2; i <= 14; i++) {
			sotm.add(new Card(Suit.HEARTS, i));
		}
		sotm.add(new Card(Suit.SPADES, 12));
		Collections.sort(sotm);
	}



	@Override
	public void run() {

		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.getPlayerHand().sortHand();

		//System.out.println("Starting Game|  Player " + this.getPlayerId() + ": " + this.getPlayerHand().getCards());

		
		this.swap();

		this.clubCheck();


		while (table.getIsGameFinished() == false) {
			
			if (table.getTurn() == this.playerId && table.isPlay() == true && table.isResults() == false) {

				synchronized (trick) {

					if (trick.getTrickCards().isEmpty() == true) {
						if (this.getPlayerHand().getCards().isEmpty()) { break; } 
						
						else {
							if (table.getHeartState() == true) { // Can't throw Hearts yet!										
								//System.out.println("Player: " + this.playerId + "\t Empty Trick with Hearts On");
								this.emptyTrickwithHeartStateOn();
								System.out.println("Player: " + this.playerName + " added to the trick " + "\t Current Trick: " + trick.getTrickCards() + "\n");
							}
							else { //Can throw Hearts
								//System.out.println("Player: " + this.playerId + "\t Empty Trick with Hearts Off");
								this.emptyTrickwithHeartStateOff();
								System.out.println("Player: " + this.playerName + " added to the trick " + "\t Current Trick: " + trick.getTrickCards() + "\n");
							}
						}

					}

					else {
						if (trick.isTrickFull()) {
								//System.out.println("Player: " + this.playerId + "\t Trick is full");
								table.setPlay(false);
								table.setResults(true);
								trick.calculateScore();
								trick.calculateWinner();
								table.setTurn(trick.getWinner());
								System.out.println("\t \t \t \t The trick is full! " + trick.getTrickCards() + "\n");
						}

						else {
							if (this.getPlayerHand().getCards().isEmpty()) { break; } 
							else {
								//System.out.println("Player: " + this.playerId + "\t Throwing lowest suitable");
								this.throwLowestSuitable();
								System.out.println("Player: " + this.playerName + " added to the trick " + "\t Current Trick: " + trick.getTrickCards() + "\n");
							}
						}
					} 
					//System.out.println("player: " + this.getPlayerId() + "\t Current Trick: " + trick.getTrickCards());
					//System.out.println("Player: " + this.playerName + " added to the trick " + "\t Current Trick: " + trick.getTrickCards() + "\n");
					if (table.getHeartState() == true) {
					this.heartCheck();
					}
				}

			} // if table turn == this player

			if (table.isResults() == true) {
				//System.out.println("Player: " + this.playerId + " Entered Results");
				
				while (table.isResults() == true) {
					try {
						//System.out.println("Player: " + this.playerId + " Going to sleep");
						sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if (this == trick.getWinner()) {
						//System.out.println("Player: " + this.playerId + " is the trick winner... Actual winner is... " + trick.getWinner().getPlayerId());
						synchronized (trick) {
							//System.out.println("Player: " + this.playerId + " is getting Results...");
							System.out.println("Player: " + playerName + " has won the trick!");
							this.setScore(trick.getScoreCount());
							System.out.println("Player: " + playerName + " gained the score of " + trick.getScoreCount());
							this.addToTrickCardsWon(trick.getTrickCards());
							System.out.println("Player: " + playerName + " has won the trick cards : " + trick.getTrickCards() + "\n \n \n");
							trick.clearTrick();
							table.incTrickNo();
							trick.resetScore();
							table.setResults(false);
							table.setPlay(true);
							
							if (table.getTrickNo() < 13){
								System.out.println("<---------------------------------------------------------Trick " + table.getTrickNo() + " has started! ----------------------------------------------------------->\n\n");
							}
							else {
								System.out.println("<------------------------------------------------------------------Last trick!-------------------------------------------------------------------->\n\n");
							}
							}
					} else {
						try {
							//System.out.println("Player: " + this.playerId + " Going to sleep using else");
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if (this.getPlayerHand().getCards().isEmpty()) { break; }
			}

			else {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} // Game is finished after this

		try {
			sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		this.printScore();

	}

	private void swap() {
		Card a, b, c;
		a = this.getPlayerHand().getCards().stream().max(compareByValue).get();
		b = this.getPlayerHand().getCards().stream().filter(x -> x !=a).max(compareByValue).get();
		c = this.getPlayerHand().getCards().stream().filter(x -> x !=b && x != a).max(compareByValue).get();
		
		synchronized (table) {
			
		//System.out.println("Player: " + this.playerId + "swapping [" + a + ", " + b + ", " + c + "]");
		
		table.SwapCards(a, b, c, playerId);
				
		Iterator<Card> iter = this.getPlayerHand().getCards().iterator();
		while(iter.hasNext()) {
			Card x = iter.next();
			if (x.equals(a) || x.equals(b) || x.equals(c)) {
				iter.remove();
			}
		}
		}
		
		try {
			sleep(1000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
			
		
		synchronized (table) {
		this.getPlayerHand().addCards(table.SwappedCards((this.playerId == 0) ? 3 : playerId-1));
		this.getPlayerHand().sortHand();
		//System.out.println("Player: " + this.playerId + "has " + this.getPlayerHand().getCards());
		}
		
		
		
		try {
			sleep(2000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
	}
	
	private void heartCheck() {
		trick.getTrickCards().forEach(x -> {
			if (x.getSuit() == Suit.HEARTS) {
				table.setHeartState(false);
				System.out.println("\t \t \t \t \t  Hearts have been broken! \t \t Players can now use Hearts suit cards! \n \n \n");
			}
		});

	}
	private synchronized void printScore() {
		synchronized (trick) {
			
		
		Collections.sort(this.getTrickCardsWon());
		System.out.println("Player " + playerName + ": has finished with the score of " + this.getScore());
		System.out.println("Player " + playerName + ": Trick Cards Won: " + this.getTrickCardsWon());
		System.out.println("Player " + playerName + ": Current Hand: " + this.getPlayerHand() + "\n");
		}
	}

	
	private void throwLowestSuitable() {
		drop = trick.getTrickCards().stream()
				.filter(x -> trick.getTrickCards().get(0).getSuit() == x.getSuit())
				.max(compareByValue).get();

		this.getPlayerHand().getCards().stream()
		.filter(x -> x.getSuit() == drop.getSuit()).forEach(suitableCards::add);



		if (suitableCards.isEmpty()) {

			Card rand = this.getPlayerHand().getCards().stream().findAny().get();
			trick.addtoTrick(rand, this);
			this.getPlayerHand().removeCard(rand);
			table.incTurn();
		}

		else {
			Collections.sort(suitableCards);

			Card rand = suitableCards.stream().findAny().get();
			trick.addtoTrick(rand, this);
			this.getPlayerHand().removeCard(rand);
			table.incTurn();
			}
		suitableCards.clear(); // housekeeping!
		
	}


	private void emptyTrickwithHeartStateOff() {

		Card rand = this.getPlayerHand().getCards().stream().findAny().get();
		trick.addtoTrick(rand, this);
		this.getPlayerHand().removeCard(rand);
		table.incTurn();
		
	}


	private void emptyTrickwithHeartStateOn() {

		Card rand = this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS).findAny()
				.orElse(this.getPlayerHand().getCards().stream().findAny().get());
		trick.addtoTrick(rand, this);
		this.getPlayerHand().removeCard(rand);
		table.incTurn();

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
				System.out.println("Player: " + playerName + "\t Current Trick: " + trick.getTrickCards() + "\n");
				//System.out.println("Club check" + "Player" + this.getPlayerId() + ": " + this.getPlayerHand().getCards());
				//System.out.println("Player : " + this.getPlayerId() + "\t Current Trick: " + trick.getTrickCards());
			}
		}

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









}
