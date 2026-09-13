# Gatopera Client

[English](README.md) | [Español](README_ES.md)

Cliente de Crystal PvP para Minecraft 1.20.4 Fabric.

[![Build](https://github.com/26140070-cpu/Gatopera-Client/actions/workflows/build.yml/badge.svg)](https://github.com/26140070-cpu/Gatopera-Client/actions/workflows/build.yml)
[![Licencia: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.4-green.svg)](https://www.minecraft.net/)

![Captura de pantalla](screenshot.png)

## Descargas

**[Última versión](https://github.com/26140070-cpu/Gatopera-Client/releases/latest)**

## Funciones

<details>
<summary><b>Combate</b></summary>

| Módulo | Descripción |
|--------|-------------|
| [AnchorAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AnchorAura.java) | Colocación y detonación automática de anclas de reaparición |
| [AntiCrawl](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AntiCrawl.java) | Evita entrar en estado de gateo o fuerza al jugador a levantarse |
| [AntiPiston](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AntiPiston.java) | Evita que los pistones enemigos te empujen o se coloquen cerca |
| [AntiRegear](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AntiRegear.java) | Rompe automáticamente shulkers enemigos para evitar que se reequipen |
| [AntiWeak](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AntiWeak.java) | Cambia a un arma automáticamente bajo el efecto de debilidad |
| [AutoAnchor](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoAnchor.java) | Combate automatizado con anclas de reaparición y piedra luminosa |
| [AutoBurrow](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoBurrow.java) | Se incrusta automáticamente dentro de un bloque al acercarse enemigos |
| [AutoCev](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoCev.java) | Ataques automáticos destructores de tipo CEV (Crystal End Vibration) |
| [AutoCity](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoCity.java) | Mina automáticamente la obsidiana del surround de los enemigos |
| [AutoCrystal](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoCrystal.java) | Colocación y detonación automática de cristales de End para PvP |
| [AutoCrystalBase](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoCrystalBase.java) | Asistente automático para colocar la base de obsidiana para cristales |
| [AutoEXP](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoEXP.java) | Lanza botellas de experiencia automáticamente para reparar armadura |
| [AutoHoleFill](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoHoleFill.java) | Rellena agujeros automáticamente alrededor del enemigo |
| [AutoLadder](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoLadder.java) | Coloca escaleras automáticamente cerca de los enemigos para entorpecerlos |
| [AutoPush](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoPush.java) | Empuja a los enemigos fuera de agujeros seguros usando pistones |
| [AutoTotem](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoTotem.java) | Equipa tótems de inmortalidad automáticamente en la mano |
| [AutoTrap](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoTrap.java) | Encierra a los enemigos en jaulas de obsidiana automáticamente |
| [AutoWeb](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/AutoWeb.java) | Coloca telarañas automáticamente en los pies de los enemigos |
| [BedAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/BedAura.java) | Combate automatizado con camas explosivas en Nether y End |
| [Blocker](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Blocker.java) | Coloca bloques automáticamente para mitigar ataques y explosiones |
| [Burrow](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Burrow.java) | Se incrusta dentro del bloque bajo tus pies para mayor protección |
| [Criticals](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Criticals.java) | Fuerza golpes críticos en cada ataque mediante paquetes de red |
| [HoleKick](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/HoleKick.java) | Expulsa a los enemigos de agujeros seguros utilizando pistones |
| [KillAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/KillAura.java) | Ataca automáticamente a entidades hostiles y jugadores cercanos |
| [PistonCrystal](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/PistonCrystal.java) | Combinación automatizada de pistón y cristal de End para daño masivo |
| [Quiver](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Quiver.java) | Dispara automáticamente flechas con efectos beneficiosos hacia ti mismo |
| [Reach](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Reach.java) | Aumenta la distancia de alcance para atacar e interactuar |
| [SelfFill](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/SelfFill.java) | Rellena el bloque donde estás parado con obsidiana o yunques |
| [SelfTrap](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/SelfTrap.java) | Coloca obsidiana sobre tu cabeza para defenderte contra ataques superiores |
| [SilentDouble](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/SilentDouble.java) | Rompe dos bloques de manera simultánea y silenciosa |
| [Surround](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/Surround.java) | Rodea tus pies con obsidiana para protegerte de explosiones de cristal |
| [TPAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/TPAura.java) | Teletransporta al jugador junto al objetivo mientras lo ataca |
| [WebAura](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/WebAura.java) | Rodea continuamente a los enemigos con telarañas |
| [WebCleaner](src/main/java/cc/gatopera/dev/mod/modules/impl/combat/WebCleaner.java) | Rompe y limpia automáticamente las telarañas a tu alrededor |

</details>

<details>
<summary><b>Jugador</b></summary>

| Módulo | Descripción |
|--------|-------------|
| [AutoArmor](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoArmor.java) | Equipa automáticamente la mejor armadura del inventario |
| [AutoGapple](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoGapple.java) | Consume manzanas doradas o de Notch automáticamente |
| [AutoHeal](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoHeal.java) | Usa pociones u objetos de curación automáticamente al tener poca vida |
| [AutoMine](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoMine.java) | Minado automático de bloques seleccionados y defensas |
| [AutoPearl](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoPearl.java) | Lanzamiento automático de perlas de Ender |
| [AutoPot](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoPot.java) | Lanza pociones arrojadizas beneficiosas de forma automática |
| [AutoTool](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoTool.java) | Cambia a la mejor herramienta según el bloque a romper |
| [AutoTrade](src/main/java/cc/gatopera/dev/mod/modules/impl/player/AutoTrade.java) | Automatiza las transacciones e intercambios con aldeanos |
| [Freecam](src/main/java/cc/gatopera/dev/mod/modules/impl/player/Freecam.java) | Desacopla la cámara del cuerpo permitiendo vuelo libre para explorar |
| [FreeLook](src/main/java/cc/gatopera/dev/mod/modules/impl/player/freelook/FreeLook.java) | Rotación de cámara libre en 360 grados sin cambiar la dirección del jugador |
| [InteractTweaks](src/main/java/cc/gatopera/dev/mod/modules/impl/player/InteractTweaks.java) | Ajustes y optimización en las mecánicas y retardos de interacción |
| [InventorySorter](src/main/java/cc/gatopera/dev/mod/modules/impl/player/InventorySorter.java) | Ordena y organiza automáticamente el inventario |
| [NoFall](src/main/java/cc/gatopera/dev/mod/modules/impl/player/NoFall.java) | Evita recibir daño por caída |
| [NoInteract](src/main/java/cc/gatopera/dev/mod/modules/impl/player/NoInteract.java) | Evita abrir contenedores o interactuar con bloques no deseados |
| [NoTerrainScreen](src/main/java/cc/gatopera/dev/mod/modules/impl/player/NoTerrainScreen.java) | Omite la pantalla de "Cargando terreno" para accesos más rápidos |
| [OffFirework](src/main/java/cc/gatopera/dev/mod/modules/impl/player/OffFirework.java) | Gestiona el uso de cohetes de fuegos artificiales en la mano secundaria |
| [PacketEat](src/main/java/cc/gatopera/dev/mod/modules/impl/player/PacketEat.java) | Come alimentos rápidamente utilizando paquetes de red |
| [PacketMine](src/main/java/cc/gatopera/dev/mod/modules/impl/player/PacketMine.java) | Minado silencioso y rápido de bloques mediante paquetes |
| [Replenish](src/main/java/cc/gatopera/dev/mod/modules/impl/player/Replenish.java) | Rellena automáticamente los stacks de la barra rápida desde el inventario |
| [TimerModule](src/main/java/cc/gatopera/dev/mod/modules/impl/player/TimerModule.java) | Modifica la velocidad del tiempo del juego en el cliente |
| [YawLock](src/main/java/cc/gatopera/dev/mod/modules/impl/player/YawLock.java) | Bloquea el ángulo de rotación horizontal (yaw) del jugador |

</details>

<details>
<summary><b>Movimiento</b></summary>

| Módulo | Descripción |
|--------|-------------|
| [AntiVoid](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/AntiVoid.java) | Evita caer al vacío congelando el movimiento o impulsándote |
| [AutoWalk](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/AutoWalk.java) | Camina automáticamente hacia adelante |
| [BlockStrafe](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/BlockStrafe.java) | Aumenta la aceleración lateral al rozar con bloques |
| [ElytraFly](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/ElytraFly.java) | Modos avanzados de vuelo y velocidad para élitros |
| [EntityControl](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/EntityControl.java) | Controla y monta entidades sin montura |
| [FastFall](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/FastFall.java) | Acelera la velocidad de caída vertical en el aire |
| [FastSwim](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/FastSwim.java) | Aumenta la velocidad de nado en agua y lava |
| [FastWeb](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/FastWeb.java) | Aumenta la velocidad de movimiento y caída en telarañas |
| [Flatten](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Flatten.java) | Aplana el suelo automáticamente rompiendo bloques a nivel del paso |
| [Fly](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Fly.java) | Permite volar en supervivencia con múltiples modalidades |
| [Glide](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Glide.java) | Reduce la velocidad de caída para planear en el aire |
| [HoleSnap](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/HoleSnap.java) | Desplaza y encaja rápidamente al jugador en el agujero seguro más cercano |
| [MoveFix](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/MoveFix.java) | Corrige la trayectoria de movimiento al utilizar cámaras desacopladas |
| [NoJumpDelay](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/NoJumpDelay.java) | Elimina el tiempo de espera entre saltos consecutivos |
| [NoSlow](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/NoSlow.java) | Elimina la ralentización al comer, agacharse, en almas o telarañas |
| [PacketFly](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/PacketFly.java) | Vuelo por paquetes atravesando bloques y evadiendo colisiones |
| [SafeWalk](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/SafeWalk.java) | Evita caer por los bordes de los bloques |
| [Scaffold](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Scaffold.java) | Coloca bloques automáticamente bajo tus pies para construir puentes |
| [Speed](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Speed.java) | Aumenta notablemente la velocidad de desplazamiento |
| [Sprint](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Sprint.java) | Mantiene el sprint (carrera) activo automáticamente |
| [Step](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Step.java) | Sube bloques completos automáticamente sin tener que saltar |
| [Strafe](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Strafe.java) | Control direccional completo en aire y suelo |
| [VClip](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/VClip.java) | Teletransportación vertical instantánea a través de bloques |
| [Velocity](src/main/java/cc/gatopera/dev/mod/modules/impl/movement/Velocity.java) | Modifica o anula por completo el empuje (knockback) recibido |

</details>

<details>
<summary><b>Renderizado</b></summary>

| Módulo | Descripción |
|--------|-------------|
| [Ambience](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Ambience.java) | Personalización de colores del cielo, iluminación, niebla y nubes |
| [AspectRatio](src/main/java/cc/gatopera/dev/mod/modules/impl/render/AspectRatio.java) | Modificador personalizado de la relación de aspecto |
| [BlinkDetect](src/main/java/cc/gatopera/dev/mod/modules/impl/render/BlinkDetect.java) | Detecta y resalta visualmente a jugadores que usan el exploit Blink |
| [BreakESP](src/main/java/cc/gatopera/dev/mod/modules/impl/render/BreakESP.java) | Resalta bloques que están siendo picados en tiempo real |
| [CameraClip](src/main/java/cc/gatopera/dev/mod/modules/impl/render/CameraClip.java) | Permite que la cámara en tercera persona atraviese paredes y bloques |
| [CityESP](src/main/java/cc/gatopera/dev/mod/modules/impl/render/CityESP.java) | Resalta bloques del surround enemigo vulnerables a ataques de city |
| [Crosshair](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Crosshair.java) | Mira personalizable en pantalla con estilos y colores personalizados |
| [CrystalChams](src/main/java/cc/gatopera/dev/mod/modules/impl/render/CrystalChams.java) | Renderizado visual, texturas y colores personalizados para cristales |
| [CustomFov](src/main/java/cc/gatopera/dev/mod/modules/impl/render/CustomFov.java) | Ajusta el campo de visión (FOV) personalizado |
| [ESP](src/main/java/cc/gatopera/dev/mod/modules/impl/render/ESP.java) | Resalta entidades, jugadores y objetos a través de las paredes |
| [ExplosionSpawn](src/main/java/cc/gatopera/dev/mod/modules/impl/render/ExplosionSpawn.java) | Muestra marcas visuales en el lugar donde ocurren explosiones |
| [HighLight](src/main/java/cc/gatopera/dev/mod/modules/impl/render/HighLight.java) | Resalta el bloque apuntado con recuadros y rellenos de color |
| [HoleESP](src/main/java/cc/gatopera/dev/mod/modules/impl/render/HoleESP.java) | Resalta agujeros seguros de obsidiana y bedrock para crystal PvP |
| [ItemTag](src/main/java/cc/gatopera/dev/mod/modules/impl/render/ItemTag.java) | Muestra etiquetas con nombre sobre objetos tirados en el suelo |
| [LogoutSpots](src/main/java/cc/gatopera/dev/mod/modules/impl/render/LogoutSpots.java) | Marca posiciones y coordenadas exactas donde jugadores se desconectaron |
| [NameTags](src/main/java/cc/gatopera/dev/mod/modules/impl/render/NameTags.java) | Etiquetas sobre jugadores con vida, armadura, ping y equipamiento |
| [NoRender](src/main/java/cc/gatopera/dev/mod/modules/impl/render/NoRender.java) | Desactiva renderizado de elementos molestos (fuego, niebla, etc.) |
| [PearlPredict](src/main/java/cc/gatopera/dev/mod/modules/impl/render/PearlPredict.java) | Dibuja la trayectoria prevista y punto de impacto de perlas de Ender |
| [PlaceRender](src/main/java/cc/gatopera/dev/mod/modules/impl/render/PlaceRender.java) | Efectos y animaciones visuales al colocar bloques y cristales |
| [PopChams](src/main/java/cc/gatopera/dev/mod/modules/impl/render/PopChams.java) | Muestra figuras fantasma de jugadores cuando explotan un tótem |
| [Shader](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Shader.java) | Shaders de posprocesamiento gráfico (glow, contornos, efectos) |
| [TotemParticle](src/main/java/cc/gatopera/dev/mod/modules/impl/render/TotemParticle.java) | Personaliza partículas y colores al romper un tótem |
| [Tracers](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Tracers.java) | Dibuja líneas guía desde la pantalla hacia entidades y jugadores |
| [Trajectories](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Trajectories.java) | Dibuja la parábola de vuelo proyectada para objetos arrojables |
| [ViewModel](src/main/java/cc/gatopera/dev/mod/modules/impl/render/ViewModel.java) | Personaliza la posición, escala y animación de objetos en mano |
| [XRay](src/main/java/cc/gatopera/dev/mod/modules/impl/render/XRay.java) | Vuelve invisibles bloques opacos para encontrar minerales y túneles |
| [Zoom](src/main/java/cc/gatopera/dev/mod/modules/impl/render/Zoom.java) | Ampliación de visión suave con zoom |

</details>

<details>
<summary><b>Exploits</b></summary>

| Módulo | Descripción |
|--------|-------------|
| [AntiBowBomb](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/AntiBowBomb.java) | Protección contra ataques y exploits de bow bomb de daño masivo |
| [AntiHunger](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/AntiHunger.java) | Reduce o anula el consumo de comida modificando paquetes |
| [Blink](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/Blink.java) | Retiene el envío de paquetes de movimiento para teletransportarse |
| [BowBomb](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/BowBomb.java) | Explota paquetes de velocidad para disparar flechas letales al instante |
| [ChorusExploit](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/ChorusExploit.java) | Explota paquetes de teletransporte con frutas de chorus |
| [FakePearl](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/FakePearl.java) | Simula el lanzamiento de perlas mediante paquetes |
| [HitboxDesync](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/HitboxDesync.java) | Desincroniza la hitbox de tu jugador con la posición en el servidor |
| [NewChunks](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/NewChunks.java) | Resalta chunks recién generados para rastrear jugadores y bases |
| [NoBadEffects](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/NoBadEffects.java) | Cancela los efectos negativos de pociones como ceguera y levitación |
| [PacketControl](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PacketControl.java) | Control detallado del flujo y cola de paquetes de red |
| [PearlPhase](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PearlPhase.java) | Atraviesa bloques y paredes usando perlas de Ender |
| [PearlSpoof](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PearlSpoof.java) | Suplanta paquetes de posición de perlas |
| [PingSpoof](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PingSpoof.java) | Simula una latencia de ping mayor hacia el servidor |
| [PortalGod](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/PortalGod.java) | Otorga invulnerabilidad dentro de la interfaz de portales |
| [RaytraceBypass](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/RaytraceBypass.java) | Evade las comprobaciones de visión en línea recta del servidor |
| [RocketExtend](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/RocketExtend.java) | Extiende la duración e impulso de los cohetes de fuegos artificiales |
| [ServerLagger](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/ServerLagger.java) | Envía ráfagas de paquetes para evaluar la estabilidad del servidor |
| [WallClip](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/WallClip.java) | Atraviesa colisiones de paredes y bloques sólidos |
| [XCarry](src/main/java/cc/gatopera/dev/mod/modules/impl/exploit/XCarry.java) | Mantiene objetos en la cuadrícula de crafteo sin soltarlos al cerrar |

</details>

<details>
<summary><b>Misceláneo</b></summary>

| Módulo | Descripción |
|--------|-------------|
| [AddFriend](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AddFriend.java) | Clic central sobre jugadores para añadirlos o quitarlos como amigos |
| [AntiBookBan](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AntiBookBan.java) | Previene caídas y bloqueos causados por libros con contenido malicioso |
| [AutoDupe](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoDupe.java) | Automatización de duplicación de objetos en servidores compatibles |
| [AutoEat](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoEat.java) | Come automáticamente cuando disminuye la barra de comida |
| [AutoEZ](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoEZ.java) | Envía mensajes de victoria automáticos y configurables tras una baja |
| [AutoQueue](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoQueue.java) | Automatiza reconexiones y posición en cola para servidores como 2b2t |
| [AutoReconnect](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/AutoReconnect.java) | Reconecta automáticamente al servidor tras ser desconectado |
| [ChatAppend](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/ChatAppend.java) | Agrega una firma o marca personalizada al final de mensajes en el chat |
| [ChestStealer](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/ChestStealer.java) | Saquea cofres abiertos de forma instantánea y automática |
| [Debug](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/Debug.java) | Registro de eventos y utilidades de depuración |
| [FakePlayer](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/FakePlayer.java) | Genera jugadores ficticios en el cliente para pruebas de combate |
| [LavaFiller](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/LavaFiller.java) | Coloca bloques automáticamente en lava para extinguirla |
| [NoSoundLag](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/NoSoundLag.java) | Previene lag y congelamientos por saturación de efectos de sonido |
| [Nuker](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/Nuker.java) | Rompe masiva y rápidamente bloques en un radio determinado |
| [PearlMark](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/PearlMark.java) | Marca y visualiza el punto exacto de caída de perlas de Ender |
| [PopCounter](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/PopCounter.java) | Cuenta y anuncia en el chat los tótems consumidos por enemigos |
| [Spammer](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/Spammer.java) | Envía mensajes automatizados o recurrentes al chat |
| [Tips](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/Tips.java) | Muestra consejos informativos del cliente y notificaciones en pantalla |
| [TrueAttackCooldown](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/TrueAttackCooldown.java) | Sincroniza con precisión el enfriamiento real del ataque con armas |
| [TrueDurability](src/main/java/cc/gatopera/dev/mod/modules/impl/misc/TrueDurability.java) | Muestra los valores reales de durabilidad sin compresión |

</details>

<details>
<summary><b>Cliente</b></summary>

| Módulo | Descripción |
|--------|-------------|
| [AntiCheat](src/main/java/cc/gatopera/dev/mod/modules/impl/client/AntiCheat.java) | Compatibilidad y configuraciones de evasión de anticheats |
| [BaritoneModule](src/main/java/cc/gatopera/dev/mod/modules/impl/client/BaritoneModule.java) | Integración del sistema de navegación autónoma Baritone |
| [ClickGui](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ClickGui.java) | Interfaz gráfica interactiva de configuración de módulos y ajustes |
| [ClientSetting](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ClientSetting.java) | Preferencias globales, estilo de mensajes y ajustes generales |
| [Colors](src/main/java/cc/gatopera/dev/mod/modules/impl/client/Colors.java) | Configuración de colores globales y esquemas visuales del cliente |
| [FontSetting](src/main/java/cc/gatopera/dev/mod/modules/impl/client/FontSetting.java) | Ajustes tipográficos y fuentes personalizadas |
| [HUD](src/main/java/cc/gatopera/dev/mod/modules/impl/client/HUD.java) | Interfaz superpuesta personalizable en pantalla con widgets |
| [ItemsCount](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ItemsCount.java) | Contador en pantalla de objetos vitales (tótems, cristales, etc.) |
| [ModuleList](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ModuleList.java) | Lista de módulos activos en pantalla con degradados de color |
| [ServerApply](src/main/java/cc/gatopera/dev/mod/modules/impl/client/ServerApply.java) | Aplica perfiles de configuración recomendados según el servidor |

</details>

## Requisitos del sistema

- Minecraft 1.20.4
- Fabric Loader 0.15.7+
- Java 17+

## Compilación

```bash
git clone https://github.com/26140070-cpu/Gatopera-Client.git
cd Gatopera-Client
./gradlew build
```

El archivo generado se encontrará en `build/libs/`.

## Licencia

[GNU General Public License v3](LICENSE)
