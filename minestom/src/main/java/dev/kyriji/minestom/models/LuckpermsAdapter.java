package dev.kyriji.minestom.models;

import dev.kyriji.common.config.controllers.ConfigManager;
import dev.kyriji.common.config.documents.CoreConfig;
import dev.kyriji.common.config.enums.ConfigType;
import dev.kyriji.common.config.models.SqlConnection;
import me.lucko.luckperms.common.config.generic.adapter.StringBasedConfigurationAdapter;
import me.lucko.luckperms.common.plugin.LuckPermsPlugin;

public class LuckpermsAdapter extends StringBasedConfigurationAdapter {
	private final LuckPermsPlugin plugin;

	public LuckpermsAdapter(LuckPermsPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	protected String resolveValue(String s) {

		CoreConfig coreConfig = ConfigManager.getConfig(ConfigType.CORE);
		if(coreConfig == null) throw new NullPointerException("CoreConfig is null");

		SqlConnection connection = coreConfig.getLuckPermsConnection();
		if(connection == null) throw new NullPointerException("SqlConnection is null");

		return switch(s) {
			case "storage-method" -> "mariadb";
			case "data.address" -> connection.getHost() + ":" + connection.getPort();
			case "data.database" -> connection.getDatabase();
			case "data.username" -> connection.getUsername();
			case "data.password" -> connection.getPassword();
			default -> null;
		};

	}

	@Override
	public LuckPermsPlugin getPlugin() {
		return plugin;
	}

	@Override
	public void reload() {

	}
}
