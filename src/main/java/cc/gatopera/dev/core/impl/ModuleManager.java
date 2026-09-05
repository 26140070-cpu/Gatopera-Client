package cc.gatopera.dev.core.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.i18n.I18n;
import cc.gatopera.dev.api.events.impl.Render3DEvent;
import cc.gatopera.dev.api.utils.Wrapper;
import cc.gatopera.dev.mod.Mod;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.client.*;
import cc.gatopera.dev.mod.modules.impl.combat.*;
import cc.gatopera.dev.mod.modules.impl.exploit.*;
import cc.gatopera.dev.mod.modules.impl.misc.*;
import cc.gatopera.dev.mod.modules.impl.movement.*;
import cc.gatopera.dev.mod.modules.impl.player.*;
import cc.gatopera.dev.mod.modules.impl.player.freelook.FreeLook;
import cc.gatopera.dev.mod.modules.impl.render.*;
import cc.gatopera.dev.mod.modules.settings.Setting;
import cc.gatopera.dev.mod.modules.settings.impl.BindSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleManager implements Wrapper {
    public final ArrayList<Module> modules = new ArrayList<>();
    public static Mod lastLoadMod;

    public ModuleManager() {
        addModule(new VClip());
        addModule(new Glide());
        addModule(new AutoDupe());
        addModule(new FontSetting());
        addModule(new NoTerrainScreen());
        addModule(new AutoCrystal());
        addModule(new Ambience());
        addModule(new AntiHunger());
        addModule(new AntiVoid());
        addModule(new AutoWalk());
        addModule(new AutoQueue());
        addModule(new AntiWeak());
        addModule(new AutoTrade());
        addModule(new AddFriend());
        addModule(new AspectRatio());
        addModule(new NewChunks());
        addModule(new KillAura());
        addModule(new AutoAnchor());
        addModule(new AutoArmor());
        addModule(new AutoCity());
        addModule(new AutoEat());
        addModule(new AutoEZ());
        addModule(new SelfTrap());
        addModule(new InventorySorter());
        addModule(new OffFirework());
        addModule(new AutoEXP());
        addModule(new AutoHeal());
        addModule(new AutoGapple());
        addModule(new AutoPot());
        addModule(new AutoPush());
        addModule(new AutoTotem());
        addModule(new Nuker());
        addModule(new AutoTrap());
        addModule(new AutoWeb());
        addModule(new AutoLadder());
        addModule(new AnchorAura());
        addModule(new AntiPiston());
        addModule(new AutoBurrow());
        addModule(new HoleKick());
        addModule(new Quiver());
        addModule(new Reach());
        addModule(new SelfFill());
        addModule(new TPAura());
        addModule(new WebAura());
        addModule(new WebCleaner());
        addModule(new SilentDouble());
        addModule(new BedAura());
        addModule(new Blink());
        addModule(new ChorusExploit());
        addModule(new PortalGod());
        addModule(new HitboxDesync());
        addModule(new BlockStrafe());
        addModule(new FastSwim());
        addModule(new Blocker());
        addModule(new BowBomb());
        addModule(new BreakESP());
        addModule(new TrueAttackCooldown());
        addModule(new Burrow());
        addModule(new CameraClip());
        addModule(new ChatAppend());
        addModule(new BaritoneModule());
        addModule(new Colors());
        addModule(new ChestStealer());
        addModule(new LavaFiller());
        addModule(new CityESP());
        addModule(new ClickGui());
        addModule(new WallClip());
        addModule(new AntiCheat());
        addModule(new ItemsCount());
        addModule(new CustomFov());
        addModule(new Criticals());
        addModule(new AutoCev());
        addModule(new Crosshair());
        addModule(new CrystalChams());
        addModule(new ItemTag());
        addModule(new AntiBookBan());
        addModule(new AutoReconnect());
        addModule(new ExplosionSpawn());
        addModule(new ESP());
        addModule(new HoleESP());
        addModule(new Tracers());
        addModule(new ElytraFly());
        addModule(new BlinkDetect());
        addModule(new EntityControl());
        addModule(new PearlSpoof());
        addModule(new FakePearl());
        addModule(new PearlMark());
        addModule(new PingSpoof());
        addModule(new FakePlayer());
        addModule(new Spammer());
        addModule(new HighLight());
        addModule(new FastFall());
        addModule(new FastWeb());
        addModule(new Flatten());
        addModule(new Fly());
        addModule(new YawLock());
        addModule(new Freecam());
        addModule(new FreeLook());
        addModule(new TimerModule());
        addModule(new Tips());
        addModule(new ClientSetting());
        addModule(new HUD());
        addModule(new RocketExtend());
        addModule(new AutoHoleFill());
        addModule(new HoleSnap());
        addModule(new PearlPredict());
        addModule(new LogoutSpots());
        addModule(new AutoTool());
        addModule(new Trajectories());
        addModule(new AutoPearl());
        addModule(new ModuleList());
        addModule(new NameTags());
        addModule(new NoBadEffects());
        addModule(new NoFall());
        addModule(new NoRender());
        addModule(new NoSlow());
        addModule(new NoSoundLag());
        addModule(new MoveFix());
        addModule(new PacketControl());
        addModule(new XRay());
        addModule(new AutoCrystalBase());

        addModule(new PacketEat());
        addModule(new PacketFly());
        addModule(new PacketMine());
        addModule(new PearlPhase());
        addModule(new PistonCrystal());
        addModule(new PlaceRender());
        addModule(new InteractTweaks());
        addModule(new PopChams());
        addModule(new PopCounter());
        addModule(new TrueDurability());
        addModule(new Replenish());
        addModule(new ServerApply());
        addModule(new ServerLagger());
        addModule(new Scaffold());
        addModule(new Shader());
        addModule(new AntiCrawl());
        addModule(new AntiRegear());
        addModule(new AntiBowBomb());
        addModule(new SafeWalk());
        addModule(new NoJumpDelay());
        addModule(new NoInteract());
        addModule(new Speed());
        addModule(new Sprint());
        addModule(new Strafe());
        addModule(new Step());
        addModule(new Surround());
        addModule(new TotemParticle());
        addModule(new Velocity());
        addModule(new ViewModel());
        addModule(new XCarry());
        addModule(new Zoom());
        modules.sort(Comparator.comparing(Mod::getName));
    }

    public boolean setBind(int eventKey) {
        if (eventKey == -1 || eventKey == 0) {
            return false;
        }
        AtomicBoolean set = new AtomicBoolean(false);
        modules.forEach(module -> {
            for (Setting setting : module.getSettings()) {
                if (setting instanceof BindSetting bind) {
                    if (bind.isListening()) {
                        bind.setKey(eventKey);
                        bind.setListening(false);
                        if (bind.getBind().equals("DELETE")) {
                            bind.setKey(-1);
                        }
                        set.set(true);
                    }
                }
            }
        });
        return set.get();
    }

    public void onKeyReleased(int eventKey) {
        if (eventKey == -1 || eventKey == 0) {
            return;
        }
        modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey && module.getBind().isHoldEnable() && module.getBind().hold) {
                module.toggle();
                module.getBind().hold = false;
            }
            module.getSettings().stream()
                    .filter(setting -> setting instanceof BindSetting)
                    .map(setting -> (BindSetting) setting)
                    .filter(bindSetting -> bindSetting.getKey() == eventKey)
                    .forEach(bindSetting -> bindSetting.setPressed(false));
        });
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == -1 || eventKey == 0 || mc.currentScreen instanceof ClickGuiScreen) {
            return;
        }
        modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey && mc.currentScreen == null) {
                module.toggle();
                module.getBind().hold = true;
            }

            module.getSettings().stream()
                    .filter(setting -> setting instanceof BindSetting)
                    .map(setting -> (BindSetting) setting)
                    .filter(bindSetting -> bindSetting.getKey() == eventKey)
                    .forEach(bindSetting -> bindSetting.setPressed(true));
        });
    }

    public void onThread() {
        modules.stream().filter(Module::isOn).forEach(module -> {
            try {
                module.onThread();
            } catch (Exception e) {
                e.printStackTrace();
                if (ClientSetting.INSTANCE.debug.getValue())
                    CommandManager.sendChatMessage("§4" + I18n.t("msg.error_occurred", module.getDisplayName(), String.valueOf(e.getMessage())));
            }
        });
    }

    public void onUpdate() {
        if (Module.nullCheck()) return;
        modules.stream().filter(Module::isOn).forEach(module -> {
            try {
                module.onUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                if (ClientSetting.INSTANCE.debug.getValue())
                    CommandManager.sendChatMessage("§4" + I18n.t("msg.error_occurred", module.getDisplayName(), String.valueOf(e.getMessage())));
            }
        });
    }

    public void onLogin() {
        modules.stream().filter(Module::isOn).forEach(Module::onLogin);
    }

    public void onLogout() {
        modules.stream().filter(Module::isOn).forEach(Module::onLogout);
    }

    public void render2D(DrawContext drawContext) {
        ModuleList.INSTANCE.counter = 20;
        modules.stream().filter(Module::isOn).forEach(module -> module.onRender2D(drawContext, MinecraftClient.getInstance().getTickDelta()));
    }

    public void render3D(MatrixStack matrixStack) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_CULL_FACE);
        RenderSystem.disableDepthTest();
        matrixStack.push();
        modules.stream().filter(Module::isOn).forEach(module -> module.onRender3D(matrixStack));
        Gatopera.EVENT_BUS.post(new Render3DEvent(matrixStack, mc.getTickDelta()));
        matrixStack.pop();
        RenderSystem.enableDepthTest();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public void addModule(Module module) {
        module.add(module.getBind());
        modules.add(module);

    }

    public void disableAll() {
        for (Module module : modules) {
            module.disable();
        }
    }

    public Module getModuleByName(String string) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(string)) {
                return module;
            }
        }
        return null;
    }
}
