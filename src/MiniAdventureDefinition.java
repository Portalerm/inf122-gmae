public class MiniAdventureDefinition {
    private final String id;
    private final String displayName;
    private final String description;
    private final MiniAdventureContext context;

    public MiniAdventureDefinition(String id, String displayName, String description, MiniAdventureContext context) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public MiniAdventureContext getContext() {
        return context;
    }
}