package com.github.namiuni.modernexample.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Locale;

@ConfigSerializable
@DefaultQualifier(NonNull.class)
public final class PrimaryConfig {

    private Locale defaultLocale = Locale.US;

    public Locale defaultLocale() {
        return defaultLocale;
    }
}
