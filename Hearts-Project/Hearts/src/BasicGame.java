import java.util.ArrayList;

public class BasicGame {

	public static void main(String[] args) {
		
		Deck d = new Deck(); 
		Table t = new Table(); 
		ArrayList<Player> players = new ArrayList<>(); 
		//Player g = new AIPlayer(5); 
		for(int i = 0; i < 4; i++) {
			players.add(new AIPlayer(i)); 
		}
		
		players.forEach(x -> System.out.println(x.toString())); 
		d.shuffle();
		players.forEach(x -> x.getPlayerHand().addCards(d.dealCards()));
		
/*		players.get(0).getPlayerHand().addCards(d.dealCards());
		players.get(1).getPlayerHand().addCards(d.dealCards());
		players.get(2).getPlayerHand().addCards(d.dealCards());
		players.get(3).getPlayerHand().addCards(d.dealCards());*/
		System.out.println(d.cardsLeft());
		
		players.forEach(x -> x.getPlayerHand().sortHand());
		
		
		// --------------------------------Game Play--------------------------------------
		
		Trick tt = t.newTrick(); 
		
		for (int i = 0; i < 4; i++) {
			tt.addtoTrick(players.get(i).getPlayerHand().throwCard());
		}
		
		System.out.println(tt.toString());
		
		tt.calculateScore();
		
		tt.setWinner(players.get(0));
		
		for (int i = 0; i < 4; i++) {
			if (players.get(i).getPlayerId() == tt.getWinner()) {
				players.get(i).setScore(tt.getScoreCount());
			}
		}
		
		players.forEach(x -> System.out.println(x.getPlayerHand().getCards()));
		players.forEach(x -> System.out.println(x.toString()));
		
		

	}

}
