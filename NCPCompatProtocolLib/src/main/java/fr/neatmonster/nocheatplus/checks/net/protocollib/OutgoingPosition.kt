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
import org.bukkit.GameMode
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
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
import org.bukkit.Bukkit
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
import org.bukkit.Sound
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
import fr.neatmonster.nocheatplus.logging.Streams
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.Plugin

class OutgoingPosition(plugin: Plugin?) : BaseAdapter(
    plugin, ListenerPriority.HIGHEST, *arrayOf(
        PacketType.Play.Server.POSITION // TODO: POSITION_LOOK ??
    )
) {
    private val ID_OUTGOING_POSITION_UNTRACKED = counters.registerKey("packet.outgoing_position.untracked")
    private var hasTeleportId = true
    override fun onPacketSending(event: PacketEvent) {
        try {
            if (event.isPlayerTemporary) return
        } catch (e: NoSuchMethodError) {
        }
        if (event.isCancelled) {
            return
        }
        val time = System.currentTimeMillis()
        val player = event.player
        if (player == null) {
            counters.add(ProtocolLibComponent.Companion.idNullPlayer, 1)
            return
        }
        val pData = DataManager.getPlayerDataSafe(player)
        if (pData == null) {
            StaticLog.logWarning("Failed to fetch player data with " + event.packetType + " for: " + player.toString())
            return
        }
        // TODO: In future multiple checks might use this (!)
        if (pData.isCheckActive(CheckType.NET_FLYINGFREQUENCY, player)) {
            interpretPacket(
                player, event.packet, time,
                pData.getGenericInstance(NetData::class.java),
                pData.isDebugActive(CheckType.NET_FLYINGFREQUENCY)
            )
        }
    }

    private fun interpretPacket(
        player: Player, packet: PacketContainer,
        time: Long, data: NetData, debug: Boolean
    ) {
        val doubles = packet.doubles
        val floats = packet.float
        if (doubles.size() != 3 || floats.size() != 2) {
            packetMismatch(packet)
            return
        }

        // TODO: Detect/skip data with relative coordinates.
        // TODO: Concept: force KeepAlive vs. set expected coordinates in Bukkit events.
        val x = doubles.read(indexX)
        val y = doubles.read(indexY)
        val z = doubles.read(indexZ)
        val yaw = floats.read(indexYaw)
        val pitch = floats.read(indexPitch)
        var teleportId = Int.MIN_VALUE
        if (hasTeleportId) {
            try {
                val integers = packet.integers
                if (integers.size() == 1) {
                    // Accept as id.
                    teleportId = integers.read(0)
                    if (teleportId == null) {
                        teleportId = Int.MIN_VALUE
                    }
                    if (teleportId != Int.MIN_VALUE && debug) {
                        debug(player, "Outgoing confirm teleport id: $teleportId")
                    }
                } else {
                    hasTeleportId = false
                    NCPAPIProvider.getNoCheatPlusAPI().logManager.info(
                        Streams.STATUS,
                        "PacketPlayOutPosition: Teleport confirm id not available, field mismatch: " + integers.size()
                    )
                }
            } catch (t: Throwable) {
                hasTeleportId = false
                NCPAPIProvider.getNoCheatPlusAPI().logManager.info(
                    Streams.STATUS,
                    "PacketPlayOutPosition: Teleport confirm id not available."
                )
            }
        }
        val packetData = data.teleportQueue.onOutgoingTeleport(x, y, z, yaw, pitch, teleportId)
        if (packetData == null) {
            // Add counter for untracked (by Bukkit API) outgoing teleport.
            // TODO: There may be other cases which are indicated by Bukkit API events.
            counters.add(ID_OUTGOING_POSITION_UNTRACKED, 1)
            if (debug) {
                debug(player, "Untracked outgoing position: $x, $y, $z (yaw=$yaw, pitch=$pitch).")
            }
        } else {
            if (debug) {
                debug(player, "Expect ACK on outgoing position: $packetData")
            }
        }
    }

    private fun packetMismatch(packet: PacketContainer) {
        // TODO: What? Add to counters?
    }

    companion object {
        const val indexX = 0
        const val indexY = 1
        const val indexZ = 2
        const val indexYaw = 0
        const val indexPitch = 1
    }
}