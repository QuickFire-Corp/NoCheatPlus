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
package fr.neatmonster.nocheatplus.workaround

import fr.neatmonster.nocheatplus.time.monotonic.MonotonicClock
import fr.neatmonster.nocheatplus.time.monotonic.MonotonicNanosClock
import fr.neatmonster.nocheatplus.time.monotonic.MonotonicMillisClock
import fr.neatmonster.nocheatplus.time.monotonic.MonotonicSynchClock
import fr.neatmonster.nocheatplus.time.monotonic.MonotonicAbstractClock
import fr.neatmonster.nocheatplus.logging.LoggerID
import fr.neatmonster.nocheatplus.logging.details.ContentLogger
import fr.neatmonster.nocheatplus.logging.details.LogOptions
import java.lang.Runnable
import java.io.File
import java.text.SimpleDateFormat
import java.lang.StringBuilder
import java.util.Locale
import java.util.logging.FileHandler
import java.io.IOException
import java.lang.SecurityException
import kotlin.Throws
import fr.neatmonster.nocheatplus.logging.details.FileLogger.FileLogFormatter
import fr.neatmonster.nocheatplus.logging.StreamID
import fr.neatmonster.nocheatplus.logging.details.LogOptions.CallContext
import kotlin.jvm.JvmOverloads
import fr.neatmonster.nocheatplus.logging.details.FileLogger
import fr.neatmonster.nocheatplus.logging.details.LogNodeDispatcher
import fr.neatmonster.nocheatplus.logging.details.ContentStream
import java.util.IdentityHashMap
import java.util.HashMap
import java.lang.RuntimeException
import java.lang.NullPointerException
import java.lang.IllegalArgumentException
import fr.neatmonster.nocheatplus.logging.details.DefaultContentStream
import fr.neatmonster.nocheatplus.logging.details.LoggerAdapter
import fr.neatmonster.nocheatplus.logging.details.FileLoggerAdapter
import fr.neatmonster.nocheatplus.utilities.ds.corw.IQueueRORA
import fr.neatmonster.nocheatplus.utilities.ds.corw.QueueRORA
import java.lang.InterruptedException
import java.lang.IllegalStateException
import fr.neatmonster.nocheatplus.logging.details.ILogString
import fr.neatmonster.nocheatplus.logging.details.ILogThrowable
import fr.neatmonster.nocheatplus.utilities.ds.map.CoordHash
import fr.neatmonster.nocheatplus.utilities.ds.map.HashMapLOW.LHMEntry
import java.util.Arrays
import fr.neatmonster.nocheatplus.utilities.ds.map.HashMapLOW
import fr.neatmonster.nocheatplus.utilities.ds.map.HashMapLOW.LHMBucket
import java.util.NoSuchElementException
import java.util.concurrent.locks.ReentrantLock
import fr.neatmonster.nocheatplus.utilities.ds.map.HashMapLOW.LHMIterator
import fr.neatmonster.nocheatplus.utilities.ds.map.HashMapLOW.LHMIterable
import java.util.LinkedHashSet
import java.util.LinkedHashMap
import fr.neatmonster.nocheatplus.utilities.ds.map.ManagedMap.ValueWrap
import java.util.LinkedList
import fr.neatmonster.nocheatplus.utilities.ds.map.AbstractCoordHashMap
import fr.neatmonster.nocheatplus.utilities.ds.map.CoordMap
import fr.neatmonster.nocheatplus.utilities.ds.map.CoordHashMap
import fr.neatmonster.nocheatplus.utilities.ds.map.LinkedCoordHashMap.LinkedHashEntry
import fr.neatmonster.nocheatplus.utilities.ds.map.LinkedCoordHashMap
import fr.neatmonster.nocheatplus.utilities.ds.map.LinkedCoordHashMap.MoveOrder
import fr.neatmonster.nocheatplus.utilities.ds.corw.DualCollection
import java.util.Collections
import fr.neatmonster.nocheatplus.utilities.ds.count.acceptdeny.IResettableAcceptDenyCounter
import fr.neatmonster.nocheatplus.utilities.ds.count.acceptdeny.ICounterWithParent
import fr.neatmonster.nocheatplus.utilities.ds.count.acceptdeny.IAcceptDenyCounter
import fr.neatmonster.nocheatplus.utilities.ds.count.acceptdeny.AcceptDenyCounter
import fr.neatmonster.nocheatplus.utilities.ds.count.acceptdeny.IAcceptCounter
import fr.neatmonster.nocheatplus.utilities.ds.count.acceptdeny.IDenyCounter
import fr.neatmonster.nocheatplus.utilities.ds.count.acceptdeny.IResettableCounter
import fr.neatmonster.nocheatplus.utilities.ds.count.ActionFrequency
import fr.neatmonster.nocheatplus.utilities.ds.bktree.BKModTree.HashMapNode
import fr.neatmonster.nocheatplus.utilities.ds.bktree.BKModTree.MapNode
import fr.neatmonster.nocheatplus.utilities.ds.bktree.BKLevenshtein.LevenNode
import fr.neatmonster.nocheatplus.utilities.ds.bktree.BKModTree
import fr.neatmonster.nocheatplus.utilities.ds.bktree.TimedBKLevenshtein.TimedLevenNode
import fr.neatmonster.nocheatplus.utilities.ds.bktree.BKLevenshtein
import fr.neatmonster.nocheatplus.utilities.ds.bktree.TimedBKLevenshtein.SimpleTimedLevenNode
import fr.neatmonster.nocheatplus.utilities.ds.bktree.TimedBKLevenshtein
import fr.neatmonster.nocheatplus.utilities.ds.bktree.SimpleTimedBKLevenshtein.STBKLResult
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.CharPrefixTree.CharNode
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.CharPrefixTree.CharLookupEntry
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.CharPrefixTree.SimpleCharNode
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.CharPrefixTree
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.SimplePrefixTree.SimpleLookupEntry
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.TimedCharPrefixTree.TimedCharNode
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.TimedCharPrefixTree.TimedCharLookupEntry
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.TimedCharPrefixTree.SimpleTimedCharNode
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.TimedCharPrefixTree
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.SimpleCharPrefixTree.SimpleCharLookupEntry
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.SimpleTimedCharPrefixTree.SimpleTimedCharLookupEntry
import java.util.concurrent.TimeUnit
import java.util.UUID
import fr.neatmonster.nocheatplus.utilities.IdUtil
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.lang.StackTraceElement
import java.lang.NoSuchFieldException
import fr.neatmonster.nocheatplus.utilities.ReflectionUtil
import java.lang.IllegalAccessException
import java.lang.reflect.InvocationTargetException
import java.lang.NoSuchMethodException
import java.lang.InstantiationException
import java.lang.ClassNotFoundException
import java.lang.reflect.Member
import java.lang.reflect.AccessibleObject
import fr.neatmonster.nocheatplus.workaround.IWorkaround
import fr.neatmonster.nocheatplus.workaround.AbstractWorkaround
import fr.neatmonster.nocheatplus.workaround.WorkaroundCounter
import fr.neatmonster.nocheatplus.workaround.IStagedWorkaround
import fr.neatmonster.nocheatplus.workaround.IWorkaroundRegistry.WorkaroundSet
import fr.neatmonster.nocheatplus.workaround.WorkaroundCountDown
import fr.neatmonster.nocheatplus.workaround.IWorkaroundRegistry
import fr.neatmonster.nocheatplus.utilities.build.BuildParameters
import java.util.HashSet
import fr.neatmonster.nocheatplus.utilities.PenaltyTime
import fr.neatmonster.nocheatplus.test.TestWorkarounds
import fr.neatmonster.nocheatplus.test.TestAcceptDenyCounters
import fr.neatmonster.nocheatplus.workaround.SimpleWorkaroundRegistry
import fr.neatmonster.nocheatplus.utilities.ds.count.ActionAccumulator
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.SimpleCharPrefixTree
import java.util.ArrayList

