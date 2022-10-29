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
package fr.neatmonster.nocheatplus.compat.cb2882

import org.bukkit.craftbukkit.v1_6_R3.entity.CraftLivingEntity

class AttributeAccess : IAttributeAccess {
    init {
        if (ReflectionUtil.getClass("net.minecraft.server.v1_6_R3.AttributeInstance") == null) {
            throw RuntimeException("Service not available.")
        }
    }

    @Override
    fun getSpeedAttributeMultiplier(player: Player): Double {
        val attr: AttributeInstance =
            (player as CraftLivingEntity).getHandle().getAttributeInstance(GenericAttributes.d)
        val `val`: Double = attr.getValue() / attr.b()
        val mod: AttributeModifier = attr.a(AttribUtil.ID_SPRINT_BOOST)
        return if (mod == null) {
            `val`
        } else {
            `val` / AttribUtil.getMultiplier(mod.c(), mod.d())
        }
    }

    @Override
    fun getSprintAttributeMultiplier(player: Player): Double {
        val mod: AttributeModifier = (player as CraftLivingEntity).getHandle().getAttributeInstance(GenericAttributes.d)
            .a(AttribUtil.ID_SPRINT_BOOST)
        return if (mod == null) {
            1.0
        } else {
            AttribUtil.getMultiplier(mod.c(), mod.d())
        }
    }
}