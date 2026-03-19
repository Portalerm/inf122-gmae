import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiniAdventureManager {
    private static MiniAdventureManager instance;
    private final List<MiniAdventureDefinition> adventures;

    private MiniAdventureManager() {
        this.adventures = new ArrayList<>();
    }

    public static MiniAdventureManager getInstance() {
        if (instance == null) {
            instance = new MiniAdventureManager();
        }
        return instance;
    }

    public void register(MiniAdventureDefinition definition) {
        if (definition != null) {
            adventures.add(definition);
        }
    }

    public List<MiniAdventureDefinition> getAllAdventures() {
        return Collections.unmodifiableList(adventures);
    }

    public MiniAdventureDefinition getAdventure(int index) {
        if (index >= 0 && index < adventures.size()) {
            return adventures.get(index);
        }
        return null;
    }

    public int size() {
        return adventures.size();
    }

    public void clear() {
        adventures.clear();
    }
}