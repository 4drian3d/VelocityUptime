package io.github._4drian3d.velocityuptime.configuration;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ConfigSerializable
public class Configuration {
    @Comment("Default Format")
    public TimeUnit defaultFormatUnit = TimeUnit.SECONDS;

    @Comment("TimeUnit to format")
    public Map<TimeUnit, List<String>> formatMap = Map.of(
            TimeUnit.MILLISECONDS, List.of(
                    "<aqua>Velocity <gold>UpTime <gray>in milliseconds: <white><uptime>"
            ),
            TimeUnit.SECONDS, List.of(
                    "<aqua>Velocity <gold>UpTime <gray>in seconds: <white><uptime>"
            ),
            TimeUnit.MINUTES, List.of(
                    "<aqua>Velocity <gold>UpTime <gray>in minutes: <white><uptime>"
            ),
            TimeUnit.HOURS, List.of(
                    "<aqua>Velocity <gold>UpTime <gray>in hours: <white><uptime>"
            )
    );

    @Comment("No format available message")
    public String noFormatAvailable = "<red>No format available for the provided time unit";
}
