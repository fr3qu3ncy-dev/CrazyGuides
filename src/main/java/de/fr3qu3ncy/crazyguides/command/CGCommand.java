package de.fr3qu3ncy.crazyguides.command;

import de.fr3qu3ncy.bukkittools.commands.BukkitCommand;

public class CGCommand extends BukkitCommand {

    @Override
    public String getName() {
        return "crazyguides";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
