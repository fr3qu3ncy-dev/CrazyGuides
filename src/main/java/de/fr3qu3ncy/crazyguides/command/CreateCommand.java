package de.fr3qu3ncy.crazyguides.command;

import de.fr3qu3ncy.bukkittools.commands.SubCommand;
import de.fr3qu3ncy.bukkittools.setup.Setup;
import de.fr3qu3ncy.crazyguides.Main;
import de.fr3qu3ncy.crazyguides.setup.GuideManager;
import de.fr3qu3ncy.crazyguides.setup.GuideSetup;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand extends SubCommand {

    private final Main main;

    public CreateCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean getCondition(String[] args) {
        return args.length >= 1 && args[0].equalsIgnoreCase("create");
    }

    @Override
    public String getPermission() {
        return "cg.create";
    }

    @Override
    public void performCommand(Player player, String[] args) {
        GuideManager manager = main.getGuideManager();
        List<Setup<?>> setups = Setup.SETUPS.get(player.getUniqueId());

        if (setups != null && setups.size() > 0) {
            player.sendMessage("§cYou have to finish your current setup first!");
            return;
        }

        GuideSetup setup = new GuideSetup(main, player);
        setup.start();
    }
}
