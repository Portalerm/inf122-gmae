public class EscortEnemyFactory extends EnemyFactory {

    public static Loot createLoot(int row, int col) {
        return new Loot('L', row, col);
    }
}