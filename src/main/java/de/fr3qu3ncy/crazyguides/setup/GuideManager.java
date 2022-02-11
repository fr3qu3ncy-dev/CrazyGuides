package de.fr3qu3ncy.crazyguides.setup;

import de.fr3qu3ncy.bukkittools.datastorage.YAMLStorage;
import de.fr3qu3ncy.crazyguides.guide.PathGuide;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GuideManager {

    private static final List<PathGuide> GUIDES = new ArrayList<>();

    private final JavaPlugin plugin;
    private final YAMLStorage storage;

    public GuideManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.storage = new YAMLStorage(plugin, "plugins/CrazyGuides", "guides.yml", false);

        PathGuide.plugin = plugin;

        loadGuides();
    }

    private void loadGuides() {
        plugin.getLogger().info("Loading guides...");
        int i = 0;
        for (String name : storage.getData().getKeys(false)) {
            PathGuide guide = storage.getData().getSerializable(name, PathGuide.class);

            System.out.println("Checking " + name);

            if (guide == null) continue;

            GUIDES.add(guide);
            guide.play();

            i++;
        }
        plugin.getLogger().info("Loaded " + i + " guides!");
    }

    public void addGuide(PathGuide guide) {
        if (containsWithName(guide.getName())) return;

        storage.getData().set(guide.getName(), guide);
        storage.saveDataFile();

        GUIDES.add(guide);

        guide.play();
    }

    private boolean containsWithName(String name) {
        return GUIDES.stream().anyMatch(guide -> guide.getName().equalsIgnoreCase(name));
    }
}
