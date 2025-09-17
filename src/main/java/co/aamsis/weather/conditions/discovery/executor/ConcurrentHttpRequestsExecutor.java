package co.aamsis.weather.conditions.discovery.executor;

import java.util.concurrent.*;

public class ConcurrentHttpRequestsExecutor {
    static private ExecutorService service;

    private ConcurrentHttpRequestsExecutor() {
    }

    public synchronized static void instantiateVirtual() {
        if (service == null) {
            service = Executors.newVirtualThreadPerTaskExecutor();
        }
    }

    public synchronized static void instantiatePlatformThread(int numberOfThreads) {
        if (service == null) {
            service = Executors.newFixedThreadPool(numberOfThreads);
        }
    }

    public static <T> Future<T> execute(Callable<T> callable) throws ExecutionException, InterruptedException {
        return service.submit(callable);
    }

    public static void closeService() {
        service.close();
    }

}
