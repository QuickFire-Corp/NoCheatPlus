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
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.Plugin
import java.lang.reflect.Method

class UseEntityAdapter(plugin: Plugin?) : BaseAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
    private class LegacyReflectionSet(versionDetail: String) {
        /** Hacks.  */
        var packetClass_legacy: Class<*>? = null
        var enumClassAction_legacy: Class<*>? = null
        var methodGetAction_legacy: Method? = null
        var methodName_legacy: Method? = null

        /**
         *
         * @param versionDetail
         * @throws RuntimeException
         * if not matching/supported.
         */
        init {
            val packetClass = getClass("net.minecraft.server.$versionDetail.PacketPlayInUseEntity")
            val actionClass = getClass("net.minecraft.server.$versionDetail.EnumEntityUseAction")
            val methodGetAction =
                if (packetClass == null || actionClass == null) null else getMethodNoArgs(packetClass, "c", actionClass)
            if (packetClass == null || actionClass == null || methodGetAction == null) {
                packetClass_legacy = null
                enumClassAction_legacy = null
                methodGetAction_legacy = null
                methodName_legacy = null
            } else {
                packetClass_legacy = packetClass
                enumClassAction_legacy = actionClass
                methodGetAction_legacy = methodGetAction
                methodName_legacy = getMethodNoArgs(enumClassAction_legacy, "name", String::class.java)
            }
            if (methodName_legacy == null) {
                throw RuntimeException("Not supported.")
            }
        }

        fun getActionFromNMSPacket(handle: Any): String? {
            val clazz: Class<*> = handle.javaClass
            if (clazz != packetClass_legacy) {
                return null
            }
            val action = invokeMethodNoArgs(methodGetAction_legacy, handle) ?: return null
            val actionName = invokeMethodNoArgs(methodName_legacy, action)
            return if (actionName is String) {
                actionName
            } else {
                null
            }
        }
    }

    private val attackFrequency: AttackFrequency
    private val legacySet: LegacyReflectionSet?

    init {
        checkType = CheckType.NET_ATTACKFREQUENCY
        // Add feature tags for checks.
        if (NCPAPIProvider.getNoCheatPlusAPI().worldDataManager.isActiveAnywhere(
                CheckType.NET_ATTACKFREQUENCY
            )
        ) {
            NCPAPIProvider.getNoCheatPlusAPI().addFeatureTags(
                "checks", Arrays.asList(AttackFrequency::class.java.simpleName)
            )
        }
        attackFrequency = AttackFrequency()
        NCPAPIProvider.getNoCheatPlusAPI().addComponent(attackFrequency)
        legacySet = legacyReflectionSet
    }

    // +-
    private val legacyReflectionSet: LegacyReflectionSet?
        private get() {
            for (versionDetail in arrayOf("v1_7_R4", "v1_7_R1")) {
                try {
                    return LegacyReflectionSet(versionDetail)
                } catch (e: RuntimeException) {
                } // +-
            }
            return null
        }

    override fun onPacketReceiving(event: PacketEvent) {
        try {
            if (event.isPlayerTemporary) return
        } catch (e: NoSuchMethodError) {
        }
        val time = System.currentTimeMillis()
        val player = event.player
            ?: // TODO: Warn once?
            return
        val pData = DataManager.getPlayerDataSafe(player) ?: return
        val data = pData.getGenericInstance(NetData::class.java)
        // Always set last received time.
        data.lastKeepAliveTime = time

        // Quick return, if no checks are active.
        if (!pData.isCheckActive(CheckType.NET_ATTACKFREQUENCY, player)) {
            return
        }
        val packet = event.packet

        // MIGHT: use entity, use block both on packet level?
        var isAttack = false
        var packetInterpreted = false
        if (legacySet != null) {
            // Attempt to extract legacy information.
            val flags = getAction_legacy(packet)
            if (flags and INTERPRETED != 0) {
                packetInterpreted = true
                if (flags and ATTACK != 0) {
                    isAttack = true
                }
            }
        }
        if (!packetInterpreted) {
            // Handle as if latest.
            try {
                val actions = packet.entityUseActions
                if (actions.size() == 1 && actions.read(0) == EntityUseAction.ATTACK) {
                    isAttack = true
                    packetInterpreted = true
                }
            } catch (e: NullPointerException) {
                /*
                 * TODO: Observed somewhere on 1_7_R4, probably a custom build -
                 * why doesn't the LegacyReflectionSet work here?
                 */
            }
        }
        if (!packetInterpreted) {
            // TODO: Log warning once, if the packet could not be interpreted.
            return
        }

        // Run checks.
        var cancel = false

        // AttackFrequency
        if (isAttack) {
            val cc = pData.getGenericInstance(NetConfig::class.java)
            if (attackFrequency.isEnabled(player, pData)
                && attackFrequency.check(player, time, data, cc, pData)
            ) {
                cancel = true
            }
        }
        if (cancel) {
            event.isCancelled = true
        }
    }

    private fun getAction_legacy(packetContainer: PacketContainer): Int {
        // (For some reason the object didn't appear work with equality checks, thus compare the short string.)
        val actionName = legacySet!!.getActionFromNMSPacket(packetContainer.handle)
        return if (actionName == null) 0 else INTERPRETED or if ("ATTACK" == actionName) ATTACK else 0
    }

    companion object {
        private const val INTERPRETED = 0x01
        private const val ATTACK = 0x02
    }
}