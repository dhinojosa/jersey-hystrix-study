package com.xyzcorp.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.Random;

public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        Random random = new Random();
        int value = random.nextInt(9) + 1;
        if (value == 6) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (value == 1) {
            throw new RuntimeException("Super sad!");
        }
        return "Hello " + name + "!";
    }

    @Override
    protected String getFallback() {
        return "Unable to reach Command HelloWorld";
    }
}
