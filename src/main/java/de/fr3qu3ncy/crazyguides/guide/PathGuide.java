package de.fr3qu3ncy.crazyguides.guide;

import com.sun.jna.platform.unix.X11;
import de.fr3qu3ncy.crazyguides.particle.ArrowParticlePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathGuide implements ConfigurationSerializable {

    public static JavaPlugin plugin;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private List<Location> path;

    @Getter
    private Map<Location, BlockData> setupPath;

    public PathGuide(String name, List<Location> path) {
        this.name = name;
        this.path = path;
    }

    public PathGuide() {
        this.path = new ArrayList<>();
        this.setupPath = new HashMap<>();
    }

    public void play() {
        for (int index = 0 ; index < path.size() - 1 ; index++) {
            Location loc = path.get(index);

            ArrowParticlePlayer player = new ArrowParticlePlayer(plugin, loc, getArrowRotation(index));
            player.debug = true;
            player.playParticles();
        }
    }

    private float getArrowRotation(int index) {
        //Stop if there is not at least 1 next block
        if (index < 0 || index >= path.size()) return 0F;

        //Load the current and the next block
        Location currentBlock = path.get(index);
        Location nextBlock = path.get(index + 1);

        return currentBlock.toVector().angle(nextBlock.toVector());
    }

    @Nonnull @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("name", this.name);
        map.put("path", this.path);

        return map;
    }

    public static PathGuide deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        List<Location> path = (List<Location>) map.get("path");

        return new PathGuide(name, path);
    }
}
