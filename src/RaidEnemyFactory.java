public class RaidEnemyFactory extends EnemyFactory {

    public static Objective createObjective(int row, int col, String name) {
        return new Objective(name, 'O', row, col);
    }
}