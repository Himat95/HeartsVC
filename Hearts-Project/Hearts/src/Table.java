import java.util.concurrent.ArrayBlockingQueue;

public class Table extends Thread {

	private int round;
	private int trickNo;
	private int turn;
	private boolean heartStateOn;
	private Trick trick;
	private boolean gameFinished;
	private ArrayBlockingQueue<AIPlayer> queue;
	private boolean play;
	private boolean results;



	public Table(ArrayBlockingQueue<AIPlayer> queue2, Trick trick) {
		round = 0;
		trickNo = 0;
		heartStateOn = true;
		gameFinished = false;
		play = false;
		results = false;
		this.queue = queue2;
		this.trick = trick;
	}


	@Override
	public void run() {
		/*
		 * Check for players (4)
		 * shuffle and deal cards to each player
		 * increment trickno, and create a new trick
		 * notify all players to search for clubs (start game)
		 * increment turn variabe and notify that player
		 * check if trick is full,
		 * calculate winner and return the score
		 * new trick and increment trickno, check heart state
		 * notify winning players to start next turn.
		 */

		Deck d = new Deck();
		d.shuffle();

		this.incTrickNo(); this.incRound();


		queue.forEach(x -> x.getPlayerHand().addCards(d.dealCards()));
		System.out.println("Dealing Cards");


		try {
			this.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

/*		queue.forEach(x -> {System.out.println("test1");
			//Card c = x.getThrownCard().takeMVar();
			System.out.println("test");
			if (x.getPlayerHand().lastThrownCard().equals(new Card(Suit.CLUBS, 2))) {
				//trick.addtoTrick(c);
				this.setTurn(x);
			}
		});*/


		while(this.getTrickNo() != 4) {

			this.setPlay(true);
			if (trick.isTrickFull()) {
				synchronized (trick) {
					trick.calculateScore();
					trick.calculateWinner();
					this.setTurn(trick.getWinner());
					//this.incTrickNo();
					//trick.clearTrick();
					this.setResults(true);
				}
			}
			
				else {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

		}

/*		while(!trick.isTrickFull()) {
			this.setPlay(true);

			queue.forEach(x -> {
				if (x.getPlayerId() == this.getTurn()) {
					//x.notify();

					while(!x.isReady()) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							System.err.println("Table died! Nooo");
							e.printStackTrace();
						}
					}

					trick.addtoTrick(x.getThrownCard().takeMVar());
				}
			});

			//this.incTurn();

		}

		this.setPlay(false);

*/
		/* ----------------------------------Split Section------------------------------- */

/*
		if (this.isResults()) {
			trick.calculateScore();
			trick.calculateWinner();

		}*/




/*		queue.forEach(x -> {
			if (x.getPlayerHand().lastThrownCard() == trick.calculateWinner()) {
				trick.setWinner(x);
				//x.setScore(t.getScoreCount());
				x.getResult().putMVar(trick.getScoreCount());
				x.addToTrickCardsWon(trick.getTrickCards());
				this.setTurn(x);
			}
		}); //IM MODIFYING STATE! Let player add his own score!

		this.setResults(false);
*/
/*		trick.clearTrick();
		this.incTrickNo();
		}*/
		try {
			sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setIsGameFinished(true);
		System.out.println("Table turning lifeless...");

	}


	public Trick getTrick() {
		return trick;
	}

	public ArrayBlockingQueue<AIPlayer> getPlayerQueue() {
		return queue;
	}

	public int getRound() {
		return round;
	}

	public void incRound() {
		round++;
	}

	public void incTrickNo() {
		trickNo++;
	}

	public int getTrickNo() {
		return trickNo;
	}

	public void resetTrickNo() {
		trickNo = 0;
	}

	public void resetRound() {
		round = 0;
	}

	public void setTurn(AIPlayer x) {
		turn = x.getPlayerId();
	}

	public int getTurn() {
		return turn;
	}

	public void incTurn() {
		if (turn == 3) {
			turn = 0;
		}
		else { turn++; }
	}

	public void setHeartState(boolean b) {
		heartStateOn = b;
	}

	public boolean getHeartState() {
		return heartStateOn;
	}

	public void setIsGameFinished(boolean b) {
		gameFinished = b;
	}

	public boolean getIsGameFinished() {
		return gameFinished;
	}


	public boolean isPlay() {
		return play;
	}


	public void setPlay(boolean play) {
		this.play = play;
	}


	public boolean isResults() {
		return results;
	}


	public void setResults(boolean results) {
		this.results = results;
	}

}
