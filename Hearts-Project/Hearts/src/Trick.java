import java.util.ArrayList;
import java.util.Collections;

public class Trick {

	private Player winner;
	private ArrayList<Card> trickCards;
	private Card queenSpade;
	private ArrayList<Card> heartCards;
	private int scoreCount;
	private ArrayList<Pair<Card, Player>> playerlist;


	public Trick() {
		trickCards = new ArrayList<Card>();
		queenSpade = new Card(Suit.SPADES, 12);
		heartCards = new ArrayList<Card>();
		playerlist = new ArrayList<Pair<Card, Player>>();
		scoreCount = 0;

		for (int i = 2; i < 15; i++) {
			heartCards.add(new Card(Suit.HEARTS, i));
		}
	}


	public boolean isTrickFull() {
		return trickCards.size() == 4;
	}

	public void setWinner(Player player) {
		winner = player;
	}

	public Player getWinner() {
		//int n = winner.getPlayerId();
		return winner;
	}

	public void sortTrick() {
		Collections.sort(trickCards);
	}

	public ArrayList<Card> getTrickCards() {
		return trickCards;
	}

	public void addtoTrick(Card c, Player x) {
		trickCards.add(c);
		playerlist.add(new Pair<Card, Player>(c, x));
	}

	public void calculateScore() {

		trickCards.iterator().forEachRemaining(x -> {
			if (x.equals(queenSpade)) {
				scoreCount += 13;
				System.out.println(x);
			}
		});

		trickCards.forEach(x -> {
			if (x.getSuit().getSuitValue() == 3) {
				scoreCount += 1;
			}
		});
	}

	public void calculateWinner() {
		ArrayList<Card> winningCards = new ArrayList<>();

		trickCards.iterator().forEachRemaining(x -> {
			if (x.getSuit() == trickCards.get(0).getSuit()) {
				winningCards.add(x);
			}
		});

		Collections.sort(winningCards);

		playerlist.forEach(x -> {
			if (x.fst() ==  winningCards.get(winningCards.size()-1)) {
				this.setWinner(x.snd());
			}
		});
		//return winningCards.get(winningCards.size()-1);
	}

	public int getScoreCount() {
		return scoreCount;
	}

	public void setScoreCount(int i) {
		scoreCount = i;
	}

	public void resetScore() {
		scoreCount = 0;
	}

	public void clearTrick() {
		trickCards.clear();
		playerlist.clear();
	}

	public String toString() {
		return "Current Trick has: " + trickCards.toString();
	}

}
