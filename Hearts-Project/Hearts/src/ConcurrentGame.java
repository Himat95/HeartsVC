import java.util.concurrent.ArrayBlockingQueue;

public class ConcurrentGame {

	public static void main(String[] args) {

		ArrayBlockingQueue<AIPlayer> queue = new ArrayBlockingQueue<AIPlayer>(4);

		Trick trick = new Trick();

		Table table = new Table(queue, trick);
		AIPlayer p = new AIPlayer("Himat", 0, table, trick);
		AIPlayer p2 = new AIPlayer("Raj", 1, table, trick);
		AIPlayer p3 = new AIPlayer("Sunny", 2, table, trick);
		AIPlayer p4 = new AIPlayer("Tarun", 3, table, trick);

		queue.add(p); queue.add(p2); queue.add(p3); queue.add(p4);

		table.start();

		p.start();
		p2.start();
		p3.start();
		p4.start();
	}

}
