
public class EnemyFactory {

    public static Enemy createSkeleton(int row, int col) {
        return new Enemy("Skeleton", 'M', row, col, 2, 1);
    }

    public static Enemy createOgre(int row, int col) {
        return new Enemy("Ogre", 'M', row, col, 4, 2);
    }

    public static Enemy createDragonling(int row, int col) {
        return new Enemy("Dragonling", 'M', row, col, 6, 3);
    }

    public static Enemy createSpikeTrap(int row, int col) {
        return new Enemy("Spike Trap", 'T', row, col, 0, 1);
    }

    public static Enemy createPoisonTrap(int row, int col) {
        return new Enemy("Poison Trap", 'T', row, col, 0, 2);
    }
}
