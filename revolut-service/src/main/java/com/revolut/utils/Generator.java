package com.revolut.utils;

import java.util.UUID;

public class Generator {
    public String getRandomId() {
        return UUID.randomUUID().toString();
    }
}
