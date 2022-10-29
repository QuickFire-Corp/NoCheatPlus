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

/**
 * More handy high level methods throwing one type of exception.
 * @author asofold
 */
class ReflectHelper {
    // TODO: Many possible exceptions are not yet caught (...).
    // TODO: Some places: should actually try-catch and fail() instead of default values and null return values.
    /** Failure to use / apply [ / setup ? ].  */
    class ReflectFailureException : RuntimeException {
        constructor() : super() {}
        constructor(ex: ClassNotFoundException?) : super(ex) {} // TODO: Might add a sub-error enum/code/thing support.

        companion object {
            /**
             *
             */
            private const val serialVersionUID = -3934791920291782604L
        }
    }

    protected var reflectBase: ReflectBase? = null
    protected val reflectAxisAlignedBB: ReflectAxisAlignedBB? = null
    protected val reflectBlockPosition: ReflectBlockPosition? = null
    protected var reflectBlock: IReflectBlock? = null
    protected var reflectMaterial: ReflectMaterial? = null
    protected var reflectWorld: ReflectWorld? = null
    protected var reflectDamageSource: ReflectDamageSource? = null
    protected var reflectEntity: ReflectEntity? = null
    protected var reflectLivingEntity: ReflectEntity? = null
    protected var reflectPlayer: ReflectPlayer? = null
    private val tempBounds = DoubleArray(6)

    init {
        // TODO: Store one instance of ReflectFailureException?
        // TODO: Allow some more to not work?
        try {
            reflectBase = ReflectBase()
            var reflectAxisAlignedBB: ReflectAxisAlignedBB? = null
            try {
                reflectAxisAlignedBB = ReflectAxisAlignedBB(reflectBase)
            } catch (ex1: NullPointerException) {
            }
            this.reflectAxisAlignedBB = reflectAxisAlignedBB
            var reflectBlockPosition: ReflectBlockPosition? = null
            try {
                reflectBlockPosition = ReflectBlockPosition(reflectBase)
            } catch (ex: ClassNotFoundException) {
            }
            this.reflectBlockPosition = reflectBlockPosition
            reflectMaterial = ReflectMaterial(reflectBase)
            reflectWorld = ReflectWorld(reflectBase, reflectMaterial, reflectBlockPosition)
            var reflectBlockLatest: ReflectBlock? = null
            try {
                reflectBlockLatest = ReflectBlock(
                    reflectBase, this.reflectBlockPosition,
                    reflectMaterial, reflectWorld
                )
            } catch (t: Throwable) {
            }
            if (reflectBlockLatest == null) {
                // More lenient constructor.
                reflectBlock = ReflectBlockSix(reflectBase, this.reflectBlockPosition)
            } else {
                reflectBlock = reflectBlockLatest
            }
            reflectDamageSource = ReflectDamageSource(reflectBase)
            reflectEntity = ReflectEntity(reflectBase, this.reflectAxisAlignedBB, reflectDamageSource)
            reflectLivingEntity = ReflectLivingEntity(reflectBase, this.reflectAxisAlignedBB, reflectDamageSource)
            reflectPlayer = ReflectPlayer(reflectBase, this.reflectAxisAlignedBB, reflectDamageSource)
        } catch (ex: ClassNotFoundException) {
            throw ReflectFailureException(ex)
        }
        if (ConfigManager.getConfigFile().getBoolean(ConfPaths.LOGGING_EXTENDED_STATUS)) {
            val parts: MutableList<String?> = LinkedList()
            for (rootField in this.javaClass.declaredFields) {
                if (rootField.isAnnotationPresent(MostlyHarmless::class.java)) {
                    continue
                }
                val accessible = rootField.isAccessible
                if (!accessible) {
                    rootField.isAccessible = true
                }
                val obj = ReflectionUtil[rootField, this, null]
                if (obj == null) {
                    parts.add("(Not available: " + rootField.name + ")")
                    continue
                } else if (rootField.name.startsWith("reflect")) {
                    val clazz: Class<*> = obj.javaClass
                    // TODO: Skip attributes silently before 1.6.1 (and not unknown version).
                    for (field in clazz.fields) {
                        if (field.isAnnotationPresent(MostlyHarmless::class.java)) {
                            continue
                        }
                        if (ReflectionUtil[field, obj, null] == null) {
                            parts.add(clazz.name + "." + field.name)
                        }
                    }
                }
                if (!accessible) {
                    rootField.isAccessible = false
                }
            }
            if (!reflectBlock.isFetchBoundsAvailable) {
                parts.add("fetch-block-shape")
            }
            if (!parts.isEmpty()) {
                parts.add(0, "CompatCBReflect: The following properties could not be set:")
                NCPAPIProvider.getNoCheatPlusAPI().logManager.warning(Streams.INIT, join(parts, "\n"))
            }
        }
    }

    /**
     * Quick fail with exception. Used both for setup and runtime.
     * @throws ReflectFailureException Always.
     */
    private fun fail() {
        throw ReflectFailureException()
    }

