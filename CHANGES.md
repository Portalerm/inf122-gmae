# Changes — Items

## Modified Items (3)

### `Inventory.java`
Added a findItemByIndex and removeItemByIndex helper functions to allow for item ID retrieval through index number

### `LootTable.java`
Added a lootTableLookup helper function to help determine item effects

### `EscortAdventureContext.java`
Added effects and interactions for items when battling a monster

# Changes — Refactors

## New Files (14)

### `Enemy.java`

Pull up refactoring to be used for both mini adventures, subclass of Entity.

### `EnemyFactory.java`

Pull up refactoring to be used for both mini adventures.

### `Entity.java`

Pull up refactoring to be used for both mini adventures. Derived from VirtualEntity.

### `EscortAdventureContext.java`

Provides the context for mini adventure #2 (Escort Across the Realm). To be passed into MiniAdventure.

### `EscortEnemyFactory.java`

Derived from EnemyFactory to be used specifically for mini adventure #2 (Escort Across the Realm).

### `EscortPlayer.java`

Derived from Player to be used specifically for mini adventure #2 (Escort Across the Realm).

### `Hurtable.java`

An interface to signal that this entity can take damange and be killed.

### `Loot.java`

A free item that can be found in mini adventure #2 (Escort Across the Realm).

### `MiniAdvantureContext.java`

An abstract base class that contains necessary info, default methods, and abstract methods in order for the MiniAdventure class to run the mini adventures.

### `Objective.java`

An objective that can be found in mini adventure #1 (Timed Raid Window).

### `Player.java`

Derived from Entity to serve as the base for RaidPlayer and EscortPlayer.

### `RaidEnemyFactory.java`

Derived from EnemyFactory to be used specifically for mini adventure #1 (Timed Raid Window).

### `TimedRaidAdventureContext.java`

Provides the context for mini adventure #1 (Timed Raid Window). To be passed into MiniAdventure.

### `VirtualEntity.java`

Implements Hurtable, contains information such as hp, attackPower, without any coordinates.

## Modified Files (5)

### `Inventory.java`

Add isEmpty() and toString() to reduce coupling.

### `RaidLootTable.java` -> `LootTable.java`

Pull up refactoring to be used for both mini adventures.

### `RaidMap.java` -> `Map.java`

Pull up refactoring to be used for both mini adventures.

### `MiniAdventure.java`

Used to be an interface. Has been converted into a class that has an initialize() and run() methods and a context instance variable, which we can send TimedRaidAdventureContext or EscortAdventureContext into.

### `RaidPlayer.java`

Used to be the sole player. Has been converted to derive from Player to be used as players for mini adventure #1 (Timed Raid Window).

## Removed Files (3)

### `RaidEntity.java`

### `RaidEntityFactory.java`

### `TimedRaidAdventure.java`

# Changes — Escort Across the Realm Mini-Adventure

## New Files (3)

### `NPC.java`

Class defining the npc with a name, hp, and alive status

### `EscortPlayer.java`

Reused RaidPlayer.java with the exclusion of objectives and inclusion of NPC related attributes like whether one is following a certain player

### `EscortAdventure.java`

Reused TimedRaidAdventure.java, excluding any mention of objectives or deadlines. Modifications towards the entity layout. Inclusions for NPC interactions with entities and traps, changing victory and defeat requirements.

## Modified Files (1)

### `Main.java`

Added second mini-adventure option to interactive menu

- "Launch Escort Across the Realm" in between "Launch Timed Raid" and "Quit" options
- Reuses character creation but modified for additional NPC options
- Reuses demo Realm with a +2 hour time offset rule

# Changes — Timed Raid Window Mini-Adventure

## New Files (7)

### `MiniAdventure.java`

Interface defining the mini-adventure contract: `initialize()`, `run()`, `isComplete()`, `isVictory()`, `getResultSummary()`, `getName()`, `getDescription()`.

### `RaidMap.java`

2D grid map (12x12) with wall/floor tiles, rendering with player overlays, and a hand-designed default dungeon layout via `createDefaultRaid()`. Supports bounds checking and passability queries.

### `RaidEntity.java`

Represents grid entities (monsters, traps, objectives) with name, symbol, position, HP, damage, and alive state. Used for all interactive elements on the raid map.

### `RaidEntityFactory.java`

Factory for creating pre-configured raid entities:

- Monsters: Skeleton (2HP/1dmg), Ogre (4HP/2dmg), Dragonling (6HP/3dmg)
- Traps: Spike Trap (1dmg), Poison Trap (2dmg)
- Objectives: Named collectible artifacts

### `RaidPlayer.java`

Wraps an existing `Character` with raid-session stats (HP, attack power, position, objectives collected). Stats are derived from class type: Warrior (12HP/3ATK), Mage (8HP/5ATK), Archer (10HP/4ATK), Rogue (10HP/4ATK), Monk (9HP/3ATK).

### `RaidLootTable.java`

Generates loot using existing `Item`/`ItemInfo`/`Rarity` classes:

- Victory loot: 2 RARE/EPIC items per player
- Monster drops: 50% chance of COMMON/UNCOMMON item
- Objective rewards: Guaranteed UNCOMMON item

### `TimedRaidAdventure.java`

Main adventure implementation (`implements MiniAdventure`). Features:

- Co-op turn-based gameplay for 2 local players
- WASD movement with auto-combat, trap triggering, and objective collection
- Realm-local time window mechanic using `WorldClock` + `LocalTimeRule`
- Win condition: collect all objectives and reach exit before deadline
- Loss conditions: time expires, both players die, or quit

## Modified Files (2)

### `GameFacade.java`

Added `launchMiniAdventure(MiniAdventure, Character, Character, Realm)` method that initializes and returns the adventure for running.

### `Main.java`

Replaced minimal stub with interactive menu system:

- Main menu with "Launch Timed Raid" and "Quit" options
- Character creation flow for both players (name + class selection)
- Creates a demo Realm ("Shadowfell Catacombs") with a +2 hour time offset rule
- Launches the raid through GameFacade
