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
package fr.neatmonster.nocheatplus.compat.cb2545

import java.util.Iterator

class BlockCacheCB2545(world: World?) : BlockCache() {
    protected var world: World? = null
    protected var bukkitWorld: World? = null
    private val iBlockAccess: IBlockAccess = object : IBlockAccess() {
        @SuppressWarnings("deprecation")
        @Override
        fun getTypeId(x: Int, y: Int, z: Int): Int {
            return this@BlockCacheCB2545.getType(x, y, z).getId()
        }

        @Override
        fun getData(x: Int, y: Int, z: Int): Int {
            return getData(x, y, z)
        }

        @Override
        fun getMaterial(x: Int, y: Int, z: Int): Material {
            return this@BlockCacheCB2545.world.getMaterial(x, y, z)
        }

        @Override
        fun getTileEntity(x: Int, y: Int, z: Int): TileEntity {
            return world.getTileEntity(x, y, z)
        }

        @get:Override
        val vec3DPool: Vec3DPool
            get() = world.getVec3DPool()

        @Override
        fun isBlockFacePowered(arg0: Int, arg1: Int, arg2: Int, arg3: Int): Boolean {
            return world.isBlockFacePowered(arg0, arg1, arg2, arg3)
        }

        @Override
        fun t(x: Int, y: Int, z: Int): Boolean {
            return world.t(x, y, z)
        }
    }

    init {
        setAccess(world)
    }

    @Override
    fun setAccess(world: World?): BlockCache {
        bukkitWorld = world
        if (world != null) {
            this.maxBlockY = world.getMaxHeight() - 1
            this.world = (world as CraftWorld).getHandle()
        } else {
            this.world = null
        }
        return this
    }

    @Override
    fun fetchTypeId(x: Int, y: Int, z: Int): Material {
        return bukkitWorld.getBlockAt(x, y, z).getType()
    }

    @Override
    fun fetchData(x: Int, y: Int, z: Int): Int {
        return world.getData(x, y, z)
    }

    @Override
    fun fetchBounds(x: Int, y: Int, z: Int): DoubleArray? {

        // TODO: change api for this / use nodes (!)
        val id: Int = iBlockAccess.getTypeId(x, y, z)
        val block: net.minecraft.server.v1_4_6.Block = net.minecraft.server.v1_4_6.Block.byId.get(id) ?: return null
        block.updateShape(iBlockAccess, x, y, z) // TODO: use THIS instead of world.

        // minX, minY, minZ, maxX, maxY, maxZ
        return doubleArrayOf(block.v(), block.x(), block.z(), block.w(), block.y(), block.A())
    }

    @Override
    fun standsOnEntity(
        entity: Entity,
        minX: Double,
        minY: Double,
        minZ: Double,
        maxX: Double,
        maxY: Double,
        maxZ: Double
    ): Boolean {
        try {
            // TODO: Probably check other ids too before doing this ?
            val mcEntity: net.minecraft.server.v1_4_6.Entity = (entity as CraftEntity).getHandle()
            val box: AxisAlignedBB = useBox.b(minX, minY, minZ, maxX, maxY, maxZ)
            @SuppressWarnings("rawtypes") val list: List = world.getEntities(mcEntity, box)
            @SuppressWarnings("rawtypes") val iterator: Iterator = list.iterator()
            while (iterator.hasNext()) {
                val other: net.minecraft.server.v1_4_6.Entity = iterator.next() as net.minecraft.server.v1_4_6.Entity
                val type: EntityType = other.getBukkitEntity().getType()
                if (type !== EntityType.BOAT && type !== EntityType.MINECART) continue
                val otherBox: AxisAlignedBB = other.boundingBox
                return if (box.a > otherBox.d || box.d < otherBox.a || box.b > otherBox.e || box.e < otherBox.b || box.c > otherBox.f || box.f < otherBox.c) continue else {
                    true
                }
            }
        } catch (t: Throwable) {
            // Ignore exceptions (Context: DisguiseCraft).
        }
        return false
    }

    /* (non-Javadoc)
     * @see fr.neatmonster.nocheatplus.utilities.BlockCache#cleanup()
     */
    @Override
    fun cleanup() {
        super.cleanup()
        world = null
        bukkitWorld = null
    }

    companion object {
        /** Box for one time use, no nesting, no extra storing this(!).  */
        protected val useBox: AxisAlignedBB = AxisAlignedBB.a(0, 0, 0, 0, 0, 0)
    }
}