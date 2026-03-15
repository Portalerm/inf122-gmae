
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class EscortAdventureContext extends MiniAdventureContext {
    private EscortPlayer p1;
    private EscortPlayer p2;
    private Npc npc = new Npc("Barton");

    public EscortAdventureContext() {
        super();
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
    public void placeMobs() {
        getMobs().add(RaidEntityFactory.createSkeleton(3, 5));
        getMobs().add(RaidEntityFactory.createSkeleton(5, 3));
        getMobs().add(RaidEntityFactory.createOgre(7, 5));
        getMobs().add(RaidEntityFactory.createOgre(3, 9));
        getMobs().add(RaidEntityFactory.createDragonling(9, 5));
        getMobs().add(RaidEntityFactory.createDragonling(10, 1));

        getMobs().add(RaidEntityFactory.createSpikeTrap(1, 6));
        getMobs().add(RaidEntityFactory.createPoisonTrap(5, 6));
        getMobs().add(RaidEntityFactory.createSpikeTrap(9, 8));
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
        System.out.println(npc.getStatusLine());
    }

    @Override
    public void displayVictoryMessage() {
        System.out.println("\n*** VICTORY! ***");
        System.out.println("The escort is complete! Distributing loot...\n");
        List<Item> victoryLoot = RaidLootTable.generateVictoryLoot();
        for (Item item : victoryLoot) {
            p1.getCharacter().getInventory().addItem(item);
            System.out.println("  " + p1.getCharacter().getName() + " received: "
                    + item.getInfo().getName() + " (" + item.getInfo().getRarity() + ")");
        }
        victoryLoot = RaidLootTable.generateVictoryLoot();
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
    public void handleMobInteraction(Player player, Mob mob) {
        EscortPlayer escortPlayer = (EscortPlayer) player;
        String pName = "P" + escortPlayer.getPlayerNumber();
        boolean npcPresent = escortPlayer.isEscortingNPC();
        switch (mob.getSymbol()) {
            case 'M':
                System.out.println(pName + " engages " + mob.getName() + "!");
                while (mob.isAlive() && escortPlayer.isAlive() && npc.isAlive()) {
                    mob.takeDamage(escortPlayer.getAttackPower());
                    System.out.println("  " + pName + " attacks for " + escortPlayer.getAttackPower()
                            + " damage! (" + mob.getName() + " HP: " + mob.getHp() + ")");
                    if (mob.isAlive()) {
                        escortPlayer.takeDamage(mob.getAttackPower());
                        System.out.println("  " + mob.getName() + " strikes back for "
                                + mob.getAttackPower() + " damage! (" + pName + " HP: " + escortPlayer.getHp() + ")");
                        if (npcPresent) {
                            npc.takeDamage(mob.getAttackPower());
                        }
                        System.out.println("  " + mob.getName() + " also strikes the NPC for "
                                + mob.getAttackPower() + " damage! (" + npc.getName() + " HP: " + npc.getHp() + ")");
                    }
                }
                if (!mob.isAlive()) {
                    System.out.println("  " + mob.getName() + " defeated!");
                    Item drop = RaidLootTable.generateMonsterDrop();
                    if (drop != null) {
                        escortPlayer.getCharacter().getInventory().addItem(drop);
                        System.out.println("  Loot: " + drop.getInfo().getName()
                                + " (" + drop.getInfo().getRarity() + ")");
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
                System.out.println(pName + " triggered a " + mob.getName() + "! (-"
                        + mob.getAttackPower() + " HP)");
                escortPlayer.takeDamage(mob.getAttackPower());
                if (npcPresent) {
                    System.out.println(npc.getName() + " is hit by " + mob.getName() + " as well! (-"
                            + mob.getAttackPower() + " HP)");
                    npc.takeDamage(mob.getAttackPower());
                }
                mob.kill();
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

    @Override
    public String getDescription() {
        return "A co-op turn-based mission where 2 players traverse a dungeon filled "
                + "monsters and traps while one player is personally escorting an NPC towards the exit.";
    }
}
