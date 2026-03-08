public class CampaignService {

	public Campaign createCampaign(User owner, String name, CampaignVisibility visibility) {
		Campaign campaign = new Campaign(name, visibility);
		owner.addCampaign(campaign);
		return campaign;
	}

	public Campaign createCampaign(User owner, String name) {
		return createCampaign(owner, name, CampaignVisibility.PRIVATE);
	}

	public void renameCampaign(User actor, Campaign campaign, String newName) {
		AccessControlService acs = new AccessControlService();
		if (acs.canEditCampaign(actor, campaign)) {
			campaign.rename(newName);
		}
	}

	public void setVisibility(User actor, Campaign campaign, CampaignVisibility visibility) {
		AccessControlService acs = new AccessControlService();
		if (acs.canEditCampaign(actor, campaign)) {
			campaign.setVisibility(visibility);
		}
	}

	public void archiveCampaign(User actor, Campaign campaign) {
		AccessControlService acs = new AccessControlService();
		if (acs.canEditCampaign(actor, campaign)) {
			campaign.archive();
		}
	}

	public void unarchiveCampaign(User actor, Campaign campaign) {
		AccessControlService acs = new AccessControlService();
		if (acs.canEditCampaign(actor, campaign)) {
			campaign.unarchive();
		}
	}

	public CampaignShare shareCampaign(User sender, Campaign campaign, User recipient, PermissionLevel perm) {
		CampaignShare share = new CampaignShare(campaign, recipient, sender, perm);
		recipient.addSharedCampaign(share);
		return share;
	}
}
