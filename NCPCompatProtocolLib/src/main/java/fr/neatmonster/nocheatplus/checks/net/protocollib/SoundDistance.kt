/*
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.neatmonster.nocheatplus.checks.net.protocollib

import fr.neatmonster.nocheatplus.utilities.ds.count.ActionFrequency.Companion.subtract
import fr.neatmonster.nocheatplus.logging.details.ILogString.info
import fr.neatmonster.nocheatplus.time.monotonic.Monotonic.synchMillis
import fr.neatmonster.nocheatplus.utilities.StringUtil.join
import fr.neatmonster.nocheatplus.logging.details.ILogString.warning
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getClass
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getMethodNoArgs
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.invokeMethodNoArgs
import fr.neatmonster.nocheatplus.checks.net.protocollib.BaseAdapter
import com.comphenix.protocol.events.ListenerPriority
import fr.neatmonster.nocheatplus.checks.net.protocollib.Fight
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.PacketType
import org.bukkit.entity.Player
import fr.neatmonster.nocheatplus.checks.net.protocollib.ProtocolLibComponent
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.reflect.StructureModifier
import fr.neatmonster.nocheatplus.players.IPlayerData
import fr.neatmonster.nocheatplus.players.DataManager
import fr.neatmonster.nocheatplus.checks.moving.MovingData
import java.util.LinkedList
import java.util.Arrays
import fr.neatmonster.nocheatplus.checks.net.protocollib.NoSlow
import fr.neatmonster.nocheatplus.components.NoCheatPlusAPI
import fr.neatmonster.nocheatplus.NCPAPIProvider
import fr.neatmonster.nocheatplus.event.mini.MiniListener
import fr.neatmonster.nocheatplus.logging.StaticLog
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.EventPriority
import fr.neatmonster.nocheatplus.components.registry.order.RegistrationOrder.RegisterMethodWithOrder
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import fr.neatmonster.nocheatplus.compat.Bridge1_9
import fr.neatmonster.nocheatplus.utilities.InventoryUtil
import org.bukkit.inventory.EquipmentSlot
import fr.neatmonster.nocheatplus.compat.Bridge1_13
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.inventory.PlayerInventory
import com.comphenix.protocol.events.PacketAdapter
import fr.neatmonster.nocheatplus.components.debug.IDebugPlayer
import com.comphenix.protocol.events.PacketAdapter.AdapterParameteters
import com.comphenix.protocol.events.ListenerOptions
import fr.neatmonster.nocheatplus.utilities.CheckUtils
import fr.neatmonster.nocheatplus.checks.net.protocollib.MovingFlying
import fr.neatmonster.nocheatplus.checks.net.FlyingFrequency
import fr.neatmonster.nocheatplus.checks.net.Moving
import java.util.HashSet
import fr.neatmonster.nocheatplus.checks.net.model.DataPacketFlying.PACKET_CONTENT
import java.util.LinkedHashSet
import com.comphenix.protocol.PacketType.Protocol
import com.comphenix.protocol.PacketType.Sender
import java.lang.NoSuchMethodError
import fr.neatmonster.nocheatplus.checks.net.NetData
import fr.neatmonster.nocheatplus.compat.AlmostBoolean
import fr.neatmonster.nocheatplus.utilities.ds.count.ActionFrequency
import fr.neatmonster.nocheatplus.worlds.IWorldData
import fr.neatmonster.nocheatplus.checks.net.NetConfig
import fr.neatmonster.nocheatplus.checks.net.model.DataPacketFlying
import fr.neatmonster.nocheatplus.checks.net.model.TeleportQueue.AckResolution
import fr.neatmonster.nocheatplus.checks.net.model.TeleportQueue.AckReference
import fr.neatmonster.nocheatplus.utilities.location.LocUtil
import java.lang.IllegalStateException
import java.lang.StringBuilder
import fr.neatmonster.nocheatplus.compat.versions.ServerVersion
import fr.neatmonster.nocheatplus.checks.net.protocollib.SoundDistance
import java.util.Locale
import fr.neatmonster.nocheatplus.utilities.location.TrigUtil
import fr.neatmonster.nocheatplus.checks.net.protocollib.CatchAllAdapter
import fr.neatmonster.nocheatplus.checks.net.PacketFrequency
import fr.neatmonster.nocheatplus.checks.net.KeepAliveFrequency
import fr.neatmonster.nocheatplus.checks.net.protocollib.OutgoingPosition
import fr.neatmonster.nocheatplus.checks.net.model.CountableLocation
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil
import java.lang.RuntimeException
import fr.neatmonster.nocheatplus.checks.net.AttackFrequency
import fr.neatmonster.nocheatplus.checks.net.protocollib.UseEntityAdapter.LegacyReflectionSet
import fr.neatmonster.nocheatplus.checks.net.protocollib.UseEntityAdapter
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction
import java.lang.NullPointerException
import fr.neatmonster.nocheatplus.checks.net.WrongTurn
import fr.neatmonster.nocheatplus.config.ConfigManager
import fr.neatmonster.nocheatplus.config.ConfPaths
import fr.neatmonster.nocheatplus.components.registry.feature.IDisableListener
import fr.neatmonster.nocheatplus.components.registry.feature.INotifyReload
import fr.neatmonster.nocheatplus.components.registry.feature.JoinLeaveListener
import fr.neatmonster.nocheatplus.worlds.IWorldDataManager
import com.comphenix.protocol.ProtocolLibrary
import java.lang.ClassNotFoundException
import java.lang.ClassCastException
import com.comphenix.protocol.ProtocolManager
import fr.neatmonster.nocheatplus.checks.CheckType
import org.bukkit.*
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.Plugin
import java.util.ArrayList

class SoundDistance(plugin: Plugin?) :
    BaseAdapter(plugin, ListenerPriority.LOW, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
    private val idSoundEffectCancel = counters.registerKey("packet.sound.cancel")
    private val useLoc = Location(null, 0, 0, 0)

    /** Legacy check behavior.  */
    private val pre1_9: Boolean

    init {
        checkType = CheckType.NET_SOUNDDISTANCE
        pre1_9 = ServerVersion.compareMinecraftVersion("1.9") < 0
        inflateEffectNames()
    }

    /**
     * Ensure both lower and upper case are contained.
     */
    private fun inflateEffectNames() {
        val names: List<String> = ArrayList(effectNames)
        for (name in names) {
            effectNames.add(name.lowercase(Locale.getDefault()))
            effectNames.add(name.uppercase(Locale.getDefault()))
        }
    }

    private fun isSoundMonitoredPre1_9(packetContainer: PacketContainer): Boolean {
        //debug(null, packetContainer.getStrings().read(0));
        return effectNames.contains(packetContainer.strings.read(0))
    }

    private fun isSoundMonitoredLatest(packetContainer: PacketContainer): Boolean {
        val sounds = packetContainer.soundEffects
        for (sound in sounds.values) {
            if (sound != null && effectNames.contains(sound.name)) {
                //debug(null, "MONITOR SOUND: " + sound);
                return true
            }
        }
        return false
    }

    private fun isSoundMonitored(packetContainer: PacketContainer): Boolean {
        return if (pre1_9) {
            isSoundMonitoredPre1_9(packetContainer)
        } else {
            isSoundMonitoredLatest(packetContainer)
        }
    }

    private fun handleSoundPacket(event: PacketEvent) {
        try {
            if (event.isPlayerTemporary) return
        } catch (e: NoSuchMethodError) {
            if (event.player == null) return
            if (DataManager.getPlayerDataSafe(event.player) == null) return
        }
        val packetContainer = event.packet

        // Compare sound effect name.
        if (!isSoundMonitored(packetContainer)) {
            return
        }
        val player = event.player
        val pData = DataManager.getPlayerData(player)
        if (!pData.isCheckActive(CheckType.NET_SOUNDDISTANCE, player)) {
            return
        }

        // Compare distance of player to the weather location.
        val loc = player.getLocation(useLoc)
        val ints = packetContainer.integers
        val dSq = TrigUtil.distanceSquared((ints.read(0) / 8).toDouble(), (ints.read(2) / 8).toDouble(), loc!!.x, loc.z)
        //        if (data.debug) {
        //            debug(player, "SoundDistance(" + soundName + "): " + StringUtil.fdec1.format(Math.sqrt(dSq)));
        //        }
        val cc = pData.getGenericInstance(NetConfig::class.java)
        if (dSq > cc.soundDistanceSq) {
            event.isCancelled = true
            counters.add(idSoundEffectCancel, 1)
        }
        useLoc.world = null
    }

    override fun onPacketSending(event: PacketEvent) {
        try {
            handleSoundPacket(event)
        } catch (t: Throwable) {
            t.printStackTrace()
            ProtocolLibComponent.Companion.unregister(this)
        }
    }

    companion object {
        // TODO: Will not be effective with 512 radius, if they add the patch by @Amranth.
        // TODO: For lower distances more packets might need to be intercepted.
        /** Partly by debugging, partly from various sources, possibly including wrong spelling.  */
        private val effectNames: MutableSet<String> = HashSet(
            Arrays.asList( ////////////
                // PRE 1.9
                ////////////
                // Weather
                "ambient.weather.thunder",  // Wither
                "wither-spawn-sound-radius",
                "mob.wither.spawn",
                "mob.wither.shoot",
                "mob.wither.idle",
                "mob.wither.hurt",
                "mob.wither.death",  // Enderdragon
                "dragon-death-sound-radius",
                "mob.enderdragon.wings",
                "mob.enderdragon.grow",
                "mob.enderdragon.growl",
                "mob.enderdragon.hit",
                "mob.enderdragon.end",
                "game.neutral.die",  // Enderdragon 1.8.7 (debug).
                //////////////////
                // 1.9 AND LATER
                //////////////////
                // Weather
                "ENTITY_LIGHTNING_IMPACT",
                "ENTITY_LIGHTNING_THUNDER",  // Enderdragon
                "ENTITY_ENDERDRAGON_AMBIENT",
                "ENTITY_ENDERDRAGON_DEATH",
                "ENTITY_ENDERDRAGON_FIREBALL_EXPLODE",
                "ENTITY_ENDERDRAGON_FLAP",
                "ENTITY_ENDERDRAGON_GROWL",
                "ENTITY_ENDERDRAGON_HURT",
                "ENTITY_ENDERDRAGON_SHOOT",  // Wither
                "ENTITY_WITHER_AMBIENT",
                "ENTITY_WITHER_BREAK_BLOCK",
                "ENTITY_WITHER_DEATH",
                "ENTITY_WITHER_HURT",
                "ENTITY_WITHER_SHOOT",
                "ENTITY_WITHER_SPAWN"
            )
        )
    }
}