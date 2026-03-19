import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerProfileService {

    private static final String PROFILES_FILE = "profiles.txt";

    private List<PlayerProfile> profiles;

    /** Constructs the service and immediately loads profiles from disk. */
    public PlayerProfileService() {
        this.profiles = new ArrayList<>();
        loadProfiles();
    }

    private void loadProfiles() {
        File file = new File(PROFILES_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String profileName = null;
            String charName = null;
            String charClass = null;
            int charLevel = 0;
            List<Item> items = new ArrayList<>();
            List<AdventureRecord> history = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    // End of a profile block — assemble and store it
                    if (profileName != null) {
                        User user = new User(profileName);
                        if (charName != null) {
                            Character character = new Character(charName, charClass);
                            character.setLevel(charLevel);
                            for (Item item : items) character.getInventory().addItem(item);
                            user.addCharacter(character);
                        }
                        profiles.add(new PlayerProfile(user, history));
                    }
                    // Reset state for next block
                    profileName = null;
                    charName = null;
                    charClass = null;
                    charLevel = 0;
                    items = new ArrayList<>();
                    history = new ArrayList<>();

                } else if (line.startsWith("NAME:")) {
                    profileName = line.substring(5);

                } else if (line.startsWith("CHARACTER:")) {
                    String[] parts = line.substring(10).split(",");
                    charName = parts[0];
                    charClass = parts[1];
                    charLevel = Integer.parseInt(parts[2]);

                } else if (line.startsWith("ITEM:")) {
                    String[] parts = line.substring(5).split(",");
                    String itemName = parts[0];
                    Rarity rarity = Rarity.valueOf(parts[1]);
                    items.add(new Item(new ItemInfo(itemName, "", rarity)));

                } else if (line.startsWith("RECORD:")) {
                    history.add(AdventureRecord.deserialize(line.substring(7)));
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: could not read profiles file: " + e.getMessage());
        }
    }

    public void saveProfiles() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PROFILES_FILE))) {
            for (PlayerProfile profile : profiles) {
                writer.print(profile.serialize());
            }
        } catch (IOException e) {
            System.err.println("Warning: could not save profiles: " + e.getMessage());
        }
    }

    public PlayerProfile findByName(String name) {
        for (PlayerProfile profile : profiles) {
            if (profile.getName().equalsIgnoreCase(name)) {
                return profile;
            }
        }
        return NullPlayerProfile.getInstance();
    }

    public PlayerProfile createProfile(String name) {
        PlayerProfile profile = new PlayerProfile(name);
        profiles.add(profile);
        saveProfiles();
        return profile;
    }

    public List<String> getAllProfileNames() {
        List<String> names = new ArrayList<>();
        for (PlayerProfile profile : profiles) {
            names.add(profile.getName());
        }
        return names;
    }

    public void recordAdventure(PlayerProfile profile, AdventureRecord record) {
        if (profile.isNullProfile()) return;
        profile.addAdventureRecord(record);
        saveProfiles();
    }
}