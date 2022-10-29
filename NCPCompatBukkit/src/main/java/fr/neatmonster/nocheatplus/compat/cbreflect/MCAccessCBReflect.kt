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
package fr.neatmonster.nocheatplus.compat.cbreflect

import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getClass
import fr.neatmonster.nocheatplus.logging.details.ILogString.info
import fr.neatmonster.nocheatplus.utilities.StringUtil.join
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getMethodNoArgs
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.invokeMethodNoArgs
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.invokeMethod
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getMethod
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.newInstance
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getField
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.get
import fr.neatmonster.nocheatplus.logging.details.ILogString.warning
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getBoolean
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.set
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getInt
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getFloat
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getDouble
import fr.neatmonster.nocheatplus.logging.details.ILogString.debug
import fr.neatmonster.nocheatplus.utilities.Validate.validateNotNull
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil.getConstructor
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerEditBookEvent
import fr.neatmonster.nocheatplus.players.IPlayerData
import fr.neatmonster.nocheatplus.players.DataManager
import fr.neatmonster.nocheatplus.checks.inventory.InventoryConfig
import fr.neatmonster.nocheatplus.checks.inventory.InventoryData
import org.bukkit.inventory.meta.BookMeta
import java.lang.RuntimeException
import fr.neatmonster.nocheatplus.components.registry.feature.INotifyReload
import fr.neatmonster.nocheatplus.NCPAPIProvider
import fr.neatmonster.nocheatplus.compat.AlmostBoolean
import fr.neatmonster.nocheatplus.components.config.value.OverrideType
import fr.neatmonster.nocheatplus.logging.StaticLog
import org.bukkit.event.player.PlayerItemConsumeEvent
import fr.neatmonster.nocheatplus.compat.BridgeHealth
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import fr.neatmonster.nocheatplus.utilities.TickTask
import fr.neatmonster.nocheatplus.checks.ViolationData
import fr.neatmonster.nocheatplus.utilities.InventoryUtil
import fr.neatmonster.nocheatplus.utilities.map.WrapBlockCache
import fr.neatmonster.nocheatplus.checks.inventory.HotFixFallingBlockPortalEnter
import org.bukkit.event.entity.EntityPortalEnterEvent
import org.bukkit.World
import fr.neatmonster.nocheatplus.worlds.IWorldData
import fr.neatmonster.nocheatplus.utilities.map.BlockCache
import fr.neatmonster.nocheatplus.utilities.map.BlockProperties
import fr.neatmonster.nocheatplus.compat.BridgeMaterial
import fr.neatmonster.nocheatplus.utilities.location.LocUtil
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil
import fr.neatmonster.nocheatplus.compat.versions.ServerVersion
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitShapeModel
import org.bukkit.block.data.BlockData
import org.bukkit.block.BlockFace
import org.bukkit.block.data.type.Bell.Attachment
import fr.neatmonster.nocheatplus.compat.Bridge1_13
import org.bukkit.util.BoundingBox
import org.bukkit.block.BlockState
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitDoor
import org.bukkit.block.data.type.Door.Hinge
import org.bukkit.block.data.Rail
import org.bukkit.block.data.type.Slab
import org.bukkit.block.data.type.Snow
import org.bukkit.block.data.MultipleFacing
import org.bukkit.block.data.type.Wall
import org.bukkit.block.data.type.Cocoa
import org.bukkit.block.data.type.Piston
import org.bukkit.block.data.Bisected.Half
import java.util.stream.IntStream
import java.util.function.IntFunction
import java.lang.IllegalArgumentException
import org.bukkit.block.data.type.Lantern
import org.bukkit.block.data.type.BigDripleaf
import org.bukkit.block.data.type.BigDripleaf.Tilt
import fr.neatmonster.nocheatplus.utilities.map.BlockFlags
import org.bukkit.block.data.Waterlogged
import org.bukkit.block.data.Levelled
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitTrapDoor
import org.bukkit.block.data.type.SeaPickle
import org.bukkit.block.data.type.TurtleEgg
import org.bukkit.block.data.type.Grindstone
import org.bukkit.block.data.FaceAttachable.AttachedFace
import fr.neatmonster.nocheatplus.compat.bukkit.model.ShapeModel
import org.bukkit.block.data.type.EndPortalFrame
import org.bukkit.util.VoxelShape
import fr.neatmonster.nocheatplus.compat.bukkit.model.AbstractBukkitCentered
import fr.neatmonster.nocheatplus.compat.bukkit.MCAccessBukkitBase
import fr.neatmonster.nocheatplus.compat.blocks.BlockPropertiesSetup
import fr.neatmonster.nocheatplus.config.WorldConfigProvider
import java.util.LinkedHashSet
import fr.neatmonster.nocheatplus.compat.MCAccess
import org.bukkit.command.CommandMap
import org.bukkit.Bukkit
import fr.neatmonster.nocheatplus.compat.bukkit.BlockCacheBukkit
import fr.neatmonster.nocheatplus.utilities.map.MaterialUtil
import fr.neatmonster.nocheatplus.utilities.PotionUtil
import org.bukkit.potion.PotionEffectType
import fr.neatmonster.nocheatplus.compat.bukkit.MCAccessBukkit
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectBase
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectDamageSource
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectLivingEntity
import java.util.HashMap
import fr.neatmonster.nocheatplus.compat.blocks.init.BlockInit
import java.lang.ClassNotFoundException
import fr.neatmonster.nocheatplus.compat.bukkit.BlockCacheBukkitModern
import fr.neatmonster.nocheatplus.compat.bukkit.MCAccessBukkitModern
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitFetchableBound
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitStatic
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitHopper
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitCauldron
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitPistonHead
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitBell
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitAnvil
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitGrindStone
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitGate
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitShulkerBox
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitChorusPlant
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitDripLeaf
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitEndPortalFrame
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitSeaPickle
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitCocoa
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitTurtleEgg
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitCake
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitSlab
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitStairs
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitSnow
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitPiston
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitLevelled
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitLadder
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitRail
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitDirectionalCentered
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitFence
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitWall
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitWallHead
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitBamboo
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitWaterPlant
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitLantern
import fr.neatmonster.nocheatplus.components.modifier.IAttributeAccess
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeInstance
import java.util.UUID
import fr.neatmonster.nocheatplus.compat.AttribUtil
import fr.neatmonster.nocheatplus.components.entity.IEntityAccessVehicle
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectBlockPosition
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectMaterial
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectWorld
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.IReflectBlock
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectAxisAlignedBB
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectIBlockData
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectIBlockAccess
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectHelper.ReflectFailureException
import java.lang.IllegalAccessException
import java.lang.reflect.InvocationTargetException
import kotlin.jvm.JvmOverloads
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectGetHandleBase
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.MostlyHarmless
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectEntity
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectPlayer
import java.lang.NullPointerException
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectBlock
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectBlockSix
import fr.neatmonster.nocheatplus.config.ConfigManager
import fr.neatmonster.nocheatplus.config.ConfPaths
import java.util.LinkedList
import java.lang.annotation.Documented
import java.util.Arrays
import java.util.Collections
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectAttributeAccess.ReflectGenericAttributes
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectAttributeAccess.ReflectAttributeInstance
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectAttributeAccess.ReflectAttributeModifier
import fr.neatmonster.nocheatplus.components.entity.IEntityAccessLastPositionAndLook
import fr.neatmonster.nocheatplus.components.location.ISetPositionWithLook
import kotlin.Throws
import fr.neatmonster.nocheatplus.components.location.IGetPositionWithLook
import fr.neatmonster.nocheatplus.compat.cbreflect.reflect.ReflectHelper
import fr.neatmonster.nocheatplus.compat.versions.GenericVersion
import fr.neatmonster.nocheatplus.compat.cbreflect.BlockCacheCBReflect
import fr.neatmonster.nocheatplus.compat.blocks.LegacyBlocks
import fr.neatmonster.nocheatplus.logging.Streams
import org.bukkit.entity.*

