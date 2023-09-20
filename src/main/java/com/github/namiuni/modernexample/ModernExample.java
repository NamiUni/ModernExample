package com.github.namiuni.modernexample;

import com.github.namiuni.modernexample.command.HelpCommand;
import com.github.namiuni.modernexample.command.ModernCommand;
import com.github.namiuni.modernexample.command.ReloadCommand;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "unused"})
@DefaultQualifier(NonNull.class)
public final class ModernExample extends JavaPlugin {

    private final Injector injector;

    private static final Set<Class<? extends Listener>> LISTENER_CLASSES = Set.of();

    private static final Set<Class<? extends ModernCommand>> COMMAND_CLASSES = Set.of(
            HelpCommand.class,
            ReloadCommand.class
    );

    ModernExample(final PluginProviderContext context) {
        final var module = new ModernExampleModule(this, context);
        this.injector = Guice.createInjector(module);
    }

    @Override
    public void onLoad() {
        ModernExampleProvider.register(this);
    }

    @Override
    public void onEnable() {

        // リスナークラスをインスタンス化してプラグインマネージャーで登録
        LISTENER_CLASSES.forEach(listenerClass -> {
            final var listenerInstance = this.injector.getInstance(listenerClass);
            this.getServer().getPluginManager().registerEvents(listenerInstance, this);
        });

        // コマンドクラスも同じく
        COMMAND_CLASSES.forEach(commandClass -> {
            final var commandInstance = this.injector.getInstance(commandClass);
            commandInstance.init();
        });
    }

    public static boolean miniPlaceholdersLoaded() {
        return Bukkit.getPluginManager().isPluginEnabled("MiniPlaceholders");
    }
}
