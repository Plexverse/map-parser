# Mineplex Map Parser

#### A project by Plexverse Studio for the Mineplex Studio Engine

![Datapoint Menu Example](https://i.imgur.com/j7RDTI4.png)

Mineplex Map Parser is a tool for creating datapoints. It allows you to build a map, place down armor stands and then
parse the map in the format Mineplex Studio accepts, with the appropriate datapoint format.

## Output

This plugin outputs a zip file with the format `GAME_NAME-WORLD_NAME.zip`, this is a minecraft world with the junk files
ripped out.
This zip file includes a `dataPoints.json` file: information on this file structure can
be [found here](https://studio.mineplex.com/docs/sdk/modules/world/datapoints).

This also generates a `mapMeta.json` file, with the following structure:

```json
{
  "author": "efa1e2d8-871d-4ede-93e5-3c82102cbd42,a44aece0-abb2-438a-a352-11a9829b33ae",
  "gameType": "SKYWARS",
  "mapName": "CookieBilly Land",
  "legacy": false
}
```

This is useful for figuring out who built a map at runtime, or marking it as an old Map in-game. You can encase the
Mineplex world object with extra details on the SDK.

This output zip file should be placed in `assets/world-templates` in your Mineplex project.

---

# User instructions

## Placing datapoints

### /mapsettings

In order to place datapoints, you first must use the `/mapsettings` command and give your map the following details:

- A name for your map (a-z, 1-9, nothing else is allowed)
- If the map is a legacy map (was on previous Mineplex)
- Author (this is UUIDs of the authors, seperated by commas: e.g.
  `efa1e2d8-871d-4ede-93e5-3c82102cbd42,a44aece0-abb2-438a-a352-11a9829b33ae`)

### Placing an armorstand

- In order to get started, place an armor stand on your map, and right click it.
- Select a datapoint type (for example, a spawnpoint).
- Choose the team (if applicable):
    - You can choose between pre-defined teams: Aqua, Yellow, Red, Green
    - You can define your own team using the black bed, e.g. Magenta

To speed things up, you can click the Piston to quickly clone datapoints.

### Parsing the world

Once you've met all of the requirements of a game, use the `/parse [size]` command.
This will output a zip file, as mentioned in the output section.

---

## Admin instructions

### Adding new data point types

The `DatapointType` configuration is used to define the various types of datapoints that can be used in your games.
These datapoints represent different in-game objects, locations, or entities that are necessary for setting up a game.

Each datapoint type is defined in the `datapointTypes.yml` (or similar) file and includes information about its
properties, such as its display name, whether it has teams, whether it changes yaw/pitch when placed, and the material
associated with it.

#### Example configuration

```yaml
datapointTypes:
  SPAWNPOINT:
    menuName: "Spawnpoint"
    hasTeam: true
    changeYawPitch: true
    material: "RED_BED"

  CHEST:
    menuName: "Chest"
    hasTeam: true
    changeYawPitch: true
    material: "CHEST"

  HOLOGRAM:
    menuName: "Hologram"
    hasTeam: false
    changeYawPitch: false
    material: "OAK_SIGN"

  WALLPOINT:
    menuName: "Wall Base"
    hasTeam: true
    changeYawPitch: false
    material: "GLASS"

  SPECTATOR_SPAWNPOINT:
    menuName: "Spectator Spawnpoint"
    hasTeam: false
    changeYawPitch: true
    material: "GREEN_BED"

  ISLAND_BORDER:
    menuName: "Island Border"
    hasTeam: true
    changeYawPitch: true
    material: "BARRIER"

  CENTER:
    menuName: "Center"
    hasTeam: false
    changeYawPitch: false
    material: "TOTEM_OF_UNDYING"
 ```

#### Explanation of Datapoint Type Configuration:

- `menuName`: The name that will be displayed in the armorstand menu for the datapoint. This is the text label shown to
  the player in the game.
- `hasTeam`: A boolean indicating whether the datapoint is associated with a specific team. If true, it means that the
  datapoint is associated with a team (e.g., spawnpoints may be team-specific).
- `changeYawPitch`: A boolean that specifies if this datapoint requires its orientation (yaw/pitch) to change when
  placed. This can be useful for datapoints that must be aligned in a specific direction (like holograms or certain
  borders).
- `material`: The Minecraft material associated with this datapoint type (e.g., `"RED_BED"`, `"CHEST"`, etc.). This
  determines the item/block that will represent the datapoint in the world.

### Adding New Games

To add a new game, you need to edit the config.yml file. Each game is represented as a GameType object and will contain
several properties that are essential for configuration, such as its display name, required datapoint types, and the
material representing it.

To configure it, we use the `config.yml` file

```yaml
gameTypes:
  NEW_GAME:
    displayName: "New Game"
    requirements:
      SPAWNPOINT_RED: 5
      SPAWNPOINT_BLUE: 5
      HOLOGRAM_GREEN: 1
      WALLPOINT_YELLOW: 10
    dataPointTypeList:
      - SPAWNPOINT
      - HOLOGRAM
      - WALLPOINT
    material: "DIAMOND_BLOCK"
```

#### Game Configuration Explanation:

- `displayName`: The name that represents the game in the system, shown to players.
- `requirements`: A list of the datapoints required for the game to be parsed. This will include the datapoint name as
  the
  key and the number of required points as the value. Example: "SPAWNPOINT_RED": 5 means 5 spawnpoints of type
  SPAWNPOINT_RED are needed to start the game.
- `dataPointTypes`: A list of the datapoint types relevant to the game. These are the types that will be visible in the
  armorstand menu (defined following the steps above).
- `material`: The material representing this game in the world (e.g., "DIAMOND_BLOCK").

#### Datapoint Requirements:

Datapoint requirements specify how many instances of each datapoint type are needed to correctly parse the world for a
given game. This ensures that each game has the appropriate setup before it can be played.

- For example, a game might require 5 `SPAWNPOINT_RED` datapoints. This means that the world will need at least 5 of
  these datapoints to be placed in the game world to be able to parse it.

#### Data Point Type List:

The data point type list defines the various types of datapoints relevant to the game. These are the datapoint types
that will be displayed in the armorstand menu and are used to configure the world.

For example, the `GameType` might have a `dataPointTypes` list that looks like this:

```yaml
dataPointTypes:
  - SPAWNPOINT
  - HOLOGRAM
  - SPECTATOR_SPAWNPOINT
  - BORDER
  - WALLPOINT
```

This list includes the types of datapoints that the game recognizes and expects in the world. Each of these types should
be defined in your system using the configuration file as described below.

---

## Building the plugin

### Build

Pretty simple,

- `./gradlew buildPluginJar`.
- Drag and drop the output jar (`mineplex/plugin.jar`) to your build server's plugins' folder
- enjoy yourself!

## Contributing

This plugin has a few light utils, but it needs a lot more work, the code is a mess as it was built as a quick internal
tool. If you improve this plugin in any way, we ask that you contribute back to the main repository to help others using
the tool.
