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
//		Trick t = this.newTrick(); 
		this.incTrickNo(); this.incRound(); 
		
/*		while(queue.size() != 4) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.err.println("failed waiting for players to join");
				e.printStackTrace();
			} 
		}
//		queue.forEach(x -> x.start());
*/		
		queue.forEach(x -> x.getPlayerHand().addCards(d.dealCards()));
		System.out.println("Dealing Cards");
		
//		notifyAll(); 
		
		try {
			this.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		queue.forEach(x -> {
			Card c = x.getThrownCard().takeMVar();
			if (c.equals(new Card(Suit.CLUBS, 2))) {
				trick.addtoTrick(c);
				this.setTurn(x);
			}
		});
		
		this.incTurn();

		
		while(this.getTrickNo() != 2) {
			
		while(!trick.isTrickFull()) {
			this.setPlay(true);
			
			queue.forEach(x -> {
				if (x.getPlayerId() == this.getTurn()) {
					x.notify();
					
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
			
			this.incTurn();
			
		}
		
		this.setPlay(false);
		
		
		/* ----------------------------------Split Section------------------------------- */
		
		
		trick.calculateScore();

		
		this.setResults(true);

		queue.forEach(x -> {
			if (x.getPlayerHand().lastThrownCard() == trick.calculateWinner()) {
				trick.setWinner(x);
				//x.setScore(t.getScoreCount());
				x.getResult().putMVar(trick.getScoreCount());
				x.addToTrickCardsWon(trick.getTrickCards());
				this.setTurn(x);
			}
		}); //IM MODIFYING STATE! Let player add his own score! 
		
		this.setResults(false);
		
		trick.clearTrick();
		this.incTrickNo();
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
		turn = turn++ % 4; 
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
