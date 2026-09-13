package cc.gatopera.dev.mod.modules.impl.misc;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.DeathEvent;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.settings.impl.EnumSetting;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import cc.gatopera.dev.mod.modules.settings.impl.StringSetting;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
import java.util.Random;

public class AutoEZ extends Module {
    public enum Type {
        Bot,
        Custom,
        AutoSex
    }
    private final EnumSetting<Type> type = add(new EnumSetting<>("Type", Type.Bot));
    private final SliderSetting range = add(new SliderSetting("Range", 10, 0, 20,.1));
    private final StringSetting message = add(new StringSetting("Message", "EZ %player%", () -> type.getValue() == Type.Custom));
    private final SliderSetting randoms = add(new SliderSetting("Random", 3, 0, 20,1));
    public AutoEZ() {
        super("AutoEZ", Category.Misc);
    }
    public List<String> sex = List.of("EZ %player%", "You got owned %player%", "Good fight %player%");
    public List<String> bot = List.of("Bot killed %player%", "L %player%", "Noob %player%");

    Random random = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    @EventHandler
    public void onDeath(DeathEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player != mc.player && !Gatopera.FRIEND.isFriend(player)) {
            if (range.getValue() > 0 && mc.player.distanceTo(player) > range.getValue()) {
                return;
            }
            String randomString = generateRandomString(randoms.getValueInt());
            if (!randomString.isEmpty()) {
                randomString = " " + randomString;
            }
            switch (type.getValue())  {
                case Bot -> mc.getNetworkHandler().sendChatMessage(bot.get(random.nextInt(Math.max(1, bot.size()))) + " " + player.getName().getString() + randomString);
                case Custom -> mc.getNetworkHandler().sendChatMessage(message.getValue().replaceAll("%player%", player.getName().getString()) + randomString);
                case AutoSex -> mc.getNetworkHandler().sendChatMessage(sex.get(random.nextInt(Math.max(1, sex.size()))) + " " + player.getName().getString() + randomString);
            }
        }
    }

    private String generateRandomString(int LENGTH) {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}