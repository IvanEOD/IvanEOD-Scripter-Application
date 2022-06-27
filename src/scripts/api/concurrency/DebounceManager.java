package scripts.api.concurrency;

import org.tribot.script.sdk.ScriptListening;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* Written by IvanEOD 6/26/2022, at 3:53 PM */
public class DebounceManager {


    private final ScheduledExecutorService debounceScheduler = Executors.newScheduledThreadPool(1);
    private final ConcurrentHashMap<Debouncer, DebounceContext> delayedMap = new ConcurrentHashMap<>();

    public static void call(Debouncer debouncer, int interval, TimeUnit timeUnit) {
        var scriptDebouncer = getInstance();
        DebounceContext task = new DebounceContext(debouncer, interval, timeUnit);
        DebounceContext prev;
        do {
            prev = scriptDebouncer.delayedMap.putIfAbsent(debouncer, task);
            if (prev == null) scriptDebouncer.debounceScheduler.schedule(task, interval, timeUnit);
        } while (prev != null && !prev.extend());
    }

    public void terminate() {
        debounceScheduler.shutdownNow();
    }
    private DebounceManager() {
        ScriptListening.addEndingListener(this::terminate);
    }
    public static DebounceManager getInstance() { return DebounceManagerSingleton.INSTANCE; }
    private static class DebounceManagerSingleton {
        private static final DebounceManager INSTANCE = new DebounceManager();
    }
    private static class DebounceContext implements Runnable {

        private final Debouncer debouncer;
        private final int interval;
        private final TimeUnit timeUnit;
        private long dueTime;
        private final Object lock = new Object();

        public DebounceContext(Debouncer debouncer, int interval, TimeUnit timeUnit) {
            this.debouncer = debouncer;
            this.interval = interval;
            this.timeUnit = timeUnit;
            extend();
        }

        public boolean extend() {
            synchronized (lock) {
                if (dueTime < 0) return false;
                dueTime = System.currentTimeMillis() + timeUnit.toMillis(interval);
                return true;
            }
        }

        @Override
        public void run() {
            synchronized(lock) {
                long remaining = dueTime - System.currentTimeMillis();
                if (remaining > 0) {
                    getInstance().debounceScheduler.schedule(this, remaining, TimeUnit.MILLISECONDS);
                } else {
                    dueTime = -1;
                    try {
                        debouncer.call();
                    } finally {
                        getInstance().delayedMap.remove(debouncer);
                    }
                }
            }
        }
    }

}