class MCAccessCBReflect : MCAccessBukkit() {
    protected val helper: ReflectHelper

    /** Generally supported Minecraft version (know for sure).  */
    protected var knownSupportedVersion = false

    /** We know for sure that dealFallDamage will fire a damage event.  */
    protected var dealFallDamageFiresAnEvent = false

    init {
        // TODO: Add unavailable stuff to features / missing (TBD).
        helper = ReflectHelper()
        // Version Envelope tests (1.4.5-R1.0 ... 1.8.x is considered to be ok).
        val mcVersion = ServerVersion.getMinecraftVersion()
        if (mcVersion === GenericVersion.UNKNOWN_VERSION) {
            NCPAPIProvider.getNoCheatPlusAPI().logManager.warning(
                Streams.INIT,
                "The Minecraft version could not be detected, Compat-CB-Reflect might or might not work."
            )
            knownSupportedVersion = false
        } else if (GenericVersion.compareVersions(mcVersion, "1.5") < 0) {
            NCPAPIProvider.getNoCheatPlusAPI().logManager.warning(
                Streams.INIT,
                "The Minecraft version seems to be older than what Compat-CB-Reflect can support."
            )
            knownSupportedVersion = false
        } else if (GenericVersion.compareVersions(mcVersion, "1.12.2") > 0) {
            knownSupportedVersion = false
            NCPAPIProvider.getNoCheatPlusAPI().logManager.warning(
                Streams.INIT,
                "The Minecraft version seems to be more recent than the one Compat-CB-Reflect has been built with - this might work, but there could be incompatibilities."
            )
        } else {
            knownSupportedVersion = true
        }
        // Fall damage / event. TODO: Tests between 1.8 and 1.7.2. How about spigot vs. CB?
        dealFallDamageFiresAnEvent =
            if (mcVersion === GenericVersion.UNKNOWN_VERSION || GenericVersion.compareVersions(mcVersion, "1.8") < 0) {
                false
            } else {
                // Assume higher versions to fire an event.
                true
            }
    }

