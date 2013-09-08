
package com.pahanez.wallpaper.cpu;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MainExecutor {
    private ExecutorService mParserExecutor = Executors.newSingleThreadExecutor();
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setPriority(Thread.MIN_PRIORITY);
			return thread;
		}
	});
    private volatile boolean mIsVisible = true;
    private List<Runnable> cache = new CopyOnWriteArrayList<Runnable>();

    private MainExecutor() {
    }

    private static MainExecutor sInstance;

    public static synchronized MainExecutor getInstance() {
        return sInstance == null ? sInstance = new MainExecutor() : sInstance;
    }

    public <T> void executeWhileVisible (Task<T> task, CallBack<T> callBack) { 
    	RunnableDecorator<T> runnable = new RunnableDecorator<T>(task, callBack);
    	if(mIsVisible)	mParserExecutor.execute(runnable);
    	else cache.add(runnable);
    } 
    
    public <T> void execute (Task<T> task, CallBack<T> callBack) { 
    	RunnableDecorator<T> runnable = new RunnableDecorator<T>(task, callBack);
    	mExecutorService.execute(runnable);
    }
    
    public void execute (Runnable runnable){
    	mExecutorService.execute(runnable);
    }
    
    public boolean isVisible() {
		return mIsVisible;
	}

	public void setVisible(boolean mIsVisible) {
		this.mIsVisible = mIsVisible;
		if(mIsVisible) onResume();
		//else onPause(); TODO
	}
	
	private void onResume(){
		for(Runnable runnable:cache) mExecutorService.execute(runnable);
		cache.clear();
	}
	

	public interface CallBack <T>{
        void fullfilled(T o);
    }
    
    public interface Task <T> {
        T fullfill();
    }

    private class RunnableDecorator <T> implements Runnable {
        private Task<T> mRunnable;
        private CallBack<T> mCallback;

        public RunnableDecorator(Task<T> runnable, CallBack<T> callback) {
            mRunnable = runnable;
            mCallback = callback;
        }

        @Override
        public void run() {
            T o = mRunnable.fullfill();
            if(o != null)
                mCallback.fullfilled(o);
        }
    }
}
