import java.util.concurrent.ArrayBlockingQueue;

public class ConcurrentGame {

	public static void main(String[] args) {
	
		ArrayBlockingQueue<AIPlayer> queue = new ArrayBlockingQueue<AIPlayer>(4); 
		
		Table t = new Table(queue); 
		AIPlayer p = new AIPlayer("Himat", 0, t); 
		AIPlayer p2 = new AIPlayer("Raj", 1, t); 
		AIPlayer p3 = new AIPlayer("Sunny", 2, t); 
		AIPlayer p4 = new AIPlayer("Faker", 3, t); 
		
		queue.add(p); queue.add(p2); queue.add(p3); queue.add(p4);
		
		t.start();
		
		p.start();
		p2.start();
		p3.start();
		p4.start();
	}

}
