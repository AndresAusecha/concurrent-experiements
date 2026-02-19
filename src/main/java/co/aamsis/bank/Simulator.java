package co.aamsis.bank;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulator {
    public void simulateBankAccount() {
        BankAccount bankAccount = new BankAccount(10000, 0);
        var factory = Thread.ofPlatform().factory();

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        3,
                        factory
                );

        for (int i = 0; i < 100; i++) {
            Runnable task1 = () -> {
                double newWithdraw = 1000; //ThreadLocalRandom.current().nextDouble(1, 1000);
                bankAccount.withdraw(newWithdraw);
            };
            Runnable task2 = () -> {
                double newDeposit = 1000; //ThreadLocalRandom.current().nextDouble(1, 1000);
                bankAccount.deposit(newDeposit);
            };
            executor.submit(task1);
            executor.submit(task2);

        }

        executor.shutdown();
    }

    // the result of ths experiment is that we see errors in the first logs as expected, this is caused for ReentrantLock
    // to have a queue of fairly-ordered threads
    public void simulateBankAccountConcurrentSupport() {

        ConcurrentBankAccount bankAccount = new ConcurrentBankAccount(10000, 0);
        var factory = Thread.ofPlatform().factory();

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        3,
                        factory
                );

        for (int i = 0; i < 100; i++) {
            Runnable task2 = () -> {
                double newDeposit = 1000; //ThreadLocalRandom.current().nextDouble(1, 1000);
                bankAccount.deposit(newDeposit);
            };
            Runnable task1 = () -> {
                double newWithdraw = 1000; //ThreadLocalRandom.current().nextDouble(1, 1000);
                bankAccount.withdraw(newWithdraw);
            };
            executor.submit(task1);
            executor.submit(task2);
        }

        executor.shutdown();
    }
}
