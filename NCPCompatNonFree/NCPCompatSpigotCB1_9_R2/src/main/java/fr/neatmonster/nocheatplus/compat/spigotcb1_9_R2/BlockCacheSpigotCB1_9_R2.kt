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
package fr.neatmonster.nocheatplus.compat.spigotcb1_9_R2

import java.util.Iterator

class BlockCacheSpigotCB1_9_R2(world: World?) : BlockCache() {
    protected var world: net.minecraft.server.v1_9_R2.WorldServer? = null
    protected var bukkitWorld: World? = null

    init {
        setAccess(world)
    }

    @Override
    fun setAccess(world: World?): BlockCache {
        if (world != null) {
            this.maxBlockY = world.getMaxHeight() - 1
            this.world = (world as CraftWorld).getHandle()
            bukkitWorld = world
        } else {
            this.world = null
            bukkitWorld = null
        }
        return this
    }

    @Override
    fun fetchTypeId(x: Int, y: Int, z: Int): Material {
        return bukkitWorld.getBlockAt(x, y, z).getType()
    }

    @SuppressWarnings("deprecation")
    @Override
    fun fetchData(x: Int, y: Int, z: Int): Int {
        return bukkitWorld.getBlockAt(x, y, z).getData()
    }

    @Override
    fun fetchBounds(x: Int, y: Int, z: Int): DoubleArray? {
        val mat: Material = getType(x, y, z)
        @SuppressWarnings("deprecation") val id: Int = mat.getId()
        val block: net.minecraft.server.v1_9_R2.Block = net.minecraft.server.v1_9_R2.Block.getById(id)
            ?: // TODO: Convention for null blocks -> full ?
            return null
        val shape: DoubleArray = LegacyBlocks.getShape(this, mat, x, y, z, false)
        if (shape != null) return shape
        val pos = BlockPosition(x, y, z)
        // TODO: Deprecation warning below (reason / substitute?).
        @SuppressWarnings("deprecation") val bb: AxisAlignedBB = block.a(world.getType(pos), world, pos)
            ?: return doubleArrayOf(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) // Special case.
        //return null;
        // minX, minY, minZ, maxX, maxY, maxZ
        return doubleArrayOf(bb.a, bb.b, bb.c, bb.d, bb.e, bb.f)
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
            // TODO: Find some simplification!
            val mcEntity: net.minecraft.server.v1_9_R2.Entity = (entity as CraftEntity).getHandle()
            val box = AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
            @SuppressWarnings("rawtypes") val list: List = world.getEntities(mcEntity, box)
            @SuppressWarnings("rawtypes") val iterator: Iterator = list.iterator()
            while (iterator.hasNext()) {
                val other: net.minecraft.server.v1_9_R2.Entity = iterator.next() as net.minecraft.server.v1_9_R2.Entity
                if (mcEntity === other || other !is EntityBoat && other !is EntityShulker) { // && !(other instanceof EntityMinecart)) continue;
                    continue
                }
                if (minY >= other.locY && minY - other.locY <= 0.7) {
                    return true
                }
                // Still check this for some reason.
                val otherBox: AxisAlignedBB = other.getBoundingBox()
                return if (box.a > otherBox.d || box.d < otherBox.a || box.b > otherBox.e || box.e < otherBox.b || box.c > otherBox.f || box.f < otherBox.c) {
                    continue
                } else {
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
}