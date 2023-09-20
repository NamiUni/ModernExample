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
package com.github.namiuni.modernexample.config;

import com.github.namiuni.modernexample.config.serializer.LocaleSerializer;
import com.google.inject.Inject;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings({"UnstableApiUsage", "unused"})
@DefaultQualifier(NonNull.class)
public final class ConfigManager {

    private final Path dataDirectory;
    private final ComponentLogger componentLogger;
    private final LocaleSerializer localeSerializer;

    private @MonotonicNonNull PrimaryConfig primaryConfig;

    @Inject
    public ConfigManager(
            final PluginProviderContext pluginProviderContext,
            final LocaleSerializer localeSerializer
    ) {
        this.dataDirectory = pluginProviderContext.getDataDirectory();
        this.componentLogger = pluginProviderContext.getLogger();
        this.localeSerializer = localeSerializer;
    }

    public PrimaryConfig primaryConfig() {

        return Objects.requireNonNullElseGet(this.primaryConfig, this::reloadPrimaryConfig);
    }

    public PrimaryConfig reloadPrimaryConfig() {
        try {
            this.primaryConfig = this.load(PrimaryConfig.class, "config.conf");
        } catch (final IOException exception) {
            this.componentLogger.error("Failed to load config.conf", exception);
        }

        return Objects.requireNonNull(this.primaryConfig);
    }

    public <T> @Nullable T load(final Class<T> clazz, final String fileName) throws IOException {
        if (!Files.exists(this.dataDirectory)) {
            Files.createDirectories(this.dataDirectory);
        }

        final Path file = this.dataDirectory.resolve(fileName);

        final var loader = this.configurationLoader(file);

        try {
            final var node = loader.load();
            final @Nullable T config = node.get(clazz);

            if (!Files.exists(file)) {
                node.set(clazz, config);
                loader.save(node);
            }

            return config;
        } catch (final ConfigurateException exception) {
            this.componentLogger.error("Failed to load config.", exception);
            return null;
        }
    }

    public ConfigurationLoader<?> configurationLoader(final Path file) {
        return HoconConfigurationLoader.builder()
                .prettyPrinting(true)
                .defaultOptions(opts -> {
                    final var miniMessageSerializer = ConfigurateComponentSerializer.builder()
                            .scalarSerializer(MiniMessage.miniMessage())
                            .outputStringComponents(true)
                            .build();
                    final var kyoriSerializer = ConfigurateComponentSerializer.configurate();

                    return opts.shouldCopyDefaults(true).serializers(serializerBuilder ->
                            serializerBuilder
                                    .registerAll(miniMessageSerializer.serializers())
                                    .registerAll(kyoriSerializer.serializers())
                                    .register(Locale.class, this.localeSerializer)
                    );
                })
                .path(file)
                .build();
    }
}
