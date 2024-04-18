package dev.tqqn.kireiwalls.modules.disabledfunctions;

import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DisabledFunctionsListener implements Listener {

    @EventHandler
    public void onWitherHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Wither) event.setCancelled(true);
    }
}
