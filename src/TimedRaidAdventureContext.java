
import java.util.List;
import java.util.Scanner;

public class TimedRaidAdventureContext extends MiniAdventureContext {

    private RaidPlayer p1;
    private RaidPlayer p2;
    private WorldClock worldClock;
    private int totalObjectives;
    private int objectivesCollected;
    private LocalTime raidDeadline;

    public TimedRaidAdventureContext() {
        super();
        this.objectivesCollected = 0;
        this.worldClock = WorldClock.getInstance();
    }

    @Override
    public void initializeLocal(Character c1, Character c2) {
        this.p1 = new RaidPlayer(c1, 1, P1_START_ROW, P1_START_COL);
        this.p2 = new RaidPlayer(c2, 2, P2_START_ROW, P2_START_COL);

        setPlayer1(this.p1);
        setPlayer2(this.p2);

        worldClock.setTime(new Time(1, 18, 0));

        // deadline is 4 hours from start in realm-local time
        LocalTime startLocal = getRealm().getTimeRule().toLocal(worldClock.getTime());
        raidDeadline = new LocalTime(startLocal.getDays(), startLocal.getHours() + 4, startLocal.getMinutes());
        if (raidDeadline.getHours() >= 24) {
            raidDeadline.setDays(raidDeadline.getDays() + raidDeadline.getHours() / 24);
            raidDeadline.setHours(raidDeadline.getHours() % 24);
        }
    }

    @Override
    public void placeEnemies() {
        getEnemies().add(RaidEnemyFactory.createSkeleton(3, 5));
        getEnemies().add(RaidEnemyFactory.createSkeleton(5, 3));
        getEnemies().add(RaidEnemyFactory.createOgre(7, 5));
        getEnemies().add(RaidEnemyFactory.createOgre(3, 9));
        getEnemies().add(RaidEnemyFactory.createDragonling(9, 5));

        getEnemies().add(RaidEnemyFactory.createSpikeTrap(1, 6));
        getEnemies().add(RaidEnemyFactory.createPoisonTrap(5, 6));
        getEnemies().add(RaidEnemyFactory.createSpikeTrap(9, 8));

        getEnemies().add(RaidEnemyFactory.createObjective(1, 10, "Ancient Relic"));
        getEnemies().add(RaidEnemyFactory.createObjective(7, 3, "Sacred Tome"));
        getEnemies().add(RaidEnemyFactory.createObjective(9, 1, "Crystal Shard"));

        totalObjectives = 3;
    }

    @Override
    public void displayHeader() {
        System.out.println("\n========================================");
        System.out.println("  TIMED RAID: " + getRealm().getName());
        System.out.println("========================================");
        System.out.println("Collect all " + totalObjectives + " objectives and reach the Exit (E)!");
        System.out.println("Controls: w/a/s/d = move, m = map, i = inventory, q = quit");
        System.out.println("Legend: 1/2 = Players, M = Monster, T = Trap, O = Objective, E = Exit");
        System.out.println("========================================\n");
    }

    @Override
    public void displayTurnStatus(int turnCount) {
        LocalTime currentLocal = getRealm().getTimeRule().toLocal(worldClock.getTime());
        System.out.println("--- Turn " + turnCount + " ---");
        System.out.printf("Realm Time: %d:%02d / Deadline: %d:%02d%n",
                currentLocal.getHours(), currentLocal.getMinutes(),
                raidDeadline.getHours(), raidDeadline.getMinutes());
        System.out.println(p1.getStatusLine());
        System.out.println(p2.getStatusLine());
        System.out.println("Objectives: " + objectivesCollected + "/" + totalObjectives);
        System.out.println();

        System.out.println(getMap().render(p1.getRow(), p1.getCol(),
                p2.getRow(), p2.getCol()));
    }

