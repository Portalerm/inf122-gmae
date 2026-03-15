public class Npc implements Hurtable {
    private String name;
    private int hp;
    private int maxHp;
    private boolean alive;

    public Npc(String name) {
        this.name = name;
        this.maxHp = 5;
        this.hp = maxHp;
        this.alive = true;
    }

    public String getName() { return name; }
    public boolean isAlive() { return alive; }
    public int getHp() { return hp;}

    @Override
    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }

    @Override
    public void kill() {
        hp = 0;
        alive = false;
    }

    public String getStatusLine() {
        String status = alive ? (hp + "/" + maxHp + " HP") : "DEAD";
        return "NPC " + name + " [" + status + "]";
    }
}
