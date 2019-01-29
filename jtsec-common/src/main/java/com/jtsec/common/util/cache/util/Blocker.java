package com.jtsec.common.util.cache.util;

public class Blocker {
	//private static final Logger logger = Logger.getLogger(Blocker.class);
	//每类日志是否执行入库处理的唤醒标识位
	private boolean isWakeUp = false;
	//日志同步操作的权值，日志缓存数组处理在线程中，判断每类日志的权值的当前值，当权值高于预设的值，则执行入库操作，处理相应数组中的缓存日志
	private int wakeUpWeight = 0;

	public boolean getIsWakeUp () {
		return isWakeUp;
	}

	public void setIsWakeUp (boolean isWakeUp) {
		this.isWakeUp = isWakeUp;
	}

	public int getWakeUpWeight () {
		return wakeUpWeight;
	}

	public void setWakeUpWeight (int wakeUpWeight) {
		this.wakeUpWeight = wakeUpWeight;
	}

	public synchronized void waitingCall () {
		try {
//			//while (!Thread.interrupted()) {
//				if(logger.isDebugEnabled()){
//					logger.debug(Thread.currentThread() + " begin into wait status.");
//				}
			wait ();
//				if(logger.isDebugEnabled()){
//					logger.debug(Thread.currentThread() + " wake up from wait status.");
//				}
//			//}
		} catch (InterruptedException e) {
			// OK to exit this way
		}
	}

	public synchronized void waitingCall (int time) {
		try {
//			//while (!Thread.interrupted()) {
//				if(logger.isDebugEnabled()){
//					logger.debug(Thread.currentThread() + " begin into wait " + time + " time status.");
//				}
			wait (time);
//				if(logger.isDebugEnabled()){
//					logger.debug(Thread.currentThread() + " wake up from wait " + time + " time status.");
//				}
//			//}
		} catch (InterruptedException e) {
			// OK to exit this way
		}
	}

	public synchronized void prod () {
//		if(logger.isDebugEnabled()){
//			logger.debug(Thread.currentThread() + "begin notify");
//		}
		notify ();
//		if(logger.isDebugEnabled()){
//			logger.debug(Thread.currentThread() + "notify end");
//		}
	}

	public synchronized void prodAll () {
//		if(logger.isDebugEnabled()){
//			logger.debug(Thread.currentThread() + "begin notify all");
//		}
		notifyAll ();
//		if(logger.isDebugEnabled()){
//			logger.debug(Thread.currentThread() + "notify all end");
//		}
	}
}
