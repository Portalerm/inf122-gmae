import java.util.Collections;
import java.util.List;

public class NullPlayerProfile extends PlayerProfile {

    private static final NullPlayerProfile INSTANCE = new NullPlayerProfile();

    private NullPlayerProfile() {
        super("(unknown-profile)");
    }

    public static NullPlayerProfile getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isNullProfile() {
        return true;
    }

    @Override
    public Character getActiveCharacter() {
        return null;
    }

    @Override
    public void setActiveCharacter(Character character) {
        // No-op for null profile.
    }

    @Override
    public void addAdventureRecord(AdventureRecord record) {
        // No-op for null profile.
    }

    @Override
    public List<AdventureRecord> getHistory() {
        return Collections.emptyList();
    }

    @Override
    public void displayProfile() {
        System.out.println("No existing profile found.");
    }

    @Override
    public String serialize() {
        return "";
    }
}