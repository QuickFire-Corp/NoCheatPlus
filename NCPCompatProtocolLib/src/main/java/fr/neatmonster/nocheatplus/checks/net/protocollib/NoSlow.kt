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
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.Plugin

class NoSlow(plugin: Plugin?) : BaseAdapter(plugin, ListenerPriority.MONITOR, *initPacketTypes()) {
    init {
        val api = NCPAPIProvider.getNoCheatPlusAPI()
        for (listener in miniListeners) {
            api.addComponent(listener, false)
        }
    }

    override fun onPacketReceiving(event: PacketEvent) {
        if (event.isPlayerTemporary) return
        if (event.packetType == PacketType.Play.Client.BLOCK_DIG) {
            handleDiggingPacket(event)
        } else {
            handleBlockPlacePacket(event)
        }
    }

    private fun handleBlockPlacePacket(event: PacketEvent) {
        val p = event.player
        val pData = DataManager.getPlayerData(p)
        val data = pData.getGenericInstance(MovingData::class.java)
        val packet = event.packet
        val ints = packet.integers
        // Legacy: pre 1.9
        if (ints.size() > 0) {
            val faceIndex = ints.read(0) // arg 3 if 1.7.10 below
            if (faceIndex <= 5) {
                data.mightUseItem = false
                return
            }
        }
        if (!event.isCancelled) data.mightUseItem = true
    }

    private fun handleDiggingPacket(event: PacketEvent) {
        val p = event.player
        if (p == null) {
            counters.add(ProtocolLibComponent.Companion.idNullPlayer, 1)
            return
        }
        val pData = DataManager.getPlayerDataSafe(p)
        if (pData == null) {
            StaticLog.logWarning("Failed to fetch player data with " + event.packetType + " for: " + p.toString())
            return
        }
        val data = pData.getGenericInstance(MovingData::class.java)
        val digtype = event.packet.playerDigTypes.read(0)
        // DROP_ALL_ITEMS when dead?
        if (digtype == PlayerDigType.DROP_ALL_ITEMS || digtype == PlayerDigType.DROP_ITEM) data.isUsingItem = false

        //Advanced check
        if (digtype == PlayerDigType.RELEASE_USE_ITEM) {
            data.isUsingItem = false
            val now = System.currentTimeMillis()
            if (data.releaseItemTime != 0L) {
                if (now < data.releaseItemTime) {
                    data.releaseItemTime = now
                    return
                }
                if (data.releaseItemTime + timeBetweenRL > now) {
                    data.isHackingRI = true
                }
            }
            data.releaseItemTime = now
        }
    }

