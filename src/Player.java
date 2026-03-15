
public class Player extends Entity {

    private Character character;
    private int playerNumber;

    public Player(Character character, int playerNumber, int startRow, int startCol) {
        super(startRow, startCol, 0, 0);
        this.character = character;
        this.playerNumber = playerNumber;

        int maxHp;
        int attackPower;

        switch (character.getClassType()) {
            case "Warrior":
                maxHp = 12;
                attackPower = 3;
                break;
            case "Mage":
                maxHp = 8;
                attackPower = 5;
                break;
            case "Archer":
                maxHp = 10;
                attackPower = 4;
                break;
            case "Rogue":
                maxHp = 10;
                attackPower = 4;
                break;
            case "Monk":
                maxHp = 9;
                attackPower = 3;
                break;
            default:
                maxHp = 10;
                attackPower = 3;
                break;
        }

        setHp(maxHp);
        setMaxHp(maxHp);
        setAttackPower(attackPower);
    }

    public Character getCharacter() {
        return character;
    }

    
    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getStatusLine() {
        String status = isAlive() ? (getHp() + "/" + getMaxHp() + " HP") : "DEAD";
        return "P" + playerNumber + " " + character.getName()
                + " (" + character.getClassType() + ") [" + status
                + "]";
    }
}
