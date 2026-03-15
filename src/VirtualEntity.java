public class VirtualEntity implements Hurtable {
    private int hp;
    private int maxHp;
    private boolean alive;
    private int attackPower;

    public VirtualEntity(int hp, int maxHp, int attackPower) {
        this.hp = hp;
        this.maxHp = maxHp;
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

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setHp(int hp) {
        this.hp = hp;
        syncAlive();
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
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
