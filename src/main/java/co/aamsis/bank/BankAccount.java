package co.aamsis.bank;

import java.util.Objects;

public class BankAccount {
    private long accountNumber;
    private double balance;

    public BankAccount(long accountNumber, double balance) {
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
        this.balance += amount;
        System.out.println("Deposited " + amount + " and the new balance is " + this.balance + " and current thread is " + Thread.currentThread().getName());
    }

    public void withdraw(double amount) {
        if(this.balance < amount || this.balance == 0 || this.balance - amount < 0) {
            System.err.println("Insufficient funds to withdraw " + amount + " with balance " + this.balance + " and current thread is " + Thread.currentThread().getName());
            return;
        }
        this.balance -= amount;
        System.out.println("Withdrawn " + amount + " and the new balance is " + this.balance  + " and current thread is " + Thread.currentThread().getName());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BankAccount that)) return false;
        return accountNumber == that.accountNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountNumber);
    }
}
