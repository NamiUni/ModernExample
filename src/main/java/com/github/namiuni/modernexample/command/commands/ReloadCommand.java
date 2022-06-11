package com.github.namiuni.modernexample.command.commands;

import cloud.commandframework.CommandManager;
import com.github.namiuni.modernexample.ModernExample;
import com.github.namiuni.modernexample.command.ModernCommand;
import com.github.namiuni.modernexample.event.events.ModernReloadEvent;
import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class ReloadCommand implements ModernCommand {

    final ModernExample modernExample;
    final CommandManager<CommandSender> commandManager;

    @Inject
    public ReloadCommand(
            ModernExample modernExample,
            CommandManager<CommandSender> commandManager
    ) {
        this.modernExample = modernExample;
        this.commandManager = commandManager;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("modernexample", "me")
                .literal("reload")
                .permission("modernexample.reload")
                .senderType(CommandSender.class)
                .handler(context -> {
                    this.modernExample.eventHandler().emit(new ModernReloadEvent());
                    var miniMessage = MiniMessage.miniMessage();
                    var component = miniMessage.deserialize("<aqua>リロードしました");
                    context.getSender().sendMessage(component);
                })
                .build();

        this.commandManager.command(command);
    }
}
