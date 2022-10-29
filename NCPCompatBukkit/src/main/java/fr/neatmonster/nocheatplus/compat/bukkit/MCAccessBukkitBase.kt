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
package fr.neatmonster.nocheatplus.compat.bukkit

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
import org.bukkit.entity.*

open class MCAccessBukkitBase : MCAccess {
    // private AlmostBoolean entityPlayerAvailable = AlmostBoolean.MAYBE;
    protected val bukkitHasGetHeightAndGetWidth: Boolean

    /**
     * Fill in already initialized blocks, to return false for guessItchyBlock.
     */
    protected val processedBlocks: Set<Material> = LinkedHashSet()

    /**
     * Constructor to let it fail.
     */
    init {
        // TODO: Add more that might fail if not supported ?
        testItchyBlock()
        // TODO: Deactivate checks that might not work. => MCAccess should have availability method, NCP deactivates check on base of that.
        // TODO: Move getHeight and the like to EntityAccessXY.
        bukkitHasGetHeightAndGetWidth =
            (getMethodNoArgs(Entity::class.java, "getHeight", Double::class.javaPrimitiveType) != null
                    && getMethodNoArgs(Entity::class.java, "getWidth", Double::class.javaPrimitiveType) != null)
    }

    private fun guessItchyBlockPre1_13(mat: Material): Boolean {
        return !mat.isOccluding || !mat.isSolid || mat.isTransparent
    }

    protected fun guessItchyBlock(mat: Material): Boolean {
        // General considerations first.
        if (processedBlocks.contains(mat)
            || BlockProperties.isAir(mat) || BlockProperties.isLiquid(mat)
        ) {
            return false
        }
        // Fully solid/ground blocks.
        val flags = BlockFlags.getBlockFlags(mat)
        /*
         * Skip fully passable blocks (partially passable blocks may be itchy,
         * though slabs will be easy to handle).
         */if (BlockFlags.hasAnyFlag(flags, BlockFlags.F_IGN_PASSABLE)) {
            // TODO: Blocks with min_height may actually be ok, if xz100 and some height are set.
            return if (BlockFlags.hasNoFlags(
                    flags,
                    BlockFlags.F_GROUND_HEIGHT
                            or BlockFlags.F_GROUND
                            or BlockFlags.F_SOLID
                )
            ) {
                // Explicitly passable.
                false
            } else {
                // Potentially itchy.
                true
            }
        }
        val testFlags1 = BlockFlags.F_SOLID or BlockFlags.F_XZ100
        val testFlags2 = BlockFlags.F_HEIGHT100
        return if (BlockFlags.hasAllFlags(flags, testFlags1) && BlockFlags.hasAnyFlag(flags, testFlags2)) {
            // Solid blocks with explicitly set bounds.
            false
        } else guessItchyBlockPre1_13(
            mat
        )

        // TODO: Use working route.
    }

    private fun testItchyBlock() {
        // TODO: Route to what works.
        guessItchyBlockPre1_13(Material.AIR)
    }

    override fun getMCVersion(): String {
        // Bukkit API. Versions lower than MC 1.13 have their own module.
        // TODO: maybe output something else.
        return "1.13-1.19|?" // uh oh
    }

    override fun getServerVersionTag(): String {
        return "Bukkit-API"
    }

    override fun getCommandMap(): CommandMap {
        return try {
            invokeMethodNoArgs(Bukkit.getServer(), "getCommandMap") as CommandMap
        } catch (t: Throwable) {
            // Nasty.
            null
        }
    }

    override fun getBlockCache(): BlockCache {
        return getBlockCache(null)
    }

    override fun getBlockCache(world: World): BlockCache {
        return BlockCacheBukkit(world)
    }

    override fun getHeight(entity: Entity): Double {
        val entityHeight: Double
        entityHeight = if (bukkitHasGetHeightAndGetWidth) {
            entity.height
        } else {
            1.0
        }
        return if (entity is LivingEntity) {
            Math.max(entity.eyeHeight, entityHeight)
        } else {
            entityHeight
        }
    }

    override fun isBlockSolid(mat: Material): AlmostBoolean {
        return if (mat == null) {
            AlmostBoolean.MAYBE
        } else {
            AlmostBoolean.match(mat.isSolid)
        }
    }

