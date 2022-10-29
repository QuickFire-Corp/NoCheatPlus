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
import java.util.concurrent.locks.Lock

/**
 * Keep two collections, one for access by the primary thread, one for
 * asynchronous access, for iteration within the primary thread. This is
 * intended to provide a way to keep locking away from the primary thread as
 * much as possible, for cases where it's certain that both the primary thread
 * as well as other threads will add elements, at least on occasion. Internal
 * collections are kept null, with a little hysteresis built in, to avoid
 * constant repetition of object creation. Multiple collections and differing
 * collection types can be processed under the same lock, to reduce the
 * probability of the primary thread running into contention.
 * <hr></hr>
 * The methods don't check if you're within the correct thread, consequently
 * they are named according to how they're supposed to be used. Thus use of this
 * class is not confined to the Bukkit primary thread, despite having been
 * created with that context in mind.<br></br>
 * The internal collections stay null, until elements are added. Setting the
 * fields to null, will happen lazily within mergePrimaryThread and
 * isEmptyPrimaryThread, to avoid re-initializing every iteration. So the
 * suggested sequence is: adding elements - mergePrimaryThread - only if not
 * isEmptyPrimaryThread() then iteratorPrimaryThread, because the iterator will
 * force creation of a collection, if the field is set to null. After iteration
 * use clearPrimaryThread. Instead of mergePrimaryThread() and then checking
 * isEmptyPrimaryThread, isEmtpyAfterMergePrimaryThread can be used to do both
 * in one method call.
 *
 * @author asofold
 *
 *
 * @param <T>
 * The type of stored elements.
 * @param <C>
 * The Collection type.
</C></T> */
abstract class DualCollection<T, C : MutableCollection<T?>?>
/**
 * Initialize with a new ReentrantLock.
 */ @JvmOverloads constructor(private val lock: Lock? = ReentrantLock()) {
    // Might consider setting sets to null, until used.
    private var primaryThreadCollection: MutableCollection<T?>? = null
    private var asynchronousCollection: MutableCollection<T?>? = null

    /** Once emptyCount reaches nullCount, empty collections are nulled.  */
    private var nullCount: Short = 6

    /**
     * Number of times, clear has been called with the collection being empty
     * already.
     */
    private var emptyCount: Short = 0
    /**
     * Initialized with the given lock.
     * @param lock
     * The lock to use for locking.
     */
    //////////
    // Setup
    //////////
    /**
     * Roughly control how fast lists reset. This is not exact, as both primary
     * thread and asynchronous collections being empty but not null, could both
     * cause the emptyCount to increase. Thus with one being null for a couple
     * of iterations, the count only is increased with the other being
     * empty.<br></br>
     * The emptyCount is increased within the primary thread only, on
     * mergePrimaryThread for the asynchronous collection being empty, on
     * isEmptyPrimaryThread for the primary thread collection being empty. The
     * emptyCount is reset to 0 only within isEmtpyPrimaryThread, on setting the
     * primaryThreadCollection to null.
     *
     * @param nullCount
     */
    fun setNullCount(nullCount: Short) {
        this.nullCount = nullCount
    }
    /////////////
    // Abstract
    /////////////
    /**
     * Retrieve a new collection for internal storage. <br></br>
     * Must be thread-safe, as collections may be created lazily, and on
     * occasion get nulled.
     */
    protected abstract fun newCollection(): C?
    ////////////////
    // Thread-safe
    ////////////////
    /**
     * Add an element to the asynchronous collection. <br></br>
     * Thread-safe.
     *
     * @param element
     * @return
     */
    fun addAsynchronous(element: T?): Boolean {
        lock.lock()
        if (asynchronousCollection == null) {
            asynchronousCollection = newCollection()
        }
        val res = asynchronousCollection.add(element)
        lock.unlock()
        return res
    }

    /**
     * Add multiple elements to the asynchronous collection. <br></br>
     * Thread-safe.
     *
     * @param elements
     * @return
     */
    fun addAllAsynchronous(elements: MutableCollection<T?>?): Boolean {
        return if (elements.isEmpty()) {
            false
        } else {
            lock.lock()
            if (asynchronousCollection == null) {
                asynchronousCollection = newCollection()
            }
            val res = asynchronousCollection.addAll(elements)
            lock.unlock()
            res
        }
    }

    /**
     * Test if the asynchronous collection contains an element, will use
     * locking, unless the field is set to null. <br></br>
     * Thread-safe.
     *
     * @param element
     * @return
     */
    fun containsAsynchronous(element: T?): Boolean {
        return if (asynchronousCollection == null) {
            // Opportunistic.
            false
        } else {
            lock.lock()
            // (Could be set to null within the primary thread.)
            val res = if (asynchronousCollection == null) false else asynchronousCollection.contains(element)
            lock.unlock()
            res
        }
    }
    /////////////////////////
    // Primary thread only.
    /////////////////////////
    /**
     * Add all elements from the asynchronous collection to the primary thread
     * collection under lock - this may be omitted, if the asynchronous
     * collection is nulled, to avoid locking. This will clear the asynchronous
     * collection. This will increase the emtpyCount, and would null the
     * asynchronousCollection on the emptyCount reaching the nullCount. <br></br>
     * Primary thread only.
     */
    fun mergePrimaryThread() {
        if (asynchronousCollection != null) { // Opportunistic.
            lock.lock()
            // (Can only be set to null within the primary thread.)
            internalMergePrimaryThreadNoLock()
            lock.unlock()
        }
    }

    /**
     * Demands asynchronousCollection to be not null, and to be called under
     * lock. <br></br>
     * Primary thread only.
     */
    private fun internalMergePrimaryThreadNoLock() {
        if (asynchronousCollection.isEmpty()) {
            if (++emptyCount >= nullCount) {
                asynchronousCollection = null
            }
        } else {
            if (primaryThreadCollection == null) {
                primaryThreadCollection = newCollection()
                emptyCount = 0
            }
            primaryThreadCollection.addAll(asynchronousCollection)
            asynchronousCollection.clear()
        }
    }

    /**
     * Same as mergePrimaryThread, just not locking. <br></br>
     * **Only use with external control over the lock and in locked
     * state.**<br></br>
     * Primary thread only.
     */
    fun mergePrimaryThreadNoLock() {
        if (asynchronousCollection != null) {
            internalMergePrimaryThreadNoLock()
        }
    }

    /**
     * Add an element to the primary thread collection. <br></br>
     * Primary thread only.
     *
     * @param element
     * @return
     */
    fun addPrimaryThread(element: T?): Boolean {
        if (primaryThreadCollection == null) {
            primaryThreadCollection = newCollection()
            emptyCount = 0
        }
        return primaryThreadCollection.add(element)
    }

    /**
     * Add multiple elements to the primary thread collection. <br></br>
     * Primary thread only.
     *
     * @param elements
     * @return
     */
    fun addAllPrimaryThread(elements: MutableCollection<T?>?): Boolean {
        if (primaryThreadCollection == null) {
            primaryThreadCollection = newCollection()
            emptyCount = 0
        }
        return primaryThreadCollection.addAll(elements)
    }

    /**
     * Test if the primary thread collection is empty. This will increase the
     * emtpyCount, if the collection is empty, and it would null the
     * primaryThreadCollection and reset the emptyCount, if the emptyCount
     * reaches the nullCount.<br></br>
     * Primary thread only.
     *
     * @return
     */
    fun isEmptyPrimaryThread(): Boolean {
        return if (primaryThreadCollection == null) {
            true
        } else if (primaryThreadCollection.isEmpty()) {
            if (++emptyCount >= nullCount) {
                primaryThreadCollection = null
                emptyCount = 0
            }
            true
        } else {
            false
        }
    }

    /**
     * Convenience method to call mergePrimaryThread() first, then return
     * isEmptyPrimaryThread().<br></br>
     * Primary thread only.
     *
     * @return If the primary thread collection is empty, after adding all
     * elements from the asynchronous collection.
     */
    fun isEmtpyAfterMergePrimaryThread(): Boolean {
        mergePrimaryThread()
        return isEmptyPrimaryThread()
    }

    /**
     * Same as isEmtpyAfterMergePrimaryThread, just not locking. <br></br>
     * **Only use with external control over the lock and in locked
     * state.**<br></br>
     * Primary thread only.
     */
    fun isEmtpyAfterMergePrimaryThreadNoLock(): Boolean {
        mergePrimaryThreadNoLock()
        return isEmptyPrimaryThread()
    }

    /**
     * Test if the primary thread collection contains the given element. <br></br>
     * Primary thread only.
     *
     * @return
     */
    fun containsPrimaryThread(element: T?): Boolean {
        return if (primaryThreadCollection == null) false else primaryThreadCollection.contains(element)
    }

    /**
     * Iterator for the primary thread collection. <br></br>
     * Primary thread only.
     *
     * @return
     */
    fun iteratorPrimaryThread(): MutableIterator<T?>? {
        // TODO: Consider to store an empty and/or unmodifiable iterator.
        if (primaryThreadCollection == null) {
            primaryThreadCollection = newCollection()
        }
        return primaryThreadCollection.iterator()
    }

    /**
     * Return a merged collection and clear internal ones - primary thread only.
     *
     * @return Returns null, if no elements are contained.
     */
    fun getMergePrimaryThreadAndClear(): MutableCollection<T?>? {
        mergePrimaryThread()
        val res = primaryThreadCollection
        primaryThreadCollection = null
        return if (res == null || res.isEmpty()) null else res
    }

    /**
     * Clear the primary thread collection. <br></br>
     * Primary thread only.
     *
     * @return
     */
    fun clearPrimaryThread() {
        if (primaryThreadCollection != null) {
            primaryThreadCollection.clear()
        }
    }
}