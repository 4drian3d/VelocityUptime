package io.github._4drian3d.velocityuptime;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import io.github._4drian3d.velocityuptime.command.UpTimeCommand;
import io.github._4drian3d.velocityuptime.configuration.ConfigurationContainer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.nio.file.Path;

@Plugin(
	id = "velocityuptime",
	name = "VelocityUpTime",
	description = "A Velocity UpTime plugin",
	version = Constants.VERSION,
	authors = { "4drian3d" }
)
public final class VelocityUpTime {
	public static final long uptimeTime = System.currentTimeMillis();
	@Inject
	private ComponentLogger logger;
	@Inject
	@DataDirectory
	private Path dataDirectory;
	@Inject
	private Injector injector;
	
	@Subscribe
	void onProxyInitialization(final ProxyInitializeEvent event) {
		logger.info("Stating VelocityUpTime");
		final ConfigurationContainer configurationContainer
				= ConfigurationContainer.load(logger, dataDirectory, "config");

		injector = injector.createChildInjector(
				binder -> binder.bind(ConfigurationContainer.class).toInstance(configurationContainer)
		);

		injector.getInstance(UpTimeCommand.class).register();
	}
}