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
	private boolean isReady;
	private Trick trick;
	private Table table;
	private Card drop;
	private MVar<Card> throwingCard;
	private MVar<Integer> result;
	private Card thrownCard;


	public static Comparator<Card> compareByValue = Comparator.comparingInt(Card::getValue);



	public EasyAIPlayer(String n, int id, Table table, Trick trick) {
		playerName = n;
		playerScore = 0;
		playerId = id;
		hand = new Hand();
		trickCardsWon = new ArrayList<Card>();
		this.table = table;
		isReady = false;
		throwingCard = new MVar<Card>();
		result = new MVar<Integer>();
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
			this.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.getPlayerHand().sortHand();

		System.out.println("Starting Game|  Player " + this.getPlayerId() + ": " + this.getPlayerHand().getCards());

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

							e.printStackTrace();
						}
					}
					}
					if(this.getPlayerHand().getCards().isEmpty())
					{break;}
				}


	
				else {
			try {
				this.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
				}
		} // Game is finished after this
			
			try {
				this.sleep(4000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

		this.printScore();

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

			Card rand = this.getPlayerHand().getCards().stream().findAny().get();
			trick.addtoTrick(rand, this);
			this.getPlayerHand().throwExactCard(rand);
			table.incTurn();
		}

		else { // There are cards in suitable Cards, we want to
				// throw lower than the drop value
			Collections.sort(suitableCards);

			Card rand = suitableCards.stream().findAny().get();
			trick.addtoTrick(rand, this);
			this.getPlayerHand().throwExactCard(rand);
			table.incTurn();
			}
		suitableCards.clear(); // housekeeping!
		
	}


	private void emptyTrickwithHeartStateOff() {

		Card rand = this.getPlayerHand().getCards().stream().findAny().get();
		trick.addtoTrick(rand, this);
		//System.out.println(this.getPlayerId() + " " + this.getPlayerHand().getCards());
		this.getPlayerHand().throwExactCard(rand);
		table.incTurn();

		suitableCards.clear();
		
	}


	private void emptyTrickwithHeartStateOn() {

		Card rand = this.getPlayerHand().getCards().stream().filter(x -> x.getSuit() != Suit.HEARTS).findAny().orElse(this.getPlayerHand().getCards().stream().findAny().get());
		trick.addtoTrick(rand, this);
		this.getPlayerHand().throwExactCard(rand);
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
				//this.getThrownCard().putMVar(c);
				System.out.println("Club check" + "Player" + this.getPlayerId() + ": " + this.getPlayerHand().getCards());
				System.out.println("Player : " + this.getPlayerId() + "\t Current Trick: " + trick.getTrickCards());
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
