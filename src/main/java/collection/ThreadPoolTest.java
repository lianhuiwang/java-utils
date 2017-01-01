package collection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by puyuan.wlh on 16/12/31.
 */
public class ThreadPoolTest {

	int size = 10;

	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(size);

	BlockingQueue<Integer> _inQueue = new LinkedBlockingQueue<Integer>();

	BlockingQueue<IntHandler> list = new LinkedBlockingQueue();

	BlockingQueue<Integer> results = new LinkedBlockingQueue();

	private AtomicInteger count = new AtomicInteger(0);

	protected void messageProcess() {

		executor.execute(new Runnable() {
			public void run() {

				Integer message;
				while ((message = _inQueue.poll()) != null) {
					if (Thread.currentThread().isInterrupted()) {
						System.out.println("interrupt" + Thread.currentThread().getId());
					} else {
						System.out.println("Nointerrupt" + Thread.currentThread().getId());
					}
					IntHandler handler = new IntHandler(message);
					try {
						System.out.println(message + "execute" + Thread.currentThread().getId());
						list.add(handler);
						handler.execute();
						if (Thread.currentThread().isInterrupted()) {
							System.out.println(message + "interrupt" + Thread.currentThread().getId());
						} else {
							System.out.println("Nointerrupt" + Thread.currentThread().getId());
						}
					} catch (ServerException e) {
						results.add(e.getiValue());
						count.incrementAndGet();
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void test1() throws InterruptedException {
		for (int i = 1; i < 11; i++) {
			_inQueue.add(i);
			this.messageProcess();
		}

		Thread.sleep(1000);
		System.out.println("okok");
		for (int i = 11; i < 21; i++) {
			_inQueue.add(i);
			this.messageProcess();
		}
		Thread.sleep(1000);
		IntHandler handler;
		while((handler = list.poll()) != null) {
			if (handler.getiVal() < 10) {
				handler.cancelHandler();
			}
		}
		Thread.sleep(1000);
		System.out.println("okokok");
		Integer iValue;
		while((iValue = results.poll()) != null) {
			System.out.println("val=" + iValue);
		}
		System.out.println("finished");
	}

	public static void main(String[] args)
	{
		ThreadPoolTest poolTest = new ThreadPoolTest();
		try {
			poolTest.test1();
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
