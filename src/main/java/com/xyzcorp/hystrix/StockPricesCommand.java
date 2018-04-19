package com.xyzcorp.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class StockPricesCommand extends HystrixObservableCommand<String> {

    private final String name;
    private ExecutorService executorService;

    public StockPricesCommand(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.executorService = Executors.newFixedThreadPool(10);
        this.name = name;
    }

    private Future<String> getStockPrices(String symbol) {
        return executorService.submit(() -> {
            String urlString = "https://www.google.com/finance/getprices?q="
                + symbol + "&i=60&p=15d&f=d,o,h,l,c,v";
            InputStream is = new URL(urlString).openStream();
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        });
    }

    @Override
    protected Observable<String> construct() {
        return Observable.from(getStockPrices(name))
                         .subscribeOn(Schedulers.io());
    }
}
