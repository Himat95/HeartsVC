import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

public class HumanPlayer extends Thread implements Player {

	private String playerName;
	private int playerScore;
	private int playerId;
	private Hand hand;
	private ArrayList<Card> trickCardsWon;
	private ArrayList<Card> sotm = new ArrayList<>();
	private Trick trick;
	private Table table;
	private Card c;
	private boolean reading = false;
	public static Comparator<Card> compareByValue = Comparator.comparingInt(Card::getValue);
	private Scanner sc;
	Card a = null, b = null, d = null;
	boolean constraint;



	public HumanPlayer(String n, int id, Table table, Trick trick) {
		playerName = n;
		playerScore = 0;
		playerId = id;
		hand = new Hand();
		trickCardsWon = new ArrayList<Card>();
		this.table = table;
		this.trick = trick;
		sc = new Scanner(System.in);


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

		this.swap();
		
		this.clubCheck();

		

		//while (this.getPlayerHand().getCards().isEmpty() == false) {
			while (table.getIsGameFinished() == false) {
			//System.out.println(this.playerId + ": Phase 1");
				if (table.getTurn() == this.playerId && table.isPlay() == true && table.isResults() == false) {
					

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
					synchronized (trick) {

					if(this.getPlayerHand().getCards().isEmpty()) {
						break;
					}
						
						while (reading != true) {
							System.out.println("Player: " + this.getPlayerId() + "Please choose a card, use format '12 C' or '4 C' or '14 C'");
							System.out.println("Your Cards: " + this.getPlayerHand().getCards());
							Integer i = sc.nextInt();
							String s = sc.next();
							


						switch(s) {
						case ("C"): c = new Card(Suit.CLUBS, i); break;
						case ("S"): c = new Card(Suit.SPADES, i); break;
						case ("H"): c = new Card(Suit.HEARTS, i); break;
						case ("D"): c = new Card(Suit.DIAMONDS, i); break;
						case ("c"): c = new Card(Suit.CLUBS, i); break;
						case ("s"): c = new Card(Suit.SPADES, i); break;
						case ("h"): c = new Card(Suit.HEARTS, i); break;
						case ("d"): c = new Card(Suit.DIAMONDS, i); break;
						case ("Clubs"): c = new Card(Suit.CLUBS, i); break;
						case ("Spades"): c = new Card(Suit.SPADES, i); break;
						case ("Hearts"): c = new Card(Suit.HEARTS, i); break;
						case ("Diamonds"): c = new Card(Suit.DIAMONDS, i); break;
						case ("clubs"): c = new Card(Suit.CLUBS, i); break;
						case ("spades"): c = new Card(Suit.SPADES, i); break;
						case ("hearts"): c = new Card(Suit.HEARTS, i); break;
						case ("diamonds"): c = new Card(Suit.DIAMONDS, i); break;
						default: reading = false;
						System.out.println("Please put in a valid choice!");
						break;
						}

/*						this.getPlayerHand().getCards().forEach(x -> {
							if (x.equals(c)) {
								trick.addtoTrick(x, this);
								this.getPlayerHand().throwExactCard(x);
								reading = true;
								}
						});
						*/
						
						if (trick.getTrickCards().isEmpty()) {
							if (table.getHeartState()) {
								if (c.getSuit() == Suit.HEARTS) {
									reading = false;
									System.out.println("Your not allowed to throw Hearts Cards Yet!");
								}
								
								else {
									Iterator<Card> iter = this.getPlayerHand().getCards().iterator();

									while(iter.hasNext()) {
										Card x = iter.next();
										if (x.equals(c)) {
											trick.addtoTrick(x, this);
											iter.remove();
											reading = true;
											System.out.println("Throwing card... " + c);
										}
									}
									if (reading == false) {
										System.out.println("Invalid Card!");
									}
								}
								
							}
							
							else {
								Iterator<Card> iter = this.getPlayerHand().getCards().iterator();

								while(iter.hasNext()) {
									Card x = iter.next();
									if (x.equals(c)) {
										trick.addtoTrick(x, this);
										iter.remove();
										reading = true;
										System.out.println("Throwing card... " + c);
									}
								}
								if (reading == false) {
									System.out.println("Invalid Card!");
								}
							}
						}
						
						else {
							long num = this.getPlayerHand().getCards().stream()
									.filter(x -> x.getSuit() == trick.getTrickCards().get(0).getSuit()).count();
					
							if (num == 0) {
								constraint = false;
							} else {
								constraint = true;
							}
							
							if (constraint == true) {
								if (c.getSuit() != trick.getTrickCards().get(0).getSuit()) {
									reading = false; 
									System.out.println("Please use the same suit Card as the first card thrown in the trick!");
								}
								
								else {
									Iterator<Card> iter = this.getPlayerHand().getCards().iterator();

									while(iter.hasNext()) {
										Card x = iter.next();
										if (x.equals(c)) {
											trick.addtoTrick(x, this);
											iter.remove();
											reading = true;
										}
									}
									if (reading == false) {
										System.out.println("Invalid Card!");
									}
								}
							}
							
							else {
								Iterator<Card> iter = this.getPlayerHand().getCards().iterator();

								while(iter.hasNext()) {
									Card x = iter.next();
									if (x.equals(c)) {
										trick.addtoTrick(x, this);
										iter.remove();
										reading = true;
									}
								}
								if (reading == false) {
									System.out.println("Invalid Card!");
								}							
							}
						}

						}
						reading = false;
						System.out.println("player: " + this.getPlayerId() + "\t Current Trick: " + trick.getTrickCards());
						this.heartCheck();
						table.incTurn();
					}
					}



						//System.out.println(this.playerId + " Phase 5");
					} // greedy heuristic



				// if table turn == this player


				if (table.isResults() == true) {
					System.out.println("Player: " + this.playerId + " Entered Results");

					while(table.isResults() == true) {
						try {
							System.out.println("Player: " + this.playerId + " Going to sleep");
							sleep(1000);
						} catch (InterruptedException e1) {
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

		}
			
			try {
				this.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		this.printScore();

	}




	private Card constructor(int i, String s, Card c) {
		switch(s) {
		case ("C"): c = new Card(Suit.CLUBS, i); break;
		case ("S"): c = new Card(Suit.SPADES, i); break;
		case ("H"): c = new Card(Suit.HEARTS, i); break;
		case ("D"): c = new Card(Suit.DIAMONDS, i); break;
		case ("c"): c = new Card(Suit.CLUBS, i); break;
		case ("s"): c = new Card(Suit.SPADES, i); break;
		case ("h"): c = new Card(Suit.HEARTS, i); break;
		case ("d"): c = new Card(Suit.DIAMONDS, i); break;
		case ("Clubs"): c = new Card(Suit.CLUBS, i); break;
		case ("Spades"): c = new Card(Suit.SPADES, i); break;
		case ("Hearts"): c = new Card(Suit.HEARTS, i); break;
		case ("Diamonds"): c = new Card(Suit.DIAMONDS, i); break;
		case ("clubs"): c = new Card(Suit.CLUBS, i); break;
		case ("spades"): c = new Card(Suit.SPADES, i); break;
		case ("hearts"): c = new Card(Suit.HEARTS, i); break;
		case ("diamonds"): c = new Card(Suit.DIAMONDS, i); break;
		default: reading = false;
		System.out.println("Please put in a valid choice!");
		break;
		}
		return c;
	}

	private void swap() {
		
		synchronized (table) {
			
		boolean reading2 = false; 
		int count = 0; 
		
			while (reading2 != true) {
				reading2 = false;
				count = 0;
				System.out.println(
						"Please input for swap 3 Cards for swapping, press return key (Enter) after each input e.g. '12 c' or 13' C\n");
				System.out.println("Your Cards: " + this.getPlayerHand().getCards());

				Integer i = sc.nextInt();
				String s = sc.next();

				Integer i2 = sc.nextInt();
				String s2 = sc.next();

				Integer i3 = sc.nextInt();
				String s3 = sc.next();

				a = this.constructor(i, s, a);
				b = this.constructor(i2, s2, b);
				d = this.constructor(i3, s3, d);

				System.out.println("Player: " + this.playerId + "swapping " + a + b + d);

				Iterator<Card> iter = this.getPlayerHand().getCards().iterator();

				if (a != null && b != null && d != null) {

					while (iter.hasNext()) {
						Card x = iter.next();
						if (x.equals(a) || x.equals(b) || x.equals(d)) {
							count++;
						}
					}
				}

				else {
					System.out.println("Please select YOUR OWN cards, no cheating!");
				}

				if (count == 3) {
					reading2 = true;
					System.out.println(count);
				}

				else {
					reading2 = false;
					System.out.println(count);
				}

				if (reading2 == false) {
					System.out.println("Invalid Cards, please re-select!");
				}
			}

			table.SwapCards(a, b, d, playerId);

			Iterator<Card> iter = this.getPlayerHand().getCards().iterator();
			while (iter.hasNext()) {
				Card x = iter.next();
				if (x.equals(a) || x.equals(b) || x.equals(d)) {
					iter.remove();
				}
			}

		}
		try {
			this.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		synchronized (table) {
			this.getPlayerHand().addCards(table.SwappedCards((this.playerId == 0) ? 3 : playerId - 1));
			this.getPlayerHand().sortHand();
			System.out.println("Player: " + this.playerId + "has " + this.getPlayerHand().getCards());
		}
		try {
			this.sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
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
				System.out.println("Club check" + "Player " + this.getPlayerId() + ": " + this.getPlayerHand().getCards());
				System.out.println("Player : " + this.getPlayerId() + "\t Current Trick: " + trick.getTrickCards());
			}
		}

	}

	private void heartCheck() {
		trick.getTrickCards().forEach(x -> {
			if (x.getSuit() == Suit.HEARTS) {
				table.setHeartState(false);
			}
		});

	}
	private void printScore() {
		synchronized (trick) {
			
		
		System.out.println("Player " + this.playerId + ": has finished with the score of " + this.getScore());
		System.out.println("Player " + this.playerId + ": Trick Cards Won: " + this.getTrickCardsWon());
		System.out.println("Player " + this.playerId + ": Current Hand: " + this.getPlayerHand() + "\n");
		}
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
	
	public void hardScoreReset(int i) {
		playerScore = i;
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
