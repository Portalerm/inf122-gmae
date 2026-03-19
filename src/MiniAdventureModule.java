import java.util.Scanner;

public interface MiniAdventureModule {
   
    void initialize(Character c1, Character c2, Realm realm);

    void run(Scanner scanner);

    void reset();

    boolean isComplete();

    boolean isVictory();
}