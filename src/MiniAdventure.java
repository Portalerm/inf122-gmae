
import java.util.Scanner;

public class MiniAdventure implements MiniAdventureModule {

    private final MiniAdventureContext context;
    private Character initialCharacter1;
    private Character initialCharacter2;
    private Realm initialRealm;

    public MiniAdventure(MiniAdventureContext context) {
        this.context = context;
    }

    public MiniAdventureContext getContext() {
        return context;
    }

    @Override
    public void initialize(Character c1, Character c2, Realm realm) {
        this.initialCharacter1 = c1;
        this.initialCharacter2 = c2;
        this.initialRealm = realm;

        context.setRealm(realm);
        context.setMap(Map.createDefaultMap());

        context.placeEnemies();

        for (Enemy enemy : context.getEnemies()) {
            context.getMap().setTile(enemy.getRow(), enemy.getCol(), enemy.getSymbol());
        }

        context.initializeLocal(c1, c2);
    }

    @Override
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

    @Override
    public void reset() {
        if (initialCharacter1 == null || initialCharacter2 == null || initialRealm == null) {
            return;
        }
        context.reset();
        initialize(initialCharacter1, initialCharacter2, initialRealm);
    }

    @Override
    public boolean isComplete() {
        return context.isComplete();
    }

    @Override
    public boolean isVictory() {
        return context.isVictory();
    }
}
