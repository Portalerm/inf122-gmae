public class QuestEventShare extends Identifiable {
	private QuestEvent questEvent;
	private User sharedWith;
	private User sender;
	private PermissionLevel permissionLevel;

	public QuestEventShare(QuestEvent questEvent, User sharedWith, User sender, PermissionLevel permissionLevel) {
		this.questEvent = questEvent;
		this.sharedWith = sharedWith;
		this.sender = sender;
		this.permissionLevel = permissionLevel;
	}

	public QuestEvent getQuestEvent() {
		return questEvent;
	}

	public void setQuestEvent(QuestEvent questEvent) {
		this.questEvent = questEvent;
	}

	public User getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(User sharedWith) {
		this.sharedWith = sharedWith;
	}

	public User getSender() {
		return sender;
	}

	public PermissionLevel getPermissionLevel() {
		return permissionLevel;
	}

	public void setPermissionLevel(PermissionLevel permissionLevel) {
		this.permissionLevel = permissionLevel;
	}
}