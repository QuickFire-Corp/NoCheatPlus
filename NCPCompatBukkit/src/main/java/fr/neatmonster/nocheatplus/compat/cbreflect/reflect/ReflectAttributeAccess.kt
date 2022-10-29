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
package fr.neatmonster.nocheatplus.compat.cbreflect.reflect

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
import org.bukkit.entity.Player
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
import org.bukkit.entity.FallingBlock
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
import org.bukkit.entity.EntityType
import fr.neatmonster.nocheatplus.compat.MCAccess
import org.bukkit.command.CommandMap
import org.bukkit.Bukkit
import fr.neatmonster.nocheatplus.compat.bukkit.BlockCacheBukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Slime
import org.bukkit.entity.Minecart
import fr.neatmonster.nocheatplus.utilities.map.MaterialUtil
import fr.neatmonster.nocheatplus.utilities.PotionUtil
import org.bukkit.potion.PotionEffectType
import org.bukkit.entity.ComplexEntityPart
import org.bukkit.entity.ComplexLivingEntity
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
import java.lang.reflect.Method

class ReflectAttributeAccess : IAttributeAccess {
    private class ReflectGenericAttributes(base: ReflectBase?) {
        val nmsMOVEMENT_SPEED: Any? = null

        init {
            val clazz = Class.forName(base!!.nmsPackageName + ".GenericAttributes")
            val clazzIAttribute = Class.forName(base.nmsPackageName + ".IAttribute")
            var nmsMOVEMENT_SPEED: Any? = null
            nmsMOVEMENT_SPEED = get_nmsMOVEMENT_SPEED("MOVEMENT_SPEED", clazz, clazzIAttribute)
            if (nmsMOVEMENT_SPEED == null) {
                nmsMOVEMENT_SPEED = get_nmsMOVEMENT_SPEED("d", clazz, clazzIAttribute)
            }
            if (nmsMOVEMENT_SPEED == null) {
                throw RuntimeException("Not available.")
            } else {
                this.nmsMOVEMENT_SPEED = nmsMOVEMENT_SPEED
            }
        }

        private fun get_nmsMOVEMENT_SPEED(fieldName: String, clazz: Class<*>, type: Class<*>): Any? {
            val field = getField(clazz, fieldName, type)
            return if (field != null) {
                ReflectionUtil[field, clazz, null]
            } else {
                null
            }
        }
    }

    private class ReflectAttributeInstance(base: ReflectBase?) {
        /** (Custom naming.)  */
        val nmsGetBaseValue: Method?
        val nmsGetValue: Method?

        /** (Custom naming.)  */
        val nmsGetAttributeModifier: Method?

        init {
            val clazz = Class.forName(base!!.nmsPackageName + ".AttributeInstance")
            // Base value.
            var method = getMethodNoArgs(clazz, "b", Double::class.javaPrimitiveType)
            if (method == null) {
                // TODO: Consider to search (as long as only two exist).
                method = getMethodNoArgs(clazz, "getBaseValue", Double::class.javaPrimitiveType)
                if (method == null) {
                    method = getMethodNoArgs(clazz, "getBase", Double::class.javaPrimitiveType)
                }
            }
            nmsGetBaseValue = method
            // Value (final value).
            method = getMethodNoArgs(clazz, "getValue", Double::class.javaPrimitiveType)
            if (method == null) {
                // TODO: Consider to search (as long as only two exist).
                method = getMethodNoArgs(clazz, "e", Double::class.javaPrimitiveType) // 1.6.1
            }
            nmsGetValue = method
            // Get AttributeModifier.
            // TODO: If name changes: scan.
            method = getMethod(clazz, "a", UUID::class.java)
            if (method == null) {
                method = getMethod(clazz, "getAttributeModifier", UUID::class.java)
                if (method == null) {
                    method = getMethod(clazz, "getModifier", UUID::class.java)
                }
            }
            nmsGetAttributeModifier = method
            if (nmsGetAttributeModifier == null || nmsGetBaseValue == null || nmsGetValue == null) {
                throw RuntimeException("Not available.")
            }
        }
    }

    private class ReflectAttributeModifier(base: ReflectBase?) {
        /** (Custom naming.)  */
        var nmsGetOperation: Method?

        /** (Custom naming.)  */
        var nmsGetValue: Method?

