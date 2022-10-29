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

class MCAccessBukkitModern : MCAccessBukkit() {
    protected var reflectBase: ReflectBase? = null
    protected var reflectDamageSource: ReflectDamageSource? = null
    protected var reflectLivingEntity: ReflectLivingEntity? = null
    protected val shapeModels: MutableMap<Material, BukkitShapeModel> = HashMap()

    init {
        // TODO: Generic setup via Bukkit interface existence/relations, +- fetching methods.
        BlockInit.assertMaterialExists("OAK_LOG")
        BlockInit.assertMaterialExists("CAVE_AIR")
        try {
            reflectBase = ReflectBase()
            reflectDamageSource = ReflectDamageSource(reflectBase)
            reflectLivingEntity = ReflectLivingEntity(reflectBase, null, reflectDamageSource)
        } catch (ex: ClassNotFoundException) {
        }
    }

    override fun getMCVersion(): String {
        return "1.13-1.19|?"
    }

    override fun getBlockCache(): BlockCache {
        return BlockCacheBukkitModern(shapeModels)
    }

    fun addModel(mat: Material, model: BukkitShapeModel) {
        processedBlocks.add(mat)
        shapeModels[mat] = model
    }

    override fun setupBlockProperties(worldConfigProvider: WorldConfigProvider<*>?) {
        /*
         * TODO: Incomplete blocks bounds still use workarounds(not affect movement):
         * All fences, glass pane, iron bar, chorus plant, hopper
         * 
         * Legacy blocks still use workarounds(not affect movement):
         * All fences, glass pane, iron bar, chorus plant, hopper, cauldron
         *
         * Scaffolding(affect fall dmg)
         * 
         */

        // Variables for repeated flags (Temporary flags, these should be fixed later so that they are not added here)
        val blockFix = BlockFlags.SOLID_GROUND
        // Adjust flags for individual blocks.
        BlockFlags.setBlockFlags(Material.COCOA, blockFix)
        BlockFlags.setBlockFlags(Material.TURTLE_EGG, blockFix)
        BlockFlags.setBlockFlags(Material.CHORUS_PLANT, blockFix)
        BlockFlags.setBlockFlags(Material.CREEPER_WALL_HEAD, blockFix)
        BlockFlags.setBlockFlags(Material.ZOMBIE_WALL_HEAD, blockFix)
        BlockFlags.setBlockFlags(Material.PLAYER_WALL_HEAD, blockFix)
        BlockFlags.setBlockFlags(Material.DRAGON_WALL_HEAD, blockFix)
        BlockFlags.setBlockFlags(Material.WITHER_SKELETON_WALL_SKULL, blockFix)
        BlockFlags.setBlockFlags(Material.SKELETON_WALL_SKULL, blockFix)

        // Directly keep blocks as is.
        for (mat in arrayOf(
            BridgeMaterial.COBWEB,
            BridgeMaterial.MOVING_PISTON,
            Material.SNOW,
            Material.BEACON,
            Material.VINE,
            Material.CHORUS_FLOWER
        )) {
            processedBlocks.add(mat)
        }
        for (mat in BridgeMaterial.getAllBlocks(
            "light", "glow_lichen", "big_dripleaf_stem",  // TODO: Not fully tested
            "scaffolding", "powder_snow"
        )) {
            processedBlocks.add(mat)
        }

        // Candle
        for (mat in MaterialUtil.ALL_CANDLES) {
            addModel(mat, MODEL_AUTO_FETCH)
        }

        // Amethyst
        for (mat in MaterialUtil.AMETHYST) {
            addModel(mat, MODEL_AUTO_FETCH)
        }

        // new flower, and others
        for (mat in BridgeMaterial.getAllBlocks(
            "azalea", "flowering_azalea",
            "sculk_sensor", "pointed_dripstone",
            "stonecutter", "chain"
        )) {
            addModel(mat, MODEL_AUTO_FETCH)
        }

        // Camp fire
        for (mat in BridgeMaterial.getAllBlocks(
            "campfire", "soul_campfire"
        )) {
            addModel(mat, MODEL_CAMPFIRE)
        }

        // Cauldron
        for (mat in MaterialUtil.CAULDRON) {
            BlockFlags.setBlockFlags(mat, BlockFlags.SOLID_GROUND)
            addModel(mat, MODEL_CAULDRON)
        }

        //Anvil
        for (mat in arrayOf(
            Material.ANVIL,
            Material.CHIPPED_ANVIL,
            Material.DAMAGED_ANVIL
        )) {
            addModel(mat, MODEL_ANVIL)
        }

        // Lily pad
        addModel(BridgeMaterial.LILY_PAD, MODEL_LILY_PAD)

        // End portal frame.
        addModel(BridgeMaterial.END_PORTAL_FRAME, MODEL_END_PORTAL_FRAME)

        // Cake
        addModel(BridgeMaterial.CAKE, MODEL_CAKE)

        // End Rod / Lightning Rod.
        for (mat in MaterialUtil.RODS) {
            addModel(mat, MODEL_END_ROD)
        }

        // Hoppers - min height changed in 1.13+
        addModel(Material.HOPPER, MODEL_HOPPER)

        // Ladder
        addModel(Material.LADDER, MODEL_LADDER)

        // BrewingStand
        addModel(Material.BREWING_STAND, MODEL_BREWING_STAND)

        // 1/16 inset at full height.
        addModel(Material.DRAGON_EGG, MODEL_INSET16_1_HEIGHT100)
        addModel(Material.CACTUS, MODEL_HONEY_BLOCK)

        // 1/8 height.
        for (mat in arrayOf(
            BridgeMaterial.REPEATER,
            Material.COMPARATOR
        )) {
            addModel(mat, MODEL_XZ100_HEIGHT8_1)
        }

        // 3/8 height.
        for (mat in arrayOf(
            Material.DAYLIGHT_DETECTOR
        )) {
            addModel(mat, MODEL_XZ100_HEIGHT8_3)
        }

        // 3/4 height.
        for (mat in arrayOf(
            BridgeMaterial.ENCHANTING_TABLE
        )) {
            addModel(mat, MODEL_XZ100_HEIGHT4_3)
        }
        for (mat in MaterialUtil.ALL_CANDLE_CAKE) {
            addModel(mat, MODEL_CANDLE_CAKE)
        }

        // 7/8 height.
        for (mat in arrayOf(
            Material.SOUL_SAND
        )) {
            addModel(mat, MODEL_XZ100_HEIGHT8_7)
        }

        // 16/15 height, full xz bounds.
        for (mat in arrayOf(
            BridgeMaterial.GRASS_PATH,
            BridgeMaterial.FARMLAND
        )) {
            addModel(mat, MODEL_XZ100_HEIGHT16_15)
        }

        // Thin fence: Glass panes, iron bars.
        for (mat in MaterialUtil.addBlocks(
            MaterialUtil.GLASS_PANES,
            BridgeMaterial.IRON_BARS
        )) {
            addModel(mat, MODEL_THIN_FENCE)
        }

        // Slabs
        for (mat in MaterialUtil.SLABS) {
            addModel(mat, MODEL_SLAB)
        }

        // Shulker boxes.
        for (mat in MaterialUtil.SHULKER_BOXES) {
            addModel(mat, MODEL_SHULKER_BOX)
        }

        // Chests.
        // TOOD: Might add a facing/directional extension for double chests.
        for (mat in BridgeMaterial.getAllBlocks(
            "chest", "trapped_chest", "ender_chest"
        )) {
            addModel(mat, MODEL_SINGLE_CHEST)
        }

        // Beds
        for (mat in MaterialUtil.BEDS) {
            addModel(mat, MODEL_XZ100_HEIGHT16_9)
        }

        // Flower pots.
        for (mat in MaterialUtil.FLOWER_POTS) {
            addModel(mat, MODEL_FLOWER_POT)
        }

        // Turtle Eggs.
        for (mat in arrayOf(
            Material.TURTLE_EGG
        )) {
            addModel(mat, MODEL_TURTLE_EGG)
        }

        // Conduit
        for (mat in arrayOf(
            Material.CONDUIT
        )) {
            addModel(mat, MODEL_CONDUIT)
        }

        // Cocoa
        for (mat in arrayOf(
            Material.COCOA
        )) {
            addModel(mat, MODEL_COCOA)
        }

        // Sea Pickles
        for (mat in arrayOf(
            Material.SEA_PICKLE
        )) {
            addModel(mat, MODEL_SEA_PICKLE)
        }

        // Carpets.
        for (mat in MaterialUtil.CARPETS) {
            addModel(mat, MODEL_XZ100_HEIGHT16_1)
        }

        // Ground heads.
        for (mat in MaterialUtil.HEADS_GROUND) {
            addModel(mat, MODEL_GROUND_HEAD)
        }

        // Heads on walls.
        for (mat in MaterialUtil.HEADS_WALL) {
            addModel(mat, MODEL_WALL_HEAD)
        }

        // Doors.
        for (mat in MaterialUtil.ALL_DOORS) {
            addModel(mat, MODEL_DOOR)
        }

        // Trapdoors.
        for (mat in MaterialUtil.ALL_TRAP_DOORS) {
            addModel(mat, MODEL_TRAP_DOOR)
        }

        // Chorus Plant.
        for (mat in arrayOf(
            Material.CHORUS_PLANT
        )) {
            addModel(mat, MODEL_CHORUS_PLANT)
        }

        // Lantern.
        for (mat in BridgeMaterial.getAllBlocks(
            "lantern", "soul_lantern"
        )) {
            addModel(mat, MODEL_LANTERN)
        }

        // Piston.
        for (mat in BridgeMaterial.getAllBlocks(
            "piston", "sticky_piston", "piston_base", "piston_sticky_base"
        )) {
            addModel(mat, MODEL_PISTON)
        }

        // Piston Head.
        addModel(BridgeMaterial.PISTON_HEAD, MODEL_PISTON_HEAD)

        // Snow.
        addModel(Material.SNOW, MODEL_SNOW)

        // Levelled blocks.
        for (mat in MaterialUtil.WATER) {
            addModel(mat, MODEL_LEVELLED)
        }
        for (mat in MaterialUtil.LAVA) {
            addModel(mat, MODEL_LEVELLED)
        }
        for (mat in MaterialUtil.WATER_PLANTS) {
            addModel(mat, MODEL_WATER_PLANTS)
        }

        // Rails.
        for (mat in MaterialUtil.RAILS) {
            addModel(mat, MODEL_RAIL)
        }

        // Walls.
        for (mat in MaterialUtil.ALL_WALLS) {
            BlockFlags.setBlockFlags(mat, BlockFlags.SOLID_GROUND or BlockFlags.F_VARIABLE)
            addModel(mat, MODEL_THICK_FENCE2)
        }

        // Lectern.
        var mt = BridgeMaterial.getBlock("lectern")
        if (mt != null) addModel(mt, MODEL_LECTERN)

        // Bamboo.      
        mt = BridgeMaterial.getBlock("bamboo")
        if (mt != null) addModel(mt, MODEL_BAMBOO)

        // Bell.
        mt = BridgeMaterial.getBlock("bell")
        if (mt != null) addModel(mt, MODEL_BELL)

        // Composter.
        mt = BridgeMaterial.getBlock("composter")
        if (mt != null) addModel(mt, MODEL_COMPOSTER)

        // Honey Block.
        mt = BridgeMaterial.getBlock("honey_block")
        if (mt != null) addModel(mt, MODEL_HONEY_BLOCK)

        // Big DripLeaf.
        mt = BridgeMaterial.getBlock("big_dripleaf")
        if (mt != null) addModel(mt, MODEL_DRIP_LEAF)

        // Grindstone.
        mt = BridgeMaterial.getBlock("grindstone")
        if (mt != null) addModel(mt, MODEL_GRIND_STONE)

        // Sculk Shrieker
        mt = BridgeMaterial.getBlock("sculk_shrieker")
        if (mt != null) addModel(mt, MODEL_SCULK_SHRIEKER)

        // Mud
        mt = BridgeMaterial.getBlock("mud")
        if (mt != null) addModel(mt, MODEL_XZ100_HEIGHT8_7)

        // Sort to processed by flags.
        for (mat in Material.values()) {
            val flags = BlockFlags.getBlockFlags(mat)
            // Stairs.
            if (BlockFlags.hasAnyFlag(flags, BlockFlags.F_STAIRS)) {
                addModel(mat, MODEL_STAIRS)
            }
            // Fences.
            if (BlockFlags.hasAnyFlag(flags, BlockFlags.F_THICK_FENCE)) {
                if (BlockFlags.hasAnyFlag(flags, BlockFlags.F_PASSABLE_X4)) {
                    // TODO: Perhaps another model flag.
                    addModel(mat, MODEL_GATE)
                } else {
                    addModel(mat, MODEL_THICK_FENCE)
                }
            }
        }
        super.setupBlockProperties(worldConfigProvider)
    }