    private fun legacyGetWidth(entity: Entity): Double {
        // TODO: Make readable from file for defaults + register individual getters where appropriate.
        // TODO: For height too. [Automatize most by spawning + checking?]
        // Values taken from 1.7.10.
        val type = entity.type
        when (type) {
            EntityType.ENDER_SIGNAL, EntityType.FIREWORK, EntityType.FISHING_HOOK, EntityType.DROPPED_ITEM, EntityType.SNOWBALL -> return 0.25
            EntityType.CHICKEN, EntityType.SILVERFISH -> return 0.3f
            EntityType.SMALL_FIREBALL, EntityType.WITHER_SKULL -> return 0.3125f
            EntityType.GHAST, EntityType.SNOWMAN -> return 0.4f
            EntityType.ARROW, EntityType.BAT, EntityType.EXPERIENCE_ORB, EntityType.ITEM_FRAME, EntityType.PAINTING -> return 0.5f
            EntityType.PLAYER, EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.OCELOT, EntityType.BLAZE, EntityType.VILLAGER, EntityType.WITCH, EntityType.WOLF -> return 0.6f // (Default entity width.)
            EntityType.CAVE_SPIDER -> return 0.7f
            EntityType.COW, EntityType.MUSHROOM_COW, EntityType.PIG, EntityType.SHEEP, EntityType.WITHER -> return 0.9f
            EntityType.SQUID -> return 0.95f
            EntityType.PRIMED_TNT -> return 0.98f
            EntityType.FIREBALL -> return 1.0f
            EntityType.IRON_GOLEM, EntityType.SPIDER -> return 1.4f
            EntityType.BOAT -> return 1.5f
            EntityType.ENDER_CRYSTAL -> return 2.0f
            EntityType.GIANT -> return 3.6f // (Better than nothing.)
            EntityType.ENDER_DRAGON -> return 16.0f
            EntityType.SLIME, EntityType.MAGMA_CUBE -> if (entity is Slime) {
                // setSize(i): this.a(0.6F * (float) i, 0.6F * (float) i);
                return (0.6f * entity.size).toDouble()
            }
            else -> {}
        }
        // Check by instance for minecarts (too many).
        if (entity is Minecart) {
            return 0.98f // this.a(0.98F, 0.7F);
        }
        // Latest Bukkit API.
        try {
            when (type) {
                EntityType.LEASH_HITCH -> return 0.5f
                EntityType.HORSE -> return 1.4f
                EntityType.ENDERMITE -> return 0.4f
                EntityType.ARMOR_STAND -> return 0.5f
                EntityType.RABBIT -> return 0.6f
                EntityType.GUARDIAN -> return 0.95f
                else -> {}
            }
        } catch (t: Throwable) {
        }
        // Default entity width.
        return 0.6f
    }

    override fun getWidth(entity: Entity): Double {
        return if (bukkitHasGetHeightAndGetWidth) {
            entity.width
        } else {
            legacyGetWidth(entity)
        }
    }

    override fun isBlockLiquid(mat: Material): AlmostBoolean {
        return if (mat == null) {
            AlmostBoolean.MAYBE
        } else if (MaterialUtil.WATER.contains(mat) || MaterialUtil.LAVA.contains(mat)) {
            AlmostBoolean.YES
        } else {
            AlmostBoolean.NO
        }
    }

    override fun isIllegalBounds(player: Player): AlmostBoolean {
        if (player.isDead) {
            return AlmostBoolean.NO
        }
        if (!player.isSleeping) { // TODO: ignored sleeping ?
            // TODO: This can test like ... nothing !
            // (Might not be necessary.)
        }
        return AlmostBoolean.MAYBE
    }

    override fun getJumpAmplifier(player: Player): Double {
        return PotionUtil.getPotionEffectAmplifier(player, PotionEffectType.JUMP)
    }

    override fun getFasterMovementAmplifier(player: Player): Double {
        return PotionUtil.getPotionEffectAmplifier(player, PotionEffectType.SPEED)
    }

    override fun getInvulnerableTicks(player: Player): Int {
        return Int.MAX_VALUE // NOT SUPPORTED.
    }

    override fun setInvulnerableTicks(player: Player, ticks: Int) {
        // IGNORE.
    }

    override fun dealFallDamage(player: Player, damage: Double) {
        // TODO: Document in knowledge base.
        // TODO: Account for armor, other.
        // TODO: use setLastDamageCause here ?
        BridgeHealth.damage(player, damage)
    }

    override fun isComplexPart(entity: Entity): Boolean {
        return entity is ComplexEntityPart || entity is ComplexLivingEntity
    }

    override fun shouldBeZombie(player: Player): Boolean {
        // Not sure :) ...
        return BridgeHealth.getHealth(player) <= 0.0 && !player.isDead
    }

    override fun setDead(player: Player, deathTicks: Int) {
        // TODO: Test / kick ? ...
        BridgeHealth.setHealth(player, 0.0)
        // TODO: Might try stuff like setNoDamageTicks.
        BridgeHealth.damage(player, 1.0)
    }

    override fun hasGravity(mat: Material): Boolean {
        return try {
            mat.hasGravity()
        } catch (t: Throwable) {
            // Backwards compatibility.
            when (mat) {
                Material.SAND, Material.GRAVEL -> true
                else -> false
            }
        }
    }

    override fun dealFallDamageFiresAnEvent(): AlmostBoolean {
        return AlmostBoolean.NO // Assumption.
    }

    override fun resetActiveItem(player: Player): Boolean {
        return false // Assumption.
    } //  @Override
    //  public void correctDirection(Player player) {
    //      // TODO: Consider using reflection (detect CraftPlayer, access EntityPlayer + check if possible (!), use flags for if valid or invalid.)
    //  }
}