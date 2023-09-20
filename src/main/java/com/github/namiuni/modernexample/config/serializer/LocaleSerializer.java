/*
 * CarbonChat
 *
 * Copyright (c) 2023 Josua Parks (Vicarious)
 *                    Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.namiuni.modernexample.config.serializer;

import com.google.inject.Inject;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.translation.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings({"UnstableApiUsage", "unused"})
@DefaultQualifier(NonNull.class)
public final class LocaleSerializer implements TypeSerializer<Locale> {

    private final ComponentLogger componentLogger;

    @Inject
    public LocaleSerializer(final PluginProviderContext pluginProviderContext) {
        this.componentLogger = pluginProviderContext.getLogger();
    }

    @Override
    public Locale deserialize(final Type type, final ConfigurationNode node) {
        final @Nullable String value = node.getString();

        if (value == null) {
            this.componentLogger.warn("value null for locale! defaulting to en_US");
            return Locale.ENGLISH;
        }

        return Objects.requireNonNull(Translator.parseLocale(value), "value locale cannot be null!");
    }

    @Override
    public void serialize(final Type type, final @Nullable Locale obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
        } else {
            node.set(obj.toString());
        }
    }
}
