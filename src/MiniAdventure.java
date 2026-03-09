import java.util.Scanner;

public interface MiniAdventure {
    void initialize(Character p1, Character p2, Realm realm);
    void run(Scanner scanner);
    boolean isComplete();
    boolean isVictory();
    String getResultSummary();
    String getName();
    String getDescription();
}
