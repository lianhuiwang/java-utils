package collection;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import sun.misc.Unsafe;

/**
 * Created by puyuan.wlh on 16/12/31.
 */
public class IntHandler {

	private volatile Thread current;
	private Integer iVal;
	private AtomicInteger count = new AtomicInteger(0);
	private Object lock = new Object();

	private volatile int state;
	private static final int NEW          = 0;
	private static final int COMPLETING   = 1;
	private static final int NORMAL       = 2;
	private static final int EXCEPTIONAL  = 3;
	private static final int CANCELLED    = 4;
	private static final int INTERRUPTING = 5;
	private static final int INTERRUPTED  = 6;

	// Unsafe mechanics
	private static final sun.misc.Unsafe UNSAFE;
	private static final long stateOffset;
	private static final long runnerOffset;
	static {
		try {
			Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
			unsafeField.setAccessible(true);
			UNSAFE = (sun.misc.Unsafe) unsafeField.get(null);
			Class<?> k = IntHandler.class;
			stateOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("state"));
			runnerOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("current"));
		} catch (Throwable e) {
			throw new Error(e);
		}
	}

	public IntHandler(Integer iVal) {
		this.iVal = iVal;
		this.state = NEW;
	}


	public Integer getiVal() {
		return iVal;
	}

	public void execute() throws ServerException {
		// current = Thread.currentThread();
		if (state != NEW || !UNSAFE.compareAndSwapObject(this, runnerOffset, null, Thread.currentThread()))
			return;
		if (iVal > 10) {
			//System.out.println(Thread.currentThread().getId() + ":  " +  current.isInterrupted());
			if (current.isInterrupted()) {
				throw new ServerException(new InterruptedException(), iVal + " exception"+ current.getId(), iVal);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				throw new ServerException(e, iVal + " exception"+ current.getId(), iVal);
			}
			System.out.println(iVal + " finished.");
			return;
		}

		int i = 0;
		while (true) {
			i ++;
			if (iVal == count.get()) {
				System.out.println(iVal + " finished.");
				break;
			}
		}
		current = null;
		int s = state;
		if (s >= INTERRUPTING) {
			handlePossibleCancellationInterrupt();
		}
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			throw new ServerException(e, iVal + " exception"+ current.getId(), iVal);
//		}
		System.out.println(iVal + " INTERRUPTING.");

//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			throw new ServerException(e, iVal + " exception", iVal);
//		}
	}

	private void handlePossibleCancellationInterrupt() {
		// It is possible for our interrupter to stall before getting a
		// chance to interrupt us.  Let's spin-wait patiently.
		while (state == INTERRUPTING)
			Thread.yield(); // wait out pending interrupt

		Thread.interrupted();
	}

	public void cancelHandler() {
		synchronized (lock) {
			if (!UNSAFE.compareAndSwapInt(this, stateOffset, NEW, INTERRUPTING))
				return;
			// state = INTERRUPTING;
			if (current != null) {
				current.interrupt();
			}
			state = INTERRUPTED;
			System.out.println("cancel=" + iVal);
			//UNSAFE.putOrderedInt(this, stateOffset, INTERRUPTED); // final state
		}
		count.set(iVal);
	}

}
