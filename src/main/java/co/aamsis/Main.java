package co.aamsis;

import co.aamsis.weather.conditions.discovery.clients.OpenMeteoClient;
import co.aamsis.weather.conditions.discovery.executor.ConcurrentHttpRequestsExecutor;

import java.util.concurrent.ExecutionException;

public class Main implements AutoCloseable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        OpenMeteoClient.instantiate();
        ConcurrentHttpRequestsExecutor.instantiatePlatformThread(5);
        double[] latitudes = { 6.2798652316551316, 6.557981968954068, 5.690106984074268, 7.992501417936491, 6.2765902188199085 };
        double[] longitudes = { -75.58276991899643, -75.82894721199372, -76.67037397794111, -75.20780624507267, -75.59497135432744 };

        for (int i = 0; i < 5; i++) {
            final int it = i;
            ConcurrentHttpRequestsExecutor
                    .execute(() -> {
                        System.out.println("Current thread is virtual for: " + it + ":" + Thread.currentThread().isVirtual());
                        System.out.println("Current executor thread for: " + it + " is " + Thread.currentThread().getName());
                        System.out.println("Current execution for " + it + " started at: " + System.currentTimeMillis());

                        var result = OpenMeteoClient.fetchForecastForLocation(latitudes[it], longitudes[it]);

                        System.out.println("Current execution for " + it + " finished at: " + System.currentTimeMillis());

                        return result;
                    });
        }


    }

    @Override
    public void close() throws Exception {
        ConcurrentHttpRequestsExecutor.closeService();
    }
}