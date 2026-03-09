import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TimedRaidAdventure implements MiniAdventure {
    private RaidPlayer player1;
    private RaidPlayer player2;
    private RaidMap map;
    private List<RaidEntity> entities;
    private Realm realm;
    private int totalObjectives;
    private int objectivesCollected;
    private boolean complete;
    private boolean victory;
    private WorldClock worldClock;
    private LocalTime raidDeadline;
    private StringBuilder log;

    public TimedRaidAdventure() {
        this.entities = new ArrayList<>();
        this.objectivesCollected = 0;
        this.complete = false;
        this.victory = false;
        this.worldClock = WorldClock.getInstance();
        this.log = new StringBuilder();
    }

    @Override
    public void initialize(Character p1, Character p2, Realm realm) {
        this.realm = realm;

        worldClock.setTime(new Time(1, 18, 0));

        // deadline is 4 hours from start in realm-local time
        LocalTime startLocal = realm.getTimeRule().toLocal(worldClock.getTime());
        raidDeadline = new LocalTime(startLocal.getDays(), startLocal.getHours() + 4, startLocal.getMinutes());
        if (raidDeadline.getHours() >= 24) {
            raidDeadline.setDays(raidDeadline.getDays() + raidDeadline.getHours() / 24);
            raidDeadline.setHours(raidDeadline.getHours() % 24);
        }

        map = RaidMap.createDefaultRaid();

        player1 = new RaidPlayer(p1, 1, 1, 1);
        player2 = new RaidPlayer(p2, 2, 1, 2);

        placeEntities();

        for (RaidEntity entity : entities) {
            map.setTile(entity.getRow(), entity.getCol(), entity.getSymbol());
        }
    }

    private void placeEntities() {
        entities.add(RaidEntityFactory.createSkeleton(3, 5));
        entities.add(RaidEntityFactory.createSkeleton(5, 3));
        entities.add(RaidEntityFactory.createOgre(7, 5));
        entities.add(RaidEntityFactory.createOgre(3, 9));
        entities.add(RaidEntityFactory.createDragonling(9, 5));

        entities.add(RaidEntityFactory.createSpikeTrap(1, 6));
        entities.add(RaidEntityFactory.createPoisonTrap(5, 6));
        entities.add(RaidEntityFactory.createSpikeTrap(9, 8));

        entities.add(RaidEntityFactory.createObjective(1, 10, "Ancient Relic"));
        entities.add(RaidEntityFactory.createObjective(7, 3, "Sacred Tome"));
        entities.add(RaidEntityFactory.createObjective(9, 1, "Crystal Shard"));

        totalObjectives = 3;
    }

    @Override
    public void run(Scanner scanner) {
        System.out.println("\n========================================");
        System.out.println("  TIMED RAID: " + realm.getName());
        System.out.println("========================================");
        System.out.println("Collect all " + totalObjectives + " objectives and reach the Exit (E)!");
        System.out.println("Controls: w/a/s/d = move, m = map, i = inventory, q = quit");
        System.out.println("Legend: 1/2 = Players, M = Monster, T = Trap, O = Objective, E = Exit");
        System.out.println("========================================\n");

        int turnCount = 0;

        while (!complete) {
            turnCount++;

            LocalTime currentLocal = realm.getTimeRule().toLocal(worldClock.getTime());
            System.out.println("--- Turn " + turnCount + " ---");
            System.out.printf("Realm Time: %d:%02d / Deadline: %d:%02d%n",
                currentLocal.getHours(), currentLocal.getMinutes(),
                raidDeadline.getHours(), raidDeadline.getMinutes());
            System.out.println(player1.getStatusLine());
            System.out.println(player2.getStatusLine());
            System.out.println("Objectives: " + objectivesCollected + "/" + totalObjectives);
            System.out.println();

            System.out.println(map.render(player1.getRow(), player1.getCol(),
                player2.getRow(), player2.getCol()));

            if (player1.isAlive()) {
                System.out.print("Player 1 action: ");
                String action = scanner.nextLine().trim().toLowerCase();
                if (action.equals("q")) {
                    complete = true;
                    victory = false;
                    System.out.println("Raid abandoned!");
                    break;
                }
                handleAction(player1, action);
                if (checkVictory(player1)) break;
            }

            if (player2.isAlive() && !complete) {
                System.out.print("Player 2 action: ");
                String action = scanner.nextLine().trim().toLowerCase();
                if (action.equals("q")) {
                    complete = true;
                    victory = false;
                    System.out.println("Raid abandoned!");
                    break;
                }
                handleAction(player2, action);
                if (checkVictory(player2)) break;
            }

            if (!player1.isAlive() && !player2.isAlive()) {
                complete = true;
                victory = false;
                System.out.println("\nBoth raiders have fallen! The raid is lost.");
                break;
            }

            advanceTime(10);

            LocalTime newLocal = realm.getTimeRule().toLocal(worldClock.getTime());
            if (compareLocalTime(newLocal, raidDeadline) >= 0) {
                complete = true;
                victory = false;
                System.out.println("\nThe raid window has closed! Time has run out.");
                break;
            }
        }

        if (victory) {
            System.out.println("\n*** VICTORY! ***");
            System.out.println("The raid is complete! Distributing loot...\n");
            List<Item> victoryLoot = RaidLootTable.generateVictoryLoot();
            for (Item item : victoryLoot) {
                player1.getCharacter().getInventory().addItem(item);
                System.out.println("  " + player1.getCharacter().getName() + " received: "
                    + item.getInfo().getName() + " (" + item.getInfo().getRarity() + ")");
            }
            victoryLoot = RaidLootTable.generateVictoryLoot();
            for (Item item : victoryLoot) {
                player2.getCharacter().getInventory().addItem(item);
                System.out.println("  " + player2.getCharacter().getName() + " received: "
                    + item.getInfo().getName() + " (" + item.getInfo().getRarity() + ")");
            }
        }

        complete = true;
        System.out.println("\n" + getResultSummary());
    }

    private void handleAction(RaidPlayer player, String action) {
        int newRow = player.getRow();
        int newCol = player.getCol();

        switch (action) {
            case "w": newRow--; break;
            case "s": newRow++; break;
            case "a": newCol--; break;
            case "d": newCol++; break;
            case "m":
                System.out.println(map.render(player1.getRow(), player1.getCol(),
                    player2.getRow(), player2.getCol()));
                return;
            case "i":
                showInventory(player);
                return;
            default:
                System.out.println("Invalid action. Use w/a/s/d to move, m=map, i=inventory, q=quit.");
                return;
        }

        if (!map.isInBounds(newRow, newCol)) {
            System.out.println("You can't go that way!");
            return;
        }
        if (!map.isPassable(newRow, newCol)) {
            System.out.println("Blocked by a wall!");
            return;
        }

        RaidEntity entity = findEntity(newRow, newCol);
        if (entity != null && entity.isAlive()) {
            handleEntityInteraction(player, entity);
            if (!player.isAlive()) return;
            if (!entity.isAlive()) {
                map.setTile(newRow, newCol, '.');
                player.setRow(newRow);
                player.setCol(newCol);
            }
        } else {
            player.setRow(newRow);
            player.setCol(newCol);
        }
    }

    private void handleEntityInteraction(RaidPlayer player, RaidEntity entity) {
        String pName = "P" + player.getPlayerNumber();
        switch (entity.getSymbol()) {
            case 'M':
                System.out.println(pName + " engages " + entity.getName() + "!");
                while (entity.isAlive() && player.isAlive()) {
                    entity.takeDamage(player.getAttackPower());
                    System.out.println("  " + pName + " attacks for " + player.getAttackPower()
                        + " damage! (" + entity.getName() + " HP: " + entity.getHp() + ")");
                    if (entity.isAlive()) {
                        player.takeDamage(entity.getDamage());
                        System.out.println("  " + entity.getName() + " strikes back for "
                            + entity.getDamage() + " damage! (" + pName + " HP: " + player.getHp() + ")");
                    }
                }
                if (!entity.isAlive()) {
                    System.out.println("  " + entity.getName() + " defeated!");
                    Item drop = RaidLootTable.generateMonsterDrop();
                    if (drop != null) {
                        player.getCharacter().getInventory().addItem(drop);
                        System.out.println("  Loot: " + drop.getInfo().getName()
                            + " (" + drop.getInfo().getRarity() + ")");
                    }
                }
                if (!player.isAlive()) {
                    System.out.println("  " + player.getCharacter().getName() + " has been slain!");
                }
                break;

            case 'T':
                System.out.println(pName + " triggered a " + entity.getName() + "! (-"
                    + entity.getDamage() + " HP)");
                player.takeDamage(entity.getDamage());
                entity.kill();
                if (!player.isAlive()) {
                    System.out.println("  " + player.getCharacter().getName() + " has been slain!");
                }
                break;

            case 'O':
                System.out.println(pName + " collected: " + entity.getName() + "!");
                player.collectObjective();
                objectivesCollected++;
                entity.kill();
                Item reward = RaidLootTable.generateObjectiveReward(entity.getName());
                player.getCharacter().getInventory().addItem(reward);
                System.out.println("  Reward: " + reward.getInfo().getName()
                    + " (" + reward.getInfo().getRarity() + ")");
                break;
        }
    }

    private boolean checkVictory(RaidPlayer player) {
        if (objectivesCollected >= totalObjectives
                && map.getTile(player.getRow(), player.getCol()) == 'E') {
            complete = true;
            victory = true;
            return true;
        }
        return false;
    }

    private RaidEntity findEntity(int row, int col) {
        for (RaidEntity e : entities) {
            if (e.isAlive() && e.getRow() == row && e.getCol() == col) {
                return e;
            }
        }
        return null;
    }

    private void showInventory(RaidPlayer player) {
        System.out.println(player.getCharacter().getName() + "'s Inventory:");
        List<Item> items = player.getCharacter().getInventory().getItems();
        if (items.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (Item item : items) {
                System.out.println("  - " + item.getInfo().getName()
                    + " [" + item.getInfo().getRarity() + "]");
            }
        }
    }

    private void advanceTime(int minutes) {
        Time current = worldClock.getTime();
        int totalMinutes = current.getMinute() + minutes;
        int newHour = current.getHour() + totalMinutes / 60;
        int newMinute = totalMinutes % 60;
        int newDay = current.getDay() + newHour / 24;
        newHour = newHour % 24;
        worldClock.setTime(new Time(newDay, newHour, newMinute));
    }

    private int compareLocalTime(LocalTime a, LocalTime b) {
        if (a.getDays() != b.getDays()) return Integer.compare(a.getDays(), b.getDays());
        if (a.getHours() != b.getHours()) return Integer.compare(a.getHours(), b.getHours());
        return Integer.compare(a.getMinutes(), b.getMinutes());
    }

    @Override
    public boolean isComplete() { return complete; }

    @Override
    public boolean isVictory() { return victory; }

    @Override
    public String getResultSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Raid Summary ===\n");
        sb.append("Realm: ").append(realm.getName()).append("\n");
        sb.append("Result: ").append(victory ? "VICTORY" : "DEFEAT").append("\n");
        sb.append("Objectives Collected: ").append(objectivesCollected)
          .append("/").append(totalObjectives).append("\n");
        sb.append(player1.getStatusLine()).append("\n");
        sb.append(player2.getStatusLine()).append("\n");
        return sb.toString();
    }

    @Override
    public String getName() {
        return "Timed Raid Window";
    }

    @Override
    public String getDescription() {
        return "A co-op turn-based raid where 2 players navigate a dungeon, "
            + "fight monsters, collect objectives, and escape before time runs out.";
    }
}
