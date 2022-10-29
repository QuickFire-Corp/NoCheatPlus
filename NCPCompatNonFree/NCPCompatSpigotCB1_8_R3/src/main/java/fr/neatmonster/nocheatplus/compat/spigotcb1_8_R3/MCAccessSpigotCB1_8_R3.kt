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
package fr.neatmonster.nocheatplus.compat.spigotcb1_8_R3

import org.bukkit.Bukkit

class MCAccessSpigotCB1_8_R3 : MCAccess {
    /**
     * Test for availability in constructor.
     */
    init {
        commandMap
        ReflectionUtil.checkMembers("net.minecraft.server.v1_8_R3.", arrayOf("Entity", "length", "width", "locY"))
        ReflectionUtil.checkMembers(
            "net.minecraft.server.v1_8_R3.",
            arrayOf("EntityPlayer", "dead", "deathTicks", "invulnerableTicks")
        )
        // block bounds, original: minX, maxX, minY, maxY, minZ, maxZ
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            net.minecraft.server.v1_8_R3.EntityLiving::class.java,
            arrayOf("getHeadHeight"),
            Float::class.javaPrimitiveType
        )
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            net.minecraft.server.v1_8_R3.EntityPlayer::class.java,
            arrayOf("getHealth"),
            Float::class.javaPrimitiveType
        )
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            net.minecraft.server.v1_8_R3.Block::class.java,
            arrayOf("B", "C", "D", "E", "F", "G"),
            Double::class.javaPrimitiveType
        )
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            AttributeInstance::class.java,
            arrayOf("b"),
            Double::class.javaPrimitiveType
        )
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            AttributeModifier::class.java,
            arrayOf("c"),
            Int::class.javaPrimitiveType
        )
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            AttributeModifier::class.java,
            arrayOf("d"),
            Double::class.javaPrimitiveType
        )
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            Material::class.java,
            arrayOf("isSolid", "isLiquid"),
            Boolean::class.javaPrimitiveType
        )
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            net.minecraft.server.v1_8_R3.Block::class.java,
            arrayOf("getMaterial"),
            Material::class.java
        )
        // obc: getHandle() for CraftWorld, CraftPlayer, CraftEntity.
        // nms: Several: AxisAlignedBB, WorldServer
        // nms: Block.getById(int), BlockPosition(int, int, int), WorldServer.getEntities(Entity, AxisAlignedBB)
        // nms: AttributeInstance.a(UUID), EntityComplexPart, EntityPlayer.getAttributeInstance(IAttribute).
    }

    // 1.8.4-1.8.8 (1_8_R3)
    @get:Override
    val mCVersion: String
        get() =// 1.8.4-1.8.8 (1_8_R3)
            "1.8.4-1.8.8"

    @get:Override
    val serverVersionTag: String
        get() = "Spigot-CB-1.8_R3"

    @get:Override
    val commandMap: CommandMap
        get() = (Bukkit.getServer() as CraftServer).getCommandMap()

    @get:Override
    val blockCache: BlockCache
        get() = getBlockCache(null)

    @Override
    fun getBlockCache(world: World?): BlockCache {
        return BlockCacheSpigotCB1_8_R3(world)
    }

    @Override
    fun getHeight(entity: Entity): Double {
        val mcEntity: net.minecraft.server.v1_8_R3.Entity = (entity as CraftEntity).getHandle()
        val boundingBox: AxisAlignedBB = mcEntity.getBoundingBox()
        val entityHeight: Double =
            Math.max(mcEntity.length, Math.max(mcEntity.getHeadHeight(), boundingBox.e - boundingBox.b))
        return if (entity is LivingEntity) {
            Math.max((entity as LivingEntity).getEyeHeight(), entityHeight)
        } else entityHeight
    }

    @Override
    fun isBlockSolid(id: Material): AlmostBoolean {
        @SuppressWarnings("deprecation") val block: Block = Block.getById(id.getId())
        return if (block == null || block.getMaterial() == null) {
            AlmostBoolean.MAYBE
        } else {
            AlmostBoolean.match(block.getMaterial().isSolid())
        }
    }

    @Override
    fun isBlockLiquid(id: Material): AlmostBoolean {
        @SuppressWarnings("deprecation") val block: Block = Block.getById(id.getId())
        return if (block == null || block.getMaterial() == null) {
            AlmostBoolean.MAYBE
        } else {
            AlmostBoolean.match(block.getMaterial().isLiquid())
        }
    }

    @Override
    fun getWidth(entity: Entity): Double {
        return (entity as CraftEntity).getHandle().width
    }

    @Override
    fun isIllegalBounds(player: Player): AlmostBoolean {
        val entityPlayer: EntityPlayer = (player as CraftPlayer).getHandle()
        if (entityPlayer.dead) {
            return AlmostBoolean.NO
        }
        // TODO: Does this need a method call for the "real" box? Might be no problem during moving events, though.
        val box: AxisAlignedBB = entityPlayer.getBoundingBox()
        if (LocUtil.isBadCoordinate(box.a, box.b, box.c, box.d, box.e, box.f)) {
            return AlmostBoolean.YES
        }
        if (!entityPlayer.isSleeping()) {
            // This can not really test stance but height of bounding box.
            val dY: Double = Math.abs(box.e - box.b)
            if (dY > 1.8) {
                return AlmostBoolean.YES // dY > 1.65D || 
            }
            if (dY < 0.1 && entityPlayer.length >= 0.1) {
                return AlmostBoolean.YES
            }
        }
        return AlmostBoolean.MAYBE
    }

    @Override
    fun getJumpAmplifier(player: Player): Double {
        val mcPlayer: EntityPlayer = (player as CraftPlayer).getHandle()
        return if (mcPlayer.hasEffect(MobEffectList.JUMP)) {
            mcPlayer.getEffect(MobEffectList.JUMP).getAmplifier()
        } else {
            Double.NEGATIVE_INFINITY
        }
    }

    @Override
    fun getFasterMovementAmplifier(player: Player): Double {
        val mcPlayer: EntityPlayer = (player as CraftPlayer).getHandle()
        return if (mcPlayer.hasEffect(MobEffectList.FASTER_MOVEMENT)) {
            mcPlayer.getEffect(MobEffectList.FASTER_MOVEMENT).getAmplifier()
        } else {
            Double.NEGATIVE_INFINITY
        }
    }

    @Override
    fun getInvulnerableTicks(player: Player): Int {
        return (player as CraftPlayer).getHandle().invulnerableTicks
    }

    @Override
    fun setInvulnerableTicks(player: Player, ticks: Int) {
        (player as CraftPlayer).getHandle().invulnerableTicks = ticks
    }

    @Override
    fun dealFallDamage(player: Player, damage: Double) {
        (player as CraftPlayer).getHandle().damageEntity(DamageSource.FALL, damage.toFloat())
    }

    @Override
    fun isComplexPart(entity: Entity): Boolean {
        return (entity as CraftEntity).getHandle() is EntityComplexPart
    }

    @Override
    fun shouldBeZombie(player: Player): Boolean {
        val mcPlayer: EntityPlayer = (player as CraftPlayer).getHandle()
        return !mcPlayer.dead && mcPlayer.getHealth() <= 0.0f
    }

    @Override
    fun setDead(player: Player, deathTicks: Int) {
        val mcPlayer: EntityPlayer = (player as CraftPlayer).getHandle()
        mcPlayer.deathTicks = deathTicks
        mcPlayer.dead = true
    }

    @Override
    fun hasGravity(mat: Material): Boolean {
        return mat.hasGravity()
    }

    @Override
    fun dealFallDamageFiresAnEvent(): AlmostBoolean {
        return AlmostBoolean.YES
    }

    //	@Override
    //	public void correctDirection(final Player player) {
    //		final EntityPlayer mcPlayer = ((CraftPlayer) player).getHandle();
    //		// Main direction.
    //		mcPlayer.yaw = LocUtil.correctYaw(mcPlayer.yaw);
    //		mcPlayer.pitch = LocUtil.correctPitch(mcPlayer.pitch);
    //		// Consider setting the lastYaw here too.
    //	}
    @Override
    fun resetActiveItem(player: Player): Boolean {
        (player as CraftPlayer).getHandle().bU()
        return true
    }
}