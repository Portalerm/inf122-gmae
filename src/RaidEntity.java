public class RaidEntity {
    private String name;
    private char symbol;
    private int row;
    private int col;
    private int hp;
    private int damage;
    private boolean alive;

    public RaidEntity(String name, char symbol, int row, int col, int hp, int damage) {
        this.name = name;
        this.symbol = symbol;
        this.row = row;
        this.col = col;
        this.hp = hp;
        this.damage = damage;
        this.alive = true;
    }

    public String getName() { return name; }
    public char getSymbol() { return symbol; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getHp() { return hp; }
    public int getDamage() { return damage; }
    public boolean isAlive() { return alive; }

    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }

    public void kill() {
        hp = 0;
        alive = false;
    }
}
