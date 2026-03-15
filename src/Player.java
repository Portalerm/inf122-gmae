
public class Player extends Entity {

    private Character character;
    private int playerNumber;
    private int maxHp;

    public Player(Character character, int playerNumber, int startRow, int startCol) {
        super(startRow, startCol, 0, 0);
        this.character = character;
        this.playerNumber = playerNumber;
        int attackPower;

        switch (character.getClassType()) {
            case "Warrior":
                this.maxHp = 12;
                attackPower = 3;
                break;
            case "Mage":
                this.maxHp = 8;
                attackPower = 5;
                break;
            case "Archer":
                this.maxHp = 10;
                attackPower = 4;
                break;
            case "Rogue":
                this.maxHp = 10;
                attackPower = 4;
                break;
            case "Monk":
                this.maxHp = 9;
                attackPower = 3;
                break;
            default:
                this.maxHp = 10;
                attackPower = 3;
                break;
        }

        setHp(this.maxHp);
        setAttackPower(attackPower);
    }

    public Character getCharacter() {
        return character;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getStatusLine() {
        String status = isAlive() ? (getHp() + "/" + this.maxHp + " HP") : "DEAD";
        return "P" + playerNumber + " " + character.getName()
                + " (" + character.getClassType() + ") [" + status
                + "]";
    }
}
