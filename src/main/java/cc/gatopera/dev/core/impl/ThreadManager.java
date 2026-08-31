package cc.gatopera.dev.core.impl;

import cc.gatopera.dev.api.utils.world.BlockUtil;
import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.eventbus.EventPriority;
import cc.gatopera.dev.api.events.impl.TickEvent;
import cc.gatopera.dev.mod.modules.impl.render.PlaceRender;

public class ThreadManager {
    public static ClientService clientService;

    public ThreadManager() {
        Gatopera.EVENT_BUS.subscribe(this);
        clientService = new ClientService();
        clientService.setName("GatoperaClientService");
        clientService.setDaemon(true);
        clientService.start();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(TickEvent event) {
        if (event.isPre()) {
            if (!clientService.isAlive()) {
                clientService = new ClientService();
                clientService.setName("GatoperaClientService");
                clientService.setDaemon(true);
                clientService.start();
            }
            BlockUtil.placedPos.forEach(pos -> PlaceRender.renderMap.put(pos, PlaceRender.INSTANCE.create(pos)));
            BlockUtil.placedPos.clear();
            Gatopera.SERVER.onUpdate();
            Gatopera.PLAYER.onUpdate();
            Gatopera.MODULE.onUpdate();
            Gatopera.GUI.onUpdate();
            Gatopera.POP.onUpdate();
        }
    }

    public static class ClientService extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (Gatopera.MODULE != null) {
                        Gatopera.MODULE.onThread();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
