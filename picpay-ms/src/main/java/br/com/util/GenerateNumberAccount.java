package br.com.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class GenerateNumberAccount {
    public String generate(){
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
    }
}
