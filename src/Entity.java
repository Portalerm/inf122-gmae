public class Entity implements Hurtable {
    private int row;
    private int col;
    private int hp;
    private boolean alive;
    private int attackPower;

    public Entity(int row, int col, int hp, int attackPower) {
        this.row = row;
        this.col = col;
        this.hp = hp;
        this.attackPower = attackPower;
        this.alive = true;
    }

    private void syncAlive() {
        if (this.hp <= 0) {
            this.hp = 0;
            this.alive = false;
        }
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getHp() {
        return hp;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setHp(int hp) {
        this.hp = hp;
        syncAlive();
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    @Override
    public void takeDamage(int amount) {
        hp -= amount;
        syncAlive();
    }

    @Override
    public void kill() {
        hp = 0;
        alive = false;
    }
}
