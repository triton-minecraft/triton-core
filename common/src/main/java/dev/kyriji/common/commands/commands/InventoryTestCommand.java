package dev.kyriji.common.commands.commands;

import dev.kyriji.common.commands.enums.CommandType;
import dev.kyriji.common.commands.models.TritonCommand;
import dev.kyriji.common.inventory.enums.TritonMaterial;
import dev.kyriji.common.inventory.models.TritonInventory;
import dev.kyriji.common.inventory.models.TritonItemStack;
import dev.kyriji.common.inventory.records.InventoryClickInfo;
import dev.kyriji.common.models.TritonCommandSender;
import dev.kyriji.common.models.TritonPlayer;
import dev.kyriji.common.playerdata.controllers.PlayerDataManager;
import dev.kyriji.common.playerdata.documents.NetworkData;
import dev.kyriji.common.playerdata.enums.PlayerDataType;

import java.util.Arrays;
import java.util.List;

public class InventoryTestCommand extends TritonCommand {
	@Override
	public String getIdentifier() {
		return "inventory";
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

		TritonInventory inventory = new TritonInventory(3, "Test Inventory", false) {
			@Override
			public void onClick(InventoryClickInfo clickInfo) {
				clickInfo.player().sendMessage("You clicked slot " + clickInfo.slot());

				if(clickInfo.slot() == 0) close(clickInfo.player());
			}

			@Override
			public void onClose(TritonPlayer player) {

			}
		};

		TritonItemStack item = new TritonItemStack(TritonMaterial.DIAMOND, 1);
		item.setDisplayName("Diamond");
		item.setLore(List.of("A shiny diamond"));

		inventory.setItem(0, item);

		inventory.open(player);

	}

}
