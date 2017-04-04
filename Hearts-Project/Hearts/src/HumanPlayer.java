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
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.getPlayerHand().sortHand();

		System.out.println("Starting Game|  Your Cards: " + this.getPlayerHand().getCards() + "\n");

		this.swap();
		
		this.clubCheck();

		


			while (table.getIsGameFinished() == false) {

				if (table.getTurn() == this.playerId && table.isPlay() == true && table.isResults() == false) {
					

					if(trick.isTrickFull()) {
						synchronized (trick) {
						System.out.println("\t \t \t \t The trick is full! " + trick.getTrickCards() + "\n");
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
							System.out.println("\t Please choose a card, Jack(11), Queen(12), King(13), Ace(14)... use format '12 C' or '12 c' or '12 Clubs' or '12 clubs' \n");
							System.out.println("Your Cards: " + this.getPlayerHand().getCards() + "\n");
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
						System.out.println("Please put in a valid choice! \n ");
						break;
						}

						
						if (trick.getTrickCards().isEmpty()) {
							if (table.getHeartState()) {
								if (c.getSuit() == Suit.HEARTS) {
									reading = false;
									System.out.println("You are not allowed to throw Hearts Cards Yet! \n");
								}
								
								else {
									Iterator<Card> iter = this.getPlayerHand().getCards().iterator();

									while(iter.hasNext()) {
										Card x = iter.next();
										if (x.equals(c)) {
											trick.addtoTrick(x, this);
											iter.remove();
											reading = true;
											System.out.println("Throwing card... " + c + "\n");
										}
									}
									if (reading == false) {
										System.out.println("Invalid Card! \n");
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
										System.out.println("Throwing card... " + c + "\n");
									}
								}
								if (reading == false) {
									System.out.println("Invalid Card! \n ");
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
									System.out.println("Please use the same suit Card as the first card thrown in the trick! \n");
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
										System.out.println("Invalid Card! \n");
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
									System.out.println("Invalid Card! \n");
								}							
							}
						}

						}
						reading = false;
						System.out.println("Player: " + this.playerName + " added to the trick " + "\t Current Trick: " + trick.getTrickCards() + "\n");
						table.incTurn();
						if (table.getHeartState() == true) {
							this.heartCheck();
							}
						
					}
					
					
					}
					}



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
				System.out.println("\t Please input 3 Cards for swapping, input style: '13 C 14 D 12 H' or you can press Enter after each input '5 C'\n\n");
				System.out.println("Your Cards: " + this.getPlayerHand().getCards() + "\n");

				Integer i = sc.nextInt();
				String s = sc.next();

				Integer i2 = sc.nextInt();
				String s2 = sc.next();

				Integer i3 = sc.nextInt();
				String s3 = sc.next();

				a = this.constructor(i, s, a);
				b = this.constructor(i2, s2, b);
				d = this.constructor(i3, s3, d);

				System.out.println("Swapping... " + a + ", "  + b + ", " + d + "\n");

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
					System.out.println("Please select your OWN cards, no cheating!");
				}

				if (count == 3) {
					reading2 = true;
				}

				else {
					reading2 = false;
				}

				if (reading2 == false) {
					System.out.println("Invalid cards, please re-select!");
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
