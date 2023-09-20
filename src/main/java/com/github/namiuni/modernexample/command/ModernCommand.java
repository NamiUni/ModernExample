package com.github.namiuni.modernexample.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public abstract class ModernCommand {

    protected final CommandManager<CommandSender> commandManager;

    ModernCommand(final CommandManager<CommandSender> commandManager) {
        this.commandManager = commandManager;
    }

    public void init() {
        commandManager.command(this.command());
    }

    abstract Command<CommandSender> command();
}
