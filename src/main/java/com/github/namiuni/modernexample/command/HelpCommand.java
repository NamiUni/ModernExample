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
package com.github.namiuni.modernexample.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandHelpHandler;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import cloud.commandframework.minecraft.extras.RichDescription;
import com.github.namiuni.modernexample.message.ModernMessageSource;
import com.github.namiuni.modernexample.message.ModernMessages;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.color;

@DefaultQualifier(NonNull.class)
public final class HelpCommand extends ModernCommand {

    private final ModernMessages modernMessages;
    private final MinecraftHelp<CommandSender> minecraftHelp;

    @Inject
    public HelpCommand(
        final CommandManager<CommandSender> commandManager,
        final ModernMessageSource messageSource,
        final ModernMessages modernMessages
    ) {
        super(commandManager);
        this.modernMessages = modernMessages;
        this.minecraftHelp = createHelp(commandManager, messageSource);
    }

    @Override
    Command<CommandSender> command() {
        return this.commandManager.commandBuilder("modernexample", "me")
            .literal("help",
                RichDescription.of(this.modernMessages.commandHelpDescription()))
            .argument(StringArgument.<CommandSender>builder("query")
                    .greedy().withSuggestionsProvider(this::suggestQueries).asOptional(),
                RichDescription.of(this.modernMessages.commandHelpArgumentQuery()))
            .permission("modernexample.help")
            .handler(this::execute)
            .build();
    }

    private void execute(final CommandContext<CommandSender> ctx) {
        this.minecraftHelp.queryCommands(ctx.getOrDefault("query", ""), ctx.getSender());
    }

    private List<String> suggestQueries(final CommandContext<CommandSender> ctx, final String input) {
        final var topic = this.commandManager.createCommandHelpHandler().queryRootIndex(ctx.getSender());
        return topic.getEntries().stream().map(CommandHelpHandler.VerboseHelpEntry::getSyntaxString).toList();
    }

    private static MinecraftHelp<CommandSender> createHelp(
        final CommandManager<CommandSender> manager,
        final ModernMessageSource messageSource
    ) {
        final MinecraftHelp<CommandSender> help = new MinecraftHelp<>(
            "/modernexample help",
            AudienceProvider.nativeAudience(),
            manager
        );

        help.setHelpColors(
            MinecraftHelp.HelpColors.of(
                color(0xE099FF),
                WHITE,
                color(0xDD1BC4),
                GRAY,
                DARK_GRAY
            )
        );

        help.messageProvider((sender, key, args) -> {
            final String messageKey = "command.help.misc." + key;
            final TagResolver.Builder tagResolver = TagResolver.builder();

            // Total hack but works for now
            if (args.length == 2) {
                tagResolver
                    .tag("page", Tag.inserting(Component.text(args[0])))
                    .tag("max_pages", Tag.inserting(Component.text(args[1]))
                );
            }

            return MiniMessage.miniMessage().deserialize(messageSource.messageOf(sender, messageKey), tagResolver.build());
        });

        return help;
    }

}
