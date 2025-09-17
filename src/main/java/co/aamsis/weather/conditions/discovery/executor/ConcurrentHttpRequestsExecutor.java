package co.aamsis.weather.conditions.discovery.executor;

import java.util.Queue;
import java.util.concurrent.*;

public class ConcurrentHttpRequestsExecutor {
    static private ExecutorService service;
    static private final Semaphore semaphore = new Semaphore(5);
    static private final Queue<Callable<?>> callableQueue = new ConcurrentLinkedQueue<>();

    private ConcurrentHttpRequestsExecutor() {

    }

    public static void addElementsToTheQueue(Callable<?> e) {
        callableQueue.add(e);
    }

    public static void cleanQueue() {
        callableQueue.clear();
    }

    public synchronized static void instantiateVirtual() {
        if (service == null) {
            service = Executors.newVirtualThreadPerTaskExecutor();
        }
    }

    public synchronized static void instantiateVirtual(ThreadFactory factory) {
        if (service == null) {
            service = Executors.newThreadPerTaskExecutor(factory);
        }
    }

    public synchronized static void instantiatePlatformThread(int numberOfThreads) {
        if (service == null) {
            service = Executors.newFixedThreadPool(numberOfThreads);
        }
    }

    public static <T> Future<T> execute(Callable<T> callable) {
        return service.submit(callable);
    }

    public static Future<?> executeOrderly() throws Exception {
        semaphore.acquire();
        var result = service.submit(() -> callableQueue.poll().call());
        semaphore.release();

        return result;
    }

    public static void closeService() {
        service.close();
    }

}
