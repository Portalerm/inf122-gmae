public class Enemy extends Entity {
    private String name;
    private char symbol;

    public Enemy(String name, char symbol, int row, int col, int hp, int attackPower) {
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
