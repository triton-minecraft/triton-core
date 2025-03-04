package dev.kyriji.common.punishments.controllers;

import com.google.gson.*;
import dev.kyriji.bigminecraftapi.BigMinecraftAPI;
import dev.kyriji.bigminecraftapi.controllers.RedisListener;
import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.commands.commands.punishments.*;
import dev.kyriji.common.commands.controllers.CommandManager;
import dev.kyriji.common.enums.ServerType;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.punishments.enums.PunishmentType;
import dev.kyriji.common.punishments.hooks.TritonPunishmentHook;
import dev.kyriji.common.punishments.models.PunishmentAction;
import dev.kyriji.common.punishments.models.TimedPunishmentAction;
import dev.kyriji.common.punishments.utils.PunishmentUtils;

import java.lang.reflect.Type;
import java.util.UUID;

public class PunishmentManager {

	private static final String PUNISHMENT_CHANNEL = "punishments";
	private final TritonPunishmentHook hook;
	private final Gson gson;

	public PunishmentManager(TritonPunishmentHook hook) {
		this.hook = hook;

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(PunishmentAction.class, new PunishmentActionTypeAdapter());
		this.gson = gsonBuilder.create();
	}

	public void init() {
		CommandManager commandManager = TritonCoreCommon.INSTANCE.getCommandManager();
		if(commandManager == null) throw new NullPointerException("The command feature must be registered to use this feature");

		commandManager.registerCommand(new BanCommand());
		commandManager.registerCommand(new IpBanCommand());
		commandManager.registerCommand(new KickCommand());
		commandManager.registerCommand(new MuteCommand());
		commandManager.registerCommand(new UnmuteCommand());
		commandManager.registerCommand(new UnbanCommand());
		commandManager.registerCommand(new WarnCommand());

		commandManager.registerCommand(new LogsCommand());

		registerPunishmentListener();
		registerJoinCallback();
		registerChatCallback();
	}

	public void broadcastPunishment(TritonProfile target, PunishmentAction action) {
		String actionJson = gson.toJson(action);
		BigMinecraftAPI.getRedisManager().publish(PUNISHMENT_CHANNEL, target.getUuid().toString() + "|" + actionJson);
	}

	private void registerPunishmentListener() {
		if(TritonCoreCommon.SERVER_TYPE != ServerType.VELOCITY) return;

		BigMinecraftAPI.getRedisManager().addListener(new RedisListener(PUNISHMENT_CHANNEL) {
			@Override
			public void onMessage(String s) {
				UUID target = UUID.fromString(s.split("\\|")[0]);
				PunishmentAction action = gson.fromJson(s.split("\\|")[1], PunishmentAction.class);

				hook.getOnlinePlayers().forEach(player -> {
					if(!player.getUuid().equals(target)) return;

					switch(action.getPunishmentType()) {
						case BAN, IP_BAN:
							applyBan(player, action);
							break;
						case KICK:
							applyKick(player, action);
							break;
						case MUTE:
							applyMute(player, action);
							break;
						case UNMUTE:
							applyUnmute(player, action);
							break;
						case WARN:
							applyWarn(player, action);
							break;
						case UNBAN:
							break;
					}
				});
			}
		});
	}

	private void applyBan(TritonPlayer player, PunishmentAction action) {
		if(action instanceof TimedPunishmentAction) {
			player.disconnect(PunishmentUtils.getBanMessage((TimedPunishmentAction) action));
		} else throw new IllegalArgumentException("Ban action must be timed");
	}

	private void applyKick(TritonPlayer player, PunishmentAction action) {
		player.disconnect(PunishmentUtils.getKickMessage(action));
	}

	private void applyMute(TritonPlayer player, PunishmentAction action) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		if(action instanceof TimedPunishmentAction) {
			player.sendMessage(chatManager.formatMessage(PunishmentUtils.getMuteMessage(((TimedPunishmentAction) action))));
		} else throw new IllegalArgumentException("Mute action must be timed");
	}

	private void applyWarn(TritonPlayer player, PunishmentAction action) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();
		player.sendMessage(chatManager.formatMessage(PunishmentUtils.getWarnMessage(action)));
	}


	private void applyUnmute(TritonPlayer player, PunishmentAction action) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();
		player.sendMessage(chatManager.formatMessage(PunishmentUtils.getUnmuteMessage(action)));
	}

	private void registerJoinCallback() {
		hook.registerJoinCallback(player -> {
			PunishmentAction ban = PunishmentUtils.getActiveBan(player);
			if(ban == null) return;

			player.disconnect(PunishmentUtils.getBanMessage((TimedPunishmentAction) ban));
		});
	}

	private void registerChatCallback() {
		hook.registerChatCallback(player -> {
			PunishmentAction mute = PunishmentUtils.getActiveMute(player);

			if(mute != null) {
				ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();
				player.sendMessage(chatManager.formatMessage(PunishmentUtils.getMuteMessage((TimedPunishmentAction) mute)));
			}

			return PunishmentUtils.getActiveMute(player) != null;
		});
	}

	public static class PunishmentActionTypeAdapter implements JsonDeserializer<PunishmentAction> {
		@Override
		public PunishmentAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();

			boolean isTimed = jsonObject.has("duration");

			if(isTimed) {
				TimedPunishmentAction timedAction = new TimedPunishmentAction();
				deserializeCommonFields(timedAction, jsonObject);
				timedAction.setDuration(jsonObject.get("duration").getAsLong());
				return timedAction;
			} else {
				PunishmentAction action = new PunishmentAction();
				deserializeCommonFields(action, jsonObject);
				return action;
			}
		}

		private void deserializeCommonFields(PunishmentAction action, JsonObject jsonObject) {
			if(jsonObject.has("punishmentType")) {
				action.setPunishmentType(PunishmentType.valueOf(jsonObject.get("punishmentType").getAsString()));
			}
			if(jsonObject.has("reason")) {
				action.setReason(jsonObject.get("reason").getAsString());
			}
			if(jsonObject.has("issuer")) {
				action.setIssuer(String.valueOf(UUID.fromString(jsonObject.get("issuer").getAsString())));
			}
		}
	}


}
