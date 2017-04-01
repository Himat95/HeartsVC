import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;

public class Table extends Thread {

	private int round;
	private int trickNo;
	private int turn;
	private boolean heartStateOn;
	private Trick trick;
	private boolean gameFinished;
	private ArrayBlockingQueue<Player> queue;
	private boolean play;
	private boolean results;
	private int count;
	private Comparator<Player> comp = (x1 ,x2) -> Integer.compare(x1.getScore(), x2.getScore());
	private ArrayList<Pair<ArrayList<Card>, Integer>> swappedCards; 



	public Table(ArrayBlockingQueue<Player> queue2, Trick trick) {
		round = 0;
		trickNo = 0;
		heartStateOn = true;
		gameFinished = false;
		play = false;
		results = false;
		this.queue = queue2;
		this.trick = trick;
		count=0;
		swappedCards = new ArrayList<Pair<ArrayList<Card>, Integer>>(); 
		turn = 5; 
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

		this.setPlay(true);
		
		while (this.getTrickNo() !=14) {
			try {
				this.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		System.out.println("Breaking loop");
		queue.forEach(x -> {
			if (x.ShotOverTheMoon()) {
				queue.iterator().forEachRemaining(x2 -> x2.hardScoreReset(26));
				x.resetScore();
				System.out.println("Player: " + x.getPlayerId() + " " +
				x.getPlayerName() + " HAS SHOT OVER THE MOON!");
			}
		});
		try {
			this.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		synchronized (trick) {
		this.setIsGameFinished(true);
		System.out.println("Winner is... " + queue.stream().min(comp).get());
		}

	}


	public synchronized void SwapCards(Card a, Card b, Card c, int i) {
		ArrayList<Card> temp = new ArrayList<>();  

		temp.addAll(Arrays.asList(a, b, c)); 
		swappedCards.add(new Pair<ArrayList<Card>, Integer>(temp, i)); 
		
	}
	
	public synchronized ArrayList<Card> SwappedCards(int i) {
		ArrayList<Card> temp = new ArrayList<Card>(); 
		
		swappedCards.forEach(x -> {
			if (x.snd().intValue() == i) {
				 temp.addAll(x.fst());
			}
		});
		return temp;
		//return swappedCards.stream().filter(x -> i == x.snd()).map(x -> x.fst()).toArray();
	}
	
	public Trick getTrick() {
		return trick;
	}

	public ArrayBlockingQueue<Player> getPlayerQueue() {
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

	public void setTurn(Player x) {
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
