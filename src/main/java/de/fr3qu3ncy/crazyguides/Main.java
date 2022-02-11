package de.fr3qu3ncy.crazyguides;

import de.fr3qu3ncy.bukkittools.BukkitPlugin;
import de.fr3qu3ncy.bukkittools.commands.BukkitCommand;
import de.fr3qu3ncy.crazyguides.command.CGCommand;
import de.fr3qu3ncy.crazyguides.command.CreateCommand;
import de.fr3qu3ncy.crazyguides.guide.PathGuide;
import de.fr3qu3ncy.crazyguides.setup.GuideManager;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter
    private GuideManager guideManager;

    @Override
    public void onEnable() {
        BukkitPlugin.onEnable(this);

        ConfigurationSerialization.registerClass(PathGuide.class);

        this.guideManager = new GuideManager(this);

        registerCommands();
    }

    private void registerCommands() {
        BukkitCommand cmd = new CGCommand();

        cmd.registerSubCommand(new CreateCommand(this));

        getCommand("crazyguides").setExecutor(cmd);
    }
}
