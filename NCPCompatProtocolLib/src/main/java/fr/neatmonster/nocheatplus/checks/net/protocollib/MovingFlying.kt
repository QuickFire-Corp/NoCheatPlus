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

/**
 * Run checks related to moving (pos/look/flying). Skip packets that shouldn't
 * get processed anyway due to a teleport. Also update lastKeepAliveTime.
 *
 * @author dev1mc
 */
class MovingFlying(plugin: Plugin?) : BaseAdapter(plugin, ListenerPriority.LOW, *initPacketTypes()) {
    private val plugin = Bukkit.getPluginManager().getPlugin("NoCheatPlus")

    /** Frequency check for flying packets.  */
    private val flyingFrequency = FlyingFrequency()

    /** Other checks related to packet content.  */
    private val moving = Moving()
    private val idFlying = counters.registerKey("packet.flying")
    private val idAsyncFlying = counters.registerKey("packet.flying.asynchronous")

    /** If a packet can't be parsed, this time stamp is set for occasional logging.  */
    private var packetMismatch = Long.MIN_VALUE
    private val packetMismatchLogFrequency: Long = 60000 // Every minute max, good for updating :).
    private val validContent: HashSet<PACKET_CONTENT?> = LinkedHashSet()
    private val confirmTeleportType: PacketType? =
        ProtocolLibComponent.Companion.findPacketTypeByName(Protocol.PLAY, Sender.CLIENT, "TeleportAccept")
    private var acceptConfirmTeleportPackets = confirmTeleportType != null

    init {
        // PacketPlayInFlying[3, legacy: 10]
        // Keep the CheckType NET for now.
        // Add feature tags for checks.
        if (NCPAPIProvider.getNoCheatPlusAPI().worldDataManager.isActiveAnywhere(CheckType.NET_FLYINGFREQUENCY)) {
            NCPAPIProvider.getNoCheatPlusAPI()
                .addFeatureTags("checks", Arrays.asList(FlyingFrequency::class.java.simpleName))
        }
        NCPAPIProvider.getNoCheatPlusAPI().addComponent(flyingFrequency)
    }

    override fun onPacketReceiving(event: PacketEvent) {
        try {
            if (event.isPlayerTemporary) return
        } catch (e: NoSuchMethodError) {
            if (event.player == null) return
            if (DataManager.getPlayerDataSafe(event.player) == null) return
        }
        if (event.packetType == confirmTeleportType) {
            if (acceptConfirmTeleportPackets) {
                onConfirmTeleportPacket(event)
            }
        } else onFlyingPacket(event)
    }

    private fun onConfirmTeleportPacket(event: PacketEvent) {
        try {
            processConfirmTeleport(event)
        } catch (t: Throwable) {
            noConfirmTeleportPacket()
        }
    }

    private fun processConfirmTeleport(event: PacketEvent) {
        val packet = event.packet
        val integers = packet.integers
        if (integers.size() != 1) {
            noConfirmTeleportPacket()
            return
        }
        // TODO: Cross check legacy types (if they even had an integer).
        val teleportId = integers.read(0)
            ?: // TODO: Not sure ...
            return
        val player = event.player
        val pData = DataManager.getPlayerDataSafe(player)
        val data = pData.getGenericInstance(NetData::class.java)
        val matched = data.teleportQueue.processAck(teleportId)
        if (matched.decideOptimistically()) {
            subtract(System.currentTimeMillis(), 1f, data.flyingFrequencyAll)
        }
        if (pData.isDebugActive(checkType)) {
            debug(
                player,
                "Confirm teleport packet" + (if (matched.decideOptimistically()) " (matched=$matched)" else "") + ": " + teleportId
            )
        }
    }

    private fun noConfirmTeleportPacket() {
        acceptConfirmTeleportPackets = false
        // TODO: Attempt to unregister.
        NCPAPIProvider.getNoCheatPlusAPI().logManager.info(Streams.STATUS, "Confirm teleport packet not available.")
    }