/**
 * Simple registry for workarounds. No thread-safety built in.
 *
 * @author asofold
 */
open class SimpleWorkaroundRegistry : IWorkaroundRegistry {
    /** Global counter by id.  */
    private val counters: MutableMap<String?, IAcceptDenyCounter?>? = HashMap()

    /** Workaround blue print by id.  */
    private val bluePrints: MutableMap<String?, IWorkaround?>? = HashMap()

    /** Map group id to array of workaround ids.  */
    private val groups: MutableMap<String?, Array<String?>?>? = HashMap()

    /** Map WorkaroundSet id to the contained blueprint ids.  */
    private val workaroundSets: MutableMap<String?, Array<String?>?>? = HashMap()

    /** Map WorkaroundSet id to the contained group ids. Might not contain entries for all ids.  */
    private val workaroundSetGroups: MutableMap<String?, Array<String?>?>? = HashMap()
    override fun setWorkaroundBluePrint(vararg bluePrints: IWorkaround?) {
        // TODO: Might consistency check, plus policy for overriding (ignore all if present).
        for (i in bluePrints.indices) {
            val bluePrintCopy = bluePrints[i].getNewInstance()
            this.bluePrints[bluePrintCopy.id] = bluePrintCopy
            // Set a parent counter, if not already set. 
            val allTimeCounter = bluePrintCopy.allTimeCounter
            if (allTimeCounter is ICounterWithParent) {
                val bluePrintCopyWithParentCounter = allTimeCounter as ICounterWithParent
                if (bluePrintCopyWithParentCounter.parentCounter == null) {
                    bluePrintCopyWithParentCounter.parentCounter = createGlobalCounter(bluePrintCopy.id)
                }
            }
        }
    }

