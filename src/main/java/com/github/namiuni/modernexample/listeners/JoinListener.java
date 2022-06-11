package com.github.namiuni.modernexample.listeners;

import com.github.namiuni.modernexample.ModernExample;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class JoinListener implements Listener {

    private final ModernExample modernExample;

    @Inject
    public JoinListener(final ModernExample modernExample) {
        this.modernExample = modernExample;
    }

    @EventHandler
    public void onPlayerInteract(final @NonNull PlayerInteractEvent event) {
        // TODO なんか書く
    }
}
