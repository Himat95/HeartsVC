import java.util.concurrent.ArrayBlockingQueue;

public class HeartsGame {

	public static void main(String[] args) {
		
		//--------------You are advised to change this value as it adjusts the difficulty, 1 = Hard and 2 = Easy. ---------------------------
		int s = 1; 
		
		ArrayBlockingQueue<Player> queue = new ArrayBlockingQueue<Player>(4);

		
		Trick trick = new Trick();
		Table table = new Table(queue, trick);
		
		
		if (s == 1) {
			Player p = new HumanPlayer("You", 0, table, trick);
			Player p2 = new AIPlayer("Raj", 1, table, trick);
			Player p3 = new AIPlayer("Sunny", 2, table, trick);
			Player p4 = new AIPlayer("Tarun", 3, table, trick);

			queue.add(p); queue.add(p2); queue.add(p3); queue.add(p4);

			System.out.println("You have selected Hard Mode");
			table.start();

			p.start();
			p2.start();
			p3.start();
			p4.start();
		}
		
		else if (s == 2) {
				Player p1 = new HumanPlayer("You", 0, table, trick);
				Player ep1 = new EasyAIPlayer("Raj", 1, table, trick);
				Player ep2 = new EasyAIPlayer("Sunny", 2, table, trick);
				Player ep3 = new EasyAIPlayer("Tarun", 3, table, trick);

				queue.add(p1); queue.add(ep1); queue.add(ep2); queue.add(ep3);

				System.out.println("You have selected: Easy Mode");
				table.start();

				p1.start();
				ep1.start();
				ep2.start();
				ep3.start();
			}
		
		else {
			System.out.println("Incorrect input, please select difficulty again...");
			}
		
	}
}