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
import fr.neatmonster.nocheatplus.logging.Streams
import java.lang.reflect.Method
import java.util.ArrayList

/**
 * Classic Block with 6 consecutive getter methods for the shape and
 * updateShape.
 *
 * @author asofold
 */
class ReflectBlockSix(
    base: ReflectBase?, // Reference.
    private val reflectBlockPosition: ReflectBlockPosition?
) : IReflectBlock {
    /** (static)  */
    val nmsGetById: Method?
    val nmsById: Any?
    val nmsGetMaterial: Method?
    var useBlockPosition = false
    val nmsUpdateShape: Method?
    // Block bounds in the order the methods (used to) appear in the nms class.
    /** If this is null, all other nmsGetMin/Max... methods are null too.  */
    val nmsGetMinX: Method?
    val nmsGetMaxX: Method?
    val nmsGetMinY: Method?
    val nmsGetMaxY: Method?
    val nmsGetMinZ: Method?
    val nmsGetMaxZ: Method?

    init {
        val clazz = Class.forName(base!!.nmsPackageName + ".Block")
        // byID (static)
        nmsGetById = getMethod(clazz, "getById", Int::class.javaPrimitiveType)
        val blockArray: Class<*> = java.lang.reflect.Array.newInstance(clazz, 0).javaClass
        val byIdField = getField(clazz, "byId", blockArray)
        nmsById = if (byIdField == null) null else ReflectionUtil[byIdField, blockArray, null]

        // getMaterial
        nmsGetMaterial = getMethodNoArgs(clazz, "getMaterial")
        // updateShape
        var method: Method? = null
        val clazzIBlockAccess = Class.forName(base.nmsPackageName + ".IBlockAccess")
        if (reflectBlockPosition != null) {
            method = getMethod(clazz, "updateShape", clazzIBlockAccess, reflectBlockPosition.nmsClass)
        }
        if (method == null) {
            method = getMethod(
                clazz,
                "updateShape",
                clazzIBlockAccess,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            useBlockPosition = false
        } else {
            useBlockPosition = true
        }
        nmsUpdateShape = method
        // Block bounds fetching. The array uses the order the methods (used to) appear in the nms class.
        var names: Array<String?>? =
            arrayOf("getMinX", "getMaxX", "getMinY", "getMaxY", "getMinZ", "getMaxZ") // FUTURE GUESS.
        var methods = tryBoundsMethods(clazz, names)
        if (methods == null) {
            names = guessBoundsMethodNames(clazz)
            if (names != null) {
                methods = tryBoundsMethods(clazz, names)
            }
            if (methods == null) {
                methods = arrayOf(null, null, null, null, null, null)
            }
        }
        // TODO: Test which is which [ALLOW to configure and also save used ones to config, by mc version].
        // TODO: Dynamically test these ? [needs an extra world/space to place blocks inside of...]
        if (ConfigManager.getConfigFile().getBoolean(ConfPaths.LOGGING_EXTENDED_STATUS)) {
            NCPAPIProvider.getNoCheatPlusAPI().logManager.debug(
                Streams.INIT,
                "ReflectBlock: Use methods for shape: " + join(Arrays.asList(*names), ", ")
            )
        }
        nmsGetMinX = methods[0]
        nmsGetMaxX = methods[1]
        nmsGetMinY = methods[2]
        nmsGetMaxY = methods[3]
        nmsGetMinZ = methods[4]
        nmsGetMaxZ = methods[5]
    }

    /**
     *
     * @param names
     * @return null on failure, otherwise the methods in order.
     */
    private fun tryBoundsMethods(clazz: Class<*>, names: Array<String?>?): Array<Method?>? {
        val methods = arrayOfNulls<Method>(6)
        for (i in 0..5) {
            methods[i] = getMethodNoArgs(clazz, names!![i], Double::class.javaPrimitiveType)
            if (methods[i] == null) {
                return null
            }
        }
        return methods
    }

    private fun guessBoundsMethodNames(clazz: Class<*>): Array<String?>? {
        // Filter accepted method names.
        val names: MutableList<String> = ArrayList()
        for (method in clazz.methods) {
            if (method.returnType == Double::class.javaPrimitiveType && method.parameterTypes.size == 0 && possibleNames.contains(
                    method.name
                )
            ) {
                names.add(method.name)
            }
        }
        if (names.size < 6) {
            return null
        }
        // Sort in the expected order.
        Collections.sort(names) { o1, o2 -> Integer.compare(possibleNames.indexOf(o1), possibleNames.indexOf(o2)) }
        // Test for a sequence of exactly 6 consecutive entries.
        var startIndex = 0
        if (names.size > 6) {
            // Attempt to guess the start index. Currently FUTURE, as there are only six such methods.
            startIndex = -1 // Start index within list (!).
            var lastIndex = -2 // Index of a sequence within possibleNames.
            var currentStart = -1 // Possibly incomplete sequence.
            for (i in names.indices) {
                val name = names[i]
                val nameIndex = possibleNames.indexOf(name)
                if (nameIndex - lastIndex == 1) {
                    if (currentStart == -1) {
                        currentStart = nameIndex - 1
                    } else {
                        val length = nameIndex - currentStart + 1
                        if (length > 6) {
                            // Can't decide (too many).
                            return null
                        } else if (length == 6) {
                            startIndex = if (startIndex != -1) {
                                // Can't decide (too many).
                                return null
                            } else {
                                i + 1 - length
                                // (Not reset currentStart to allow detecting too long sequences.)
                            }
                        }
                    }
                } else {
                    currentStart = -1
                }
                lastIndex = nameIndex
            }
            if (startIndex == -1) {
                return null
            }
        }
        val res = arrayOfNulls<String>(6)
        for (i in 0..5) {
            res[i] = names[startIndex + i]
        }
        return res
    }

    /**
     * Quick fail with exception.
     */
    private fun fail() {
        throw ReflectFailureException()
    }

    private fun nmsBlockPosition(x: Int, y: Int, z: Int): Any? {
        if (!useBlockPosition || reflectBlockPosition!!.new_nmsBlockPosition == null) {
            fail()
        }
        val blockPos = newInstance(reflectBlockPosition!!.new_nmsBlockPosition, x, y, z)
        if (blockPos == null) {
            fail()
        }
        return blockPos
    }

    override fun nms_getByMaterial(id: Material): Any? {
        if (nmsById != null) {
            return java.lang.reflect.Array.get(nmsById, id.id)
        }
        if (nmsGetById == null) {
            fail()
        }
        return invokeMethod(nmsGetById, null, id.id)
    }

    override fun nms_getMaterial(block: Any): Any? {
        if (nmsGetMaterial == null) {
            fail()
        }
        return invokeMethodNoArgs(nmsGetMaterial, block)
    }

    fun nms_updateShape(
        block: Any?, iBlockAccess: Any?,
        x: Int, y: Int, z: Int
    ) {
        if (nmsUpdateShape == null) {
            fail()
        }
        if (useBlockPosition) {
            invokeMethod(nmsUpdateShape, block, iBlockAccess, nmsBlockPosition(x, y, z))
        } else {
            invokeMethod(nmsUpdateShape, block, iBlockAccess, x, y, z)
        }
    }

    override fun nms_fetchBounds(
        nmsWorld: Any?, nmsBlock: Any,
        x: Int, y: Int, z: Int
    ): DoubleArray? {
        nms_updateShape(nmsBlock, nmsWorld, x, y, z)
        // TODO: The methods could return null [better try-catch here].
        return doubleArrayOf(
            (invokeMethodNoArgs(nmsGetMinX, nmsBlock) as Number?)!!.toDouble(),
            (invokeMethodNoArgs(nmsGetMinY, nmsBlock) as Number?)!!.toDouble(),
            (invokeMethodNoArgs(nmsGetMinZ, nmsBlock) as Number?)!!.toDouble(),
            (invokeMethodNoArgs(nmsGetMaxX, nmsBlock) as Number?)!!.toDouble(),
            (invokeMethodNoArgs(nmsGetMaxY, nmsBlock) as Number?)!!.toDouble(),
            (invokeMethodNoArgs(nmsGetMaxZ, nmsBlock) as Number?)!!.toDouble()
        )
    }

    override val isFetchBoundsAvailable: Boolean
        get() = (nmsGetById != null || nmsById != null) && nmsUpdateShape != null && nmsGetMinX != null

    companion object {
        /** Obfuscated nms names, allowing to find the order in the source code under certain circumstances.  */
        private val possibleNames: MutableList<String> = ArrayList()

        init {
            // These might suffice for a while.
            for (c in "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
                possibleNames.add("" + fr.neatmonster.nocheatplus.compat.cbreflect.reflect.c)
            }
        }
    }
}