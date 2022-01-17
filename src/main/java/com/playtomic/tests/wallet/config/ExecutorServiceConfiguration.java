package com.playtomic.tests.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
class ExecutorServiceConfiguration {

    private int executorThreadCount = 10;


    public static class MyExecutorService {
        private ThreadPoolExecutor executor;

        public MyExecutorService(ThreadPoolExecutor executor) {
            this.executor = executor;
        }

    }

    @Bean("executorThreadPool")
    public ThreadPoolExecutor cachedThreadPool() {
        return new ThreadPoolExecutor(executorThreadCount, executorThreadCount, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    @Bean
    public MyExecutorService configureDestroyableBean(ThreadPoolExecutor cachedThreadPool)
    {
        return new MyExecutorService(cachedThreadPool);
    }

}