    private fun onFlyingPacket(event: PacketEvent) {
        val primaryThread = Bukkit.isPrimaryThread()
        counters.add(idFlying, 1, primaryThread)
        if (event.isAsync == primaryThread) {
            counters.add(ProtocolLibComponent.Companion.idInconsistentIsAsync, 1, primaryThread)
        }
        if (!primaryThread) {
            // Count all asynchronous events extra.
            counters.addSynchronized(idAsyncFlying, 1)
            // TODO: Detect game phase for the player?
        }
        val time = System.currentTimeMillis()
        val player = event.player
        if (player == null) {
            // TODO: Need config?
            counters.add(ProtocolLibComponent.Companion.idNullPlayer, 1, primaryThread)
            event.isCancelled = true
            return
        }
        val pData = DataManager.getPlayerDataSafe(player)
        // Always update last received time.
        val data = pData.getGenericInstance(NetData::class.java)
        data.lastKeepAliveTime = time // Update without much of a contract.
        // TODO: Leniency options too (packet order inversion). -> current: flyingQueue is fetched.
        val worldData = pData.currentWorldDataSafe
        if (!worldData.isCheckActive(CheckType.NET_FLYINGFREQUENCY)) {
            return
        }
        val cc = pData.getGenericInstance(NetConfig::class.java)
        var cancel = false
        // Interpret the packet content.
        val packetData = interpretPacket(event, time)

        // Early return tests, if the packet can be interpreted.
        var skipFlyingFrequency = false
        if (packetData != null) {
            // Prevent processing packets with obviously malicious content.
            if (isInvalidContent(packetData)) {
                // TODO: extra actions: log and kick (cancel state is not evaluated)
                event.isCancelled = true
                if (pData.isDebugActive(checkType)) {
                    debug(player, "Incoming packet, cancel due to malicious content: $packetData")
                }
                return
            }
            when (data.teleportQueue.processAck(packetData)) {
                AckResolution.WAITING -> {
                    if (pData.isDebugActive(checkType)) {
                        debug(player, "Incoming packet, still waiting for ACK on outgoing position.")
                    }
                    if (confirmTeleportType != null && cc.supersededFlyingCancelWaiting) {
                        // Don't add to the flying queue for now (assumed invalid).
                        val ackReference = data.teleportQueue.lastAckReference
                        if (ackReference.lastOutgoingId != Int.MIN_VALUE
                            && ackReference.lastOutgoingId != ackReference.maxConfirmedId
                        ) {
                            // Still waiting for a 'confirm teleport' packet. More or less safe to cancel this out.
                            /*
                             * TODO: The actual issue with this, apart from
                             * potential freezing, also concerns gameplay experience
                             * in case of minor set backs, which also could be
                             * caused by the server, e.g. with 'moved wrongly' or
                             * setting players outside of blocks. In this case the
                             * moves sent before teleport ack would still be valid
                             * after the teleport, because distances are small. The
                             * actual solution should still be to a) not have false
                             * positives b) somehow get rid all the
                             * position-correction teleporting the server does, for
                             * the cases a plugin can handle.
                             */
                            // TODO: Timeout -> either skip cancel or schedule a set back (to last valid pos or other).
                            // TODO: Config?
                            cancel = true
                        }
                    }
                }
                AckResolution.ACK -> {
                    run {

                        // Skip processing ACK packets, no cancel.
                        skipFlyingFrequency = true
                        if (pData.isDebugActive(this.checkType)) {
                            debug(player, "Incoming packet, interpret as ACK for outgoing position.")
                        }
                    }
                    run {
                        // Continue.
                        data.addFlyingQueue(packetData) // TODO: Not the optimal position, perhaps.
                    }
                }
                else -> {
                    data.addFlyingQueue(packetData)
                }
            }
            // Add as valid packet (exclude invalid coordinates etc. for now).
            validContent.add(packetData.simplifiedContentType)
        }

        // Actual packet frequency check.
        // TODO: Consider using the NetStatic check.
        if (!cancel && !skipFlyingFrequency
            && !pData.hasBypass(CheckType.NET_FLYINGFREQUENCY, player)
            && flyingFrequency.check(player, packetData, time, data, cc, pData)
        ) {
            cancel = true
        }

        // More packet checks.
        if (!cancel && !pData.hasBypass(CheckType.NET_MOVING, player) && !skipFlyingFrequency
            && moving.check(player, packetData, data, cc, pData, plugin)
        ) {
            cancel = true
        }

        // Cancel redundant packets, when frequency is high anyway.
        // TODO: Recode to detect cheating in a more reliable way, normally this is not the primary thread.
        //        if (!cancel && primaryThread && packetData != null && cc.flyingFrequencyRedundantActive && checkRedundantPackets(player, packetData, allScore, time, data, cc)) {
        //            event.setCancelled(true);
        //        }

        // Process cancel and debug log.
        if (cancel) {
            event.isCancelled = true
        }
        if (pData.isDebugActive(checkType)) {
            debug(player, (packetData?.toString() ?: "(Incompatible data)") + if (event.isCancelled) " CANCEL" else "")
        }
    }

