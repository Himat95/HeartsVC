class MVar<E>
/* The MVAR is used for thread communication as it embodies the methods of
 * wait/notify/notifyAll
 */
{
	    private E state;
	    private boolean isSet = false;

	    public synchronized void putMVar(E s) {
	        while (isSet) {
	            try {
	                wait();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        isSet = true;
	        state = s;
	        notifyAll();
	    }

	    public synchronized E takeMVar() {
	        while (!isSet) {
	            try {
	                wait();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        isSet = false;
	        notifyAll();
	        return state;
	    }
}
