package com.github.namiuni.modernexample;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.github.namiuni.modernexample.message.ModernMessageSource;
import com.github.namiuni.modernexample.message.ModernMessages;
import com.github.namiuni.modernexample.message.PaperMessageRenderer;
import com.github.namiuni.modernexample.message.ReceiverResolver;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.Moonshine;
import net.kyori.moonshine.exception.scan.UnscannableMethodException;
import net.kyori.moonshine.strategy.StandardPlaceholderResolverStrategy;
import net.kyori.moonshine.strategy.supertype.StandardSupertypeThenInterfaceSupertypeStrategy;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.function.Function;

@SuppressWarnings({"UnstableApiUsage", "unused"})
@DefaultQualifier(NonNull.class)
public final class ModernExampleModule extends AbstractModule {

    private final ModernExample modernExample;
    private final PluginProviderContext pluginProviderContext;

    ModernExampleModule(
            final ModernExample modernExample,
            final PluginProviderContext pluginProviderContext
    ) {
        this.modernExample = modernExample;
        this.pluginProviderContext = pluginProviderContext;
    }

    @Provides
    @Singleton
    public CommandManager<CommandSender> commandManager() {
        final PaperCommandManager<CommandSender> commandManager;

        try {
            commandManager = new PaperCommandManager<>(
                    this.modernExample,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );
        } catch (final Exception exception) {
            throw new RuntimeException("Failed to initialize command manager.", exception);
        }
        commandManager.registerAsynchronousCompletions();
        return commandManager;
    }

    @Provides
    @Singleton
    public ModernMessages modernExampleMessages(
            final ReceiverResolver receiverResolver,
            final ModernMessageSource modernMessageSource,
            final PaperMessageRenderer<Audience> paperMessageRenderer
    ) throws UnscannableMethodException {
        return Moonshine.<ModernMessages, Audience>builder(new TypeToken<>() {
                })
                .receiverLocatorResolver(receiverResolver, 0)
                .sourced(modernMessageSource)
                .rendered(paperMessageRenderer)
                .sent(Audience::sendMessage)
                .resolvingWithStrategy(new StandardPlaceholderResolverStrategy<>(new StandardSupertypeThenInterfaceSupertypeStrategy(false)))
                .create(this.getClass().getClassLoader());
    }

    @Override
    public void configure() {
        this.bind(ModernExample.class).toInstance(this.modernExample);
        this.bind(PluginProviderContext.class).toInstance(this.pluginProviderContext);
    }
}
