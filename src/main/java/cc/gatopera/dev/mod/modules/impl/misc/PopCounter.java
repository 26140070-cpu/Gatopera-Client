package cc.gatopera.dev.mod.modules.impl.misc;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.i18n.I18n;
import cc.gatopera.dev.mod.modules.impl.client.ClientSetting;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.DeathEvent;
import cc.gatopera.dev.api.events.impl.TotemEvent;
import cc.gatopera.dev.core.impl.CommandManager;
import cc.gatopera.dev.mod.modules.Module;
import net.minecraft.entity.player.PlayerEntity;

public class PopCounter
        extends Module {

    public static PopCounter INSTANCE;
    public final BooleanSetting unPop =
            add(new BooleanSetting("Dead", true));
    public PopCounter() {
        super("PopCounter", Category.Misc);
        setChinese("图腾计数器");
        INSTANCE = this;
    }

    @EventHandler
    public void onPlayerDeath(DeathEvent event) {
        PlayerEntity player = event.getPlayer();
        if (Gatopera.POP.popContainer.containsKey(player.getName().getString())) {
            int l_Count = Gatopera.POP.popContainer.get(player.getName().getString());
            if (l_Count == 1) {
                if (player.equals(mc.player)) {
                    sendMessage("§f" + I18n.t("msg.pop.you_died_singular", "§f" + l_Count + "§r"), player.getId());
                } else {
                    sendMessage("§f" + I18n.t("msg.pop.player_died_singular", player.getName().getString(), "§f" + l_Count + "§r"), player.getId());
                }
            } else {
                if (player.equals(mc.player)) {
                    sendMessage("§f" + I18n.t("msg.pop.you_died_plural", "§f" + l_Count + "§r"), player.getId());
                } else {
                    sendMessage("§f" + I18n.t("msg.pop.player_died_plural", player.getName().getString(), "§f" + l_Count + "§r"), player.getId());
                }
            }
        } else if (unPop.getValue()) {
            if (player.equals(mc.player)) {
                sendMessage("§f" + I18n.t("msg.pop.you_died"), player.getId());
            } else {
                sendMessage("§f" + I18n.t("msg.pop.player_died", player.getName().getString()), player.getId());
            }
        }
    }

    @EventHandler
    public void onTotem(TotemEvent event) {
        PlayerEntity player = event.getPlayer();
        int l_Count = 1;
        if (Gatopera.POP.popContainer.containsKey(player.getName().getString())) {
            l_Count = Gatopera.POP.popContainer.get(player.getName().getString());
        }
        if (l_Count == 1) {
            if (player.equals(mc.player)) {
                sendMessage("§f" + I18n.t("msg.pop.you_popped_singular", "§f" + l_Count + "§r"), player.getId());
            } else {
                sendMessage("§f" + I18n.t("msg.pop.player_popped", player.getName().getString(), "§f" + l_Count + "§r"), player.getId());
            }
        } else {
            if (player.equals(mc.player)) {
                sendMessage("§f" + I18n.t("msg.pop.you_popped_singular", "§f" + l_Count + "§r"), player.getId());
            } else {
                sendMessage("§f" + I18n.t("msg.pop.player_has_popped", player.getName().getString(), "§f" + l_Count + "§r"), player.getId());
            }
        }
    }

    public void sendMessage(String message, int id) {
        if (!nullCheck()) {
            if (ClientSetting.INSTANCE.messageStyle.getValue() == ClientSetting.Style.Moon) {
                CommandManager.sendChatMessageWidthId("§f[" + "§3" + getDisplayName() + "§f] " + message, id);
                return;
            }
            CommandManager.sendChatMessageWidthId(message, id);
        }
    }
}
