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
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.Plugin

/**
 * Pre-1.9.
 *
 * @author asofold
 */
class CatchAllAdapter(plugin: Plugin?) : BaseAdapter(plugin, ListenerPriority.LOWEST, packetTypes) {
    private val packetFrequency: PacketFrequency

    init {
        packetFrequency = PacketFrequency()
        NCPAPIProvider.getNoCheatPlusAPI().addComponent(packetFrequency)
    }

    override fun onPacketReceiving(event: PacketEvent) {
        try {
            if (event.isPlayerTemporary) return
        } catch (e: NoSuchMethodError) {
        }
        val player = event.player
        if (player == null) {
            counters.add(ProtocolLibComponent.Companion.idNullPlayer, 1)
            // TODO: Is this a problem, as the server has the player so it could break a block)?
            return
        }
        val pData = DataManager.getPlayerDataSafe(player)
        if (pData == null) {
            StaticLog.logWarning("Failed to fetch player data with " + event.packetType + " for: " + player.toString())
            return
        }
        if (packetFrequency.isEnabled(player, pData)) {
            val cc = pData.getGenericInstance(NetConfig::class.java)
            val data = pData.getGenericInstance(NetData::class.java)
            if (packetFrequency.check(player, data, cc)) {
                event.isCancelled = true
            }
        }
    } //    @Override

    //    public void onPacketSending(PacketEvent event) {
    //        final Player player = event.getPlayer();
    //        final NetConfig cc = configFactory.getConfig(player);
    //        if (cc.packetFrequencyActive) {
    //            packetFrequency.relax(event.getPlayer(), dataFactory.getData(player), cc);
    //        }
    //    }
    companion object {// TODO: Config ?
        //        // relax.
        //        types.add(PacketType.Play.Server.POSITION);
        /**
         * Somehow determine types to monitor.
         *
         * @return
         */
        private val packetTypes: Iterable<PacketType>
            private get() {
                // TODO: Config ?
                val types: MutableSet<PacketType> = LinkedHashSet()
                for (type in PacketType.Play.Client.getInstance().values()) {
                    if (type.isSupported) {
                        types.add(type)
                    }
                }
                //        // relax.
                //        types.add(PacketType.Play.Server.POSITION);
                return types
            }
    }
}