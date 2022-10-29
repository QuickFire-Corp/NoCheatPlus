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
package fr.neatmonster.nocheatplus.compat.bukkit.model

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
import org.bukkit.block.data.MultipleFacing
import org.bukkit.block.data.Bisected.Half
import java.util.stream.IntStream
import java.util.function.IntFunction
import java.lang.IllegalArgumentException
import org.bukkit.block.data.type.BigDripleaf.Tilt
import fr.neatmonster.nocheatplus.utilities.map.BlockFlags
import org.bukkit.block.data.Waterlogged
import org.bukkit.block.data.Levelled
import fr.neatmonster.nocheatplus.compat.bukkit.model.BukkitTrapDoor
import org.bukkit.block.data.FaceAttachable.AttachedFace
import fr.neatmonster.nocheatplus.compat.bukkit.model.ShapeModel
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
import fr.neatmonster.nocheatplus.utilities.collision.Axis
import org.bukkit.block.data.type.*
import java.util.ArrayList

class BukkitStairs : BukkitShapeModel {
    // Taken from NMS - StairBlock.java
    private val topslabs = doubleArrayOf(0.0, 0.5, 0.0, 1.0, 1.0, 1.0)
    private val bottomslabs = doubleArrayOf(0.0, 0.0, 0.0, 1.0, 0.5, 1.0)
    private val octet_nnn = doubleArrayOf(0.0, 0.0, 0.0, 0.5, 0.5, 0.5)
    private val octet_nnp = doubleArrayOf(0.0, 0.0, 0.5, 0.5, 0.5, 1.0)
    private val octet_pnn = doubleArrayOf(0.5, 0.0, 0.0, 1.0, 0.5, 0.5)
    private val octet_pnp = doubleArrayOf(0.5, 0.0, 0.5, 1.0, 0.5, 1.0)
    private val octet_npn = doubleArrayOf(0.0, 0.5, 0.0, 0.5, 1.0, 0.5)
    private val octet_npp = doubleArrayOf(0.0, 0.5, 0.5, 0.5, 1.0, 1.0)
    private val octet_ppn = doubleArrayOf(0.5, 0.5, 0.0, 1.0, 1.0, 0.5)
    private val octet_ppp = doubleArrayOf(0.5, 0.5, 0.5, 1.0, 1.0, 1.0)
    private val top_stairs = makeshape(topslabs, octet_nnn, octet_pnn, octet_nnp, octet_pnp)
    private val bottom_stairs = makeshape(bottomslabs, octet_npn, octet_ppn, octet_npp, octet_ppp)
    private val shape_by_state = intArrayOf(12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8)
    override fun getShape(
        blockCache: BlockCache?,
        world: World, x: Int, y: Int, z: Int
    ): DoubleArray? {
        val block = world.getBlockAt(x, y, z)
        val blockData = block.state.blockData
        if (blockData is Stairs) {
            val stairs = blockData
            val half = stairs.half
            return when (half) {
                Half.BOTTOM -> bottom_stairs[shape_by_state[getShapeStateIndex(stairs)]]
                Half.TOP -> top_stairs[shape_by_state[getShapeStateIndex(stairs)]]
            }
        }
        return doubleArrayOf(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    }

    override fun getFakeData(
        blockCache: BlockCache?,
        world: World, x: Int, y: Int, z: Int
    ): Int {
        //final Block block = world.getBlockAt(x, y, z);
        //final BlockState state = block.getState();
        //final BlockData blockData = state.getBlockData();

        //if (blockData instanceof Bisected) {
        //    final Bisected stairs = (Bisected) blockData;
        //    final Half half = stairs.getHalf();
        //    switch (half) {
        //        case TOP:
        //            return 0x4;
        //        default:
        //            break;
        //    }
        //}
        return 0
    }

    private fun makeshape(
        slab: DoubleArray,
        octet_nn: DoubleArray, octet_pn: DoubleArray, octet_np: DoubleArray, octet_pp: DoubleArray
    ): Array<DoubleArray> {
        return IntStream
            .range(0, 16)
            .mapToObj { flags: Int -> makeStairShape(flags, slab, octet_nn, octet_pn, octet_np, octet_pp) }
            .toArray { _Dummy_.__Array__() }
    }

    private fun makeStairShape(
        flags: Int, slab: DoubleArray,
        octet_nn: DoubleArray, octet_pn: DoubleArray, octet_np: DoubleArray, octet_pp: DoubleArray
    ): DoubleArray {
        var res = slab
        if (flags and 1 != 0) {
            res = merge(res, octet_nn)
        }
        if (flags and 2 != 0) {
            res = merge(res, octet_pn)
        }
        if (flags and 4 != 0) {
            res = merge(res, octet_np)
        }
        if (flags and 8 != 0) {
            res = merge(res, octet_pp)
        }
        return res
    }

    private fun getShapeStateIndex(stair: Stairs): Int {
        return stair.shape.ordinal * 4 + directionToValue(stair.facing)
    }

    private fun directionToValue(face: BlockFace): Int {
        return when (face) {
            BlockFace.UP, BlockFace.DOWN -> -1
            BlockFace.NORTH -> 2
            BlockFace.SOUTH -> 0
            BlockFace.WEST -> 1
            BlockFace.EAST -> 3
            else -> -1
        }
    }

    // TODO: Poorly designed, Will recode better version later
    private fun merge(bounds: DoubleArray, octet: DoubleArray): DoubleArray {
        val minX = octet[0]
        val minY = octet[1]
        val minZ = octet[2]
        val maxX = octet[3]
        val maxY = octet[4]
        val maxZ = octet[5]
        for (i in 2..bounds.size / 6) {
            val tminX = bounds[i * 6 - 6]
            val tminY = bounds[i * 6 - 5]
            val tminZ = bounds[i * 6 - 4]
            val tmaxX = bounds[i * 6 - 3]
            val tmaxY = bounds[i * 6 - 2]
            val tmaxZ = bounds[i * 6 - 1]
            if (sameshape(
                    minX, minY, minZ, maxX, maxY, maxZ,
                    tminX, tminY, tminZ, tmaxX, tmaxY, tmaxZ
                )
            ) {
                val a = getRelative(
                    minX, minY, minZ, maxX, maxY, maxZ,
                    tminX, tminY, tminZ, tmaxX, tmaxY, tmaxZ
                )
                if (a.size == 1) {
                    val axis = a[0]
                    when (axis) {
                        Axis.X_AXIS -> {
                            bounds[i * 6 - 6] = Math.min(tminX, minX)
                            bounds[i * 6 - 3] = Math.max(tmaxX, maxX)
                            return bounds
                        }
                        Axis.Z_AXIS -> {
                            bounds[i * 6 - 4] = Math.min(tminZ, minZ)
                            bounds[i * 6 - 1] = Math.max(tmaxZ, maxZ)
                            return bounds
                        }
                        else -> {}
                    }
                }
            }
        }
        return add(bounds, octet)
    }

    private fun add(array1: DoubleArray, array2: DoubleArray): DoubleArray {
        val newArray = DoubleArray(array1.size + array2.size)
        System.arraycopy(array1, 0, newArray, 0, array1.size)
        System.arraycopy(array2, 0, newArray, array1.size, array2.size)
        return newArray
    }

    private fun getRelative(
        minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double,
        tminX: Double, tminY: Double, tminZ: Double, tmaxX: Double, tmaxY: Double, tmaxZ: Double
    ): List<Axis> {
        val list: MutableList<Axis> = ArrayList()
        if (minX == tmaxX || maxX == tminX) {
            list.add(Axis.X_AXIS)
        }
        if (minY == tmaxY || maxY == tminY) {
            list.add(Axis.Y_AXIS)
        }
        if (minZ == tmaxZ || maxZ == tminZ) {
            list.add(Axis.Z_AXIS)
        }
        return list
    }

    private fun sameshape(
        minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double, tminX: Double,
        tminY: Double, tminZ: Double, tmaxX: Double, tmaxY: Double, tmaxZ: Double
    ): Boolean {
        val dx = maxX - minX
        val dy = maxY - minY
        val dz = maxZ - minZ
        val tdx = tmaxX - tminX
        val tdy = tmaxY - tminY
        val tdz = tmaxZ - tminZ
        return dx == tdx && dy == tdy && dz == tdz
    }
}