    fun getHandle(player: Player?): Any? {
        // TODO: CraftPlayer check (isAssignableFrom)?
        if (reflectPlayer!!.obcGetHandle == null) {
            fail()
        }
        val handle = invokeMethodNoArgs(reflectPlayer.obcGetHandle, player)
        if (handle == null) {
            fail()
        }
        return handle
    }

    fun nmsPlayer_getHealth(handle: Any?): Double {
        if (reflectPlayer!!.nmsGetHealth == null) {
            fail()
        }
        return (invokeMethodNoArgs(reflectPlayer.nmsGetHealth, handle) as Number?)!!.toDouble()
    }

    fun nmsPlayer_dead(handle: Any?): Boolean {
        if (reflectPlayer!!.nmsDead == null) {
            fail()
        }
        return getBoolean(reflectPlayer.nmsDead, handle, true)
    }

    /**
     * Set the value for the dead field.
     * @param handle
     * @param value
     */
    fun nmsPlayer_dead(handle: Any?, value: Boolean) {
        if (reflectPlayer!!.nmsDead == null || !set(reflectPlayer.nmsDead, handle, value)) {
            fail()
        }
    }

    /**
     * Set the value for the dead field.
     * @param handle
     * @param value
     */
    fun nmsPlayer_deathTicks(handle: Any?, value: Int) {
        if (reflectPlayer!!.nmsDeathTicks == null || !set(reflectPlayer.nmsDeathTicks, handle, value)) {
            fail()
        }
    }

    fun canDealFallDamage(): Boolean {
        return reflectPlayer!!.nmsDamageEntity != null && reflectDamageSource!!.nmsFALL != null
    }

    fun dealFallDamage(player: Player?, damage: Double) {
        if (reflectDamageSource!!.nmsFALL == null) {
            fail()
        }
        val handle = getHandle(player)
        nmsPlayer_dealDamage(handle, reflectDamageSource.nmsFALL, damage)
    }

    fun nmsPlayer_dealDamage(handle: Any?, damage_source: Any?, damage: Double) {
        if (reflectPlayer!!.nmsDamageEntity == null) {
            fail()
        }
        if (reflectPlayer.nmsDamageEntityInt) {
            invokeMethod(reflectPlayer.nmsDamageEntity, handle, damage_source, damage.toInt())
        } else {
            invokeMethod(reflectPlayer.nmsDamageEntity, handle, damage_source, damage.toFloat())
        }
    }

    fun getInvulnerableTicks(player: Player): Int {
        if (reflectPlayer!!.nmsInvulnerableTicks == null) {
            fail()
        }
        val handle = getHandle(player)
        return getInt(reflectPlayer.nmsInvulnerableTicks, handle, player.noDamageTicks / 2)
    }

    fun setInvulnerableTicks(player: Player?, ticks: Int) {
        if (reflectPlayer!!.nmsInvulnerableTicks == null) {
            fail()
        }
        val handle = getHandle(player)
        if (!set(reflectPlayer.nmsInvulnerableTicks, handle, ticks)) {
            fail()
        }
    }

    fun getHandle(world: World?): Any? {
        if (reflectWorld!!.obcGetHandle == null) {
            fail()
        }
        val handle = invokeMethodNoArgs(reflectWorld.obcGetHandle, world)
        if (handle == null) {
            fail()
        }
        return handle
    }

    fun nmsBlockPosition(x: Int, y: Int, z: Int): Any? {
        if (reflectBlockPosition!!.new_nmsBlockPosition == null) {
            fail()
        }
        val blockPos = newInstance(reflectBlockPosition.new_nmsBlockPosition, x, y, z)
        if (blockPos == null) {
            fail()
        }
        return blockPos
    }

    /**
     *
     * @param id
     * @return Block instance (could be null).
     */
    fun nmsBlock_getByMaterial(id: Material): Any? {
        if (reflectBlock == null) {
            fail()
        }
        return reflectBlock!!.nms_getByMaterial(id)
    }

    fun nmsBlock_getMaterial(block: Any): Any? {
        if (reflectBlock == null) {
            fail()
        }
        return reflectBlock!!.nms_getMaterial(block)
    }

    fun nmsMaterial_isSolid(material: Any?): Boolean {
        if (reflectMaterial!!.nmsIsSolid == null) {
            fail()
        }
        return invokeMethodNoArgs(reflectMaterial.nmsIsSolid, material) as Boolean
    }

    fun nmsMaterial_isLiquid(material: Any?): Boolean {
        if (reflectMaterial!!.nmsIsLiquid == null) {
            fail()
        }
        return invokeMethodNoArgs(reflectMaterial.nmsIsLiquid, material) as Boolean
    }

