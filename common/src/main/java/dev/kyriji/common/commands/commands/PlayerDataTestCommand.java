package dev.kyriji.common.commands.commands;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.NetworkDocument;
import dev.kyriji.common.playerdata.enums.PlayerDataType;

import java.util.Arrays;
import java.util.List;

public class PlayerDataTestCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "playerdata";
	}

	@Override
	public String getDescription() {
		return "A test command";
	}

	@Override
	public CommandType getType() {
		return CommandType.SERVER;
	}

	@Override
	public List<String> getTabCompletions(TritonCommandSender sender, String[] args) {
		return Arrays.asList("test1", "test2", "test3");
	}

	@Override
	public void execute(TritonCommandSender sender, String[] args) {
		if(!(sender instanceof TritonPlayer player)) return;
		NetworkDocument document = PlayerDataManager.getPlayerData(player.getUuid(), PlayerDataType.NETWORK);
		if(document == null) {
			player.sendMessage("Failed to load player data");
			return;
		}

		if(args.length >= 1 && args[0].equals("save")) {
			document.setName(player.getName());
			document.save();
			return;
		}

		player.sendMessage(document.getName());
	}

}
