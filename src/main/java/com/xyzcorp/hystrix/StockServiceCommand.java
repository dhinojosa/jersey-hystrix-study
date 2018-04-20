package com.xyzcorp.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.schedulers.Schedulers;

public class StockServiceCommand extends
                                 HystrixObservableCommand<Pair<String, Long>> {

    private String name;

    public StockServiceCommand(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    private Observable<Long> getAverageVolume(Observable<String> data) {
        Observable<Long> volumes = data.map(s -> s.split(",")[5])
                                       .map(Long::valueOf)
                                       .onExceptionResumeNext(Observable.just
                                           (0L));
        Observable<Long> subtotalObservable = volumes.reduce(0L, (total,
                                                                  next) ->
            total + next);
        return subtotalObservable.flatMap(t -> volumes.count().map(c -> t / c));
    }

    @Override
    protected Observable<Pair<String, Long>> construct() {
        StockPricesCommand stockPricesCommand = new StockPricesCommand(name);
        Observable<String> stringObservable = stockPricesCommand.observe();
        Observable<Long> averageObservable =
            stringObservable
                .observeOn(Schedulers.computation())
                .flatMap(b ->
                    getAverageVolume(Observable.from(b.split
                        ("\n")).skip(8)));

        return averageObservable.map(a -> new Pair<>(name, a));
    }

    @Override
    protected Observable<Pair<String, Long>> resumeWithFallback() {
        return Observable.just(new Pair<>("Unknown", 0L));
    }
}
