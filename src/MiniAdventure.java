
import java.util.Scanner;

public class MiniAdventure {
    private MiniAdventureContext context;

    public MiniAdventure(MiniAdventureContext context) {
        this.context = context;
    }
    
    public void initialize(Character p1, Character p2, Realm realm) {
        context.setRealm(realm);
        context.setMap(Map.createDefaultMap());

        context.setPlayer1(new EscortPlayer(p1, 1, 1, 1));
        context.setPlayer2(new EscortPlayer(p2, 2, 1, 2));

        context.placeMobs();

        for (Mob mob : context.getMobs()) {
            context.getMap().setTile(mob.getRow(), mob.getCol(), mob.getSymbol());
        }

        context.initializeLocal();
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
    };
}
