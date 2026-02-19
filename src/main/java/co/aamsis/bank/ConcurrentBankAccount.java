package co.aamsis.bank;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentBankAccount {
    private long accountNumber;
    private double balance;
    private final ReentrantLock lock = new ReentrantLock(true);

    public ConcurrentBankAccount(long accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        lock.lock();
        this.balance += amount;
        System.out.println("Deposited " + amount + " to account " + accountNumber + " and the new balance is " + this.balance + "thread " + Thread.currentThread().getName());
        lock.unlock();
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            if(this.balance < amount || this.balance == 0 || this.balance - amount < 0) {
                throw new IllegalArgumentException("Insufficient funds to withdraw " + amount + "thread " + Thread.currentThread().getName());
            }
            this.balance -= amount;
            System.out.println("Withdrawn " + amount + " to account " + accountNumber + " and the new balance is " + this.balance + "thread " + Thread.currentThread().getName());
        } catch (IllegalArgumentException e) {
            System.err.println("Insufficient funds to withdraw " + amount  + "thread " + Thread.currentThread().getName());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConcurrentBankAccount that)) return false;
        return accountNumber == that.accountNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountNumber);
    }
}
