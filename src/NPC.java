public class NPC extends VirtualEntity {
    private String name;

    public NPC(String name) {
        super(5, 5, 0);
        this.name = name;
    }

    public String getName() { return name; }

    public String getStatusLine() {
        String status = isAlive() ? (getHp() + "/" + getMaxHp() + " HP") : "DEAD";
        return "NPC " + name + " [" + status + "]";
    }
}