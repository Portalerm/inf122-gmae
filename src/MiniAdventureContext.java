
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class MiniAdventureContext {

    static final int P1_START_ROW = 1;
    static final int P1_START_COL = 1;
    static final int P2_START_ROW = 1;
    static final int P2_START_COL = 2;

    private Player player1;
    private Player player2;
    private Map map;
    private List<Enemy> enemies;
    private Realm realm;
    private boolean complete;
    private boolean victory;
    private StringBuilder log;

    public MiniAdventureContext() {
        this.enemies = new ArrayList<>();
        this.complete = false;
        this.victory = false;
        this.log = new StringBuilder();
    }

    abstract void initializeLocal(Character c1, Character c2);

    abstract void placeEnemies();

    abstract void displayHeader();

    abstract void displayTurnStatus(int turnCount);

    abstract void displayVictoryMessage();

    abstract String getName();

    abstract String getDescription();

    abstract void handleEnemyInteraction(Player player, Enemy enemy);

    abstract boolean checkVictory(Player player);

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Map getMap() {
        return map;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Realm getRealm() {
        return realm;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isVictory() {
        return victory;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    boolean checkCompletion(Scanner scanner) {
        if (player1.isAlive()) {
            System.out.print("Player 1 action: ");
            String action = scanner.nextLine().trim().toLowerCase();
            if (action.equals("q")) {
                complete = true;
                victory = false;
                System.out.println("Mission abandoned!");
                return true;
            }
            handleAction(player1, action);
            if (checkVictory(player1)) {
                return true;
            }
        }

        if (player2.isAlive() && !complete) {
            System.out.print("Player 2 action: ");
            String action = scanner.nextLine().trim().toLowerCase();
            if (action.equals("q")) {
                complete = true;
                victory = false;
                System.out.println("Mission abandoned!");
                return true;
            }
            handleAction(player2, action);
            if (checkVictory(player2)) {
                return true;
            }
        }

        if (!player1.isAlive() && !player2.isAlive()) {
            complete = true;
            victory = false;
            System.out.println("\nBoth characters have fallen! The mission was a failure.");
            return true;
        }

        return false;
    }

    ;

    void showInventory(Player player) {
        System.out.println(player.getCharacter().getName() + "'s " + player.getCharacter().getInventory());
    }

    Enemy findEnemy(int row, int col) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.getRow() == row && enemy.getCol() == col) {
                return enemy;
            }
        }
        return null;
    }

    void handleAction(Player player, String action) {
        int newRow = player.getRow();
        int newCol = player.getCol();

        switch (action) {
            case "w":
                newRow--;
                break;
            case "s":
                newRow++;
                break;
            case "a":
                newCol--;
                break;
            case "d":
                newCol++;
                break;
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

        Enemy enemy = findEnemy(newRow, newCol);
        if (enemy != null && enemy.isAlive()) {
            handleEnemyInteraction(player, enemy);
            if (!player.isAlive()) {
                return;
            }
            if (!enemy.isAlive()) {
                map.setTile(newRow, newCol, '.');
                player.setRow(newRow);
                player.setCol(newCol);
            }
        } else {
            player.setRow(newRow);
            player.setCol(newCol);
        }
    }

    String getResultSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Mission Summary ===\n");
        sb.append("Realm: ").append(realm.getName()).append("\n");
        sb.append("Result: ").append(victory ? "VICTORY" : "DEFEAT").append("\n");
        sb.append(player1.getStatusLine()).append("\n");
        sb.append(player2.getStatusLine()).append("\n");
        return sb.toString();
    }
}
