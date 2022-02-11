package de.fr3qu3ncy.crazyguides.setup;

import de.fr3qu3ncy.bukkittools.setup.PhaseTask;
import de.fr3qu3ncy.bukkittools.setup.Setup;
import de.fr3qu3ncy.bukkittools.setup.tasks.ChatTask;
import de.fr3qu3ncy.bukkittools.setup.tasks.InteractTask;
import de.fr3qu3ncy.crazyguides.Main;
import de.fr3qu3ncy.crazyguides.guide.PathGuide;
import de.fr3qu3ncy.crazyguides.setup.GuideSetup.SetupPhase;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.util.ArrayList;

public class GuideSetup extends Setup<SetupPhase> {

    private static final String PREFIX = "§7[§6CG§67] ";

    private final PathGuide guide;
    private final Main plugin;

    public GuideSetup(Main plugin, Player player) {
        super(player);
        this.plugin = plugin;
        this.guide = new PathGuide();

        addPhases(
                getNameTask(),
                getPathTask()
        );
    }

    private PhaseTask<SetupPhase> getNameTask() {
        return new PhaseTask<>(SetupPhase.NAME)
                .setStartMessage(PREFIX + "§2Please type the name of this guide into the chat.")
                .addTasks(
                        new ChatTask()
                                .setMessageHandler(guide::setName)
                );
    }

    private PhaseTask<SetupPhase> getPathTask() {
        return new PhaseTask<>(SetupPhase.PATH)
                .disableAutoProgress()
                .setStartMessage(PREFIX + "§2Please start left-clicking the path of this guide.\n§2Undo with right-click.\n" +
                        "§2Finish with §6OK§2.")
                .addTasks(
                        new InteractTask()
                                .setBlockAction(Action.LEFT_CLICK_BLOCK)
                                .setUndoAction(Action.RIGHT_CLICK_BLOCK)
                                .setHandler(this::addBlockToPath)
                                .setUndoHandler(this::removeBlockFromPath),
                        new ChatTask()
                                .setAcceptedMessages("OK")
                                .setMessageHandler(msg -> finishSetup())
                );
    }

    private void addBlockToPath(Location loc) {
        if (guide.getSetupPath().containsKey(loc) || loc.getWorld() == null) return;

        //Add the blockData to the setupPath
        guide.getSetupPath().put(loc, loc.getBlock().getBlockData());

        //Change selected block to green wool
        loc.getWorld().setBlockData(loc, Material.GREEN_WOOL.createBlockData());
    }

    private void removeBlockFromPath(Location loc) {
        if (!guide.getSetupPath().containsKey(loc) || loc.getWorld() == null) return;

        //Reset block
        BlockData formerData = guide.getSetupPath().get(loc);
        loc.getWorld().setBlockData(loc, formerData);

        //Remove block from setupPath
        guide.getSetupPath().remove(loc);
    }

    private void resetSetupBlocks() {
        guide.getSetupPath().forEach((loc, data) -> loc.getWorld().setBlockData(loc, data));
    }

    private void finishSetup() {
        resetSetupBlocks();

        guide.setPath(new ArrayList<>(guide.getSetupPath().keySet()));
        plugin.getGuideManager().addGuide(this.guide);

        player.sendMessage(PREFIX + "§2Guide has been created!");
        done();
    }

    public enum SetupPhase {
        NAME,
        PATH
    }
}
