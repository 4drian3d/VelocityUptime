package io.github._4drian3d.velocityuptime.command;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import io.github._4drian3d.velocityuptime.VelocityUpTime;
import io.github._4drian3d.velocityuptime.configuration.ConfigurationContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class UpTimeCommand {
    @Inject
    private CommandManager commandManager;
    @Inject
    private VelocityUpTime plugin;
    @Inject
    private ConfigurationContainer configurationContainer;

    public void register() {
        final var node = BrigadierCommand.literalArgumentBuilder("vuptime")
                .requires(src -> src.hasPermission("vuptime.command"))
                .executes(ctx -> {
                    final TimeUnit defaultUnit = configurationContainer.get().defaultFormatUnit;
                    sendUpTimeMessage(ctx.getSource(), defaultUnit);
                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("timeunit", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            for (final TimeUnit timeUnit : configurationContainer.get().formatMap.keySet()) {
                                builder.suggest(timeUnit.toString());
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            final String providedTimeUnit = StringArgumentType.getString(ctx, "timeunit");
                            final TimeUnit timeUnit = TimeUnit.valueOf(providedTimeUnit);
                            sendUpTimeMessage(ctx.getSource(), timeUnit);
                            return Command.SINGLE_SUCCESS;
                        })
                );
        final BrigadierCommand command = new BrigadierCommand(node);
        final CommandMeta meta = commandManager.metaBuilder(command)
                .plugin(plugin)
                .build();

        commandManager.register(meta, command);
    }

    private void sendUpTimeMessage(final CommandSource source, final TimeUnit timeUnit) {
        final List<String> format = configurationContainer.get().formatMap.get(timeUnit);
        if (format == null) {
            source.sendMessage(miniMessage().deserialize(configurationContainer.get().noFormatAvailable));
            return;
        }

        final long now = System.currentTimeMillis();
        final long uptime = now - VelocityUpTime.uptimeTime;
        final long convertedUpTime =  timeUnit.convert(uptime, TimeUnit.MILLISECONDS);
        final TagResolver resolver = Placeholder.parsed("uptime", Long.toString(convertedUpTime));
        final TextComponent.Builder builder = Component.text();
        for (final String s : format) {
            builder.append(miniMessage().deserialize(s, resolver)).appendNewline();
        }
        source.sendMessage(builder.build());
    }
}
