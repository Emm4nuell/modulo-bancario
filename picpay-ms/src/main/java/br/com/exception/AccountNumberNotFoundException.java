package br.com.exception;

public class AccountNumberNotFoundException extends RuntimeException{
    public AccountNumberNotFoundException(String message) {
        super(message);
    }
}
