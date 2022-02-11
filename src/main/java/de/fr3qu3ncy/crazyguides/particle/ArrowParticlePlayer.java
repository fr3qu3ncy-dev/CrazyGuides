package de.fr3qu3ncy.crazyguides.particle;

import de.fr3qu3ncy.bukkittools.particle.ColoredParticlePlayer;
import net.minecraft.util.Tuple;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrowParticlePlayer extends ColoredParticlePlayer {

    private final Location loc;
    private final double rotation;

    public boolean debug;

    public ArrowParticlePlayer(JavaPlugin plugin, Location loc, double rotation) {
        super(plugin, 10, 0, 1, -1, true, Collections.singletonList(Color.ORANGE));
        this.loc = loc;
        this.rotation = rotation;
    }

    @Override
    protected void calculateParticles() {
        double centerX = loc.getX() + 0.5D;
        double topY = loc.getY() + 1.1D;
        double centerZ = loc.getZ() + 0.5D;

        double minX = centerX - 0.5D;
        double minZ = centerZ - 0.5D;

        double maxX = centerX + 0.5D;
        double maxZ = centerZ + 0.5D;

        if (debug) {
            System.out.println("minX: " + minX);
            System.out.println("maxX: " + maxX);

            System.out.println("minZ: " + minZ);
            System.out.println("maxZ: " + maxZ);

            System.out.println("centerX: " + centerX);
            System.out.println("centerZ: " + centerZ);
        }

        List<Double> arrowBaseX = getArrowBase(minX, maxX);
        List<Tuple<Double, Double>> arrowHead = getArrowHead(maxX, centerZ);

        arrowBaseX.forEach(x -> play(x, topY, centerZ));
        arrowHead.forEach(bounds -> play(bounds.a(), topY, bounds.b()));

        debug = false;
    }

    private void play(double x, double y, double z) {
        World world = loc.getWorld();
        if (world == null) return;

        if (debug) {
            System.out.println("before X: " + x);
            System.out.println("before Z: " + z);
        }

        //Rotate the position to the next block
        //x = x * Math.cos(Math.toRadians(rotation)) - y * Math.sin(Math.toRadians(rotation));
        //y = x * Math.sin(Math.toRadians(rotation)) + y * Math.cos(Math.toRadians(rotation));

        if (debug) {
            System.out.println("after X: " + x);
            System.out.println("after Z: " + z);
        }

        Location spawnLocation = new Location(loc.getWorld(), x, y, z);

        Particle.DustOptions dustOptions = new Particle.DustOptions(
                Color.fromRGB(
                        Math.round(255F * (getCurrentColorRed() != 0f ? getCurrentColorRed() : 0.0001F)),
                        Math.round(255F * getCurrentColorGreen()),
                        Math.round(255F * getCurrentColorBlue())), 1/6F);

        getNearbyPlayers(spawnLocation).forEach(player -> player.spawnParticle(Particle.REDSTONE, spawnLocation, 0, dustOptions));
    }

    private List<Double> getArrowBase(double minX, double maxX) {
        List<Double> list = new ArrayList<>();

        double startX = minX + 1/6D;
        double endX = maxX - 1/6D;

        if (debug) {
            System.out.println("Arrow base startX: " + startX);
            System.out.println("Arrow base endX: " + endX);
        }

        for (double x = startX ; x <= endX ; x+= 1/12D) {
            list.add(x);
        }
        return list;
    }

    private List<Tuple<Double, Double>> getArrowHead(double maxX, double centerZ) {
        List<Tuple<Double, Double>> list = new ArrayList<>();

        list.addAll(getDiagonal(maxX, centerZ, 45D));
        list.addAll(getDiagonal(maxX, centerZ, 360D - 45D));

        return list;
    }

    private List<Tuple<Double, Double>> getDiagonal(double maxX, double centerZ, double angle) {
        List<Tuple<Double, Double>> list = new ArrayList<>();
        double length = 1/3D;

        double endX = Math.cos(Math.toRadians(angle)) * length + maxX;

        double width = endX - maxX;
        double particleCount = width / (1/12D);

        for (int i = 0 ; i < particleCount ; i++) {
            double flag = angle > 180 ? -1D : 1D;
            list.add(new Tuple<>((maxX + (1/12D)) + i * (1/12D), centerZ + flag * i * (1/12D)));
        }

        return list;
    }
}
