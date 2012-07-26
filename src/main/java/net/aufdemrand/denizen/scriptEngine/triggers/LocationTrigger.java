package net.aufdemrand.denizen.scriptEngine.triggers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.aufdemrand.denizen.npc.DenizenNPC;
import net.aufdemrand.denizen.npc.DenizenTrait;
import net.aufdemrand.denizen.scriptEngine.AbstractTrigger;
import net.aufdemrand.denizen.scriptEngine.ScriptHelper;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class LocationTrigger extends AbstractTrigger implements Listener {

	/* Listens for the PlayerMoveEvent to see if a player is within range
	 * of a Denizen to trigger a Location Trigger */

	@EventHandler
	public void locationTrigger(PlayerMoveEvent event) {

		/* Do not run any code unless the player actually moves blocks */
		if (!event.getTo().getBlock().equals(event.getFrom().getBlock())) {

			ScriptHelper sE = plugin.getScriptEngine().helper;

			/* Do not run any code if there aren't any location triggers */
			if (!plugin.bookmarks.getLocationTriggerList().isEmpty()) {

				/* Check player location against each Location Trigger */
				for (Location theLocation : plugin.bookmarks.getLocationTriggerList().keySet()) {

					if (plugin.bookmarks.checkLocation(event.getPlayer(), theLocation, plugin.settings.LocationTriggerRangeInBlocks()) 
							&& sE.checkCooldown(event.getPlayer(), LocationTrigger.class)) {

						DenizenNPC theDenizen = null;
						String locationTriggered = plugin.bookmarks.getLocationTriggerList().get(theLocation).split(":")[2];


						/* Player matches Location, find NPC it belongs to */

						if (plugin.bookmarks.getLocationTriggerList().get(theLocation).contains("ID:"))
							theDenizen = plugin.getDenizenNPCRegistry().getDenizen(CitizensAPI.getNPCRegistry().getById(Integer.valueOf(plugin.bookmarks.getLocationTriggerList().get(theLocation).split(":")[1])));

						else if (plugin.bookmarks.getLocationTriggerList().get(theLocation).contains("NAME:")) {
							List<DenizenNPC> denizenList = new ArrayList<DenizenNPC>();

							/* Find all the NPCs with the name */
							for (DenizenNPC npc : plugin.getDenizenNPCRegistry().getDenizens().values()) {
								if(npc.getName().equals(plugin.bookmarks.getLocationTriggerList().get(theLocation).split(":")[1])) {
									denizenList.add(npc);
									theDenizen = npc;
								}
							}

							/* Check which NPC is closest */
							for (DenizenNPC npc : denizenList) {
								if (npc.getEntity().getLocation().distance(event.getPlayer().getLocation())
										< theDenizen.getEntity().getLocation().distance(event.getPlayer().getLocation()))
									theDenizen = npc;
							}
						} 

						/* Cancel out if for some reason no denizen can be found */
						if (theDenizen == null) return;

						/* Set MetaData and Trigger */
						if (event.getPlayer().hasMetadata("locationtrigger")) {

							/* Unless current MetaData already contains the location trigger. This means the player
							 * is still in the location... */
							if (locationTriggered.equals(event.getPlayer().getMetadata("locationtrigger")))
								return;

							/* Before triggering, check if LocationTriggers are enabled, cooldown is met, and NPC
							 * is not already engaged... */
							else if (theDenizen.IsInteractable(triggerName, event.getPlayer())) {

								/* Set Metadata value to avoid retrigger. */
								event.getPlayer().setMetadata("locationtrigger", new FixedMetadataValue(plugin, locationTriggered));

								/* TRIGGER! */
								sE.setCooldown(event.getPlayer(), LocationTrigger.class, plugin.settings.DefaultLocationCooldown());
								parseLocationTrigger(theDenizen, event.getPlayer(), locationTriggered);
							}

						} else {

							/* Check if proximity triggers are enabled, check player cooldown, check if NPC is engaged... */
							if (theDenizen.IsInteractable(triggerName, event.getPlayer())) {

								/* Set Metadata value to avoid retrigger. */
								event.getPlayer().setMetadata("locationtrigger", new FixedMetadataValue(plugin, locationTriggered));

								/* TRIGGER! */
								sE.setCooldown(event.getPlayer(), LocationTrigger.class, plugin.settings.DefaultLocationCooldown());
								parseLocationTrigger(theDenizen, event.getPlayer(), locationTriggered);
							}
						}
					}
				}
			}
		}
	}



	private void parseLocationTrigger(DenizenNPC theDenizen, Player thePlayer, String theLocationName) {

		/* Find script and run it */		

	}




}