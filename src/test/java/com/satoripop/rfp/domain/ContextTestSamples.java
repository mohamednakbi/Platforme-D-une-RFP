package com.satoripop.rfp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContextTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Context getContextSample1() {
        return new Context().id(1L).name("name1").description("description1");
    }

    public static Context getContextSample2() {
        return new Context().id(2L).name("name2").description("description2");
    }

    public static Context getContextRandomSampleGenerator() {
        return new Context().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