        init {
            val clazz = Class.forName(base!!.nmsPackageName + ".AttributeModifier")
            // TODO: Scan in a more future proof way.
            nmsGetOperation = getMethodNoArgs(clazz, "c", Int::class.javaPrimitiveType)
            nmsGetValue = getMethodNoArgs(clazz, "d", Double::class.javaPrimitiveType)
            if (nmsGetOperation == null || nmsGetValue == null) {
                throw RuntimeException("Not available.")
            }
        }
    }

    // TODO: Register each and every one of these as generic instances and fetch from there.
    private var reflectBase: ReflectBase? = null
    private var reflectGenericAttributes: ReflectGenericAttributes? = null
    private var reflectAttributeInstance: ReflectAttributeInstance? = null
    private var reflectAttributeModifier: ReflectAttributeModifier? = null
    private var reflectPlayer: ReflectPlayer? = null

    init {
        try {
            reflectBase = ReflectBase()
            var reflectAxisAlignedBB: ReflectAxisAlignedBB? = null
            try {
                reflectAxisAlignedBB = ReflectAxisAlignedBB(reflectBase)
            } catch (e: NullPointerException) {
            }
            reflectGenericAttributes = ReflectGenericAttributes(reflectBase)
            reflectAttributeInstance = ReflectAttributeInstance(reflectBase)
            reflectAttributeModifier = ReflectAttributeModifier(reflectBase)
            reflectPlayer = ReflectPlayer(reflectBase, reflectAxisAlignedBB, ReflectDamageSource(reflectBase))
        } catch (ex: ClassNotFoundException) {
            throw RuntimeException("Not available.")
        }
        if (reflectBase.nmsPackageName == null || reflectBase.obcPackageName == null || reflectPlayer.obcGetHandle == null) {
            throw RuntimeException("Not available.")
        }
    }

    override fun getSpeedAttributeMultiplier(player: Player): Double {
        return getSpeedAttributeMultiplier(player, true)
    }

    override fun getSprintAttributeMultiplier(player: Player): Double {
        return nmsAttributeInstance_getSprintAttributeModifierMultiplier(getMovementSpeedAttributeInstance(player))
    }

    /**
     * Get the speed attribute (MOVEMENT_SPEED) for a player.
     * @param handle EntityPlayer
     * @return AttributeInstance
     */
    private fun getMovementSpeedAttributeInstance(player: Player): Any? {
        return invokeMethod(
            reflectPlayer!!.nmsGetAttributeInstance,
            getHandle(player),
            reflectGenericAttributes!!.nmsMOVEMENT_SPEED
        )
    }

    /**
     *
     * @param player
     * @param removeSprint If to calculate away the sprint boost modifier.
     * @return
     */
    private fun getSpeedAttributeMultiplier(player: Player, removeSprint: Boolean): Double {
        val attributeInstance = getMovementSpeedAttributeInstance(player)
        val `val` = (invokeMethodNoArgs(
            reflectAttributeInstance!!.nmsGetValue,
            attributeInstance
        ) as Double?)!!.toDouble() / (invokeMethodNoArgs(
            reflectAttributeInstance!!.nmsGetBaseValue,
            attributeInstance
        ) as Double?)!!.toDouble()
        return if (!removeSprint) {
            `val`
        } else {
            val sprintModifier = nmsAttributeInstance_getSprintAttributeModifierMultiplier(attributeInstance)
            if (sprintModifier == 1.0) {
                `val`
            } else {
                `val` / sprintModifier
            }
        }
    }

    /**
     * (Not an existing method.)
     * @param attributeInstance
     */
    private fun nmsAttributeInstance_getSprintAttributeModifierMultiplier(attributeInstance: Any?): Double {
        val mod = invokeMethod(
            reflectAttributeInstance!!.nmsGetAttributeModifier,
            attributeInstance,
            AttribUtil.ID_SPRINT_BOOST
        )
        return if (mod == null) {
            1.0
        } else {
            AttribUtil.getMultiplier(
                (invokeMethodNoArgs(
                    reflectAttributeModifier!!.nmsGetOperation,
                    mod
                ) as Int?)!!,
                (invokeMethodNoArgs(reflectAttributeModifier!!.nmsGetValue, mod) as Double?)!!
            )
        }
    }

    private fun getHandle(player: Player): Any? {
        return invokeMethodNoArgs(reflectPlayer!!.obcGetHandle, player)
    }
}