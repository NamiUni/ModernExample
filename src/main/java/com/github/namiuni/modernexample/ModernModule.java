package com.github.namiuni.modernexample;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;

@DefaultQualifier(NonNull.class)
public final class ModernModule extends AbstractModule {

    private final Logger logger = LogManager.getLogger("ModernExample");
    private final ModernExample modernExample;
    private final Path dataDirectory;

    ModernModule(
            final ModernExample modernExample,
            final Path dataDirectory
    ) {
        this.modernExample = modernExample;
        this.dataDirectory = dataDirectory;
    }

    @Provides
    @Singleton
    public CommandManager<CommandSender> commandManager() {
        final PaperCommandManager<CommandSender> commandManager;
        try {

            commandManager = new PaperCommandManager<>(
                    this.modernExample,
                    AsynchronousCommandExecutionCoordinator.<CommandSender>newBuilder().build(),
                    commandSender -> commandSender,
                    commander -> commander
            );
        } catch (final Exception exception) {
            throw new RuntimeException("コマンドマネージャーの初期化に失敗しました。", exception);
        }

        commandManager.registerAsynchronousCompletions();

        return commandManager;
    }

    @Override
    public void configure() {
        this.bind(Logger.class).toInstance(this.logger);
        this.bind(ModernExample.class).toInstance(this.modernExample);
        this.bind(Path.class).toInstance(this.dataDirectory);
    }
}
