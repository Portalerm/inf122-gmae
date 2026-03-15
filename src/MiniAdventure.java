
import java.util.Scanner;

public class MiniAdventure {

    private MiniAdventureContext context;

    public MiniAdventure(MiniAdventureContext context) {
        this.context = context;
    }

    public void initialize(Character c1, Character c2, Realm realm) {
        context.setRealm(realm);
        context.setMap(Map.createDefaultMap());

        context.placeEnemies();

        for (Enemy enemy : context.getEnemies()) {
            context.getMap().setTile(enemy.getRow(), enemy.getCol(), enemy.getSymbol());
        }

        context.initializeLocal(c1, c2);
    }

    public void run(Scanner scanner) {
        context.displayHeader();
        int turnCount = 0;

        while (!context.isComplete()) {
            turnCount++;
            context.displayTurnStatus(turnCount);
            if (context.checkCompletion(scanner)) {
                break;
            }
        }

        if (context.isVictory()) {
            context.displayVictoryMessage();
        }

        context.setComplete(true);
        System.out.println("\n" + context.getResultSummary());
    }
;
}
