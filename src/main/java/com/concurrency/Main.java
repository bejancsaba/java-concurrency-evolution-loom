package com.concurrency;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static com.concurrency.ConcurrencySupport.USERS;
import static com.concurrency.ConcurrencySupport.PERSISTENCE_FORK_FACTOR;
import static com.concurrency.ConcurrencySupport.persistence;
import static com.concurrency.ConcurrencySupport.serviceA;
import static com.concurrency.ConcurrencySupport.serviceB;
import static com.concurrency.ConcurrencySupport.start;
import static com.concurrency.ConcurrencySupport.stop;

@Slf4j
public class Main {

    public static void main(String[] args) {
        new Main().startConcurrency();
    }

    @SneakyThrows
    private void startConcurrency() {
        start();

        try (var e = Executors.newVirtualThreadExecutor()) {
            IntStream.rangeClosed(1, USERS).forEach(i -> e.submit(() -> userFlow(i)));
        }

        stop();
    }

    @SneakyThrows
    private void userFlow(int user) {
        List<Future<String>> result;
        try (var e = Executors.newVirtualThreadExecutor()) {
            result = e.invokeAll(List.of(
                    () -> serviceA(user),
                    () -> serviceB(user)
            ));
        }

        persist(result.get(0).get(), result.get(1).get());
    }

    private void persist(String serviceA, String serviceB) {
        try (var e = Executors.newVirtualThreadExecutor()) {
            IntStream.rangeClosed(1, PERSISTENCE_FORK_FACTOR)
                    .forEach(i -> e.submit(() -> persistence(i, serviceA, serviceB)));
        }
    }
}