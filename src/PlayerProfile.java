import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerProfile {

    private User user;
    private List<AdventureRecord> history;

    /** New profile with empty history. */
    public PlayerProfile(String playerName) {
        this.user = new User(playerName);
        this.history = new ArrayList<>();
    }

    /** Reconstruction constructor used by PlayerProfileService when loading from disk. */
    public PlayerProfile(User user, List<AdventureRecord> history) {
        this.user = user;
        this.history = new ArrayList<>(history);
    }

    public String getName() {
        return user.getName();
    }

    public User getUser() {
        return user;
    }


    public Character getActiveCharacter() {
        List<Character> chars = user.getCharacters();
        if (chars.isEmpty()) return null;
        return chars.get(chars.size() - 1);
    }

    public void setActiveCharacter(Character character) {
        user.addCharacter(character);
    }

    /** Appends a completed adventure record to this profile's history. */
    public void addAdventureRecord(AdventureRecord record) {
        history.add(record);
    }

    public List<AdventureRecord> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public void displayProfile() {
        System.out.println("========================================");
        System.out.println("  Profile: " + getName());
        System.out.println("========================================");

        Character active = getActiveCharacter();
        if (active == null) {
            System.out.println("  Character: (none yet)");
        } else {
            System.out.println("  Character: " + active.getName()
                    + " the " + active.getClassType()
                    + " (Level " + active.getLevel() + ")");

            List<Item> items = active.getInventory().getItems();
            if (items.isEmpty()) {
                System.out.println("  Inventory: (empty)");
            } else {
                System.out.println("  Inventory:");
                for (Item item : items) {
                    System.out.println("    - " + item.getInfo().getName()
                            + " [" + item.getInfo().getRarity() + "]");
                }
            }
        }

        System.out.println("  Adventure History:");
        if (history.isEmpty()) {
            System.out.println("    (no adventures yet)");
        } else {
            int start = Math.max(0, history.size() - 5);
            for (int i = start; i < history.size(); i++) {
                System.out.println("    " + history.get(i).toDisplayString());
            }
        }
        System.out.println("========================================");
    }

    /**
     * FORMAT:
     * "---"
     *   NAME:<playerName>
     *   CHARACTER:<name>,<classType>,<level>
     *   ITEM:<itemName>,<rarity>   (one per inventory item)
     *   RECORD:<AdventureRecord.serialize()>  (one per history entry)
     * "---"
     */
    public String serialize() {
        String ret = "NAME:" + user.getName() + "\n";
        Character active = getActiveCharacter();
        
        if (active != null) {
            ret += "CHARACTER:" + active.getName() + "," + active.getClassType() + "," + active.getLevel() + "\n";
 
            for (Item item : active.getInventory().getItems()) {
                ret += "ITEM:" + item.getInfo().getName() + "," + item.getInfo().getRarity() + "\n";
            }
        }
 
        for (AdventureRecord record : history) {
            ret += "RECORD:" + record.serialize() + "\n";
        }
 
        ret += "---\n";
        return ret;
    }
}