    companion object {
        private const val dftag = "system.nocheatplus.noslow"
        private val miniListeners = arrayOf<MiniListener<*>>(
            object : MiniListener<PlayerItemConsumeEvent?> {
                @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
                @RegisterMethodWithOrder(tag = dftag)
                override fun onEvent(event: PlayerItemConsumeEvent) {
                    onItemConsume(event)
                }
            },
            object : MiniListener<PlayerInteractEvent?> {
                @EventHandler(priority = EventPriority.MONITOR)
                @RegisterMethodWithOrder(tag = dftag)
                override fun onEvent(event: PlayerInteractEvent) {
                    onItemInteract(event)
                }
            },
            object : MiniListener<InventoryOpenEvent?> {
                @EventHandler(priority = EventPriority.LOWEST)
                @RegisterMethodWithOrder(tag = dftag)
                override fun onEvent(event: InventoryOpenEvent) {
                    onInventoryOpen(event)
                }
            },
            object : MiniListener<PlayerItemHeldEvent?> {
                @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
                @RegisterMethodWithOrder(tag = dftag)
                override fun onEvent(event: PlayerItemHeldEvent) {
                    onChangeSlot(event)
                }
            }
        )
        private var timeBetweenRL = 70
        private fun initPacketTypes(): Array<PacketType> {
            val types: List<PacketType> = LinkedList(
                Arrays.asList(
                    PacketType.Play.Client.BLOCK_DIG,
                    PacketType.Play.Client.BLOCK_PLACE
                )
            )
            return types.toTypedArray()
        }

        private fun onItemConsume(e: PlayerItemConsumeEvent) {
            val p = e.player
            val pData = DataManager.getPlayerData(p)
            val data = pData.getGenericInstance(MovingData::class.java)
            data.isUsingItem = false
        }

        private fun onInventoryOpen(e: InventoryOpenEvent) {
            if (e.isCancelled) return
            val p = e.player as Player
            val pData = DataManager.getPlayerData(p)
            val data = pData.getGenericInstance(MovingData::class.java)
            data.isUsingItem = false
        }

        private fun onItemInteract(e: PlayerInteractEvent) {
            // TODO: Add trident (Check for rain and verify if the player is exposed to it at all, might not be worth doing it...)
            if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
            val p = e.player
            val pData = DataManager.getPlayerData(p)
            val data = pData.getGenericInstance(MovingData::class.java)
            // Reset
            data.offHandUse = false
            if (!data.mightUseItem) return
            data.mightUseItem = false
            if (e.useItemInHand() == Event.Result.DENY) return
            if (p.gameMode == GameMode.CREATIVE) {
                data.isUsingItem = false
                return
            }
            if (e.hasItem()) {
                val item = e.item
                val m = item!!.type
                if (Bridge1_9.hasElytra() && p.hasCooldown(m)) return
                if (InventoryUtil.isConsumable(item)) {
                    // pre1.9 splash potion
                    if (!Bridge1_9.hasElytra() && item.durability > 16384) return
                    if (m == Material.POTION || m == Material.MILK_BUCKET || m.toString()
                            .endsWith("_APPLE") || m.name.startsWith("HONEY_BOTTLE")
                    ) {
                        data.isUsingItem = true
                        data.offHandUse = Bridge1_9.hasGetItemInOffHand() && e.hand == EquipmentSlot.OFF_HAND
                        return
                    }
                    if (item.type.isEdible && p.foodLevel < 20) {
                        data.isUsingItem = true
                        data.offHandUse = Bridge1_9.hasGetItemInOffHand() && e.hand == EquipmentSlot.OFF_HAND
                        return
                    }
                }
                if (m == Material.BOW && hasArrow(p.inventory, false)) {
                    data.isUsingItem = true
                    data.offHandUse = Bridge1_9.hasGetItemInOffHand() && e.hand == EquipmentSlot.OFF_HAND
                    return
                }
                if (Bridge1_9.hasElytra() && m == Material.SHIELD) {
                    //data.isUsingItem = true;
                    data.offHandUse = e.hand == EquipmentSlot.OFF_HAND
                    return
                }
                if (Bridge1_13.hasIsRiptiding() && m == Material.TRIDENT) {
                    //data.isUsingItem = true;
                    data.offHandUse = e.hand == EquipmentSlot.OFF_HAND
                    return
                }
                if (m.toString() == "CROSSBOW") {
                    if (!(item.itemMeta as CrossbowMeta?)!!.hasChargedProjectiles() && hasArrow(p.inventory, true)) {
                        data.isUsingItem = true
                        data.offHandUse = e.hand == EquipmentSlot.OFF_HAND
                    }
                }
            } else data.isUsingItem = false
        }

        private fun onChangeSlot(e: PlayerItemHeldEvent) {
            val p = e.player
            val pData = DataManager.getPlayerData(p)
            val data = pData.getGenericInstance(MovingData::class.java)
            //if (data.changeslot) {
            //    p.getInventory().setHeldItemSlot(data.olditemslot);
            //    data.changeslot = false;
            //}
            data.isUsingItem = false
        }

        private fun hasArrow(i: PlayerInventory, fw: Boolean): Boolean {
            if (Bridge1_9.hasElytra()) {
                val m = i.itemInOffHand.type
                return fw && m == Material.FIREWORK_ROCKET || m.toString().endsWith("ARROW") ||
                        i.contains(Material.ARROW) || i.contains(Material.TIPPED_ARROW) || i.contains(Material.SPECTRAL_ARROW)
            }
            return i.contains(Material.ARROW)
        }

        /**
         * Set Minimum time between RELEASE_USE_ITEM packet is sent.
         * If time lower this value, A check will flag
         * Should be set from 51-100. Larger number, more protection more false-positive
         *
         * @param milliseconds
         */
        fun setuseRLThreshold(time: Int) {
            timeBetweenRL = time
        }
    }
}