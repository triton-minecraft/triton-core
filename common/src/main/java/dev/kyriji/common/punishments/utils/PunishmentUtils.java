package dev.kyriji.common.punishments.utils;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.chat.utils.ChatUtils;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.PunishmentData;
import dev.kyriji.common.playerdata.enums.Permission;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.playerdata.utils.PlayerDataUtils;
import dev.kyriji.common.punishments.models.PaginatedPunishments;
import dev.kyriji.common.punishments.models.TimedPunishmentAction;
import dev.kyriji.common.punishments.models.PunishmentAction;
import dev.kyriji.common.punishments.enums.PunishmentType;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.utils.TimeUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class PunishmentUtils {

	public static void applyPunishment(TritonCommandSender player, String[] args, PunishmentType punishmentType) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		if(player instanceof TritonPlayer) {
			if(!((TritonPlayer) player).hasPermission(Permission.STAFF.getIdentifier())) {
				player.sendMessage(chatManager.formatMessage("&cYou do not have permission to use this command"));
				return;
			}
		}

		if(args.length < 1) {
			sendUsageMessage(player, punishmentType);
			return;
		}

		TritonProfile target = findTargetPlayer(args[0]);
		if(target == null) {
			player.sendMessage(chatManager.formatMessage("&cPlayer not found"));
			return;
		}

		if(punishmentType == PunishmentType.UNBAN || punishmentType == PunishmentType.UNMUTE) {
			PunishmentType checkType = punishmentType == PunishmentType.UNBAN ? PunishmentType.BAN : PunishmentType.MUTE;

			PunishmentAction activePunishment = getActivePunishment(target, checkType, punishmentType);

			if(activePunishment == null) {
				String punishmentName = checkType.getName().toLowerCase();
				player.sendMessage(chatManager.formatMessage("&cThis player is not currently " + punishmentName + checkType.getSuffix()));
				return;
			}
		}

		PunishmentDetails details = parsePunishmentDetails(args, punishmentType);

		PunishmentAction punishment = createPunishment(player, target, punishmentType, details);
		savePunishment(target, punishment);

		player.sendMessage(chatManager.formatMessage("&aPunishment action applied to &e" + target.getName()));
		broadcastPunishment(player, target, punishment, details.duration);
	}

	private static void sendUsageMessage(TritonCommandSender player, PunishmentType punishmentType) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();
		StringBuilder usage = new StringBuilder("&cUsage: /" + punishmentType.getName().toLowerCase().replace(" ", "") + " <player>");

		switch (punishmentType) {
			case BAN:
			case MUTE:
				usage.append(" [duration] [reason]");
				break;
			case KICK:
			case WARN:
				usage.append(" [reason]");
				break;
			case UNBAN:
			case UNMUTE:
				break;
		}

		player.sendMessage(chatManager.formatMessage(usage.toString()));
	}

	public static TritonProfile findTargetPlayer(String targetString) {
		TritonProfile target = PlayerDataUtils.loadUser(targetString);
		if(target == null) {
			try {
				target = PlayerDataUtils.loadUser(UUID.fromString(targetString));
			} catch (IllegalArgumentException ignored) { }
		}
		return target;
	}

	private record PunishmentDetails(long duration, String reason) {
	}

	private static PunishmentDetails parsePunishmentDetails(String[] args, PunishmentType punishmentType) {
		long duration = -1;
		String defaultReason = "You have been " + punishmentType.getName() + punishmentType.getSuffix();

		if(punishmentType == PunishmentType.UNBAN || punishmentType == PunishmentType.UNMUTE) {
			return new PunishmentDetails(-1, "");
		}

		if(args.length == 1) {
			return new PunishmentDetails(duration, defaultReason);
		}

		String reason = defaultReason;

		if(punishmentType == PunishmentType.BAN || punishmentType == PunishmentType.MUTE) {
			try {
				duration = TimeUtils.parseDuration(args[1]);
				if(args.length > 2) {
					reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
				}
			} catch (NumberFormatException e) {
				reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
			}
		} else if(punishmentType == PunishmentType.KICK || punishmentType == PunishmentType.WARN) {
			reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		}

		return new PunishmentDetails(duration, reason);
	}

	private static PunishmentAction createPunishment(TritonCommandSender player, TritonProfile target,
													 PunishmentType punishmentType, PunishmentDetails details) {
		PunishmentAction punishment;

		if(punishmentType == PunishmentType.BAN || punishmentType == PunishmentType.MUTE) {
			punishment = new TimedPunishmentAction();
			((TimedPunishmentAction) punishment).setDuration(details.duration);
		} else {
			punishment = new PunishmentAction();
		}

		punishment.setPunishmentType(punishmentType);
		punishment.setIssuer(player.getUuid().toString());
		punishment.setReason(details.reason);
		punishment.setTime(System.currentTimeMillis());
		punishment.setId(UUID.randomUUID().toString());

		return punishment;
	}

	private static void savePunishment(TritonProfile target, PunishmentAction punishment) {
		PunishmentData data = PlayerDataManager.getTemporaryPlayerData(target.getUuid(), PlayerDataType.PUNISHMENT);
		if(data == null) {
			throw new NullPointerException("Punishment data not found for " + target.getName());
		}

		data.addPunishment(punishment);
		data.save();
	}

	private static void broadcastPunishment(TritonCommandSender player, TritonProfile target,
											PunishmentAction punishment, long duration) {
		TritonCoreCommon.INSTANCE.getPunishmentManager().broadcastPunishment(target, punishment);

		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();
		String staffMessage = formatStaffMessage(player, target, punishment, duration);
		chatManager.getStaffChatManager().sendStaffMessage(chatManager.formatMessage(staffMessage));
	}

	private static String formatStaffMessage(TritonCommandSender player, TritonProfile target,
											 PunishmentAction punishment, long duration) {
		StringBuilder message = new StringBuilder()
				.append("&e").append(player.getName())
				.append(" &7has ").append(punishment.getPunishmentType().getName().toLowerCase())
				.append(punishment.getPunishmentType().getSuffix())
				.append(" &c").append(target.getName());

		if(punishment.getPunishmentType() != PunishmentType.UNBAN &&
				punishment.getPunishmentType() != PunishmentType.UNMUTE) {
			message.append(" &7for &f\"").append(punishment.getReason()).append("&f\"");
		}

		if(punishment.getPunishmentType() == PunishmentType.BAN ||
				punishment.getPunishmentType() == PunishmentType.MUTE) {
			message.append(" &8(&7")
					.append(duration == -1 ? "Permanent" : TimeUtils.formatDuration(duration))
					.append("&8)");
		}

		return message.toString();
	}

	public static String getBanMessage(TimedPunishmentAction punishmentAction) {
		return String.format(ChatManager.SERVER_LOGO + "\n\n" +
				"&cYou have been banned from the network\n" +
				"&7Reason: &f%s\n" +
				"&7Duration: &f%s\n\n" +
				"&bAppeal at &fhttps://tritonmc.com/appeal",
				punishmentAction.getReason(),
				punishmentAction.getDuration() == -1 ? "Permanent" : TimeUtils.formatDuration(punishmentAction.getDuration()));
	}

	public static String getMuteMessage(TimedPunishmentAction punishmentAction) {
		return String.format("""
						&8&m                                                  &f\s
						&cYou have been muted.
						&7Reason: &f%s
						&7Duration: &f%s
						&fAppeal at &fhttps://tritonmc.com/appeal
						&8&m                                                  &f\s""",
				punishmentAction.getReason(),
				punishmentAction.getDuration() == -1 ? "Permanent" : TimeUtils.formatDuration(punishmentAction.getDuration()));
	}

	public static String getKickMessage(PunishmentAction punishmentAction) {
		return String.format(ChatManager.SERVER_LOGO + "\n\n" +
						"&cYou have been kicked from the network" +
						"&7Reason: &f%s\n\n" +
						"&bFurther violations may in a ban",
				punishmentAction.getReason());
	}

	public static String getWarnMessage(PunishmentAction punishmentAction) {
		return String.format("""
						&8&m                                                  &f\s
						&cYou have been warned.
						&7Reason: &f%s
						&fFurther violations may result in a mute or ban.
						&8&m                                                  &f\s""",
				punishmentAction.getReason());
	}

	public static String getUnmuteMessage(PunishmentAction punishmentAction) {
		return "&aYou have been unmuted";
	}

	public static PunishmentAction getActivePunishment(TritonProfile player, PunishmentType type, PunishmentType overrideType) {
		PunishmentData data = PlayerDataManager.getTemporaryPlayerData(player.getUuid(), PlayerDataType.PUNISHMENT);
		if(data == null) return null;

		List<PunishmentAction> punishments = data.getPunishments().stream()
				.filter(punishment -> punishment.getPunishmentType() == type)
				.toList();

		if(punishments.isEmpty()) return null;

		PunishmentAction mostRecentOverride = data.getPunishments().stream()
				.filter(punishment -> punishment.getPunishmentType() == overrideType)
				.max(Comparator.comparingLong(PunishmentAction::getTime))
				.orElse(null);

		for (PunishmentAction punishment : punishments) {
			if(mostRecentOverride != null && mostRecentOverride.getTime() > punishment.getTime()) {
				continue;
			}

			if(punishment instanceof TimedPunishmentAction timedPunishment) {
				long duration = timedPunishment.getDuration();
				if(duration == -1 || System.currentTimeMillis() < punishment.getTime() + duration * 1000) {
					return punishment;
				}
			} else {
				System.err.println("Unexpected PunishmentAction type: " + punishment.getClass().getName());
			}
		}

		return null;
	}

	public static PaginatedPunishments paginatePunishments(TritonProfile player) {
		PunishmentData data = PlayerDataManager.getTemporaryPlayerData(player.getUuid(), PlayerDataType.PUNISHMENT);
		if(data == null) return null;

		List<PunishmentAction> punishments = data.getPunishments();
		punishments.sort(Comparator.comparingLong(PunishmentAction::getTime).reversed());

		PaginatedPunishments paginatedPunishments = new PaginatedPunishments(player);
		punishments.forEach(paginatedPunishments::addAction);
		return paginatedPunishments;
	}

	public static PunishmentAction getActiveBan(TritonPlayer player) {
		return getActivePunishment(player, PunishmentType.BAN, PunishmentType.UNBAN);
	}

	public static PunishmentAction getActiveMute(TritonPlayer player) {
		return getActivePunishment(player, PunishmentType.MUTE, PunishmentType.UNMUTE);
	}

}