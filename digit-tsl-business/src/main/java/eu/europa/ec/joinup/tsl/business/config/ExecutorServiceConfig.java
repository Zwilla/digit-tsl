package eu.europa.ec.joinup.tsl.business.config;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorServiceConfig {

    @Bean(destroyMethod = "shutdownNow")
    public ScheduledExecutorService taskScheduler() {
        ScheduledExecutorService exec = new ScheduledExecutorService() {
            private final ScheduledExecutorService wrapped = Executors.newScheduledThreadPool(50);

            @Override
            public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
                return wrapped.schedule(command, delay, unit);
            }

            @Override
            public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
                return wrapped.schedule(callable, delay, unit);
            }

            @Override
            public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
                return wrapped.scheduleAtFixedRate(command, initialDelay, period, unit);
            }

            @Override
            public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
                return wrapped.scheduleWithFixedDelay(command, initialDelay, delay, unit);
            }

            @Override
            public void shutdown() {
                wrapped.shutdown();
            }

            @Override
            public List<Runnable> shutdownNow() {
                // System.exit(0);
                return wrapped.shutdownNow();
            }

            @Override
            public boolean isShutdown() {
                return wrapped.isShutdown();
            }

            @Override
            public boolean isTerminated() {
                return wrapped.isTerminated();
            }

            @Override
            public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
                return wrapped.awaitTermination(timeout, unit);
            }

            @Override
            public <T> Future<T> submit(Callable<T> task) {
                return wrapped.submit(task);
            }

            @Override
            public <T> Future<T> submit(Runnable task, T result) {
                return wrapped.submit(task, result);
            }

            @Override
            public Future<?> submit(Runnable task) {
                return wrapped.submit(task);
            }

            @Override
            public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
                return wrapped.invokeAll(tasks);
            }

            @Override
            public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
                return wrapped.invokeAll(tasks, timeout, unit);
            }

            @Override
            public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
                return wrapped.invokeAny(tasks);
            }

            @Override
            public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return wrapped.invokeAny(tasks, timeout, unit);
            }

            @Override
            public void execute(Runnable command) {
                wrapped.execute(command);
            }
        };
        return exec;
    }

}
