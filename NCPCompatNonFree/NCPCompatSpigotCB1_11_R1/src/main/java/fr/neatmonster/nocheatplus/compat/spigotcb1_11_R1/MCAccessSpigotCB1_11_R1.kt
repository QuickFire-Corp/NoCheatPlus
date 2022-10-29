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
package fr.neatmonster.nocheatplus.compat.spigotcb1_11_R1

import java.lang.reflect.Method

class MCAccessSpigotCB1_11_R1 : MCAccess {
    private val JUMP: MobEffectList?
    private val FASTER_MOVEMENT: MobEffectList?

    /**
     * Test for availability in constructor.
     */
    init {
        //        try {
        commandMap
        val blockGetAABB: Method = ReflectionUtil.getMethod(
            Block::class.java,
            "b",
            IBlockData::class.java,
            IBlockAccess::class.java,
            BlockPosition::class.java
        )
        if (blockGetAABB.getReturnType() !== AxisAlignedBB::class.java
            || !blockGetAABB.isAnnotationPresent(Deprecated::class.java)
        ) {
            throw RuntimeException()
        }
        if (ReflectionUtil.getConstructor(
                BlockPosition::class.java,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            ) == null
        ) {
            throw RuntimeException()
        }
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            net.minecraft.server.v1_11_R1.EntityLiving::class.java,
            arrayOf("getHeadHeight"),
            Float::class.javaPrimitiveType
        )
        ReflectionUtil.checkMethodReturnTypesNoArgs(
            net.minecraft.server.v1_11_R1.EntityPlayer::class.java,
            arrayOf("getHealth"),
            Float::class.javaPrimitiveType
        )
        ReflectionUtil.checkMembers(
            net.minecraft.server.v1_11_R1.AxisAlignedBB::class.java, Double::class.javaPrimitiveType,
            "a", "b", "c", "d", "e", "f"
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
        // TODO: Confine the following by types as well.
        ReflectionUtil.checkMembers("net.minecraft.server.v1_11_R1.", arrayOf("Entity", "length", "width", "locY"))
        ReflectionUtil.checkMembers(
            "net.minecraft.server.v1_11_R1.",
            arrayOf("EntityPlayer", "dead", "deathTicks", "invulnerableTicks")
        )

        // obc: getHandle() for CraftWorld, CraftPlayer, CraftEntity.
        // nms: Several: AxisAlignedBB, WorldServer
        // nms: Block.getById(int), BlockPosition(int, int, int), WorldServer.getEntities(Entity, AxisAlignedBB)
        // nms: AttributeInstance.a(UUID), EntityComplexPart, EntityPlayer.getAttributeInstance(IAttribute).

        //        } catch(Throwable t) {
        //            NCPAPIProvider.getNoCheatPlusAPI().getLogManager().severe(Streams.INIT, t);
        //            throw new RuntimeException("NO WERK");
        //        }
        JUMP = MobEffectList.getByName("jump_boost")
        if (JUMP == null) {
            throw RuntimeException()
        }
        FASTER_MOVEMENT = MobEffectList.getByName("speed")
        if (FASTER_MOVEMENT == null) {
            throw RuntimeException()
        }
    }

    // 1.11 (1_11_R1)
    @get:Override
    val mCVersion: String
        get() =// 1.11 (1_11_R1)
            "1.11-1.11.2"

    @get:Override
    val serverVersionTag: String
        get() = "Spigot-CB-1.11_R1"

    @get:Override
    val commandMap: CommandMap
        get() = (Bukkit.getServer() as CraftServer).getCommandMap()

    @get:Override
    val blockCache: BlockCache
        get() = getBlockCache(null)

    @Override
    fun getBlockCache(world: World?): BlockCache {
        return BlockCacheSpigotCB1_11_R1(world)
    }

    @Override
    fun getHeight(entity: Entity): Double {
        // (entity.getHeight() returns the length field, but we access nms anyway.)
        val mcEntity: net.minecraft.server.v1_11_R1.Entity = (entity as CraftEntity).getHandle()
        val boundingBox: AxisAlignedBB = mcEntity.getBoundingBox()
        val entityHeight: Double =
            Math.max(mcEntity.length, Math.max(mcEntity.getHeadHeight(), boundingBox.e - boundingBox.b))
        return if (entity is LivingEntity) {
            Math.max((entity as LivingEntity).getEyeHeight(), entityHeight)
        } else {
            entityHeight
        }
    }

    private fun getMaterial(blockId: Int): Material? {
        val block: Block = Block.getById(blockId) ?: return null
        // (Currently no update state, since we don't have any position.)
        return block.getBlockData().getMaterial()
    }

    @Override
    fun isBlockSolid(id: Material): AlmostBoolean {
        @SuppressWarnings("deprecation") val material: Material? = getMaterial(id.getId())
        return if (material == null) {
            AlmostBoolean.MAYBE
        } else {
            AlmostBoolean.match(material.isSolid())
        }
    }

    @Override
    fun isBlockLiquid(id: Material): AlmostBoolean {
        @SuppressWarnings("deprecation") val material: Material? = getMaterial(id.getId())
        return if (material == null) {
            AlmostBoolean.MAYBE
        } else {
            AlmostBoolean.match(material.isLiquid())
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
        return if (mcPlayer.hasEffect(JUMP)) {
            mcPlayer.getEffect(JUMP).getAmplifier()
        } else {
            Double.NEGATIVE_INFINITY
        }
    }

    @Override
    fun getFasterMovementAmplifier(player: Player): Double {
        val mcPlayer: EntityPlayer = (player as CraftPlayer).getHandle()
        return if (mcPlayer.hasEffect(FASTER_MOVEMENT)) {
            mcPlayer.getEffect(FASTER_MOVEMENT).getAmplifier()
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

    //  @Override
    //  public void correctDirection(final Player player) {
    //      final EntityPlayer mcPlayer = ((CraftPlayer) player).getHandle();
    //      // Main direction.
    //      mcPlayer.yaw = LocUtil.correctYaw(mcPlayer.yaw);
    //      mcPlayer.pitch = LocUtil.correctPitch(mcPlayer.pitch);
    //      // Consider setting the lastYaw here too.
    //  }
    @Override
    fun resetActiveItem(player: Player): Boolean {
        (player as CraftPlayer).getHandle().clearActiveItem()
        return true
    }
}