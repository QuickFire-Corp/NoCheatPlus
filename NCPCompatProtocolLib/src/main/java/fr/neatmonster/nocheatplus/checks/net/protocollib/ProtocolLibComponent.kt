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
import fr.neatmonster.nocheatplus.stats.Counters
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.Plugin
import java.util.ArrayList

/**
 * Quick and dirty ProtocolLib setup.
 *
 * @author asofold
 */
class ProtocolLibComponent(plugin: Plugin?) : IDisableListener, INotifyReload, JoinLeaveListener, Listener {
    init {
        register(plugin)
        /*
         * TODO: Register listeners iff any check is enabled - unregister from
         * EventRegistry with unregister.
         */
    }

    private fun register(plugin: Plugin?) {
        val worldMan = NCPAPIProvider.getNoCheatPlusAPI().worldDataManager
        if (!worldMan.isActiveAnywhere(CheckType.NET)) {
            StaticLog.logInfo("No packet level checks activated.")
            return
        }
        StaticLog.logInfo("Adding packet level hooks for ProtocolLib (MC " + ProtocolLibrary.getProtocolManager().minecraftVersion.version + ")...")
        //Special purpose.
        if (ConfigManager.isTrueForAnyConfig(ConfPaths.NET + ConfPaths.SUB_DEBUG) || ConfigManager.isTrueForAnyConfig(
                ConfPaths.CHECKS_DEBUG
            )
        ) {
            // (Debug logging. Only activates if debug is set for checks or checks.net, not on the fly.)
            register("fr.neatmonster.nocheatplus.checks.net.protocollib.DebugAdapter", plugin)
        }
        // Actual checks.
        if (ServerVersion.compareMinecraftVersion("1.6.4") <= 0) {
            // Don't use this listener.
            NCPAPIProvider.getNoCheatPlusAPI().logManager.info(
                Streams.STATUS,
                "Disable EntityUseAdapter due to incompatibilities. Use fight.speed instead of net.attackfrequency."
            )
        } else if (worldMan.isActiveAnywhere(CheckType.NET_ATTACKFREQUENCY)) {
            // (Also sets lastKeepAliveTime, if enabled.)
            register("fr.neatmonster.nocheatplus.checks.net.protocollib.UseEntityAdapter", plugin)
        }
        if (worldMan.isActiveAnywhere(CheckType.NET_FLYINGFREQUENCY) || worldMan.isActiveAnywhere(CheckType.NET_MOVING)) {
            // (Also sets lastKeepAliveTime, if enabled.)
            register("fr.neatmonster.nocheatplus.checks.net.protocollib.MovingFlying", plugin)
            register("fr.neatmonster.nocheatplus.checks.net.protocollib.OutgoingPosition", plugin)
        }
        if (worldMan.isActiveAnywhere(CheckType.NET_KEEPALIVEFREQUENCY) || worldMan.isActiveAnywhere(CheckType.FIGHT_GODMODE)) {
            // (Set lastKeepAlive if this or fight.godmode is enabled.)
            register("fr.neatmonster.nocheatplus.checks.net.protocollib.KeepAliveAdapter", plugin)
        }
        if (worldMan.isActiveAnywhere(CheckType.NET_SOUNDDISTANCE)) {
            register("fr.neatmonster.nocheatplus.checks.net.protocollib.SoundDistance", plugin)
        }
        if (worldMan.isActiveAnywhere(CheckType.NET_WRONGTURN)) {
            register("fr.neatmonster.nocheatplus.checks.net.protocollib.WrongTurnAdapter", plugin)
        }
        if (ServerVersion.compareMinecraftVersion("1.9") < 0) {
            if (worldMan.isActiveAnywhere(CheckType.NET_PACKETFREQUENCY)) {
                register("fr.neatmonster.nocheatplus.checks.net.protocollib.CatchAllAdapter", plugin)
            }
        }
        if (ServerVersion.compareMinecraftVersion("1.8") >= 0) {
            if (ConfigManager.isTrueForAnyConfig(ConfPaths.MOVING_SURVIVALFLY_EXTENDED_NOSLOW)) register(
                "fr.neatmonster.nocheatplus.checks.net.protocollib.NoSlow",
                plugin
            )
            register("fr.neatmonster.nocheatplus.checks.net.protocollib.Fight", plugin)
        }
        if (!registeredPacketAdapters.isEmpty()) {
            val names: MutableList<String?> = ArrayList(registeredPacketAdapters.size)
            for (adapter in registeredPacketAdapters) {
                names.add(adapter.javaClass.simpleName)
            }
            StaticLog.logInfo("Available (and activated) packet level hooks: " + join(names, " | "))
            NCPAPIProvider.getNoCheatPlusAPI().addFeatureTags("packet-listeners", names)
        } else {
            StaticLog.logInfo("No packet level hooks activated.")
        }
    }

    private fun register(name: String, plugin: Plugin?) {
        var t: Throwable? = null
        t = try {
            val clazz = Class.forName(name)
            register(clazz as Class<out PacketAdapter>, plugin)
            return
        } catch (e: ClassNotFoundException) {
            e
        } catch (e: ClassCastException) {
            e
        }
        StaticLog.logWarning("Could not register packet level hook: $name")
        StaticLog.logWarning(t)
    }

