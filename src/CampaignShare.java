public class CampaignShare extends Identifiable {
	private Campaign campaign;
	private User sharedWith;
	private User sender;
	private PermissionLevel permissionLevel;

	public CampaignShare(Campaign campaign, User sharedWith, User sender, PermissionLevel permissionLevel) {
		this.campaign = campaign;
		this.sharedWith = sharedWith;
		this.sender = sender;
		this.permissionLevel = permissionLevel;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
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
