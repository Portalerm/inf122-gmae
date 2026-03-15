public class RaidEntityFactory {

    public static RaidMob createSkeleton(int row, int col) {
        return new RaidMob("Skeleton", 'M', row, col, 2, 1);
    }

    public static RaidMob createOgre(int row, int col) {
        return new RaidMob("Ogre", 'M', row, col, 4, 2);
    }

    public static RaidMob createDragonling(int row, int col) {
        return new RaidMob("Dragonling", 'M', row, col, 6, 3);
    }

    public static RaidMob createSpikeTrap(int row, int col) {
        return new RaidMob("Spike Trap", 'T', row, col, 0, 1);
    }

    public static RaidMob createPoisonTrap(int row, int col) {
        return new RaidMob("Poison Trap", 'T', row, col, 0, 2);
    }

    public static RaidMob createObjective(int row, int col, String name) {
        return new RaidMob(name, 'O', row, col, 0, 0);
    }
}
