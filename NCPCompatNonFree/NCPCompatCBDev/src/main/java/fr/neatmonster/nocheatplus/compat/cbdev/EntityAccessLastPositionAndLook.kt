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
package fr.neatmonster.nocheatplus.compat.cbdev

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity

class EntityAccessLastPositionAndLook : IEntityAccessLastPositionAndLook {
    init {
        ReflectionUtil.checkMembers(
            net.minecraft.server.v1_12_R1.Entity::class.java, Double::class.javaPrimitiveType, arrayOf(
                "lastX", "lastY", "lastZ"
            )
        )
        ReflectionUtil.checkMembers(
            net.minecraft.server.v1_12_R1.Entity::class.java, Float::class.javaPrimitiveType, arrayOf(
                "lastYaw", "lastPitch"
            )
        )
    }

    @Override
    fun getPositionAndLook(entity: Entity, location: ISetPositionWithLook) {
        // TODO: Error handling / conventions.
        val nmsEntity: net.minecraft.server.v1_12_R1.Entity = (entity as CraftEntity).getHandle()
        location.setX(nmsEntity.lastX)
        location.setY(nmsEntity.lastY)
        location.setZ(nmsEntity.lastZ)
        location.setYaw(nmsEntity.lastYaw)
        location.setPitch(nmsEntity.lastPitch)
    }

    @Override
    fun setPositionAndLook(entity: Entity, location: IGetPositionWithLook) {
        val nmsEntity: net.minecraft.server.v1_12_R1.Entity = (entity as CraftEntity).getHandle()
        nmsEntity.lastX = location.getX()
        nmsEntity.lastY = location.getY()
        nmsEntity.lastZ = location.getZ()
        nmsEntity.lastYaw = location.getYaw()
        nmsEntity.lastPitch = location.getPitch()
    }
}