    override fun getMCVersion(): String {
        return "1.5-1.12.2|?"
    }

    override fun getServerVersionTag(): String {
        return "CB-Reflect"
    }

    override fun getBlockCache(world: World): BlockCache {
        return try {
            BlockCacheCBReflect(helper, world)
        } catch (ex: ReflectFailureException) {
            BlockCacheBukkit(world)
        }
    }

    override fun shouldBeZombie(player: Player): Boolean {
        return try {
            val handle = helper.getHandle(player)
            !helper.nmsPlayer_dead(handle) && helper.nmsPlayer_getHealth(handle) <= 0.0
        } catch (ex: ReflectFailureException) {
            // Fall back to Bukkit.
            super.shouldBeZombie(player)
        }
    }

    override fun setDead(player: Player, deathTicks: Int) {
        try {
            val handle = helper.getHandle(player)
            helper.nmsPlayer_dead(handle, true)
            helper.nmsPlayer_deathTicks(handle, deathTicks)
        } catch (ex: ReflectFailureException) {
            super.setDead(player, deathTicks)
        }
    }

    override fun dealFallDamageFiresAnEvent(): AlmostBoolean {
        return if (!dealFallDamageFiresAnEvent) {
            AlmostBoolean.NO
        } else AlmostBoolean.match(helper.canDealFallDamage())
    }

    override fun dealFallDamage(player: Player, damage: Double) {
        try {
            helper.dealFallDamage(player, damage)
        } catch (ex: ReflectFailureException) {
            // TODO: Fire an event ?
            super.dealFallDamage(player, damage)
        }
    }

    override fun getInvulnerableTicks(player: Player): Int {
        return try {
            helper.getInvulnerableTicks(player)
        } catch (ex: ReflectFailureException) {
            super.getInvulnerableTicks(player)
        }
    }

    override fun setInvulnerableTicks(player: Player, ticks: Int) {
        try {
            helper.setInvulnerableTicks(player, ticks)
        } catch (ex: ReflectFailureException) {
            super.setInvulnerableTicks(player, ticks)
        }
    }

    override fun isBlockSolid(id: Material): AlmostBoolean {
        return try {
            helper.isBlockSolid(id)
        } catch (ex: ReflectFailureException) {
            super.isBlockSolid(id)
        }
    }

    override fun isBlockLiquid(id: Material): AlmostBoolean {
        return try {
            helper.isBlockLiquid(id)
        } catch (ex: ReflectFailureException) {
            super.isBlockLiquid(id)
        }
    }

    override fun getHeight(entity: Entity): Double {
        return if (bukkitHasGetHeightAndGetWidth) {
            super.getHeight(entity)
        } else try {
            helper.getHeight(entity)
        } catch (ex: ReflectFailureException) {
            super.getHeight(entity)
        }
    }

    override fun getWidth(entity: Entity): Double {
        return if (bukkitHasGetHeightAndGetWidth) {
            super.getWidth(entity)
        } else try {
            helper.getWidth(entity)
        } catch (ex: ReflectFailureException) {
            super.getWidth(entity)
        }
    }

    override fun isIllegalBounds(player: Player): AlmostBoolean {
        if (player.isDead) {
            return AlmostBoolean.NO
        }
        try {
            val bounds = helper.getBoundsTemp(player)
            if (LocUtil.isBadCoordinate(*bounds)) {
                return AlmostBoolean.YES
            }
            if (!player.isSleeping) {
                val dY = Math.abs(bounds!![4] - bounds[1])
                if (dY > 1.8) {
                    return AlmostBoolean.YES // dY > 1.65D || 
                }
                // TODO: Get height/length from ReflectEntity.
                if (dY < 0.1 && getHeight(player) >= 0.1) {
                    return AlmostBoolean.YES
                }
            }
        } catch (e: ReflectFailureException) {
            // Ignore.
        } catch (ne: NullPointerException) {
            // Ignore.
        }
        return AlmostBoolean.MAYBE
    } // ---- Missing (probably ok with Bukkit only) ----
    // (getCommandMap already uses reflection, but could be more speedy.).
    // getJumpAmplifier(final Player player)
    // getFasterMovementAmplifier(final Player player)
    // isComplexPart(final Entity entity) // Fails for very old builds, likely irrelevant.
    // hasGravity(Material)
}