    @Override
    public void displayVictoryMessage() {
        System.out.println("\n*** VICTORY! ***");
        System.out.println("The raid is complete! Distributing loot...\n");
        List<Item> victoryLoot = LootTable.generateVictoryLoot();
        for (Item item : victoryLoot) {
            p1.getCharacter().getInventory().addItem(item);
            System.out.println("  " + p1.getCharacter().getName() + " received: "
                    + item.getInfo().getName() + " (" + item.getInfo().getRarity() + ")");
        }
        victoryLoot = LootTable.generateVictoryLoot();
        for (Item item : victoryLoot) {
            p2.getCharacter().getInventory().addItem(item);
            System.out.println("  " + p2.getCharacter().getName() + " received: "
                    + item.getInfo().getName() + " (" + item.getInfo().getRarity() + ")");
        }
    }

    @Override
    public boolean checkCompletion(Scanner scanner) {
        advanceTime(10);

        LocalTime newLocal = getRealm().getTimeRule().toLocal(worldClock.getTime());
        if (compareLocalTime(newLocal, raidDeadline) >= 0) {
            setComplete(true);
            setVictory(false);
            System.out.println("\nThe raid window has closed! Time has run out.");
            return true;
        }

        return super.checkCompletion(scanner);
    }

    @Override
    public void handleEnemyInteraction(Player player, Enemy enemy) {
        RaidPlayer raidPlayer = (RaidPlayer) player;
        String pName = "P" + raidPlayer.getPlayerNumber();
        switch (enemy.getSymbol()) {
            case 'M':
                System.out.println(pName + " engages " + enemy.getName() + "!");
                while (enemy.isAlive() && player.isAlive()) {
                    enemy.takeDamage(raidPlayer.getAttackPower());
                    System.out.println("  " + pName + " attacks for " + raidPlayer.getAttackPower()
                            + " damage! (" + enemy.getName() + " HP: " + enemy.getHp() + ")");
                    if (enemy.isAlive()) {
                        player.takeDamage(enemy.getAttackPower());
                        System.out.println("  " + enemy.getName() + " strikes back for "
                                + enemy.getAttackPower() + " damage! (" + pName + " HP: " + raidPlayer.getHp() + ")");
                    }
                }
                if (!enemy.isAlive()) {
                    System.out.println("  " + enemy.getName() + " defeated!");
                    Item drop = LootTable.generateMonsterDrop();
                    if (drop != null) {
                        player.getCharacter().getInventory().addItem(drop);
                        System.out.println("  Loot: " + drop.getInfo().getName()
                                + " (" + drop.getInfo().getRarity() + ")");
                    }
                }
                if (!raidPlayer.isAlive()) {
                    System.out.println("  " + raidPlayer.getCharacter().getName() + " has been slain!");
                }
                break;

            case 'T':
                System.out.println(pName + " triggered a " + enemy.getName() + "! (-"
                        + enemy.getAttackPower() + " HP)");
                raidPlayer.takeDamage(enemy.getAttackPower());
                enemy.kill();
                if (!raidPlayer.isAlive()) {
                    System.out.println("  " + raidPlayer.getCharacter().getName() + " has been slain!");
                }
                break;

            case 'O':
                System.out.println(pName + " collected: " + enemy.getName() + "!");
                raidPlayer.collectObjective();
                objectivesCollected++;
                enemy.kill();
                Item reward = LootTable.generateReward();
                raidPlayer.getCharacter().getInventory().addItem(reward);
                System.out.println("  Reward: " + reward.getInfo().getName()
                        + " (" + reward.getInfo().getRarity() + ")");
                break;
        }
    }

    @Override
    public boolean checkVictory(Player player) {
        if (objectivesCollected >= totalObjectives
                && getMap().getTile(player.getRow(), player.getCol()) == 'E') {
            setComplete(true);
            setVictory(true);
            return true;
        }
        return false;
    }

    @Override
    public String getResultSummary() {
        return super.getResultSummary() + "Objectives Collected: " + objectivesCollected + "/" + totalObjectives + "\n";
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
        if (a.getDays() != b.getDays()) {
            return Integer.compare(a.getDays(), b.getDays());
        }
        if (a.getHours() != b.getHours()) {
            return Integer.compare(a.getHours(), b.getHours());
        }
        return Integer.compare(a.getMinutes(), b.getMinutes());
    }
}
