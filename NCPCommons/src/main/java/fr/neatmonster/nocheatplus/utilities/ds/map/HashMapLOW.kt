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
import java.util.concurrent.locks.Lock

/**
 * Lock on write hash map. Less jumpy than cow, bucket oriented addition, bulk
 * remove. Does not implement the Map interface, due to it not being right for
 * this purpose.
 * <hr></hr>
 * Field of use should be a thread-safe map, where get is not using locking and
 * where bulk (removal) operations can be performed with one time locking, while
 * entries usually stay for a longer time until expired.
 * <hr></hr>
 * All changes are still done under lock. Iterators are meant to iterate
 * fail-safe, even on concurrent modification of the map. Calling remove() on an
 * iterator should relay to originalMap.remove(item). The internal size may not
 * ever shrink, at least not below targetSize, but it might grow with entries. <br></br>
 * Both null keys and null values are supported.
 *
 * @author asofold
 */
class HashMapLOW<K, V>(///////////////////////
    // Instance members
    ///////////////////////
    private val lock: Lock?,
    /** Intended/expected size.  */
    private val targetSize: Int
) {
    internal class LHMEntry<K, V>(val hashCode: Int, val key: K?, var value: V?) : MutableMap.MutableEntry<K?, V?> {
        override fun getKey(): K? {
            return key
        }

        override fun getValue(): V? {
            return value
        }

        override fun setValue(value: V?): V? {
            val oldValue = this.value
            this.value = value
            return oldValue
        }

        override fun hashCode(): Int {
            // By specification, not intended to be useful here.
            return hashCode xor if (value == null) 0 else value.hashCode()
        }

        override fun equals(obj: Any?): Boolean {
            // By specification, not intended to be useful here.
            return if (obj is MutableMap.MutableEntry<*, *>) {
                val entry = obj as MutableMap.MutableEntry<*, *>?
                (obj === this
                        || (if (key == null) entry.key == null else key == entry.key)
                        && if (value == null) entry.value == null else value == entry.value)
            } else {
                false
            }
        }

        /**
         * Delegate key comparison here, using a pre-calculated hash value.
         *
         * @param otherHashCode
         * @param otherKey
         * @return
         */
        fun equalsKey(otherHashCode: Int, otherKey: K?): Boolean {
            if (otherHashCode != hashCode) {
                return false
            }
            return if (otherKey === key) {
                true
            } else key != null && key == otherKey
            // (One of both still could be null.)
        }
    }

    /**
     * Create with adding entries.
     * @author asofold
     *
     * @param <K>
     * @param <V>
    </V></K> */
    internal class LHMBucket<K, V> {
        // TODO: Link non-empty buckets.
        // TODO: final int index;
        var size = 0

        /** Must be stored externally for iteration.  */
        var contents: Array<LHMEntry<K?, V?>?>? =
            arrayOfNulls<LHMEntry<*, *>?>(3) as Array<LHMEntry<K?, V?>?> // TODO: Configurable

        /**
         * Called under lock.
         *
         * @param key
         * @param value
         * @param ifAbsent
         * If true, an existing non-null (!) value will not be
         * overridden.
         * @return
         */
        fun put(hashCode: Int, key: K?, value: V?, ifAbsent: Boolean): V? {
            var emptyIndex: Int
            if (size == 0) {
                emptyIndex = 0
                size++
            } else {
                emptyIndex = -1
                var oldEntry: LHMEntry<K?, V?>? = null
                var entriesFound = 0
                for (i in contents.indices) {
                    val entry = contents.get(i)
                    if (entry != null) {
                        entriesFound++
                        if (entry.equalsKey(hashCode, key)) {
                            oldEntry = entry
                            break
                        } else if (entriesFound == size && emptyIndex != -1) {
                            // TODO: Not sure this is just overhead for most cases.
                            break
                        }
                    } else if (emptyIndex == -1) {
                        emptyIndex = i
                    }
                }
                if (oldEntry != null) {
                    val oldValue = oldEntry.value
                    if (oldValue == null || !ifAbsent) {
                        oldEntry.setValue(value)
                    }
                    return oldValue
                }
            }
            // Create a new Entry.
            val newEntry = LHMEntry<K?, V?>(hashCode, key, value)
            if (emptyIndex == -1) {
                // Grow.
                grow(newEntry)
            } else {
                contents.get(emptyIndex) = newEntry
            }
            size++
            return null
        }

        /**
         * Called under lock.
         * @param entry The entry to add (reason for growth).
         */
        private fun grow(entry: LHMEntry<K?, V?>?) {
            val oldLength = contents.size
            val newContents = arrayOfNulls<LHMEntry<*, *>?>(
                contents.size + Math.max(
                    2,
                    contents.size / 3
                )
            ) as Array<LHMEntry<K?, V?>?>
            System.arraycopy(contents, 0, newContents, 0, contents.size)
            newContents[oldLength] = entry
            contents = newContents
        }

        /**
         * Blind adding of the entry to a free place. Called under lock.
         *
         * @param entry
         */
        fun addEntry(entry: LHMEntry<K?, V?>?) {
            size++
            for (i in contents.indices) {
                if (contents.get(i) == null) {
                    contents.get(i) = entry
                    return
                }
            }
            // Need to grow.
            grow(entry)
        }

        /**
         * Called under lock.
         * @param hashCode
         * @param key
         * @return
         */
        fun remove(hashCode: Int, key: K?): V? {
            return if (size == 0) {
                null
            } else {
                for (i in contents.indices) {
                    val entry = contents.get(i)
                    if (entry != null && entry.equalsKey(hashCode, key)) {
                        contents.get(i) = null
                        size--
                        return entry.value
                    }
                }
                null
            }
        }

        /**
         * Not necessarily called under lock.
         * @param hashCode
         * @param key
         * @return
         */
        operator fun get(hashCode: Int, key: K?): V? {
            val contents = contents // Mind iteration.
            return if (size == 0) {
                null
            } else {
                for (i in contents.indices) {
                    val entry = contents.get(i)
                    if (entry != null && entry.equalsKey(hashCode, key)) {
                        return entry.value
                    }
                }
                null
            }
        }

        /**
         * Not necessarily called under lock.
         * @param hashCode
         * @param key
         * @return
         */
        fun containsKey(hashCode: Int, key: K?): Boolean {
            val contents = contents // Mind iteration.
            return if (size == 0) {
                false
            } else {
                for (i in contents.indices) {
                    val entry = contents.get(i)
                    if (entry != null && entry.equalsKey(hashCode, key)) {
                        return true
                    }
                }
                false
            }
        }

        /**
         * Called under lock.
         */
        fun clear() {
            Arrays.fill(contents, null) // (Entries might be reused on iteration.)
            size = 0
        }
    }

    internal class LHMIterator<K, V>     // (Lazily advance.)
        (private val map: HashMapLOW<K?, V?>?, private var buckets: Array<LHMBucket<K?, V?>?>?) :
        MutableIterator<MutableMap.MutableEntry<K?, V?>?> {
        /** Next index to check.  */
        private var bucketsIndex = 0
        private var currentBucket: Array<LHMEntry<K?, V?>?>? = null

        /** Next index to check.  */
        private var currentBucketIndex = 0
        private var currentEntry: LHMEntry<K?, V?>? = null
        private var lastReturnedKey: K? = null

        /**
         * Advance internal state (generic/root). Set currentEntry or reset
         * buckets to null.
         */
        private fun advance() {
            currentEntry = null
            if (buckets == null || currentBucket != null && advanceBucket()) {
                return
            }
            for (i in bucketsIndex until buckets.size) {
                val bucket = buckets.get(i)
                if (bucket != null) {
                    currentBucket = bucket.contents // Index should already be 0.
                    if (advanceBucket()) {
                        bucketsIndex = i + 1 // Next one.
                        return
                    }
                }
            }
            // No remaining entries, finished.
            buckets = null
        }

        /**
         * Advance within currentBucket. Reset if nothing found.
         * @return true if something was found.
         */
        private fun advanceBucket(): Boolean {
            // First attempt to advance within first bucket.
            for (i in currentBucketIndex until currentBucket.size) {
                val entry = currentBucket.get(i)
                if (entry != null) {
                    currentBucketIndex = i + 1
                    currentEntry = entry
                    return true
                }
            }
            // Nothing found, reset.
            currentBucket = null
            currentBucketIndex = 0
            return false
        }

        override fun hasNext(): Boolean {
            return if (currentEntry != null) {
                true
            } else if (buckets == null) {
                false
            } else {
                advance()
                currentEntry != null
            }
        }

        override fun next(): MutableMap.MutableEntry<K?, V?>? {
            // Lazily advance.
            if (currentEntry == null) {
                advance()
                if (currentEntry == null) {
                    buckets = null
                    throw NoSuchElementException()
                }
            }
            val entry: MutableMap.MutableEntry<K?, V?>? = currentEntry
            lastReturnedKey = entry.key
            currentEntry = null
            return entry
        }

        override fun remove() {
            checkNotNull(lastReturnedKey)
            map.remove(lastReturnedKey) // TODO: CAN NOT WORK, NEED INVALIDATE ENTRY OTHERWISE
            lastReturnedKey = null
        }
    }

    internal class LHMIterable<K, V>(private val iterator: MutableIterator<MutableMap.MutableEntry<K?, V?>?>?) :
        Iterable<MutableMap.MutableEntry<K?, V?>?> {
        override fun iterator(): MutableIterator<MutableMap.MutableEntry<K?, V?>?>? {
            return iterator
        }
    }

    /** Lazily filled with objects (null iff empty).  */
    private var buckets: Array<LHMBucket<K?, V?>?>?
    private var size = 0
    private val loadFactor = 0.75f
    // TODO: Configurable: loadFactor
    // TODO: Configurable: initial size and resize multiplier for Buckets.
    // TODO: Configurable: allow shrink.
    /**
     * Initialize with a ReentrantLock.
     * @param targetSize
     * Expected (average) number of elements in the map.
     */
    constructor(targetSize: Int) : this(ReentrantLock(), targetSize) {}

    /**
     * Initialize with a certain lock.
     * @param lock
     * @param targetSize
     */
    init {
        buckets = newBuckets(targetSize)
    }

    /**
     * New buckets array for the given number of items.
     *
     * @param size
     * @return A new array to hold the given number of elements (size), using
     * internal settings.
     */
    private fun newBuckets(size: Int): Array<LHMBucket<K?, V?>?>? {
        return arrayOfNulls<LHMBucket<*, *>?>(
            Math.max(
                (1f / loadFactor * size.toFloat()).toInt(),
                targetSize
            )
        ) as Array<LHMBucket<K?, V?>?>
    }

    /**
     * Resize according to the number of elements. Called under lock.
     */
    private fun resize() {
        val newBuckets = newBuckets(size) // Hold current number of elements.
        val newLength = newBuckets.size
        // Entries are reused, but not buckets (buckets would break iteration).
        for (index in buckets.indices) {
            val bucket = buckets.get(index)
            if (bucket != null && bucket.size > 0) {
                for (j in bucket.contents.indices) {
                    val entry = bucket.contents.get(j)
                    if (entry != null) {
                        val newIndex = getBucketIndex(entry.hashCode, newLength)
                        var newBucket = newBuckets.get(newIndex)
                        if (newBucket == null) {
                            newBucket = LHMBucket()
                            newBuckets.get(newIndex) = newBucket
                        }
                        newBucket.addEntry(entry)
                    }
                }
            }
        }
        buckets = newBuckets
    }

    /**
     * Not under Lock.
     * @return
     */
    fun size(): Int {
        return size
    }

    /**
     * Not under lock.
     * @return
     */
    fun isEmpty(): Boolean {
        return size == 0
    }

    /**
     * Clear the map, detaching from iteration by unlinking storage containers.
     */
    fun clear() {
        lock.lock()
        buckets = newBuckets(targetSize)
        size = 0
        lock.unlock()
    }

    /**
     * Immediate put, under lock.
     *
     * @param key
     * @param value
     * @return
     */
    fun put(key: K?, value: V?): V? {
        return put(key, value, false)
    }

    /**
     * Immediate put, only if there is no value or a null value set for the key,
     * under lock.
     *
     * @param key
     * @param value
     * @return
     */
    fun putIfAbsent(key: K?, value: V?): V? {
        return put(key, value, true)
    }

    /**
     *
     * @param key
     * @param value
     * @param ifAbsent
     * If true, an existing non-null (!) value will not be
     * overridden.
     * @return
     */
    private fun put(key: K?, value: V?, ifAbsent: Boolean): V? {
        val hashCode = getHashCode<K?>(key)
        lock.lock()
        val index = getBucketIndex(hashCode, buckets.size)
        var bucket = buckets.get(index)
        if (bucket == null) {
            bucket = LHMBucket()
            buckets.get(index) = bucket
        }
        val oldValue = bucket.put(hashCode, key, value, ifAbsent)
        if (oldValue == null) {
            size++
            if (size > (loadFactor * buckets.size.toFloat()).toInt()) {
                resize()
            }
        }
        lock.unlock()
        return oldValue
    }

    /**
     * Immediate remove, under lock.
     *
     * @param key
     * @return
     */
    fun remove(key: K?): V? {
        val hashCode = getHashCode<K?>(key)
        lock.lock()
        val value = removeUnderLock(hashCode, key)
        // TODO: Shrink, if necessary.
        lock.unlock()
        return value
    }

    /**
     * Remove a value for a given key. Called under lock. Not intended to
     * shrink, due to being called on bulk removal.
     *
     * @param hashCode
     * @param key
     * @return
     */
    private fun removeUnderLock(hashCode: Int, key: K?): V? {
        val index = getBucketIndex(hashCode, buckets.size)
        val bucket = buckets.get(index)
        return if (bucket == null || bucket.size == 0) {
            null
        } else {
            val value = bucket.remove(hashCode, key)
            if (value != null) {
                size--
            }
            value
        }
    }

    /**
     * Remove all given keys, using minimal locking.
     *
     * @param keys
     */
    fun remove(keys: MutableCollection<K?>?) {
        lock.lock()
        for (key in keys) {
            val hashCode = getHashCode<K?>(key)
            removeUnderLock(hashCode, key)
        }
        lock.unlock()
    }

    /**
     * Retrieve a value. Does not use locking.
     *
     * @param key
     * The stored value for the given key. Returns null if no value
     * is stored.
     */
    operator fun get(key: K?): V? {
        val hashCode = getHashCode<K?>(key)
        val buckets = buckets
        val bucket = buckets.get(getBucketIndex(hashCode, buckets.size))
        return if (bucket == null || bucket.size == 0) {
            null
        } else {
            bucket[hashCode, key]
        }
    }

    /**
     * Retrieve a value for a given key, or null if not existent. This method
     * uses locking.
     *
     * @param key
     */
    fun getLocked(key: K?): V? {
        lock.lock()
        val value = get(key)
        lock.unlock()
        return value
    }

    /**
     * Test if a mapping for this key exists. Accurate if key is null. Does not
     * use locking.
     *
     * @param key
     * @return
     */
    fun containsKey(key: K?): Boolean {
        val hashCode = getHashCode<K?>(key)
        val buckets = buckets
        val bucket = buckets.get(getBucketIndex(hashCode, buckets.size))
        return if (bucket == null || bucket.size == 0) {
            false
        } else {
            bucket.containsKey(hashCode, key)
        }
    }

    /**
     * Test if a mapping for this key exists. Accurate if key is null. This does
     * use locking.
     *
     * @param key
     * @return
     */
    fun containsKeyLocked(key: K?): Boolean {
        lock.lock()
        val res = containsKey(key)
        lock.unlock()
        return res
    }

    /**
     * Get an iterator reflecting this 'stage of resetting'. During iteration,
     * entries may get removed or added, values changed. Concurrent modification
     * will not let the iteration fail.
     * <hr></hr>
     * This operation does not use locking.
     *
     * @return
     */
    operator fun iterator(): MutableIterator<MutableMap.MutableEntry<K?, V?>?>? {
        return if (size == 0) LHMIterator<K?, V?>(null, null) else LHMIterator<K?, V?>(this, buckets)
    }

    /**
     * Get an Iterable containing the same iterator, as is returned by
     * iterator(). See: [.iterator]
     *
     * @return
     */
    fun iterable(): Iterable<MutableMap.MutableEntry<K?, V?>?>? {
        return LHMIterable<K?, V?>(iterator())
    }

    /**
     * Get all keys as a LinkedHashSet fit for iteration. The returned set is a
     * new instance, so changes don't affect the original HashMapLOW instance.
     *
     * @return
     */
    fun getKeys(): MutableCollection<K?>? {
        val out: MutableSet<K?> = LinkedHashSet()
        val it = iterator()
        while (it.hasNext()) {
            out.add(it.next().key)
        }
        return out
    }

    companion object {
        ///////////////////////
        // Static members
        ///////////////////////
        private fun <K> getHashCode(key: K?): Int {
            return key?.hashCode() ?: 0
        }

        private fun getBucketIndex(hashCode: Int, buckets: Int): Int {
            return Math.abs(hashCode) % buckets
        }
    }
}