import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class EscortAdventureContext extends MiniAdventureContext {

    private EscortPlayer p1;
    private EscortPlayer p2;
    private NPC npc;

    public EscortAdventureContext() {
        super();
        this.npc = new NPC("Barton");
    }

    @Override
    public void initializeLocal(Character c1, Character c2) {
        this.p1 = new EscortPlayer(c1, 1, P1_START_ROW, P1_START_COL);
        this.p2 = new EscortPlayer(c2, 2, P2_START_ROW, P2_START_COL);

        setPlayer1(this.p1);
        setPlayer2(this.p2);

        Random random = new Random();
        int selectedCharacter = random.nextInt(2) + 1;
        if (selectedCharacter == 1) {
            this.p1.setEscort(true);
            this.p1.addNPC(npc);
        } else {
            this.p2.setEscort(true);
            this.p2.addNPC(npc);
        }
    }

    @Override
    public void placeEnemies() {
        addEnemy(EscortEnemyFactory.createSkeleton(5, 1));
        addEnemy(EscortEnemyFactory.createSkeleton(2, 4));
        addEnemy(EscortEnemyFactory.createOgre(1, 9));
        addEnemy(EscortEnemyFactory.createOgre(3, 7));
        addEnemy(EscortEnemyFactory.createDragonling(7, 4));
        addEnemy(EscortEnemyFactory.createDragonling(9, 2));

        addEnemy(EscortEnemyFactory.createSkeleton(7, 10));
        addEnemy(EscortEnemyFactory.createDragonling(10, 9));
        addEnemy(EscortEnemyFactory.createOgre(9, 10));
        addEnemy(EscortEnemyFactory.createOgre(3, 9));

        addEnemy(EscortEnemyFactory.createSpikeTrap(1, 6));
        addEnemy(EscortEnemyFactory.createPoisonTrap(5, 10));
        addEnemy(EscortEnemyFactory.createSpikeTrap(9, 8));

        addEnemy(EscortEnemyFactory.createLoot(3, 4));
        addEnemy(EscortEnemyFactory.createLoot(3, 8));
        addEnemy(EscortEnemyFactory.createLoot(7, 9));
        addEnemy(EscortEnemyFactory.createLoot(7, 6));
        addEnemy(EscortEnemyFactory.createLoot(10, 8));
    }

    @Override
    public void displayHeader() {
        System.out.println("\n========================================");
        System.out.println("  ESCORT ACROSS THE REALM: " + getRealm().getName());
        System.out.println("========================================");
        System.out.println("Player " + (p1.isEscortingNPC() ? "1" : "2") + " needs to escort " + npc.getName()
                + " to the Exit (E)!");
        System.out.println("Controls: w/a/s/d = move, m = map, i = inventory, q = quit");
        System.out.println("Legend: 1/2 = Players, M = Monster, T = Trap, E = Exit");
        System.out.println("========================================\n");
    }

    @Override
    public void displayTurnStatus(int turnCount) {
        System.out.println("--- Turn " + turnCount + " ---");
        System.out.println(p1.getStatusLine());
        System.out.println(p2.getStatusLine());
        System.out.println(npc.getStatusLine());
        System.out.println();

        System.out.println(getMap().render(p1.getRow(), p1.getCol(),
                p2.getRow(), p2.getCol()));
    }

    @Override
    public void displayVictoryMessage() {
        System.out.println("\n*** VICTORY! ***");
        System.out.println("The escort is complete! Distributing loot...\n");
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
        if (!npc.isAlive()) {
            setComplete(true);
            setVictory(false);
            System.out.println("\nNPC " + npc.getName() + " has fallen! The escort was a failure.");
            return true;
        }

        return super.checkCompletion(scanner);
    }

    @Override
    public void handleEnemyInteraction(Player player, Enemy enemy, Scanner scanner) {
        EscortPlayer escortPlayer = (EscortPlayer) player;
        String pName = "P" + escortPlayer.getPlayerNumber();
        boolean npcPresent = escortPlayer.isEscortingNPC();
        int num = 0;
        int power = 0;
        int def = 0;
        boolean escape = false;
        switch (enemy.getSymbol()) {
            case 'M':
                System.out.println(pName + " engages " + enemy.getName() + "!");
                while ((enemy.isAlive() && escortPlayer.isAlive() && npc.isAlive())) {
                    power = 0;
                    def = 0;
                    Inventory playerInventory = escortPlayer.getCharacter().getInventory();
                    if (!playerInventory.isEmpty()) {
                        System.out.println(pName + " has some items in their inventory:");

                        for (Item item : playerInventory.getItems()) {
                            String itemName = item.getInfo().getName();
                            int index = playerInventory.getItems().indexOf(item) + 1;
                            System.out.print("  " + index + ") " + itemName
                                    + " (" + item.getInfo().getRarity() + ") ");

                            switch (LootTable.lootTableLookup(itemName)[1]) {
                                case 1:
                                    System.out.println("- Attack Boost");
                                    break;
                                case 2:
                                    System.out.println("- Defense Boost");
                                    break;
                                case 3:
                                    System.out.println("- Heal");
                                    break;
                                case 4:
                                    System.out.println("- Escape Chance");
                                    break;
                            };
                        }

                        System.out.println("Which item would you like to use? (0 for none)");
                        String choice = scanner.nextLine().trim();

                        try {
                            num = Integer.parseInt(choice);
                        } catch (NumberFormatException ignored) {
                        }

                        if (num == 0) {
                            System.out.println("No item used.");
                        } else if (num <= playerInventory.getItems().size() && num > 0) {
                            String itemName = playerInventory.findItemByIndex(num - 1).getInfo().getName();
                            int row = LootTable.lootTableLookup(itemName)[0];
                            int col = LootTable.lootTableLookup(itemName)[1];
                            System.out.println(itemName + " used!");
                            playerInventory.removeItemByIndex(num - 1);
                            switch (col) {
                                case 1:
                                    System.out.println("Player " + player.getPlayerNumber() + " will deal "
                                            + "(+" + row + ") more damage for this turn!");
                                    power = row;
                                    break;
                                case 2:
                                    System.out.println("Player " + player.getPlayerNumber() + " will take "
                                            + "(-" + row + ") less damage for this turn!");
                                    if (npcPresent) {
                                        System.out.println("NPC " + npc.getName() + " will also take "
                                                + "(-" + row + ") less damage for this turn!");
                                    }
                                    def = row;
                                    break;
                                case 3:
                                    System.out.println("Player " + player.getPlayerNumber() + " will heal for "
                                            + "(+" + (2 + row) + ")!");
                                    player.setHp(Math.min(player.getHp() + (2 + row), player.getMaxHp()));
                                    if (npcPresent) {
                                        System.out.println("NPC " + npc.getName() + " will also heal "
                                                + "(+" + (2 + row) + ")!");
                                        npc.setHp(Math.min(npc.getHp() + (2 + row), npc.getMaxHp()));
                                    }
                                    break;
                                case 4:
                                    System.out.println("Player " + player.getPlayerNumber() + " has a ("
                                            + (50 + (10 * row)) + "%) chance to escape!");
                                    System.out.println("And the fate decides...");
                                    Random random = new Random();
                                    int fate = random.nextInt(100) + 1;
                                    if (50 + (10 * row) >= fate) {
                                        System.out.println("Escape success! " + enemy.getName() + " avoided!");
                                        escape = true;
                                        enemy.kill();
                                        break;
                                    } else {
                                        System.out.println("Escape failed! Battle continues!");
                                    }
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    }
                    if (!escape) {
                        int playerDamage = escortPlayer.getAttackPower() + power;
                        int enemyDamage = enemy.getAttackPower() - def;
                        enemy.takeDamage(playerDamage);
                        System.out.println("  " + pName + " attacks for " + (escortPlayer.getAttackPower() + power)
                                + " damage! (" + enemy.getName() + " HP: " + enemy.getHp() + ")");
                        if (enemy.isAlive()) {
                            escortPlayer.takeDamage(enemyDamage);
                            System.out.println("  " + enemy.getName() + " strikes back for "
                                    + enemyDamage + " damage! (" + pName + " HP: " + escortPlayer.getHp() + ")");
                            if (npcPresent) {
                                npc.takeDamage(enemyDamage);
                                System.out.println("  " + enemy.getName() + " also strikes the NPC for "
                                        + enemyDamage + " damage! (" + npc.getName() + " HP: " + npc.getHp() + ")");
                            }
                        }
                    }
                }
                if (!enemy.isAlive()) {
                    if (!escape) {
                        System.out.println("  " + enemy.getName() + " defeated!");
                        Item drop = LootTable.generateMonsterDrop();
                        if (drop != null) {
                            escortPlayer.getCharacter().getInventory().addItem(drop);
                            System.out.println("  Loot: " + drop.getInfo().getName()
                                    + " (" + drop.getInfo().getRarity() + ")");
                        }
                    }
                }
                if (npcPresent && !npc.isAlive()) {
                    System.out.println("  " + npc.getName() + " has been slain!");
                }
                if (!escortPlayer.isAlive()) {
                    System.out.println("  " + escortPlayer.getCharacter().getName() + " has been slain!");
                    if (npcPresent && (p1.isAlive() || p2.isAlive())) {
                        int slayedCharacter = escortPlayer.getPlayerNumber();
                        if (slayedCharacter == 1) {
                            p1.removeNPC();
                            p1.setEscort(false);

                            p2.addNPC(npc);
                            p2.setEscort(true);
                        } else {
                            p2.removeNPC();
                            p2.setEscort(false);

                            p1.addNPC(npc);
                            p1.setEscort(true);
                        }
                    }
                }
                break;

            case 'T':
                System.out.println(pName + " triggered a " + enemy.getName() + "! (-"
                        + enemy.getAttackPower() + " HP)");
                escortPlayer.takeDamage(enemy.getAttackPower());
                if (npcPresent) {
                    System.out.println(npc.getName() + " is hit by " + enemy.getName() + " as well! (-"
                            + enemy.getAttackPower() + " HP)");
                    npc.takeDamage(enemy.getAttackPower());
                }
                enemy.kill();
                if (npcPresent && !npc.isAlive()) {
                    System.out.println("  " + npc.getName() + " has been slain!");
                }
                if (!escortPlayer.isAlive()) {
                    System.out.println("  " + escortPlayer.getCharacter().getName() + " has been slain!");
                    if (npcPresent && (p1.isAlive() || p2.isAlive())) {
                        int slayedCharacter = escortPlayer.getPlayerNumber();
                        if (slayedCharacter == 1) {
                            p1.removeNPC();
                            p1.setEscort(false);

                            p2.addNPC(npc);
                            p2.setEscort(true);
                        } else {
                            p2.removeNPC();
                            p2.setEscort(false);

                            p1.addNPC(npc);
                            p1.setEscort(true);
                        }
                    }
                }
                break;

            case 'L':
                System.out.println(pName + " collected: " + enemy.getName() + "!");
                enemy.kill();
                Item reward = LootTable.generateReward();
                escortPlayer.getCharacter().getInventory().addItem(reward);
                System.out.println("  Reward: " + reward.getInfo().getName()
                        + " (" + reward.getInfo().getRarity() + ")");
                break;
        }
    }

    @Override
    public boolean checkVictory(Player player) {
        if (getMap().getTile(player.getRow(), player.getCol()) == 'E' && ((EscortPlayer) player).isEscortingNPC()) {
            setComplete(true);
            setVictory(true);
            return true;
        }
        return false;
    }

    @Override
    public String getResultSummary() {
        return super.getResultSummary() + npc.getStatusLine() + "\n";
    }

    @Override
    public String getName() {
        return "Escort Adventure Window";
    }
}