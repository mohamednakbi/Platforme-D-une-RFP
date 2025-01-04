package com.satoripop.rfp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Role getRoleSample1() {
        return new Role().id(1L).groupId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1");
    }

    public static Role getRoleSample2() {
        return new Role().id(2L).groupId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2");
    }

    public static Role getRoleRandomSampleGenerator() {
        return new Role().id(longCount.incrementAndGet()).groupId(UUID.randomUUID()).name(UUID.randomUUID().toString());
    }
}
