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
package fr.neatmonster.nocheatplus.utilities.ds.corw

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
 * More cows, more fun: Copy on write for a LinkedHashMap (optimized for fast reading from any thread).
 * <hr></hr>
 * Shortcomings:<br></br>
 *  * This does not allow access-ordered maps, use Collections.synchronizedMap(LinkedHashMap...) for that case.
 *  * Behavior of equals and hashCode could be problematic, because they have not been overridden, but in fact should be processed on copies of this map.
 * @author dev1mc
 */
class LinkedHashMapCOW<K, V> @JvmOverloads constructor(
    private val initialCapacity: Int = 16,
    private val loadFactor: Float = 0.75f
) : MutableMap<K?, V?> {
    // TODO: Consider a) add removeEldest... b) add option: copyMap(K) -> only copy if needed.  
    private var map: LinkedHashMap<K?, V?>? = null
    /**
     * Uses extra: false (default settings, insertion ordered).
     * @param initialCapacity
     * @param loadFactor
     */
    /**
     * Uses: 16, 0.75f, false (default settings, insertion ordered).
     */
    /**
     * Uses extra: 0.75f, false (default settings, insertion ordered).
     * @param initialCapacity
     */
    init {
        map = LinkedHashMap(initialCapacity, loadFactor, false)
    }

    /**
     * Uses: 16, 0.75f, false (default settings, insertion ordered).
     * @param map
     */
    constructor(map: MutableMap<K?, V?>?) : this() {
        this.map.putAll(map)
    }

    /**
     * Not synchronized: return a copy of the internal map.
     * @return
     */
    private fun copyMap(): LinkedHashMap<K?, V?>? {
        val newMap = LinkedHashMap<K?, V?>(initialCapacity, loadFactor, false)
        newMap.putAll(map)
        return newMap
    }

    override fun clear() {
        synchronized(this) { map.clear() }
    }

    override fun containsKey(key: Any?): Boolean {
        return map.containsKey(key)
    }

    override fun containsValue(value: Any?): Boolean {
        return map.containsValue(value)
    }

    /**
     * Unmodifiable version of the EntrySet. Entry.setValue might be possible, but dangerous :p
     */
    override fun entrySet(): MutableSet<MutableMap.MutableEntry<K?, V?>?>? {
        return Collections.unmodifiableSet(map.entries)
    }

    override operator fun get(key: Any?): V? {
        // NOTE: If accessOrder can be true, there needs to be synchronization here, defeating any purpose, better use Collections.synchronizedMap(LinkedHashMap...) for that case.
        return map.get(key)
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    /**
     * Unmodifiable version of the KeySet.
     */
    override fun keySet(): MutableSet<K?>? {
        return Collections.unmodifiableSet(map.keys)
    }

    override fun put(key: K?, value: V?): V? {
        val out: V
        synchronized(this) {
            val newMap = copyMap()
            out = newMap.put(key, value)
            map = newMap
        }
        return out
    }

    override fun putAll(m: MutableMap<out K?, out V?>?) {
        synchronized(this) {
            val newMap = copyMap()
            newMap.putAll(m)
            map = newMap
        }
    }

    override fun remove(key: Any?): V? {
        val out: V
        synchronized(this) {
            val newMap = copyMap()
            out = newMap.remove(key)
            map = newMap
        }
        return out
    }

    /**
     * Remove all given keys.<br></br>
     * Not the most efficient implementation, copying the map and then removing
     * keys, but still better than iterating remove(key).
     *
     * @param keys
     */
    fun removeAll(keys: MutableCollection<K?>?) {
        synchronized(this) {
            val newMap = copyMap()
            for (key in keys) {
                newMap.remove(key)
            }
            map = newMap
        }
    }

    override fun size(): Int {
        return map.size
    }

    /**
     * Unmodifiable version of the values (Collection).
     */
    override fun values(): MutableCollection<V?>? {
        return Collections.unmodifiableCollection(map.values)
    }
}