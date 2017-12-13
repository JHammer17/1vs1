package de.OnevsOne.Methods;

public enum JSound {

	DEATH("mob.blaze.death","ENTITY_BLAZE_DEATH"),
	CLICK("random.click","UI_BUTTON_CLICK"),
	ANVIL("random.anvil_use","BLOCK_ANVIL_USE"),
	FIREWORK("fireworks.launch","ENTITY_FIREWORK_LAUNCH"),
	ORB_PLING("random.orb","ENTITY_EXPERIENCE_ORB_PICKUP"),
	LEVEL("random.levelup","ENTITY_PLAYER_LEVELUP"),
	DRUM("note.bassattack","BLOCK_NOTE_BASEDRUM"),
	WITHER_DEATH("",""),
	ENDER_DRAGON("","");
	
	
	
	String oldVersion;
	String newVersion;
	
	private JSound(String oldVersion2,String newVersion2) {
		oldVersion = oldVersion2;
		newVersion = newVersion2;
	}
	
	public String getOld() {
		return oldVersion;
	}
	
	public  String getNew() {
		return newVersion;
	}
	
	
}
