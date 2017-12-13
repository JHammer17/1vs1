package de.OnevsOne.Methods;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import de.OnevsOne.main;

public class SoundManager {

	
	
	JSound sound;
	Player player;
	float volume;
	float pitch;
	
	public SoundManager(JSound sound, Player player, float volume,float pitch) {
		this.sound = sound;
		this.player = player;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public void play() {
		if(main.Version.contains("1_8")) {
			player.playSound(player.getLocation(), getNameOld(sound), volume, pitch);
		} else {
			Sound soundn = Sound.valueOf(getNameNew(sound));
			player.playSound(player.getLocation(), soundn, volume, pitch);
		}
	}
	
	private String getNameOld(JSound sound) {
		if(sound == JSound.ANVIL) {
			return "random.anvil_use";
		} else if(sound == JSound.CLICK) {
			return "random.click";
		} else if(sound == JSound.DEATH) {
			return "mob.blaze.death";
		} else if(sound == JSound.DRUM) {
			return "note.bassattack";
		} else if(sound == JSound.FIREWORK) {
			return "fireworks.launch";
		} else if(sound == JSound.LEVEL) {
			return "random.levelup";
		} else if(sound == JSound.ORB_PLING) {
			return "random.orb";
		} else if(sound == JSound.WITHER_DEATH) {
			return "mob.wither.death";
		} else if(sound == JSound.ENDER_DRAGON) {
			return "mob.enderdragon.growl";
		} else {
			return "random.anvil_use";
		}
	}
	
	private String getNameNew(JSound sound) {
		if(sound == JSound.ANVIL) {
			return "BLOCK_ANVIL_USE";
		} else if(sound == JSound.CLICK) {
			return "UI_BUTTON_CLICK";
		} else if(sound == JSound.DEATH) {
			return "ENTITY_BLAZE_DEATH";
		} else if(sound == JSound.DRUM) {
			return "BLOCK_NOTE_BASEDRUM";
		} else if(sound == JSound.FIREWORK) {
			return "ENTITY_FIREWORK_LAUNCH";
		} else if(sound == JSound.ORB_PLING) {
			return "ENTITY_EXPERIENCE_ORB_PICKUP";
		} else if(sound == JSound.LEVEL) {
			return "ENTITY_PLAYER_LEVELUP";
		} else if(sound == JSound.LEVEL) {
			return "ENTITY_WITHER_DEATH";
		} else if(sound == JSound.ENDER_DRAGON) {
			return "ENTITY_ENDERDRAGON_GROWL";
		} else {
			return "BLOCK_ANVIL_USE";
		}
	}
	
}
