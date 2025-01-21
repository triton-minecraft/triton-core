package dev.kyriji.common.punishments.models;

import dev.kyriji.common.TritonCoreCommon;
import dev.kyriji.common.chat.controllers.ChatManager;
import dev.kyriji.common.models.TritonProfile;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.NetworkData;
import dev.kyriji.common.playerdata.enums.PlayerDataType;
import dev.kyriji.common.punishments.enums.PunishmentType;
import dev.kyriji.common.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaginatedPunishments {
	private static final int ITEMS_PER_PAGE = 5;
	private List<PunishmentAction> actions;
	private final TritonProfile target;

	public PaginatedPunishments(TritonProfile target) {
		actions = new ArrayList<>();
		this.target = target;
	}

	public List<PunishmentAction> getActions() {
		return actions;
	}

	public void addAction(PunishmentAction action) {
		actions.add(action);
	}

	public int getTotalPages() {
		return (int) Math.ceil((double) actions.size() / ITEMS_PER_PAGE);
	}

	public boolean isValidPage(int page) {
		return page < 1 || page > getTotalPages();
	}

	public List<String> getPageContent(int page) {
		if(isValidPage(page)) {
			return new ArrayList<>();
		}

		List<String> pageContent = new ArrayList<>();
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		for(int i = 0; i < 25; i++) pageContent.add(chatManager.formatMessage("&r&r&r&r&r&r&r&r&r&r"));

		pageContent.add(chatManager.formatMessage("&ePunishment History for &c" + target.getName() + " &7(&f" + page + "/" + getTotalPages() + "&7)"));
		pageContent.add(chatManager.formatMessage("&8&m                                                      &f"));

		int startIndex = (page - 1) * ITEMS_PER_PAGE;
		int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, actions.size());

		for (int i = startIndex; i < endIndex; i++) {
			PunishmentAction action = actions.get(i);
			pageContent.addAll(getActionDisplay(action));
			pageContent.add(chatManager.formatMessage("&8&m                                                      &f"));
		}

		return pageContent;
	}

	private List<String> getActionDisplay(PunishmentAction action) {
		ChatManager chatManager = TritonCoreCommon.INSTANCE.getChatManager();

		String issueTime = TimeUtils.toRelativeTimestamp(action.getTime());
		if (Character.isLetter(issueTime.charAt(0))) {
			issueTime = Character.toUpperCase(issueTime.charAt(0)) + issueTime.substring(1);
		}

		String issuerName = getIssuerName(action);
		String reason = action.getReason();
		String punishmentType = action.getPunishmentType().getName() + action.getPunishmentType().getSuffix();

		List<String> messages = new ArrayList<>();
		messages.add(chatManager.formatMessage("&f&o" + issueTime));
		messages.add(chatManager.formatMessage("&e" + issuerName + " &7" + punishmentType + " &c" + target.getName() +
				(action.getPunishmentType().requiresReason() ? " &7for &f\"" + reason + "\"" : "")));

		if(action instanceof TimedPunishmentAction timedAction) {
			long expiry = timedAction.getDuration() == -1 ? -1 : (timedAction.getTime() + (timedAction.getDuration() * 1000));
			String expiryTime = TimeUtils.toRelativeTimestamp(expiry);

			boolean active = !isExpired(timedAction);
			String status = active ? "&aActive" : "&cInactive";

			messages.add(chatManager.formatMessage("&7Expires &f" + expiryTime + " &7(" + status + "&7)"));
		}

		return messages;
	}

	private String getIssuerName(PunishmentAction action) {
		UUID issuerUUID = UUID.fromString(action.getIssuer());
		if(issuerUUID == TritonCoreCommon.CONSOLE_UUID) return "Console";

		NetworkData issuerData = PlayerDataManager.getTemporaryPlayerData(issuerUUID, PlayerDataType.NETWORK);
		if(issuerData == null) return "Unknown";

		return issuerData.getName();
	}

	private boolean isExpired(TimedPunishmentAction timedAction) {
		long expiry = timedAction.getDuration() == -1 ? -1 : (timedAction.getTime() + (timedAction.getDuration() * 1000));
		if(expiry != -1 && System.currentTimeMillis() >= expiry) {
			return true;
		}

		PunishmentType overrideType = timedAction.getPunishmentType() == PunishmentType.MUTE
				? PunishmentType.UNMUTE : PunishmentType.UNBAN;

		boolean hasRemoval = actions.stream()
				.anyMatch(action ->
						action.getPunishmentType() == overrideType &&
								action.getTime() > timedAction.getTime()
				);

		if(hasRemoval) {
			return true;
		}

		return actions.stream()
				.anyMatch(action ->
						action.getPunishmentType() == timedAction.getPunishmentType() &&
								action.getTime() > timedAction.getTime()
				);
	}
}