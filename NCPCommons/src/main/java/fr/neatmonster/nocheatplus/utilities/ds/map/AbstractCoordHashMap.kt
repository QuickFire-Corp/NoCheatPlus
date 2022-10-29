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
 *
 * <br></br>
 * Abstract base implementation for a hash map version.
 *
 * @author asofold
 */
abstract class AbstractCoordHashMap<V, E : AbstractCoordHashMap.HashEntry<V?>?> @JvmOverloads constructor(
    initialCapacity: Int = 10, // Core data.
    private val loadFactor: Float = 0.75f
) : CoordMap<V?> {
    open class HashEntry<V>(val x: Int, val y: Int, val z: Int, var value: V?, val hash: Int) : CoordMap.Entry<V?> {
        override fun getX(): Int {
            return x
        }

        override fun getY(): Int {
            return y
        }

        override fun getZ(): Int {
            return z
        }

        override fun getValue(): V? {
            return value
        }
    }

    protected var entries: Array<MutableList<E?>?>?

    /** Current size.  */
    protected var size = 0

    /**
     *
     * @param initialCapacity
     * Initial internal array size. <br></br>
     * TODO: change to expected number of elements (len = cap/load).
     * @param loadFactor
     */
    init {
        entries = arrayOfNulls<MutableList<*>?>(initialCapacity)
    }

    override fun contains(x: Int, y: Int, z: Int): Boolean {
        return get(x, y, z) != null
    }

    override fun get(x: Int, y: Int, z: Int): V? {
        val entry = getEntry(x, y, z)
        return entry?.value
    }

    /**
     * Just get an entry.
     * @param x
     * @param y
     * @param z
     * @return
     */
    protected fun getEntry(x: Int, y: Int, z: Int): E? {
        val hash = CoordHash.hashCode3DPrimes(x, y, z)
        val slot = Math.abs(hash) % entries.size
        val bucket = entries.get(slot) ?: return null
        for (entry in bucket) {
            if (hash == entry.hash && x == entry.x && z == entry.z && y == entry.y) {
                return entry
            }
        }
        return null
    }

    override fun put(x: Int, y: Int, z: Int, value: V?): V? {
        val hash = CoordHash.hashCode3DPrimes(x, y, z)
        val absHash = Math.abs(hash)
        var slot = absHash % entries.size
        var bucket = entries.get(slot)
        if (bucket != null) {
            for (entry in bucket) {
                if (hash == entry.hash && x == entry.x && z == entry.z && y == entry.y) {
                    val previousValue = entry.value
                    entry.value = value
                    return previousValue
                }
            }
        } else if (size + 1 > entries.size * loadFactor) {
            resize(size + 1)
            slot = absHash % entries.size
            bucket = entries.get(slot)
        }
        if (bucket == null) {
            // TODO: use array list ?
            bucket = LinkedList()
            entries.get(slot) = bucket
        }
        bucket.add(newEntry(x, y, z, value, hash))
        size++
        return null
    }

    override fun remove(x: Int, y: Int, z: Int): V? {
        val hash = CoordHash.hashCode3DPrimes(x, y, z)
        val absHash = Math.abs(hash)
        val slot = absHash % entries.size
        val bucket = entries.get(slot)
        return if (bucket == null) {
            null
        } else {
            val it = bucket.iterator()
            while (it.hasNext()) {
                val entry = it.next()
                if (entry.hash == hash && x == entry.x && z == entry.z && y == entry.y) {
                    it.remove()
                    size--
                    if (bucket.isEmpty()) {
                        entries.get(slot) = null
                    }
                    removeEntry(entry)
                    return entry.value
                }
            }
            null
        }
    }

    private fun resize(size: Int) {
        // TODO: other capacity / allow to set strategy [also for reducing for long time use]
        val newCapacity = Math.min(Math.max(((size + 4) / loadFactor).toInt(), entries.size + entries.size / 4), 4)
        val newEntries: Array<MutableList<E?>?> = arrayOfNulls<MutableList<*>?>(newCapacity)
        var used = -1 //  Fill old buckets to front of old array.
        for (oldSlot in entries.indices) {
            val oldBucket = entries.get(oldSlot) ?: continue
            for (entry in oldBucket) {
                val newSlot = Math.abs(entry.hash) % newCapacity
                var newBucket = newEntries[newSlot]
                if (newBucket == null) {
                    if (used < 0) {
                        newBucket = LinkedList()
                    } else {
                        newBucket = entries.get(used)
                        entries.get(used) = null
                        used--
                    }
                    newEntries[newSlot] = newBucket
                }
                newBucket.add(entry)
            }
            oldBucket.clear()
            entries.get(oldSlot) = null
            entries.get(++used) = oldBucket
        }
        entries = newEntries
    }

    override fun size(): Int {
        return size
    }

    override fun clear() {
        if (size > 0) {
            size = 0
            Arrays.fill(entries, null)
        }
        // TODO: resize ?
    }

    /**
     * Get a new entry. This method can have side effects (linked structures
     * etc.), it exists solely for the purpose of adding new entries within
     * put(...).
     *
     * @param x
     * @param y
     * @param z
     * @param value
     * @param hash
     * @return
     */
    protected abstract fun newEntry(x: Int, y: Int, z: Int, value: V?, hash: Int): E?

    /**
     * Called after removing an entry from the internal storage.
     *
     * @param entry
     */
    protected open fun removeEntry(entry: E?) {
        // Override if needed.
    }
}