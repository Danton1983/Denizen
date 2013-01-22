package net.aufdemrand.denizen.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import org.bukkit.craftbukkit.v1_4_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.commands.core.EngageCommand;
import net.aufdemrand.denizen.npc.SpeechEngine.Reason;
import net.aufdemrand.denizen.npc.SpeechEngine.TalkType;
import net.aufdemrand.denizen.scripts.ScriptHelper;
import net.aufdemrand.denizen.triggers.AbstractTrigger;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.minecraft.server.v1_4_R1.EntityLiving;

public class DenizenNPC {

	private NPC citizensNPC;
	private Denizen plugin;
	private ScriptHelper sE;


	DenizenNPC(NPC citizensNPC) {
		this.citizensNPC = citizensNPC;
		this.plugin = (Denizen) Bukkit.getServer().getPluginManager().getPlugin("Denizen");
		this.sE = plugin.getScriptEngine().helper;
	}

	public EntityLiving getHandle() {
		return ((CraftLivingEntity) getEntity()).getHandle();
	}

	public NPC getCitizensEntity() {
		return citizensNPC;
	}


	public LivingEntity getEntity() {
		try {
			return citizensNPC.getBukkitEntity();
		} catch (NullPointerException e) {

			return null;
		}
	}


	public void talk(TalkType talkType, Player thePlayer, String theText) {
		plugin.getSpeechEngine().talk(this, thePlayer, theText, talkType);
	}


	public void talk(TalkType talkType, Player thePlayer, Reason theReason) {
		plugin.getSpeechEngine().talk(this, thePlayer, theReason, talkType);
	}


	public boolean isToggled() {
		return citizensNPC.getTrait(DenizenTrait.class).isToggled();
	}


	public EntityType getEntityType() {
		return citizensNPC.getBukkitEntity().getType();
	}


	public Navigator getNavigator() {
		return citizensNPC.getNavigator();
	}


	public int getId() {
		return citizensNPC.getId();
	}


	public String getName() {
		return citizensNPC.getName();
	}


	public void showInfo(Player theClicker) {
		plugin.getDenizenNPCRegistry().showInfo(theClicker, this);
	}


	public boolean isInteractable(String triggerName, Player thePlayer) {
		return sE.denizenIsInteractable(triggerName, this);
	}


	public String getInteractScript(Player thePlayer, Class<? extends AbstractTrigger> triggerType) {
		return sE.getInteractScript(this, thePlayer, triggerType);
	}


	public boolean isSpawned() {
		return citizensNPC.isSpawned();
	}


	public Location getLocation() {
		return citizensNPC.getBukkitEntity().getLocation();
	}


	public World getWorld() {
		return citizensNPC.getBukkitEntity().getWorld();
	}


	public void setHealth(int newHealth) {
		((CraftLivingEntity) getEntity()).getHandle().setHealth(newHealth);
	}

	@Override
	public String toString() {
		return "DenizenNPC " + citizensNPC.getName() + "/" + citizensNPC.getId();
	}

	public boolean isLookingClose() {
		citizensNPC.getTrait(LookClose.class).toggle();
		return citizensNPC.getTrait(LookClose.class).toggle();
	}

	public void lookClose(boolean lookclose) {

		if (!lookclose) {
			if (citizensNPC.getTrait(LookClose.class).toggle())
				citizensNPC.getTrait(LookClose.class).toggle();
		}

		else if (lookclose) {
			if (!citizensNPC.getTrait(LookClose.class).toggle()) 
				citizensNPC.getTrait(LookClose.class).toggle();
		}

	}

	public boolean isInteracting() {
		if (!plugin.getCommandRegistry().getCommand(EngageCommand.class).getEngaged(this))
			return true;
		else return false;
	}

	public boolean hasTrigger(String triggerName) {
		return citizensNPC.getTrait(DenizenTrait.class).triggerIsEnabled(triggerName);
	}


}
