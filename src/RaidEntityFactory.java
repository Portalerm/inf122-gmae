public class RaidEntityFactory {
    public static RaidEntity createSkeleton(int row, int col) {
        return new RaidEntity("Skeleton", 'M', row, col, 2, 1);
    }

    public static RaidEntity createOgre(int row, int col) {
        return new RaidEntity("Ogre", 'M', row, col, 4, 2);
    }

    public static RaidEntity createDragonling(int row, int col) {
        return new RaidEntity("Dragonling", 'M', row, col, 6, 3);
    }

    public static RaidEntity createSpikeTrap(int row, int col) {
        return new RaidEntity("Spike Trap", 'T', row, col, 0, 1);
    }

    public static RaidEntity createPoisonTrap(int row, int col) {
        return new RaidEntity("Poison Trap", 'T', row, col, 0, 2);
    }

    public static RaidEntity createObjective(int row, int col, String name) {
        return new RaidEntity(name, 'O', row, col, 0, 0);
    }
}
