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
package fr.neatmonster.nocheatplus.utilities.ds.map

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

/**
 * Intended for Minecraft coordinates, probably not for too high values.<br></br>
 * This implementation is not thread safe, though changing values and
 * get/contains should work if the map stays unchanged.
 * <hr></hr>
 * Linked hash map implementation of CoordMap<V>, allowing for insertion/access
 * order. By default entries are added to the end. This implementation does not
 * imitate the LinkedHashMap behavior [may be adapted to be similar, if
 * desired], instead methods are provided for manipulating the order at will.
 *
 * @author asofold
 *
 * @param <V>
</V></V> */
class LinkedCoordHashMap<V> : AbstractCoordHashMap<V?, LinkedHashEntry<V?>?>, CoordMap<V?> {
    // TODO: Add default order for get/put?
    // TODO: Tests.
    /**
     * Where to move an entry.
     * @author asofold
     */
    enum class MoveOrder {
        FRONT, NOT, END
    }

    class LinkedHashEntry<V>(x: Int, y: Int, z: Int, value: V?, hash: Int) : HashEntry<V?>(x, y, z, value, hash) {
        var previous: LinkedHashEntry<V?>? = null
        var next: LinkedHashEntry<V?>? = null
    }

    class LinkedHashIterator<V>(private val map: LinkedCoordHashMap<V?>?, private val reverse: Boolean) :
        MutableIterator<CoordMap.Entry<V?>?> {
        private var current: LinkedHashEntry<V?>? = null
        private var next: LinkedHashEntry<V?>?

        init {
            next = if (reverse) map.lastEntry else map.firstEntry
        }

        override fun hasNext(): Boolean {
            return next != null
        }

        override fun next(): LinkedHashEntry<V?>? {
            if (next == null) {
                throw NoSuchElementException()
            }
            current = next
            next = if (reverse) next.previous else next.next
            return current
        }

        override fun remove() {
            if (current != null) {
                // TODO: more efficient version ?
                map.remove(current.x, current.y, current.z)
                current = null
            }
        }
    }

    protected var firstEntry: LinkedHashEntry<V?>? = null
    protected var lastEntry: LinkedHashEntry<V?>? = null

    constructor() : super() {}
    constructor(initialCapacity: Int) : super(initialCapacity) {}
    constructor(initialCapacity: Int, loadFactor: Float) : super(initialCapacity, loadFactor) {}

    /**
     * Move an entry to the start of the linked structure.
     *
     * @param x
     * @param y
     * @param z
     * @return The value that was stored for the given coordinates. If no entry
     * is present, null is returned.
     */
    fun moveToFront(x: Int, y: Int, z: Int): V? {
        val entry = getEntry(x, y, z) ?: return null
        removeEntry(entry)
        setFirst(entry)
        return entry.value
    }

    /**
     * Move an entry to the end of the linked structure.
     *
     * @param x
     * @param y
     * @param z
     * @return The value that was stored for the given coordinates. If no entry
     * is present, null is returned.
     */
    fun moveToEnd(x: Int, y: Int, z: Int): V? {
        val entry = getEntry(x, y, z) ?: return null
        removeEntry(entry)
        setLast(entry)
        return entry.value
    }

    /**
     * Convenience method to specify moving based on MoveOrder. Note that
     * MoveOrder.NOT leads to super.get being called, in order to be bale to
     * return a present value.
     *
     * @param x
     * @param y
     * @param z
     * @param order
     * @return The value that was stored for the given coordinates. If no entry
     * is present, null is returned.
     */
    fun move(x: Int, y: Int, z: Int, order: MoveOrder?): V? {
        when (order) {
            MoveOrder.END -> return moveToEnd(x, y, z)
            MoveOrder.FRONT -> return moveToFront(x, y, z)
            else -> {}
        }
        // Ensure no changes.
        return super.get(x, y, z)
    }

    /**
     * Convenience method to control where the resulting entry is put to (front
     * or end).
     *
     * @param x
     * @param y
     * @param z
     * @param value
     * @param moveToEnd
     * @return
     */
    fun put(x: Int, y: Int, z: Int, value: V?, order: MoveOrder?): V? {
        // TODO: Optimized.
        val previousValue = super.put(x, y, z, value)
        if (order == MoveOrder.FRONT) {
            moveToFront(x, y, z)
        } else if (previousValue != null && order == MoveOrder.END) {
            // Ensure the intended order.
            moveToEnd(x, y, z)
        }
        return previousValue
    }

    operator fun get(x: Int, y: Int, z: Int, order: MoveOrder?): V? {
        // TODO: Optimized.
        val value = super.get(x, y, z)
        if (value != null && order != MoveOrder.NOT) {
            move(x, y, z, order)
        }
        return value
    }

    override fun iterator(): LinkedHashIterator<V?>? {
        return LinkedHashIterator<V?>(this, false)
    }

    /**
     * Control order of iteration.
     *
     * @param reversed
     * @return
     */
    fun iterator(reversed: Boolean): LinkedHashIterator<V?>? {
        return LinkedHashIterator<V?>(this, reversed)
    }

    override fun newEntry(x: Int, y: Int, z: Int, value: V?, hash: Int): LinkedHashEntry<V?>? {
        val entry = LinkedHashEntry<V?>(x, y, z, value, hash)
        // Always put in last.
        setLast(entry)
        return entry
    }

    /**
     * Insert entry as the first element. Assumes the entry not to be linked.
     *
     * @param entry
     */
    private fun setFirst(entry: LinkedHashEntry<V?>?) {
        if (firstEntry == null) {
            lastEntry = entry
            firstEntry = lastEntry
        } else {
            entry.next = firstEntry
            firstEntry.previous = entry
            firstEntry = entry
        }
    }

    /**
     * Insert entry as the last element. Assumes the entry not to be linked.
     *
     * @param entry
     */
    private fun setLast(entry: LinkedHashEntry<V?>?) {
        if (firstEntry == null) {
            lastEntry = entry
            firstEntry = lastEntry
        } else {
            entry.previous = lastEntry
            lastEntry.next = entry
            lastEntry = entry
        }
    }

    override fun removeEntry(entry: LinkedHashEntry<V?>?) {
        // Just unlink.
        if (entry === firstEntry) {
            firstEntry = entry.next
            if (firstEntry != null) {
                firstEntry.previous = null
            }
        } else {
            entry.previous.next = entry.next
        }
        if (entry === lastEntry) {
            lastEntry = entry.previous
            if (lastEntry != null) {
                lastEntry.next = null
            }
        } else {
            entry.next.previous = entry.previous
        }
        entry.next = null
        entry.previous = entry.next
    }

    override fun clear() {
        super.clear()
        lastEntry = null
        firstEntry = lastEntry
    }
}