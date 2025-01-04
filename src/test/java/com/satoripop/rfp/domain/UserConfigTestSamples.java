package com.satoripop.rfp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserConfig getUserConfigSample1() {
        return new UserConfig()
            .id(1L)
            .userId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .email("email1")
            .firstname("firstname1")
            .lastname("lastname1")
            .username("username1")
            .password("password1");
    }

    public static UserConfig getUserConfigSample2() {
        return new UserConfig()
            .id(2L)
            .userId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .email("email2")
            .firstname("firstname2")
            .lastname("lastname2")
            .username("username2")
            .password("password2");
    }

    public static UserConfig getUserConfigRandomSampleGenerator() {
        return new UserConfig()
            .id(longCount.incrementAndGet())
            .userId(UUID.randomUUID())
            .email(UUID.randomUUID().toString())
            .firstname(UUID.randomUUID().toString())
            .lastname(UUID.randomUUID().toString())
            .username(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString());
    }
}
