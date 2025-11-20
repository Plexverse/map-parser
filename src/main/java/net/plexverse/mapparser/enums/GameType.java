package net.plexverse.mapparser.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Getter
public class GameType {
    private static final Map<String, GameType> GAME_TYPES = new HashMap<>();

    private final String name;
    private final String displayName;
    private final Map<String, Integer> requirements;
    private final List<DataPointType> dataPointTypeList;
    private final Material material;

    private GameType(final String name, final String displayName, final Map<String, Integer> requirements,
                     final List<DataPointType> dataPointTypeList, final Material material) {
        this.name = name;
        this.displayName = displayName;
        this.requirements = requirements;
        this.dataPointTypeList = dataPointTypeList;
        this.material = material;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    @JsonCreator
    public static GameType fromName(final String name) {
        return GameType.valueOf(name.toUpperCase());
    }

    public String name() {
        return this.name.toUpperCase();
    }

    private static void loadDataPointTypes(final JavaPlugin plugin) {
        final File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdirs()) {
                plugin.getLogger().severe("Could not create plugin data folder!");
                return;
            }
            try {
                final FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                GameType.addDefaultValues(config);
                config.save(configFile);
                plugin.getLogger().info("Default config.yml has been generated.");
            } catch (final IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not generate default config.yml", e);
            }
        }
    }

    private static Map<String, Integer> getMicroBattlesRequirements() {
        final Map<String, Integer> requirements = new HashMap<>();
        requirements.put("SPAWNPOINT_RED", 4);
        requirements.put("SPAWNPOINT_GREEN", 4);
        requirements.put("SPAWNPOINT_AQUA", 4);
        requirements.put("SPAWNPOINT_YELLOW", 4);
        requirements.put("HOLOGRAM_RED", 1);
        requirements.put("HOLOGRAM_GREEN", 1);
        requirements.put("HOLOGRAM_AQUA", 1);
        requirements.put("HOLOGRAM_YELLOW", 1);
        requirements.put("WALLPOINT_RED", 10);
        requirements.put("WALLPOINT_GREEN", 10);
        requirements.put("WALLPOINT_AQUA", 10);
        requirements.put("WALLPOINT_YELLOW", 10);
        requirements.put("SPECTATOR_SPAWNPOINT", 1);
        requirements.put("BORDER", 2);
        return requirements;
    }

    private static void addDefaultValues(final FileConfiguration config) {
        config.createSection("gameTypes");

        // Example GameTypes
        config.set("gameTypes.MICRO_BATTLES.displayName", "Micro Battles");
        config.set("gameTypes.MICRO_BATTLES.requirements", GameType.getMicroBattlesRequirements());
        config.set("gameTypes.MICRO_BATTLES.dataPointTypes", List.of("SPAWNPOINT", "HOLOGRAM", "SPECTATOR_SPAWNPOINT", "BORDER", "WALLPOINT"));
        config.set("gameTypes.MICRO_BATTLES.material", "GLASS");

        config.set("gameTypes.SPEED_BUILDERS.displayName", "Speed Builders");
        config.set("gameTypes.SPEED_BUILDERS.requirements", Map.of(
                "SPAWNPOINT_1", 2,
                "ISLAND_BORDER_1", 2,
                "ISLAND_BUILD_BORDER_1", 2,
                "HOLOGRAM_1", 1,
                "CENTER", 1,
                "BORDER", 2,
                "SPECTATOR_SPAWNPOINT", 1
        ));
        config.set("gameTypes.SPEED_BUILDERS.dataPointTypes", List.of("SPAWNPOINT", "HOLOGRAM", "ISLAND_BORDER", "ISLAND_BUILD_BORDER", "CENTER", "BORDER", "SPECTATOR_SPAWNPOINT"));
        config.set("gameTypes.SPEED_BUILDERS.material", "GUARDIAN_SPAWN_EGG");

        config.set("gameTypes.SPEED_BUILDERS_MINIBUILDS.displayName", "SB Minibuilds");
        config.set("gameTypes.SPEED_BUILDERS_MINIBUILDS.requirements", Map.of(
                "MINIBUILD", 1,
                "MOB", 1
        ));
        config.set("gameTypes.SPEED_BUILDERS_MINIBUILDS.dataPointTypes", List.of("MOB", "MINIBUILD"));
        config.set("gameTypes.SPEED_BUILDERS_MINIBUILDS.material", "DIAMOND_PICKAXE");

        config.set("gameTypes.LOBBY.displayName", "Lobby");
        config.set("gameTypes.LOBBY.requirements", Map.of(
                "SPAWNPOINT_LOBBY", 2,
                "BORDER", 2
        ));
        config.set("gameTypes.LOBBY.dataPointTypes", List.of("SPAWNPOINT", "HOLOGRAM", "BORDER", "INTERACT_NPC", "INTERACTION", "GAME_NPC", "STORE_NPC", "GAME_AREA", "EVENT_BORDER"));
        config.set("gameTypes.LOBBY.material", "BEACON");

        config.set("gameTypes.SKYWARS.displayName", "Skywars");
        config.set("gameTypes.SKYWARS.requirements", Map.of(
                "SPAWNPOINT_1", 2,
                "HOLOGRAM_1", 1,
                "CHEST_1", 3,
                "ISLAND_BORDER_1", 2,
                "CHEST_MID", 6,
                "BORDER", 2,
                "SPECTATOR_SPAWNPOINT", 1
        ));
        config.set("gameTypes.SKYWARS.dataPointTypes", List.of("SPAWNPOINT", "CHEST", "HOLOGRAM", "CHEST_MID", "ISLAND_BORDER", "BORDER", "SPECTATOR_SPAWNPOINT"));
        config.set("gameTypes.SKYWARS.material", "FEATHER");

        config.set("gameTypes.WOODCHUCK.displayName", "WoodChuck");
        config.set("gameTypes.WOODCHUCK.requirements", Map.of(
                "SPAWNPOINT_RED", 6,
                "SPAWNPOINT_BLUE", 6,
                "TREE", 1,
                "SPECTATOR_SPAWNPOINT", 1,
                "BORDER", 2
        ));
        config.set("gameTypes.WOODCHUCK.dataPointTypes", List.of("SPAWNPOINT", "TREE", "BORDER", "TEAM_GOAL", "SPECTATOR_SPAWNPOINT"));
        config.set("gameTypes.WOODCHUCK.material", "OAK_SAPLING");
    }

    public static void loadGameTypes(final JavaPlugin plugin) {

        GameType.loadDataPointTypes(plugin);

        final ConfigurationSection section = plugin.getConfig().getConfigurationSection("gameTypes");
        if (section == null) {
            throw new IllegalStateException("No gameTypes section found in config.yml");
        }

        for (final String key : section.getKeys(false)) {
            final ConfigurationSection gameTypeSection = section.getConfigurationSection(key);
            if (gameTypeSection == null) {
                continue;
            }

            final String displayName = gameTypeSection.getString("displayName");
            final Material material = Material.valueOf(gameTypeSection.getString("material").toUpperCase());

            final Map<String, Integer> requirements = new HashMap<>();
            final ConfigurationSection requirementsSection = gameTypeSection.getConfigurationSection("requirements");
            if (requirementsSection != null) {
                for (final String reqKey : requirementsSection.getKeys(false)) {
                    requirements.put(reqKey, requirementsSection.getInt(reqKey));
                }
            }

            final List<DataPointType> dataPointTypeList = new ArrayList<>();
            // Check both "dataPointTypes" (new) and "dataPointTypeList" (old) for backwards compatibility
            List<String> dataPoints = gameTypeSection.getStringList("dataPointTypes");
            if (dataPoints.isEmpty()) {
                dataPoints = gameTypeSection.getStringList("dataPointTypeList");
            }
            for (final String dp : dataPoints) {
                dataPointTypeList.add(DataPointType.valueOf(dp.toUpperCase()));
            }

            GameType.GAME_TYPES.put(key.toUpperCase(), new GameType(key.toUpperCase(), displayName, requirements, dataPointTypeList, material));
        }
    }

    public static GameType valueOf(final String id) {
        return GameType.GAME_TYPES.get(id.toUpperCase());
    }

    public static List<GameType> getAllGameTypes() {
        return new ArrayList<>(GameType.GAME_TYPES.values());
    }

    public static GameType getNextGameType(final String currentGameTypeId) {
        final List<GameType> gameTypeList = GameType.getAllGameTypes();
        for (int i = 0; i < gameTypeList.size(); i++) {
            if (gameTypeList.get(i).name.equals(currentGameTypeId)) {
                return i + 1 < gameTypeList.size() ? gameTypeList.get(i + 1) : null;
            }
        }
        return null;
    }

    public static GameType[] values() {
        return GameType.GAME_TYPES.values().toArray(new GameType[0]);
    }

}
