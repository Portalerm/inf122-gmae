public class RaidPlayer extends Player {

    private int objectivesCollected;

    public RaidPlayer(Character character, int playerNumber, int startRow, int startCol) {
        super(character, playerNumber, startRow, startCol);
        this.objectivesCollected = 0;
    }

    public int getObjectivesCollected() {
        return objectivesCollected;
    }

    public void collectObjective() {
        objectivesCollected++;
    }

    @Override
    public String getStatusLine() {
        return super.getStatusLine() + " Objectives: " + objectivesCollected;
    }
}