    private fun getHandle(player: Player): Any? {
        // TODO: CraftPlayer check (isAssignableFrom)?
        return if (reflectLivingEntity == null || reflectLivingEntity!!.obcGetHandle == null) {
            null
        } else invokeMethodNoArgs(reflectLivingEntity!!.obcGetHandle, player)
    }

    private fun canDealFallDamage(): Boolean {
        return reflectLivingEntity != null && reflectLivingEntity!!.nmsDamageEntity != null && reflectDamageSource!!.nmsFALL != null
    }

    override fun dealFallDamageFiresAnEvent(): AlmostBoolean {
        return if (canDealFallDamage()) AlmostBoolean.YES else AlmostBoolean.NO
    }

    override fun dealFallDamage(player: Player, damage: Double) {
        if (canDealFallDamage()) {
            val handle = getHandle(player)
            if (handle != null) {
                invokeMethod(
                    reflectLivingEntity!!.nmsDamageEntity,
                    handle,
                    reflectDamageSource!!.nmsFALL,
                    damage.toFloat()
                )
            }
        } else BridgeHealth.damage(player, damage)
    }

    override fun resetActiveItem(player: Player): Boolean {
        if (reflectLivingEntity != null && reflectLivingEntity!!.nmsclearActiveItem != null) {
            val handle = getHandle(player)
            if (handle != null) {
                invokeMethodNoArgs(reflectLivingEntity!!.nmsclearActiveItem, handle)
                return true
            }
        }
        return false
    }

