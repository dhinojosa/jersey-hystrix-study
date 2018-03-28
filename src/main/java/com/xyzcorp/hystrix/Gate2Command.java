package com.xyzcorp.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class Gate2Command extends HystrixCommand<String> {

    private HystrixCommand<String> next;

    public Gate2Command() {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.next = new CommandHelloWorld("Cherry");
    }

    @Override
    protected String run() throws Exception {
        return next.execute();
    }
}