    private fun register(clazz: Class<out PacketAdapter>, plugin: Plugin?) {
        try {
            // Construct a new instance using reflection.
            val adapter = clazz.getDeclaredConstructor(Plugin::class.java).newInstance(plugin)
            ProtocolLibrary.getProtocolManager().addPacketListener(adapter)
            registeredPacketAdapters.add(adapter)
        } catch (t: Throwable) {
            StaticLog.logWarning("Could not register packet level hook: " + clazz.simpleName)
            StaticLog.logWarning(t)
            if (t.cause != null) {
                StaticLog.logWarning(t.cause)
            }
        }
    }

    override fun onDisable() {
        unregister()
    }

    override fun onReload() {
        unregister()
        NCPAPIProvider.getNoCheatPlusAPI().playerDataManager.removeGenericInstance(NetData::class.java) // Currently needed for FlyingFrequency.
        register(Bukkit.getPluginManager().getPlugin("NoCheatPlus")) // TODO: static plugin getter?
    }

    private fun unregister() {
        val protocolManager = ProtocolLibrary.getProtocolManager()
        val api = NCPAPIProvider.getNoCheatPlusAPI()
        for (adapter in registeredPacketAdapters) {
            try {
                protocolManager.removePacketListener(adapter)
                api.removeComponent(adapter) // Bit heavy, but consistent.
            } catch (t: Throwable) {
                StaticLog.logWarning("Failed to unregister packet level hook: " + adapter.javaClass.name)
            } // TODO Auto-generated method stub
        }
        registeredPacketAdapters.clear()
    }

    override fun playerJoins(player: Player) {
        if (!registeredPacketAdapters.isEmpty()) {
            DataManager.getGenericInstance(player, NetData::class.java).onJoin(player)
        }
    }

    override fun playerLeaves(player: Player) {
        if (!registeredPacketAdapters.isEmpty()) {
            DataManager.getGenericInstance(player, NetData::class.java).onLeave(player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        if (!registeredPacketAdapters.isEmpty()) {
            val player = event.player
            val data = DataManager.getGenericInstance(player, NetData::class.java)
            data.onJoin(player)
            val loc = event.respawnLocation
            data.teleportQueue.onTeleportEvent(loc.x, loc.y, loc.z, loc.yaw, loc.pitch)
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        if (!registeredPacketAdapters.isEmpty()) {
            // TODO: Might move to MovingListener.
            // TODO: Might still add cancelled UNKNOWN events. TEST IT
            val to = event.to ?: return
            val player = event.player
            val pData = DataManager.getPlayerData(player)
            val data = pData.getGenericInstance(NetData::class.java)
            if (pData.isCheckActive(CheckType.NET_FLYINGFREQUENCY, player)) {
                // Register expected location for comparison with outgoing packets.
                data.teleportQueue.onTeleportEvent(to.x, to.y, to.z, to.yaw, to.pitch)
            }
            data.clearFlyingQueue()
        }
    }

    companion object {
        // TODO: Static reference is problematic (needs a static and accessible Counters instance?). 
        val idNullPlayer = NCPAPIProvider.getNoCheatPlusAPI().getGenericInstance(
            Counters::class.java
        ).registerKey("packet.nullplayer")

        /** Likely does not happen, TODO: Code review protocol plugin.  */
        val idInconsistentIsAsync = NCPAPIProvider.getNoCheatPlusAPI().getGenericInstance(
            Counters::class.java
        ).registerKey("packet.inconsistent.isasync")

        /**
         * Auxiliary method for suppressing exceptions.
         *
         * @param protocol
         * @param sender
         * @param name
         * PacketType if available, null otherwise.
         * @return
         */
        fun findPacketTypeByName(protocol: Protocol?, sender: Sender?, name: String?): PacketType? {
            return try {
                PacketType.findCurrent(protocol, sender, name)
            } catch (t: Throwable) {
                // uh
                null
            }
        }

        // INSTANCE ----
        private val registeredPacketAdapters: MutableList<PacketAdapter> = LinkedList()
        fun unregister(adapter: PacketAdapter) {
            val protocolManager = ProtocolLibrary.getProtocolManager()
            val api = NCPAPIProvider.getNoCheatPlusAPI()
            try {
                protocolManager.removePacketListener(adapter)
                api.removeComponent(adapter) // Bit heavy, but consistent.
                registeredPacketAdapters.remove(adapter)
                val names: MutableList<String> = ArrayList(registeredPacketAdapters.size)
                for (adaptern in registeredPacketAdapters) {
                    names.add(adaptern.javaClass.simpleName)
                }
                api.setFeatureTags("packet-listeners", names)
                StaticLog.logInfo("Unregistered packet level hook:" + adapter.javaClass.name)
            } catch (t: Throwable) {
                StaticLog.logWarning("Failed to unregister packet level hook: " + adapter.javaClass.name)
            }
        }
    }
}