package co.aamsis;

import co.aamsis.weather.conditions.discovery.clients.OpenMeteoClient;
import co.aamsis.weather.conditions.discovery.clients.OpenMeteoResponse;
import co.aamsis.weather.conditions.discovery.executor.ConcurrentHttpRequestsExecutor;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

public class Main implements AutoCloseable {
    static void experimentWithExecutorOrderly() throws Exception {
        OpenMeteoClient.instantiate();
        var factory = Thread.ofVirtual().name("virtual-threads-", 1).factory();
        ConcurrentHttpRequestsExecutor.instantiateVirtual(factory);
        double[] latitudes = { 6.2798652316551316, 6.557981968954068, 5.690106984074268, 7.992501417936491, 6.2765902188199085 };
        double[] longitudes = { -75.58276991899643, -75.82894721199372, -76.67037397794111, -75.20780624507267, -75.59497135432744 };
        Instant start = Instant.now();

        for (int i = 0; i < 5; i++) {
            final int it = i;
            ConcurrentHttpRequestsExecutor
                    .addElementsToTheQueue(() -> {
                        System.out.println("Current thread is virtual for: " + it + ":" + Thread.currentThread().isVirtual());
                        System.out.println("Current executor thread for: " + it + " is " + Thread.currentThread().getName());
                        System.out.println("Current execution for " + it + " started at: " + System.currentTimeMillis());

                        var result = OpenMeteoClient.fetchForecastForLocation(latitudes[it], longitudes[it]);

                        System.out.println("Current execution for " + it + " finished at: " + System.currentTimeMillis());

                        return result;
                    });
            System.out.println("result: " + ((OpenMeteoResponse) ConcurrentHttpRequestsExecutor.executeOrderly().get()).getElevation());
        }
        Instant end = Instant.now();
        System.out.println("the duration of the operation was: " + Duration.between(start, end).toMillis());
    }

    static void experimentWithExecutor() throws ExecutionException, InterruptedException {
        OpenMeteoClient.instantiate();
        ConcurrentHttpRequestsExecutor.instantiatePlatformThread(5);
        double[] latitudes = { 6.2798652316551316, 6.557981968954068, 5.690106984074268, 7.992501417936491, 6.2765902188199085 };
        double[] longitudes = { -75.58276991899643, -75.82894721199372, -76.67037397794111, -75.20780624507267, -75.59497135432744 };
        Instant start = Instant.now();
        for (int i = 0; i < 5; i++) {
            final int it = i;
            var result = ConcurrentHttpRequestsExecutor
                    .execute(() -> {
                        System.out.println("Current thread is virtual for: " + it + ":" + Thread.currentThread().isVirtual());
                        System.out.println("Current executor thread for: " + it + " is " + Thread.currentThread().getName());
                        System.out.println("Current execution for " + it + " started at: " + System.currentTimeMillis());

                        var response = OpenMeteoClient.fetchForecastForLocation(latitudes[it], longitudes[it]);

                        System.out.println("Current execution for " + it + " finished at: " + System.currentTimeMillis());

                        return response;
                    }).get();

            System.out.println("result elevation: " + result.getElevation());
        }
        Instant end = Instant.now();
        System.out.println("the duration of the operation was: " + Duration.between(start, end).toMillis());
    }
    public static void main(String[] args) throws Exception {
        experimentWithExecutor();
        //experimentWithExecutorOrderly();
    }

    @Override
    public void close() throws Exception {
        ConcurrentHttpRequestsExecutor.closeService();
    }
}