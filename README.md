# Gatopera Client

[English](README.md) | [Español](README_ES.md)

A Minecraft 1.20.4 Fabric hacked client for Crystal PvP.

[![Build](https://github.com/26140070-cpu/Gatopera-Client/actions/workflows/build.yml/badge.svg)](https://github.com/26140070-cpu/Gatopera-Client/actions/workflows/build.yml)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.4-green.svg)](https://www.minecraft.net/)

![Screenshot](screenshot.png)

## Download

**[Latest Release](https://github.com/26140070-cpu/Gatopera-Client/releases/latest)**

## Features

<details>
<summary><b>Combat</b></summary>

| Module | Description |
|--------|-------------|
| [AnchorAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AnchorAura.java) | Automated respawn anchor combat placement and detonation |
| [AntiCrawl](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AntiCrawl.java) | Prevents entering crawl state or forces standing up |
| [AntiPiston](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AntiPiston.java) | Prevents pistons from pushing you or placing near you |
| [AntiRegear](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AntiRegear.java) | Automatically breaks enemy shulker boxes to prevent regearing |
| [AntiWeak](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AntiWeak.java) | Automatically switches to a weapon when affected by weakness |
| [AutoAnchor](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoAnchor.java) | Advanced respawn anchor placement and glowstone charging |
| [AutoBurrow](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoBurrow.java) | Automatically burrows into a block when enemies approach |
| [AutoCev](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoCev.java) | Crystal-End-Vibration (CEV) automated breaker attacks |
| [AutoCity](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoCity.java) | Automatically breaks opponent's surrounding obsidian blocks |
| [AutoCrystal](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoCrystal.java) | Automated End Crystal placement and breaking for crystal PvP |
| [AutoCrystalBase](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoCrystalBase.java) | Automated obsidian base block placement helper for crystals |
| [AutoEXP](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoEXP.java) | Automatically throws experience bottles to repair armor and gear |
| [AutoHoleFill](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoHoleFill.java) | Automatically fills holes around target enemies |
| [AutoLadder](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoLadder.java) | Places ladders near enemies to disrupt their movement |
| [AutoPush](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoPush.java) | Pushes enemies out of holes using pistons and redstone |
| [AutoTotem](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoTotem.java) | Automatically equips Totems of Undying in hand |
| [AutoTrap](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoTrap.java) | Traps enemies with obsidian cages |
| [AutoWeb](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoWeb.java) | Automatically places cobwebs at enemy feet |
| [BedAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/BedAura.java) | Automated bed placement and explosion combat in Nether/End |
| [Blocker](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Blocker.java) | Automatically places blocks to block incoming attacks and crystals |
| [Burrow](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Burrow.java) | Glitches and self-burrows into the block at your feet |
| [Criticals](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Criticals.java) | Deals critical hits on every attack using packet spoofing |
| [HoleKick](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/HoleKick.java) | Kicks opponents out of safe holes using piston mechanics |
| [KillAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/KillAura.java) | Automatically attacks nearby enemies and players |
| [PistonCrystal](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/PistonCrystal.java) | Automated piston push and crystal detonation setups |
| [Quiver](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Quiver.java) | Automatically shoots positive potion effect arrows at yourself |
| [Reach](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Reach.java) | Extends reach distance for attack and block interactions |
| [SelfFill](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/SelfFill.java) | Fills the block inside your player position with obsidian or anvils |
| [SelfTrap](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/SelfTrap.java) | Places obsidian overhead to protect yourself against top attacks |
| [SilentDouble](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/SilentDouble.java) | Silently breaks two blocks simultaneously |
| [Surround](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Surround.java) | Surrounds your feet with obsidian to protect against crystals |
| [TPAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/TPAura.java) | Teleports near target entities while attacking |
| [WebAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/WebAura.java) | Continuously surrounds targets with cobwebs |
| [WebCleaner](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/WebCleaner.java) | Automatically clears and breaks cobwebs around you |

</details>

<details>
<summary><b>Player</b></summary>

| Module | Description |
|--------|-------------|
| [AutoArmor](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoArmor.java) | Automatically equips the best armor pieces in your inventory |
| [AutoGapple](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoGapple.java) | Automatically eats Golden Apples or Enchanted Golden Apples |
| [AutoHeal](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoHeal.java) | Automatically heals using health potions or soup when low |
| [AutoMine](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoMine.java) | Automatically mines selected blocks |
| [AutoPearl](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoPearl.java) | Automatically throws Ender Pearls |
| [AutoPot](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoPot.java) | Automatically throws beneficial splash potions |
| [AutoTool](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoTool.java) | Automatically switches to the best tool for the targeted block |
| [AutoTrade](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoTrade.java) | Automatically trades with villagers |
| [Freecam](src/main/java/cc/gatopera/dev/mod/modules/impl/player/Freecam.java) | Detaches camera allowing free-flight exploration |
| [FreeLook](src/main/java/cc/gatopera/dev/mod/modules/impl/player/freelook/FreeLook.java) | Free 360-degree camera rotation without moving player orientation |
| [InteractTweaks](src/main/java/cc/gatopera/dev/mod/modules/impl/player/InteractTweaks.java) | Tweaks and removes interaction delays and limits |
| [InventorySorter](src/main/java/cc/gatopera/dev/mod/modules/impl/player/InventorySorter.java) | Automatically sorts and organizes inventory slots |
| [NoFall](src/main/java/cc/gatopera/dev/mod/modules/impl/player/NoFall.java) | Prevents taking fall damage |
| [NoInteract](src/main/java/cc/gatopera/dev/mod/modules/impl/player/NoInteract.java) | Prevents opening unwanted containers or block interactions |
| [NoTerrainScreen](src/main/java/cc/gatopera/dev/mod/modules/impl/player/NoTerrainScreen.java) | Disables the "Loading terrain" screen for faster loading |
| [OffFirework](src/main/java/cc/gatopera/dev/mod/modules/impl/player/OffFirework.java) | Automatically handles firework rocket usage in offhand |
| [PacketEat](src/main/java/cc/gatopera/dev/mod/modules/impl/player/PacketEat.java) | Consumes food faster using network packets |
| [PacketMine](src/main/java/cc/gatopera/dev/mod/modules/impl/player/PacketMine.java) | Fast and silent packet-based block mining |
| [Replenish](src/main/java/cc/gatopera/dev/mod/modules/impl/player/Replenish.java) | Automatically replenishes hotbar stacks from inventory |
| [TimerModule](src/main/java/cc/gatopera/dev/mod/modules/impl/player/TimerModule.java) | Modifies client-side game tick speed (Timer) |
| [YawLock](src/main/java/cc/gatopera/dev/mod/modules/impl/player/YawLock.java) | Locks camera yaw to cardinal or specified angles |

</details>

<details>
<summary><b>Movement</b></summary>

| Module | Description |
|--------|-------------|
| [AntiVoid](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/AntiVoid.java) | Prevents falling into the void |
| [AutoWalk](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/AutoWalk.java) | Automatically walks forward |
| [BlockStrafe](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/BlockStrafe.java) | Boosts strafe speed when colliding with blocks |
| [ElytraFly](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/ElytraFly.java) | Advanced flight modes and speed controls for Elytra |
| [EntityControl](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/EntityControl.java) | Ride and control entities without saddles |
| [FastFall](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/FastFall.java) | Accelerates falling speed when in the air |
| [FastSwim](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/FastSwim.java) | Increases swim speed in water and lava |
| [FastWeb](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/FastWeb.java) | Increases falling and movement speed through cobwebs |
| [Flatten](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Flatten.java) | Automatically breaks blocks obstructing level walking |
| [Fly](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Fly.java) | Enables survival flight with multiple modes |
| [Glide](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Glide.java) | Slows fall velocity to glide through the air |
| [HoleSnap](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/HoleSnap.java) | Snaps and pulls player directly into the nearest safe hole |
| [MoveFix](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/MoveFix.java) | Fixes movement angle calculations during free camera view |
| [NoJumpDelay](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/NoJumpDelay.java) | Removes the delay between consecutive jumps |
| [NoSlow](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/NoSlow.java) | Eliminates movement slowdown from eating, webs, and soulsand |
| [PacketFly](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/PacketFly.java) | Packet-based phase flight bypassing vanilla collisions |
| [SafeWalk](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/SafeWalk.java) | Prevents falling off block edges |
| [Scaffold](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Scaffold.java) | Automatically bridges and places blocks beneath your feet |
| [Speed](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Speed.java) | Boosts movement and ground speed with multiple modes |
| [Sprint](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Sprint.java) | Automatically maintains sprint constantly |
| [Step](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Step.java) | Automatically steps up blocks without jumping |
| [Strafe](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Strafe.java) | Full air and ground strafe movement control |
| [VClip](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/VClip.java) | Vertically teleports through blocks |
| [Velocity](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Velocity.java) | Modifies or completely cancels knockback and explosions |

</details>

<details>
<summary><b>Render</b></summary>

| Module | Description |
|--------|-------------|
| [Ambience](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Ambience.java) | Custom sky, world lighting, fog, and cloud color customization |
| [AspectRatio](src/main/java/cc/gatopera/dev/mod/modules/impl/render/AspectRatio.java) | Custom screen aspect ratio modifier |
| [BlinkDetect](src/main/java/cc/gatopera/dev/mod/modules/impl/render/BlinkDetect.java) | Detects and highlights players using the Blink exploit |
| [BreakESP](src/main/java/cc/gatopera/dev/mod/modules/impl/render/BreakESP.java) | Renders overlays on blocks currently being broken |
| [CameraClip](src/main/java/cc/gatopera/dev/mod/modules/impl/render/CameraClip.java) | Allows third-person camera to clip through blocks and walls |
| [CityESP](src/main/java/cc/gatopera/dev/mod/modules/impl/render/CityESP.java) | Highlights surrounding enemy blocks vulnerable to city attacks |
| [Crosshair](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Crosshair.java) | Customizable crosshair render with custom colors and shapes |
| [CrystalChams](src/main/java/cc/gatopera/dev/mod/modules/impl/render/CrystalChams.java) | Custom textures, colors, and shaders for End Crystals |
| [CustomFov](src/main/java/cc/gatopera/dev/mod/modules/impl/render/CustomFov.java) | Custom field of view adjustments |
| [ESP](src/main/java/cc/gatopera/dev/mod/modules/impl/render/ESP.java) | Entity highlighting and wall-penetrating ESP boxes |
| [ExplosionSpawn](src/main/java/cc/gatopera/dev/mod/modules/impl/render/ExplosionSpawn.java) | Renders visual markers at explosion locations |
| [HighLight](src/main/java/cc/gatopera/dev/mod/modules/impl/render/HighLight.java) | Custom targeted block highlight bounding box and fill |
| [HoleESP](src/main/java/cc/gatopera/dev/mod/modules/impl/render/HoleESP.java) | Highlights safe obsidian and bedrock holes for PvP |
| [ItemTag](src/main/java/cc/gatopera/dev/mod/modules/impl/render/ItemTag.java) | Renders descriptive tags over items on the ground |
| [LogoutSpots](src/main/java/cc/gatopera/dev/mod/modules/impl/render/LogoutSpots.java) | Marks positions and coordinates where players disconnected |
| [NameTags](src/main/java/cc/gatopera/dev/mod/modules/impl/render/NameTags.java) | Enhanced informational nameplates above players |
| [NoRender](src/main/java/cc/gatopera/dev/mod/modules/impl/render/NoRender.java) | Disables unwanted visual effects like fire, fog, hurt cam, etc. |
| [PearlPredict](src/main/java/cc/gatopera/dev/mod/modules/impl/render/PearlPredict.java) | Draws projected trajectory and landing spot for Ender Pearls |
| [PlaceRender](src/main/java/cc/gatopera/dev/mod/modules/impl/render/PlaceRender.java) | Visual animations and highlights when placing blocks and crystals |
| [PopChams](src/main/java/cc/gatopera/dev/mod/modules/impl/render/PopChams.java) | Renders ghost player chams when a totem pops |
| [Shader](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Shader.java) | Post-processing glow, outline, and shader effects |
| [TotemParticle](src/main/java/cc/gatopera/dev/mod/modules/impl/render/TotemParticle.java) | Customizes totem pop particle colors and effects |
| [Tracers](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Tracers.java) | Draws tracer lines from screen to nearby entities |
| [Trajectories](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Trajectories.java) | Draws trajectory path of thrown projectiles |
| [ViewModel](src/main/java/cc/gatopera/dev/mod/modules/impl/render/ViewModel.java) | Customizes hand/item held positions, rotations, and animations |
| [XRay](src/main/java/cc/gatopera/dev/mod/modules/impl/render/XRay.java) | Makes specific blocks invisible to see ores and caves |
| [Zoom](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Zoom.java) | Smooth camera zoom feature |

</details>

<details>
<summary><b>Exploit</b></summary>

| Module | Description |
|--------|-------------|
| [AntiBowBomb](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/AntiBowBomb.java) | Protects against bow bomb instant-kill projectile exploits |
| [AntiHunger](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/AntiHunger.java) | Reduces hunger exhaustion by modifying movement packets |
| [Blink](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/Blink.java) | Suspends outgoing movement packets for sudden teleportation |
| [BowBomb](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/BowBomb.java) | Exploits bow velocity packets to shoot ultra high damage arrows |
| [ChorusExploit](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/ChorusExploit.java) | Exploits Chorus Fruit teleportation packets |
| [FakePearl](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/FakePearl.java) | Spoofs pearl throw packets |
| [HitboxDesync](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/HitboxDesync.java) | Desynchronizes player hitbox from server-side position |
| [NewChunks](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/NewChunks.java) | Highlights newly generated chunks to find player bases |
| [NoBadEffects](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/NoBadEffects.java) | Cancels negative potion effects like levitation and blindness |
| [PacketControl](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PacketControl.java) | Fine-grained packet queue and network management |
| [PearlPhase](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PearlPhase.java) | Phases through blocks using Ender Pearls |
| [PearlSpoof](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PearlSpoof.java) | Spoofs pearl position packets |
| [PingSpoof](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PingSpoof.java) | Simulates high ping latency to the server |
| [PortalGod](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PortalGod.java) | Grants invulnerability while inside portal screens |
| [RaytraceBypass](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/RaytraceBypass.java) | Bypasses server line-of-sight raytrace checks |
| [RocketExtend](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/RocketExtend.java) | Extends duration and boost of firework rockets |
| [ServerLagger](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/ServerLagger.java) | Tests server stability by sending intensive packet payloads |
| [WallClip](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/WallClip.java) | Clips through solid walls and blocks |
| [XCarry](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/XCarry.java) | Stores items in crafting inventory grid without dropping them |

</details>

<details>
<summary><b>Misc</b></summary>

| Module | Description |
|--------|-------------|
| [AddFriend](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AddFriend.java) | Middle-click players to quickly add or remove them as friends |
| [AntiBookBan](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AntiBookBan.java) | Prevents kicks and crashes caused by malicious book packets |
| [AutoDupe](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoDupe.java) | Automated item duplication exploits for supported servers |
| [AutoEat](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoEat.java) | Automatically eats food when hunger bar is depleted |
| [AutoEZ](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoEZ.java) | Sends automated customizable victory messages upon killing opponents |
| [AutoQueue](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoQueue.java) | Automates queue positioning and reconnects for 2b2t servers |
| [AutoReconnect](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoReconnect.java) | Automatically reconnects after being disconnected from server |
| [ChatAppend](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/ChatAppend.java) | Appends a custom client watermark suffix to chat messages |
| [ChestStealer](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/ChestStealer.java) | Automatically loots items from opened chests quickly |
| [Debug](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/Debug.java) | Developer debug logs and event analysis |
| [FakePlayer](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/FakePlayer.java) | Spawns client-side fake players for combat testing |
| [LavaFiller](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/LavaFiller.java) | Automatically places blocks in lava sources to clear them |
| [NoSoundLag](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/NoSoundLag.java) | Prevents audio lag and crashes from sound spam |
| [Nuker](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/Nuker.java) | Rapidly breaks surrounding blocks in a radius |
| [PearlMark](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/PearlMark.java) | Visualizes and marks landing spots for thrown pearls |
| [PopCounter](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/PopCounter.java) | Tracks and announces enemy totem pops in chat |
| [Spammer](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/Spammer.java) | Sends automated repetitive or custom messages to chat |
| [Tips](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/Tips.java) | Displays on-screen client tips and status notifications |
| [TrueAttackCooldown](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/TrueAttackCooldown.java) | Accurately synchronizes true weapon attack cooldown |
| [TrueDurability](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/TrueDurability.java) | Displays actual uncompressed durability on tools and armor |

</details>

<details>
<summary><b>Client</b></summary>

| Module | Description |
|--------|-------------|
| [AntiCheat](src/main/java/cc/gatopera/dev/mod/modules/impl/client/AntiCheat.java) | Anticheat protocol compatibility and bypass options |
| [BaritoneModule](src/main/java/cc/gatopera/dev/mod/modules/impl/client/BaritoneModule.java) | Baritone automated pathfinding integration |
| [ClickGui](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ClickGui.java) | In-game visual module and setting configuration GUI |
| [ClientSetting](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ClientSetting.java) | Global client configuration, toggles, and notification settings |
| [Colors](src/main/java/cc/gatopera/dev/mod/modules/impl/client/Colors.java) | Global color scheme and accent color customization |
| [FontSetting](src/main/java/cc/gatopera/dev/mod/modules/impl/client/FontSetting.java) | Custom font rendering and typography options |
| [HUD](src/main/java/cc/gatopera/dev/mod/modules/impl/client/HUD.java) | On-screen heads-up display overlay with customizable widgets |
| [ItemsCount](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ItemsCount.java) | On-screen counter for vital items (totems, crystals, xp) |
| [ModuleList](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ModuleList.java) | On-screen arraylist of active modules with color gradients |
| [ServerApply](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ServerApply.java) | Applies automatic configuration presets for specific servers |

</details>

## Requirements

- Minecraft 1.20.4
- Fabric Loader 0.15.7+
- Java 17+

## Build

```bash
git clone https://github.com/26140070-cpu/Gatopera-Client.git
cd Gatopera-Client
./gradlew build
```

The built jar will be in `build/libs/`.

## License

[GNU General Public License v3](LICENSE)