    companion object {
        // Blocks that can automatic fetch bounding box from API
        private val MODEL_AUTO_FETCH: BukkitShapeModel = BukkitFetchableBound()

        // Blocks that form from multi-bounds
        private val MODEL_BREWING_STAND: BukkitShapeModel = BukkitStatic( // Bottom rod
            0.0625, 0.0, 0.0625, 0.9375, 0.125, 0.9375,  // Rod
            0.4375, 0.125, 0.4375, 0.5625, 0.875, 0.5625
        )
        private val MODEL_CANDLE_CAKE: BukkitShapeModel = BukkitStatic( // Cake
            0.0625, 0.0, 0.0625, 0.9375, 0.5, 0.9375,  // Candle
            0.4375, 0.5, 0.4375, 0.5625, 0.875, 0.5625
        )
        private val MODEL_LECTERN: BukkitShapeModel = BukkitStatic( // Post
            0.0, 0.0, 0.0, 1.0, 0.125, 1.0,  // Lectern
            0.25, 0.125, 0.25, 0.75, 0.875, 0.75
        )
        private val MODEL_HOPPER: BukkitShapeModel = BukkitHopper()
        private val MODEL_CAULDRON: BukkitShapeModel = BukkitCauldron(0.1875, 0.125, 0.8125, 0.0625)
        private val MODEL_COMPOSTER: BukkitShapeModel = BukkitCauldron(0.0, 0.125, 1.0, 0.125)
        private val MODEL_PISTON_HEAD: BukkitShapeModel = BukkitPistonHead()
        private val MODEL_BELL: BukkitShapeModel = BukkitBell()
        private val MODEL_ANVIL: BukkitShapeModel = BukkitAnvil()
        private val MODEL_GRIND_STONE: BukkitShapeModel = BukkitGrindStone()

        // Blocks that change shape based on interaction or redstone.
        private val MODEL_DOOR: BukkitShapeModel = BukkitDoor()
        private val MODEL_TRAP_DOOR: BukkitShapeModel = BukkitTrapDoor()
        private val MODEL_GATE: BukkitShapeModel = BukkitGate(0.375, 1.5)
        private val MODEL_SHULKER_BOX: BukkitShapeModel = BukkitShulkerBox()
        private val MODEL_CHORUS_PLANT: BukkitShapeModel = BukkitChorusPlant()
        private val MODEL_DRIP_LEAF: BukkitShapeModel = BukkitDripLeaf()

        // Blocks with different heights based on whatever.
        private val MODEL_END_PORTAL_FRAME: BukkitShapeModel = BukkitEndPortalFrame()
        private val MODEL_SEA_PICKLE: BukkitShapeModel = BukkitSeaPickle()
        private val MODEL_COCOA: BukkitShapeModel = BukkitCocoa()
        private val MODEL_TURTLE_EGG: BukkitShapeModel = BukkitTurtleEgg()

        // Blocks that have a different shape, based on how they have been placed.
        private val MODEL_CAKE: BukkitShapeModel = BukkitCake()
        private val MODEL_SLAB: BukkitShapeModel = BukkitSlab()
        private val MODEL_STAIRS: BukkitShapeModel = BukkitStairs()
        private val MODEL_SNOW: BukkitShapeModel = BukkitSnow()
        private val MODEL_PISTON: BukkitShapeModel = BukkitPiston()
        private val MODEL_LEVELLED: BukkitShapeModel = BukkitLevelled()
        private val MODEL_LADDER: BukkitShapeModel = BukkitLadder()
        private val MODEL_RAIL: BukkitShapeModel = BukkitRail()
        private val MODEL_END_ROD: BukkitShapeModel = BukkitDirectionalCentered(0.375, 1.0, false)

        // Blocks that have a different shape with neighbor blocks (bukkit takes care though).
        private val MODEL_THIN_FENCE: BukkitShapeModel = BukkitFence(0.4375, 1.0)
        private val MODEL_THICK_FENCE: BukkitShapeModel = BukkitFence(0.375, 1.5)
        private val MODEL_THICK_FENCE2: BukkitShapeModel = BukkitWall(0.25, 1.5, 0.3125) // .75 .25 0 max: .25 .75 .5
        private val MODEL_WALL_HEAD: BukkitShapeModel = BukkitWallHead()

        // Static blocks (various height and inset values).
        private val MODEL_CAMPFIRE: BukkitShapeModel = BukkitStatic(0.0, 0.4375)
        private val MODEL_BAMBOO: BukkitShapeModel = BukkitBamboo()
        private val MODEL_WATER_PLANTS: BukkitShapeModel = BukkitWaterPlant()
        private val MODEL_LILY_PAD: BukkitShapeModel = BukkitStatic(0.09375)
        private val MODEL_FLOWER_POT: BukkitShapeModel = BukkitStatic(0.3125, 0.375)
        private val MODEL_LANTERN: BukkitShapeModel = BukkitLantern()
        private val MODEL_CONDUIT: BukkitShapeModel = BukkitStatic(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875)
        private val MODEL_GROUND_HEAD: BukkitShapeModel = BukkitStatic(0.25, 0.5)
        private val MODEL_SINGLE_CHEST: BukkitShapeModel = BukkitStatic(0.0625, 0.875)
        private val MODEL_HONEY_BLOCK: BukkitShapeModel = BukkitStatic(0.0625, 0.9375)
        private val MODEL_SCULK_SHRIEKER: BukkitShapeModel = BukkitStatic(0.0, 0.5)

        // Static blocks with full height sorted by inset.
        private val MODEL_INSET16_1_HEIGHT100: BukkitShapeModel = BukkitStatic(0.0625, 1.0)

        // Static blocks with full xz-bounds sorted by height.
        private val MODEL_XZ100_HEIGHT16_1: BukkitShapeModel = BukkitStatic(0.0625)
        private val MODEL_XZ100_HEIGHT8_1: BukkitShapeModel = BukkitStatic(0.125)
        private val MODEL_XZ100_HEIGHT8_3: BukkitShapeModel = BukkitStatic(0.375)
        private val MODEL_XZ100_HEIGHT16_9: BukkitShapeModel = BukkitStatic(0.5625)
        private val MODEL_XZ100_HEIGHT4_3: BukkitShapeModel = BukkitStatic(0.75)
        private val MODEL_XZ100_HEIGHT16_15: BukkitShapeModel = BukkitStatic(0.9375)
        private val MODEL_XZ100_HEIGHT8_7: BukkitShapeModel = BukkitStatic(0.875)
    }
}