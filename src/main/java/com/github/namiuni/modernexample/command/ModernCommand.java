package com.github.namiuni.modernexample.command;

import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;
@DefaultQualifier(NonNull.class)
public interface ModernCommand {

    void init();

}
