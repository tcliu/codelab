package codelab.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class Multithreading {

	private static int v = 0;
	
	@Test
	public void singleton() {
		Thread[] threads = new Thread [100];
		for (int i=0; i<threads.length; i++) {
			threads[i] = new Thread(() -> {
				System.out.println( Singleton.getInstance() );
			});
		}
		for (int i=0; i<threads.length; i++) {
			threads[i].start();
		}
	}
	
	// @Test
	public void blockingList() throws InterruptedException {
		final List<Integer> l = new ArrayList<Integer>(10);
		
		Thread t1 = new Thread(() -> {
			while (true) {
				
				try {
					
					synchronized (l) {
						if (l.size() >= 10) {
							l.wait();
						}						
						l.add(v++);
						System.out.println(l);
						l.notify();
						Thread.sleep(100);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		Thread t2 = new Thread(() -> {
			while (true) {
				try {
					synchronized (l) {
						if (l.size() == 0) {
							l.wait();
						}
						Integer removed = l.remove(0);
						System.out.printf("Removed %s\n", removed);
						l.notify();
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
	}
	
	// @Test
	public void blockingListUsingLocks() throws InterruptedException {
		final Lock lock = new ReentrantLock();
		final Condition notFull = lock.newCondition();
		final Condition notEmpty = lock.newCondition();

		final List<Integer> l = new ArrayList<Integer>(10);
		Thread t1 = new Thread(() -> {
			while (true) {
				lock.lock();
				try {
					if (l.size() >= 10) {
						notFull.await();
					}						
					l.add(v++);
					System.out.println(l);
					notEmpty.signal();
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
				
			}
		});
		
		Thread t2 = new Thread(() -> {
			while (true) {
				lock.lock();
				try {
			
					if (l.size() == 0) {
						notEmpty.await();
					}
					Integer removed = l.remove(0);
					System.out.printf("Removed %s\n", removed);
					notFull.signal();
					Thread.sleep(1000);
				
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		});
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
	}
	
	
	// @Test
	public void blockingQueue() throws InterruptedException {
		final BlockingQueue<Integer> l = new LinkedBlockingQueue<Integer>(10);
		
		Thread t1 = new Thread(() -> {
			while (true) {
				
				try {					
					l.put(v++);
					System.out.println(l);
			
					Thread.sleep(500);
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		Thread t2 = new Thread(() -> {
			while (true) {
				try {
					if (l.size() > 0) {
						Integer removed = l.take();
						System.out.printf("Removed %s\n", removed);
					
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
	}
	
	
	
	// @Test
	public void playWaitNotify() throws InterruptedException {
		final Object o = new Object();
		
		Thread t1 = new Thread(() -> {
			System.out.println("waiting 1.");
			synchronized (o) {
				try {
					o.wait();
					o.notify();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("done 1.");
			
		});
		
		Thread t3 = new Thread(() -> {
			System.out.println("waiting 3.");
			synchronized (o) {
				try {
					o.wait();
					o.notify();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("done 3.");
			
		});
		
		Thread t2 = new Thread(() -> {
			System.out.println("notifying");
			synchronized (o) {
	
				o.notify();
				
			}
			System.out.println("notified");

		});
		t1.start();
		t2.start();
		t3.start();
		
		
	}
	
	@Test
	public void playThreads() throws InterruptedException {

		Thread[] threads = new Thread [10];
		for (int i=0; i<threads.length; i++) {
			threads[i] = new MyThread(i);
		}
		for (int i=0; i<threads.length; i++) {
			threads[i].start();
		}
		for (int i=0; i<threads.length; i++) {
			threads[i].join();
		}
		
		System.out.printf("v = %d\n", v);
		
	}
	
	static class Singleton {
		
		private static volatile Singleton instance;
		
		private Singleton() {}
		
		public static Singleton getInstance() {
			if (instance == null) {
				synchronized (Singleton.class) {
					Singleton temp = instance;
					if (temp == null) {
						instance = new Singleton();
					}
				}
			}
			return instance;
		}
		
	}
	
	static class MyThread extends Thread {
		
		int id;
		
		public MyThread(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			
			int prev = v;
			try {
				if (true)
					Thread.sleep(1);
			} catch (InterruptedException e) {

			}
			v = prev + 1;
			System.out.printf("[%d] %d -> %d\n", id, prev, v);
			
			
		}
		
	}
	
}
