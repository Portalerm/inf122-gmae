public class Mob extends Entity {
    private String name;
    private char symbol;

    public Mob(String name, char symbol, int row, int col, int hp, int attackPower) {
        super(row, col, hp, attackPower);
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }
}