    private fun isInvalidContent(packetData: DataPacketFlying): Boolean {
        if (packetData.hasPos && LocUtil.isBadCoordinate(packetData.x, packetData.y, packetData.z)) {
            return true
        }
        return if (packetData.hasLook && LocUtil.isBadCoordinate(packetData.yaw, packetData.pitch)) {
            true
        } else false
    }

    /**
     * Interpret the packet content and do with it whatever is suitable.
     * @param player
     * @param event
     * @param allScore
     * @param time
     * @param data
     * @param cc
     * @return Packet data if successful, or null on packet mismatch.
     */
    private fun interpretPacket(event: PacketEvent, time: Long): DataPacketFlying? {
        val packet = event.packet
        val booleans = packet.booleans.values
        if (booleans.size != 3) {
            packetMismatch(event)
            return null
        }
        val onGround: Boolean = booleans[indexOnGround].toBoolean()
        val hasPos: Boolean = booleans[indexhasPos].toBoolean()
        val hasLook: Boolean = booleans[indexhasLook].toBoolean()
        if (!hasPos && !hasLook) {
            return DataPacketFlying(onGround, time)
        }
        val doubles: List<Double>?
        val floats: List<Float>?
        if (hasPos) {
            doubles = packet.doubles.values
            if (doubles.size != 3 && doubles.size != 4) {
                // 3: 1.8, 4: 1.7.10 and before (stance).
                packetMismatch(event)
                return null
            }
            // TODO: before 1.8: stance (should make possible to reject in isInvalidContent).
        } else {
            doubles = null
        }
        if (hasLook) {
            floats = packet.float.values
            if (floats.size != 2) {
                packetMismatch(event)
                return null
            }
        } else {
            floats = null
        }
        return if (hasPos && hasLook) {
            DataPacketFlying(
                onGround,
                doubles!![indexX],
                doubles[indexY],
                doubles[indexZ],
                floats!![indexYaw],
                floats[indexPitch],
                time
            )
        } else if (hasLook) {
            DataPacketFlying(
                onGround,
                floats!![indexYaw],
                floats[indexPitch],
                time
            )
        } else if (hasPos) {
            DataPacketFlying(
                onGround,
                doubles!![indexX],
                doubles[indexY],
                doubles[indexZ],
                time
            )
        } else {
            throw IllegalStateException("Can't be, it can't be!")
        }
    }

    /**
     * Log warning to console, stop interpreting packet content.
     */
    private fun packetMismatch(packetEvent: PacketEvent) {
        val time = synchMillis()
        if (time - packetMismatchLogFrequency > packetMismatch) {
            packetMismatch = time
            val builder = StringBuilder(512)
            builder.append(CheckUtils.getLogMessagePrefix(packetEvent.player, checkType))
            builder.append("Incoming packet could not be interpreted. Are server and plugins up to date (NCP/ProtocolLib...)? This message is logged every ")
            builder.append(java.lang.Long.toString(packetMismatchLogFrequency / 1000))
            builder.append(" seconds, disregarding for which player this happens.")
            if (!validContent.isEmpty()) {
                builder.append(" On other occasion, valid content was received for: ")
                join(validContent, ", ", builder)
            }
            NCPAPIProvider.getNoCheatPlusAPI().logManager.warning(Streams.STATUS, builder.toString())
        }
    }

    companion object {
        // Setup for flying packets.
        const val indexOnGround = 0
        const val indexhasPos = 1
        const val indexhasLook = 2
        const val indexX = 0
        const val indexY = 1
        const val indexZ = 2

        /** 1.7.10  */
        const val indexStance = 3
        const val indexYaw = 0
        const val indexPitch = 1
        private fun initPacketTypes(): Array<PacketType> {
            val types: MutableList<PacketType> = LinkedList(
                Arrays.asList(
                    PacketType.Play.Client.LOOK,
                    PacketType.Play.Client.POSITION,
                    PacketType.Play.Client.POSITION_LOOK
                )
            )
            if (ServerVersion.compareMinecraftVersion("1.17") < 0) {
                types.add(PacketType.Play.Client.FLYING)
                StaticLog.logInfo("Add listener for legacy PlayInFlying packet.")
            } else types.add(PacketType.Play.Client.GROUND)
            // Add confirm teleport.
            // PacketPlayInTeleportAccept
            val confirmType: PacketType =
                ProtocolLibComponent.Companion.findPacketTypeByName(Protocol.PLAY, Sender.CLIENT, "TeleportAccept")
            if (confirmType != null && ServerVersion.compareMinecraftVersion("1.9") >= 0) {
                StaticLog.logInfo("Confirm teleport packet available (via name): $confirmType")
                types.add(confirmType)
            }
            return types.toTypedArray()
        }
    }
}