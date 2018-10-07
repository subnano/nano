package io.nano.core.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ThreadFactoriesTest {

    @Test
    void newDaemonThreadFactory() {
        ThreadFactory threadFactory = ThreadFactories.newThreadFactory("Prefix", true);
        Thread thread = threadFactory.newThread(null);
        assertThat(thread.getName()).startsWith("Prefix");
        assertThat(thread.isDaemon()).isTrue();
    }

    @Test
    void newThreadFactory() {
        ThreadFactory threadFactory = ThreadFactories.newThreadFactory("Joe", false);
        Thread thread = threadFactory.newThread(null);
        assertThat(thread.getName()).startsWith("Joe");
        assertThat(thread.isDaemon()).isFalse();
    }
}