public class AccessControlService {

	public boolean canViewCampaign(User viewer, Campaign campaign) {
		for (Campaign c : viewer.getOwnedCampaigns()) {
			if (c.getUuid() == campaign.getUuid()) {
				return true;
			}
		}
		for (CampaignShare cs : viewer.getSharedCampaigns()) {
			if (cs.getCampaign().getUuid() == campaign.getUuid()) {
				return true;
			}
		}
		return false;
	}

	public boolean canEditCampaign(User viewer, Campaign campaign) {
		for (Campaign c : viewer.getOwnedCampaigns()) {
			if (c.getUuid() == campaign.getUuid()) {
				return true;
			}
		}
		for (CampaignShare cs : viewer.getSharedCampaigns()) {
			if (cs.getCampaign().getUuid() == campaign.getUuid()
					&& cs.getPermissionLevel() == PermissionLevel.COLLABORATIVE) {
				return true;
			}
		}
		return false;
	}

	public boolean canViewEvent(User viewer, QuestEvent event) {
		for (Campaign c : viewer.getOwnedCampaigns()) {
			for (QuestEvent qe : c.getQuestEvents()) {
				if (event.getUuid() == qe.getUuid()) {
					return true;
				}
			}
		}
		for (QuestEventShare qs : viewer.getSharedQuestEvents()) {
			if (qs.getQuestEvent().getUuid() == event.getUuid()) {
				return true;
			}
		}
		return false;
	}

	public boolean canEditEvent(User viewer, QuestEvent event) {
		for (Campaign c : viewer.getOwnedCampaigns()) {
			for (QuestEvent qe : c.getQuestEvents()) {
				if (event.getUuid() == qe.getUuid()) {
					return true;
				}
			}
		}
		for (QuestEventShare qs : viewer.getSharedQuestEvents()) {
			if (qs.getQuestEvent().getUuid() == event.getUuid()
					&& qs.getPermissionLevel() == PermissionLevel.COLLABORATIVE) {
				return true;
			}
		}
		return false;
	}
}