    fun isBlockSolid(id: Material): AlmostBoolean {
        var obj: Any? = nmsBlock_getByMaterial(id) ?: return AlmostBoolean.MAYBE
        obj = nmsBlock_getMaterial(obj)
        return if (obj == null) {
            AlmostBoolean.MAYBE
        } else AlmostBoolean.match(nmsMaterial_isSolid(obj))
    }

    fun isBlockLiquid(id: Material): AlmostBoolean {
        var obj: Any? = nmsBlock_getByMaterial(id) ?: return AlmostBoolean.MAYBE
        obj = nmsBlock_getMaterial(obj)
        return if (obj == null) {
            AlmostBoolean.MAYBE
        } else AlmostBoolean.match(nmsMaterial_isLiquid(obj))
    }

    /**
     * Fetch the block shape for the given position in the given nms-world. (Not
     * a method in world types.)
     *
     * @param nmsWorld
     * @param id
     * @param x
     * @param y
     * @param z
     * @return double[6] minX, minY, minZ, maxX, maxY, maxZ. Returns null for
     * cases like air/unspecified.
     */
    fun nmsWorld_fetchBlockShape(
        nmsWorld: Any?,
        id: Material, x: Int, y: Int, z: Int
    ): DoubleArray? {
        if (reflectBlock == null) {
            fail()
        }
        val nmsBlock = nmsBlock_getByMaterial(id) ?: return null
        return reflectBlock!!.nms_fetchBounds(nmsWorld, nmsBlock, x, y, z)
    }

    fun getWidth(entity: Entity): Double {
        var width = -16f
        if (reflectEntity!!.nmsWidth == null) {
            fail()
        }
        val handle = reflectEntity.getHandle(entity)
        if (handle != null) {
            width = getFloat(reflectEntity.nmsWidth, handle, width)
        }
        if (width < 0f) {
            fail()
        }
        return width.toDouble()
    }

    fun getHeight(entity: Entity): Double {
        var floatHeight = -16f
        val handle = reflectEntity!!.getHandle(entity) // TODO: Distinguish classes (living vs not)?
        var height: Double
        if (handle == null) {
            fail()
        }
        if (reflectEntity.nmsLength != null) {
            floatHeight = Math.max(getFloat(reflectEntity.nmsLength, handle, floatHeight), floatHeight)
        }
        if (reflectEntity.nmsHeight != null) {
            floatHeight = Math.max(getFloat(reflectEntity.nmsHeight, handle, floatHeight), floatHeight)
        }
        height = floatHeight.toDouble()
        // TODO: Consider dropping the box for performance?
        if (reflectAxisAlignedBB != null && reflectEntity.nmsGetBoundingBox != null) {
            val box = invokeMethodNoArgs(reflectEntity.nmsGetBoundingBox, handle)
            if (box != null) {
                // mcEntity.boundingBox.e - mcEntity.boundingBox.b
                val y2 = getDouble(reflectAxisAlignedBB.nms_maxY, box, Double.MAX_VALUE)
                val y1 = getDouble(reflectAxisAlignedBB.nms_minY, box, Double.MAX_VALUE)
                if (y1 != Double.MAX_VALUE && y2 != Double.MAX_VALUE) {
                    height = Math.max(y2 - y1, height)
                }
            }
        }
        if (height < 0.0) {
            fail()
        }
        // On success only: Check eye height (MCAccessBukkit is better than just eye height.).
        if (entity is LivingEntity) {
            height = Math.max(height, entity.eyeHeight)
        }
        return height
    }

    /**
     * Fetch the bounding box.
     *
     * @param entity
     * @return A new double array {minX, minY, minZ, maxX, maxY, maxZ}. Not to
     * be stored etc.
     * @throws ReflectFailureException
     * On failure to fetch bounds.
     */
    fun getBounds(entity: Entity): DoubleArray {
        return getBounds(entity, DoubleArray(6))
    }

    /**
     * Fetch the bounding box.
     *
     * @param entity
     * @return The internally stored double array for bounds {minX, minY, minZ,
     * maxX, maxY, maxZ}. Not to be stored etc.
     * @throws ReflectFailureException
     * On failure to fetch bounds.
     */
    fun getBoundsTemp(entity: Entity): DoubleArray {
        return getBounds(entity, tempBounds)
    }

    /**
     * Fetch the bounding box.
     *
     * @param entity
     * @param bounds
     * The double[6+] array, which to fill values in to.
     * @return The passed bounds array filled with {minX, minY, minZ, maxX,
     * maxY, maxZ}.
     * @throws ReflectFailureException
     * On failure to fetch bounds.
     */
    fun getBounds(entity: Entity, bounds: DoubleArray): DoubleArray {
        // TODO: Also fetch for legacy versions?
        if (reflectAxisAlignedBB == null || reflectEntity == null) {
            fail()
        }
        val aabb = invokeMethodNoArgs(reflectEntity!!.nmsGetBoundingBox, reflectEntity.getHandle(entity))
        if (aabb == null) {
            fail()
        }
        reflectAxisAlignedBB!!.fillInValues(aabb, bounds)
        return bounds
    }
}