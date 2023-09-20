package com.github.namiuni.modernexample.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import com.github.namiuni.modernexample.config.ConfigManager;
import com.github.namiuni.modernexample.message.ModernMessageSource;
import com.github.namiuni.modernexample.message.ModernMessages;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.IOException;

@DefaultQualifier(NonNull.class)
public final class ReloadCommand extends ModernCommand {

    private final ConfigManager configManager;
    private final ModernMessageSource modernMessageSource;
    private final ModernMessages modernMessages;

    @Inject
    ReloadCommand(
            final CommandManager<CommandSender> commandManager,
            final ConfigManager configManager,
            final ModernMessageSource modernMessageSource,
            final ModernMessages modernMessages
    ) {
        super(commandManager);
        this.configManager = configManager;
        this.modernMessageSource = modernMessageSource;
        this.modernMessages = modernMessages;
    }

    @Override
    Command<CommandSender> command() {
        return commandManager.commandBuilder("modernexample", "me")
                .literal("reload")
                .permission("modernexample.reload")
                .senderType(CommandSender.class)
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, this.modernMessages.commandReloadDescription())
                .handler(context -> {
                    final var sender = context.getSender();
                    try {
                        this.configManager.reloadPrimaryConfig();
                        this.modernMessageSource.reloadTranslations();
                        this.modernMessages.configReloaded(sender);
                    } catch (IOException e) {
                        this.modernMessages.configReloadFailed(sender);
                        throw new RuntimeException(e);
                    }
                })
                .build();
    }
}
