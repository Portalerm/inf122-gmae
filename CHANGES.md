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
