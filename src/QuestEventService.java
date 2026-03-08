public class QuestEventService {

	public QuestEvent addEvent(User actor, Campaign campaign, String title, Time start, Time end, Realm realm) {
		AccessControlService acs = new AccessControlService();
		if (!acs.canEditCampaign(actor, campaign)) {
			return null;
		}
		QuestEvent event = new QuestEvent(title, start, end, realm);
		campaign.addQuestEvent(event);
		return event;
	}

	public void updateEvent(User actor, QuestEvent event) {
		AccessControlService acs = new AccessControlService();
		if (!acs.canEditEvent(actor, event)) {
			return;
		}
		for (Campaign c : actor.allParticipatingCampaigns()) {
			for (int j = 0; j < c.getQuestEvents().size(); ++j) {
				if (c.getQuestEvents().get(j).getUuid() == event.getUuid()) {
					c.getQuestEvents().set(j, event);
					return;
				}
			}
		}
	}

	public void removeEvent(User actor, QuestEvent event) {
		AccessControlService acs = new AccessControlService();
		if (!acs.canEditEvent(actor, event)) {
			return;
		}
		for (Campaign c : actor.allParticipatingCampaigns()) {
			c.removeQuestEvent(event);
		}
	}

	public QuestEventShare shareEvent(User sender, QuestEvent event, User recipient, PermissionLevel perm) {
		QuestEventShare share = new QuestEventShare(event, recipient, sender, perm);
		recipient.addSharedQuestEvent(share);
		return share;
	}

	public void addParticipant(User actor, QuestEvent event, Character character) {
		AccessControlService acs = new AccessControlService();
		if (!acs.canEditEvent(actor, event)) {
			return;
		}
		EventParticipation participation = new EventParticipation(event);
		character.addParticipation(participation);
	}

	public void applyInventoryDelta(Character character, EventInventoryDelta delta) {
		if (delta.getQuantityDelta() > 0) {
			ItemInfo info = new ItemInfo(delta.getItemName(), "", Rarity.COMMON);
			Item item = new Item(info);
			character.getInventory().addItem(item);
		} else if (delta.getQuantityDelta() < 0) {
			Item existing = null;
			for (Item i : character.getInventory().getItems()) {
				if (i.getInfo().getName().equals(delta.getItemName())) {
					existing = i;
					break;
				}
			}
			if (existing != null) {
				character.getInventory().removeItem(existing.getUuid());
			}
		}
	}
}
