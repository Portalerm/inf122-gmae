import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class EscortAdventure implements MiniAdventure{
    private EscortPlayer player1;
    private EscortPlayer player2;
    private Map map;
    private List<RaidEntity> entities;
    private Realm realm;
    private boolean complete;
    private boolean victory;
    private WorldClock worldClock;
    private StringBuilder log;
    private Npc npc = new Npc("Barton");
    
    public EscortAdventure() {
        this.entities = new ArrayList<>();
        this.complete = false;
        this.victory = false;
        this.worldClock = WorldClock.getInstance();
        this.log = new StringBuilder();
    }

    @Override
    public void initialize(Character p1, Character p2, Realm realm) {
        this.realm = realm;

        worldClock.setTime(new Time(1, 18, 0));

        map = Map.createDefaultMap();

        player1 = new EscortPlayer(p1, 1, 1, 1);
        player2 = new EscortPlayer(p2, 2, 1, 2);

        Random random = new Random();
        int selectedCharacter = random.nextInt(2)+1;
        if (selectedCharacter == 1) {
            player1.setEscort(true);
            player1.addNPC(npc);
        } else {
            player2.setEscort(true);
            player2.addNPC(npc);
        }

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
        entities.add(RaidEntityFactory.createDragonling(10, 1));

        entities.add(RaidEntityFactory.createSpikeTrap(1, 6));
        entities.add(RaidEntityFactory.createPoisonTrap(5, 6));
        entities.add(RaidEntityFactory.createSpikeTrap(9, 8));
    }

    @Override
    public void run(Scanner scanner) {
        System.out.println("\n========================================");
        System.out.println("  Escort Across the Realm: " + realm.getName());
        System.out.println("========================================");
        System.out.print("Player ");
        if (player1.isEscortingNPC()) {
            System.out.print("1 ");
        } else {
            System.out.print("2 ");
        }
        System.out.println("needs to escort " + npc.getName() + " to the Exit (E)!");
        System.out.println("Controls: w/a/s/d = move, m = map, i = inventory, q = quit");
        System.out.println("Legend: 1/2 = Players, M = Monster, T = Trap, E = Exit");
        System.out.println("========================================\n");

        int turnCount = 0;

        while (!complete) {
            turnCount++;

            LocalTime currentLocal = realm.getTimeRule().toLocal(worldClock.getTime());
            System.out.println("--- Turn " + turnCount + " ---");
            System.out.printf("Realm Time: %d:%02d%n",
                    currentLocal.getHours(), currentLocal.getMinutes());
            System.out.println(player1.getStatusLine());
            System.out.println(player2.getStatusLine());
            System.out.println(npc.getStatusLine());
            System.out.println();

            System.out.println(map.render(player1.getRow(), player1.getCol(),
                    player2.getRow(), player2.getCol()));

            if (!npc.isAlive()) {
                complete = true;
                victory = false;
                System.out.println("\nNPC " + npc.getName() + " has fallen! The escort was a failure.");
                break;
            }

            if (player1.isAlive()) {
                System.out.print("Player 1 action: ");
                String action = scanner.nextLine().trim().toLowerCase();
                if (action.equals("q")) {
                    complete = true;
                    victory = false;
                    System.out.println("Escort abandoned!");
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
                    System.out.println("Escort abandoned!");
                    break;
                }
                handleAction(player2, action);
                if (checkVictory(player2)) break;
            }

            if (!player1.isAlive() && !player2.isAlive()) {
                complete = true;
                victory = false;
                System.out.println("\nBoth escorts have fallen! The escort was a failure.");
                break;
            }

            advanceTime(10);

        }

        if (victory) {
            System.out.println("\n*** VICTORY! ***");
            System.out.println("The escort is complete! Distributing loot...\n");
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

    private void handleAction(EscortPlayer player, String action) {
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

    private void handleEntityInteraction(EscortPlayer player, RaidEntity entity) {
        String pName = "P" + player.getPlayerNumber();
        boolean npcPresent = player.isEscortingNPC();
        switch (entity.getSymbol()) {
            case 'M':
                System.out.println(pName + " engages " + entity.getName() + "!");
                while (entity.isAlive() && player.isAlive() && npc.isAlive()) {
                    entity.takeDamage(player.getAttackPower());
                    System.out.println("  " + pName + " attacks for " + player.getAttackPower()
                            + " damage! (" + entity.getName() + " HP: " + entity.getHp() + ")");
                    if (entity.isAlive()) {
                        player.takeDamage(entity.getDamage());
                        System.out.println("  " + entity.getName() + " strikes back for "
                                + entity.getDamage() + " damage! (" + pName + " HP: " + player.getHp() + ")");
                        if (npcPresent) {
                            npc.takeDamage(entity.getDamage());
                        }
                        System.out.println("  " + entity.getName() + " also strikes the NPC for "
                                + entity.getDamage() + " damage! (" + npc.getName() + " HP: " + npc.getHp() + ")");
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
                if (npcPresent && !npc.isAlive()) {
                    System.out.println("  " + npc.getName() + " has been slain!");
                }
                if (!player.isAlive()) {
                    System.out.println("  " + player.getCharacter().getName() + " has been slain!");
                    if (npcPresent && (player1.isAlive() || player2.isAlive())) {
                        int slayedCharacter = player.getPlayerNumber();
                        if (slayedCharacter == 1) {
                            player1.removeNPC();
                            player1.setEscort(false);

                            player2.addNPC(npc);
                            player2.setEscort(true);
                        }
                        else {
                            player2.removeNPC();
                            player2.setEscort(false);

                            player1.addNPC(npc);
                            player1.setEscort(true);
                        }
                    }
                }
                break;

            case 'T':
                System.out.println(pName + " triggered a " + entity.getName() + "! (-"
                        + entity.getDamage() + " HP)");
                player.takeDamage(entity.getDamage());
                if (npcPresent) {
                    System.out.println(npc.getName() + " is hit by " + entity.getName() + " as well! (-"
                            + entity.getDamage() + " HP)");
                    npc.takeDamage(entity.getDamage());
                }
                entity.kill();
                if (npcPresent && !npc.isAlive()) {
                    System.out.println("  " + npc.getName() + " has been slain!");
                }
                if (!player.isAlive()) {
                    System.out.println("  " + player.getCharacter().getName() + " has been slain!");
                    if (npcPresent && (player1.isAlive() || player2.isAlive())) {
                        int slayedCharacter = player.getPlayerNumber();
                        if (slayedCharacter == 1) {
                            player1.removeNPC();
                            player1.setEscort(false);

                            player2.addNPC(npc);
                            player2.setEscort(true);
                        }
                        else {
                            player2.removeNPC();
                            player2.setEscort(false);

                            player1.addNPC(npc);
                            player1.setEscort(true);
                        }
                    }
                }
                break;
        }
    }

    private boolean checkVictory(EscortPlayer player) {
        if (map.getTile(player.getRow(), player.getCol()) == 'E' && player.isEscortingNPC()) {
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

    private void showInventory(EscortPlayer player) {
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

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean isVictory() {
        return victory;
    }

    @Override
    public String getResultSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Mission Summary ===\n");
        sb.append("Realm: ").append(realm.getName()).append("\n");
        sb.append("Result: ").append(victory ? "VICTORY" : "DEFEAT").append("\n");
        sb.append(player1.getStatusLine()).append("\n");
        sb.append(player2.getStatusLine()).append("\n");
        sb.append(npc.getStatusLine()).append("\n");
        return sb.toString();
    }

    @Override
    public String getName() {
        return "Escort Adventure Window";
    }

    @Override
    public String getDescription() {
        return "A co-op turn-based mission where 2 players traverse a dungeon filled "
                + "monsters and traps while one player is personally escorting an NPC towards the exit.";
    }
}