    override fun setGroup(groupId: String?, workaroundIds: MutableCollection<String?>?) {
        for (id in workaroundIds) {
            require(bluePrints.containsKey(id)) { "No blueprint for id '$id' in group '$groupId'." }
        }
        groups[groupId] = workaroundIds.toTypedArray()
    }

    override fun setGroup(groupId: String?, vararg bluePrints: IWorkaround?) {
        setGroup(groupId, getCheckedIdSet(Arrays.asList(*bluePrints)))
    }

    private fun setWorkaroundSet(
        workaroundSetId: String?,
        bluePrints: MutableCollection<IWorkaround?>?,
        vararg groupIds: String?
    ) {
        val ids = arrayOfNulls<String?>(bluePrints.size)
        var i = 0
        for (bluePrint in bluePrints) {
            val id = bluePrint.getId()
            if (!this.bluePrints.containsKey(id)) {
                // Lazily register.
                setWorkaroundBluePrint(bluePrint)
            }
            ids[i] = id
            i++
        }
        workaroundSets[workaroundSetId] = ids
        if (groupIds != null && groupIds.size > 0) {
            i = 0
            while (i < groupIds.size) {
                require(groups.containsKey(groupIds[i])) { "Group not registered: " + groupIds[i] }
                i++
            }
            workaroundSetGroups[workaroundSetId] = groupIds
        }
    }

    override fun setWorkaroundSetByIds(
        workaroundSetId: String?,
        bluePrintIds: MutableCollection<String?>?,
        vararg groupIds: String?
    ) {
        val bluePrints: MutableList<IWorkaround?> = ArrayList(bluePrintIds.size)
        for (id in bluePrintIds) {
            val bluePrint =
                this.bluePrints.get(id) ?: throw IllegalArgumentException("the blueprint is not registered: $id")
            bluePrints.add(bluePrint)
        }
        setWorkaroundSet(workaroundSetId, bluePrints, *groupIds)
    }

    override fun getWorkaroundSet(workaroundSetId: String?): WorkaroundSet? {
        val workaroundIds = workaroundSets.get(workaroundSetId)
            ?: throw IllegalArgumentException("WorkaroundSet not registered: $workaroundSetId")
        val bluePrints = arrayOfNulls<IWorkaround?>(workaroundIds.size)
        for (i in workaroundIds.indices) {
            bluePrints[i] = this.bluePrints.get(workaroundIds[i])
        }
        val groups: MutableMap<String?, Array<String?>?>?
        val groupIds = workaroundSetGroups.get(workaroundSetId)
        if (groupIds == null) {
            groups = null
        } else {
            groups = HashMap()
            for (i in groupIds.indices) {
                val groupId = groupIds[i]
                groups[groupId] = this.groups.get(groupId)
            }
        }
        return WorkaroundSet(bluePrints, groups)
    }

    override fun getGlobalCounter(id: String?): IAcceptDenyCounter? {
        return counters.get(id)
    }

    override fun createGlobalCounter(id: String?): IAcceptDenyCounter? {
        var counter = counters.get(id)
        if (counter == null) {
            counter = AcceptDenyCounter()
            counters[id] = counter
        }
        return counter
    }

    override fun <C : IWorkaround?> getWorkaround(id: String?, workaroundClass: Class<C?>?): C? {
        val workaround = getWorkaround(id)
        return if (workaroundClass.isAssignableFrom(workaround.javaClass)) {
            workaround as C?
        } else {
            throw IllegalArgumentException("Unsupported class for id '" + id + "': " + workaroundClass.getName() + " (actual class is " + workaround.javaClass.name + ")")
        }
    }

    override fun getWorkaround(id: String?): IWorkaround? {
        val bluePrint = bluePrints.get(id) ?: throw IllegalArgumentException("Id not registered as blueprint: $id")
        return bluePrint.newInstance
    }

    override fun getGlobalCounters(): MutableMap<String?, IAcceptDenyCounter?>? {
        return Collections.unmodifiableMap(counters)
    }

    override fun getCheckedWorkaroundId(workaroundId: String?): String? {
        val bluePrint = bluePrints.get(workaroundId)
        return if (bluePrint == null) {
            throw IllegalArgumentException("No blueprint registered for: $workaroundId")
        } else {
            bluePrint.id
        }
    }

    override fun getCheckedIdSet(workarounds: MutableCollection<out IWorkaround?>?): MutableSet<String?>? {
        val ids: MutableSet<String?> = LinkedHashSet()
        for (workaround in workarounds) {
            val bluePrint = bluePrints.get(workaround.getId())
            requireNotNull(bluePrint) { "No blueprint registered for: " + workaround.getId() }
            ids.add(bluePrint.id)
        }
        return ids
    }
}