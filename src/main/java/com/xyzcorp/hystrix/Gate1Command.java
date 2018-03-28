package com.xyzcorp.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class Gate1Command extends HystrixCommand<String> {

    private HystrixCommand<String> next;

    public Gate1Command() {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.next = new Gate2Command();
    }

    @Override
    protected String run() throws Exception {
        return next.execute();
    }

    @Override
    protected String getFallback() {
        return "Unable to reach Gate1";
    }
}
