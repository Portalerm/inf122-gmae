
public class EscortPlayer extends Player {

    private boolean escortingNPC;
    private Npc escortedNPC;

    public EscortPlayer(Character character, int playerNumber, int startRow, int startCol) {
        super(character, playerNumber, startRow, startCol);
        escortedNPC = null;
    }

    public void setEscort(boolean bool) {
        escortingNPC = bool;
    }

    public boolean isEscortingNPC() {
        return escortingNPC;
    }

    public void addNPC(Npc npc) {
        escortedNPC = npc;
    }

    public void removeNPC() {
        escortedNPC = null;
    }

    @Override
    public String getStatusLine() {
        String npcInfo = escortingNPC ? " {Escorting " + escortedNPC.getName() + "}" : "";
        return super.getStatusLine() + npcInfo;
    }
}
