package com.github.namiuni.modernexample;

import com.github.namiuni.modernexample.command.ModernCommand;
import com.github.namiuni.modernexample.command.commands.ReloadCommand;
import com.github.namiuni.modernexample.event.ModernEventHandler;
import com.github.namiuni.modernexample.listeners.JoinListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.papermc.lib.PaperLib;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Level;

@Singleton
@DefaultQualifier(NonNull.class)
public class ModernExample extends JavaPlugin {
    private static final Set<Class<? extends Listener>> LISTENER_CLASSES = Set.of(
            JoinListener.class
    );
    private static final Set<Class<? extends ModernCommand>> COMMAND_CLASSES = Set.of(
            ReloadCommand.class
    );
    private final ModernEventHandler eventHandler = new ModernEventHandler();
    private @MonotonicNonNull Injector injector;
    private @MonotonicNonNull Logger logger;

    @Override
    public void onLoad() {
        if (!PaperLib.isPaper()) {
            this.getLogger().log(Level.SEVERE, "* ModernExampleはPaperAPIを使って書かれています。");
            this.getLogger().log(Level.SEVERE, "* サーバーをPaperにアップグレードしてください。");
            PaperLib.suggestPaper(this, Level.SEVERE);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.injector = Guice.createInjector(new ModernModule(this, this.dataDirectory()));
        this.logger = LogManager.getLogger("ModernExample");
    }

    @Override
    public void onEnable() {

        // Listeners
        for (final Class<? extends Listener> listenerClass : LISTENER_CLASSES) {
            var listener = this.injector.getInstance(listenerClass);
            this.getServer().getPluginManager().registerEvents(listener, this);
        }

        // Commands
        for (final Class<? extends ModernCommand> commandClass : COMMAND_CLASSES) {
            var command = this.injector.getInstance(commandClass);
            command.init();
        }
    }

    public void onDisable() {
    }

    public Logger logger() {
        return this.logger;
    }

    public Path dataDirectory() {
        return this.getDataFolder().toPath();
    }

    public @NonNull ModernEventHandler eventHandler() {
        return this.eventHandler;
    }
}
