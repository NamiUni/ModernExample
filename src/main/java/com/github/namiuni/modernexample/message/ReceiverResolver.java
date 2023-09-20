package com.github.namiuni.modernexample.message;

import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.receiver.IReceiverLocator;
import net.kyori.moonshine.receiver.IReceiverLocatorResolver;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Singleton
@DefaultQualifier(NonNull.class)
public final class ReceiverResolver implements IReceiverLocatorResolver<Audience> {

    @Override
    public IReceiverLocator<Audience> resolve(final Method method, final Type proxy) {
        return new Resolver();
    }

    private static final class Resolver implements IReceiverLocator<Audience> {
        @Override
        public Audience locate(final Method method, final Object proxy, final @Nullable Object[] parameters) {
            if (parameters.length == 0) {
                return Audience.empty();
            }

            final @Nullable Object parameter = parameters[0];

            if (parameter instanceof Audience audience) {
                return audience;
            }

            return Audience.empty();
        }
    }
}
