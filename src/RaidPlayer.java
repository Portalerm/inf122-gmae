public class RaidPlayer {
    private Character character;
    private int row;
    private int col;
    private int hp;
    private int maxHp;
    private int attackPower;
    private int objectivesCollected;
    private boolean alive;
    private int playerNumber;

    public RaidPlayer(Character character, int playerNumber, int startRow, int startCol) {
        this.character = character;
        this.playerNumber = playerNumber;
        this.row = startRow;
        this.col = startCol;
        this.objectivesCollected = 0;
        this.alive = true;

        switch (character.getClassType()) {
            case "Warrior":
                this.maxHp = 12; this.attackPower = 3; break;
            case "Mage":
                this.maxHp = 8; this.attackPower = 5; break;
            case "Archer":
                this.maxHp = 10; this.attackPower = 4; break;
            case "Rogue":
                this.maxHp = 10; this.attackPower = 4; break;
            case "Monk":
                this.maxHp = 9; this.attackPower = 3; break;
            default:
                this.maxHp = 10; this.attackPower = 3; break;
        }
        this.hp = this.maxHp;
    }

    public Character getCharacter() { return character; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttackPower() { return attackPower; }
    public int getObjectivesCollected() { return objectivesCollected; }
    public boolean isAlive() { return alive; }
    public int getPlayerNumber() { return playerNumber; }

    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }

    public void collectObjective() {
        objectivesCollected++;
    }

    public String getStatusLine() {
        String status = alive ? (hp + "/" + maxHp + " HP") : "DEAD";
        return "P" + playerNumber + " " + character.getName()
            + " (" + character.getClassType() + ") [" + status
            + "] Objectives: " + objectivesCollected;
    }
}
