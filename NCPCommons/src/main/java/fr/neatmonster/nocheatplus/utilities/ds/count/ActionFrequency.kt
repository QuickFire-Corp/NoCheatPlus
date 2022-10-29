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
package fr.neatmonster.nocheatplus.utilities.ds.count

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
 * Keep track of frequency of some action,
 * put weights into buckets, which represent intervals of time.
 * @author mc_dev
 */
class ActionFrequency @JvmOverloads constructor(nBuckets: Int, durBucket: Long, noAutoReset: Boolean = false) {
    /** Reference time for the transition from the first to the second bucket.  */
    private var time: Long = 0

    /**
     * Time of last update (add). Should be the "time of the last event" for the
     * usual case.
     */
    private var lastUpdate: Long = 0
    private val noAutoReset: Boolean

    /**
     * Buckets to fill weights in, each represents an interval of durBucket duration,
     * index 0 is the latest, highest index is the oldest.
     * Weights will get filled into the next buckets with time passed.
     */
    private val buckets: FloatArray?

    /** Duration in milliseconds that one bucket covers.  */
    private val durBucket: Long
    /**
     *
     * @param nBuckets
     * @param durBucket
     * @param noAutoReset
     * Set to true, to prevent auto-resetting with
     * "time ran backwards". Setting this to true is recommended if
     * larger time frames are monitored, to prevent data loss.
     */
    /**
     * This constructor will set noAutoReset to false, optimized for short term
     * accounting.
     *
     * @param nBuckets
     * @param durBucket
     */
    init {
        buckets = FloatArray(nBuckets)
        this.durBucket = durBucket
        this.noAutoReset = noAutoReset
    }

    /**
     * Update and add (updates reference and update time).
     * @param now
     * @param amount
     */
    fun add(now: Long, amount: Float) {
        update(now)
        buckets.get(0) += amount
    }

    /**
     * Unchecked addition of amount to the first bucket.
     * @param amount
     */
    fun add(amount: Float) {
        buckets.get(0) += amount
    }

    /**
     * Update without adding, also updates reference and update time. Detects
     * time running backwards.
     *
     * @param now
     */
    fun update(now: Long) {
        val diff = now - time
        if (now < lastUpdate) {
            // Time ran backwards.
            if (noAutoReset) {
                // Only update time and lastUpdate.
                lastUpdate = now
                time = lastUpdate
            } else {
                // Clear all.
                clear(now)
                return
            }
        } else if (diff >= durBucket * buckets.size) {
            // Clear (beyond range).
            clear(now)
            return
        } else if (diff < durBucket) {
            // No changes (first bucket).
        } else {
            val shift = (diff.toFloat() / durBucket.toFloat()).toInt()
            // Update buckets.
            for (i in 0 until buckets.size - shift) {
                buckets.get(buckets.size - (i + 1)) = buckets.get(buckets.size - (i + 1 + shift))
            }
            for (i in 0 until shift) {
                buckets.get(i) = 0f
            }
            // Set time according to bucket duration (!).
            time += durBucket * shift
        }
        // Ensure lastUpdate is set.
        lastUpdate = now
    }

    /**
     * Clear all counts, reset reference and update time.
     * @param now
     */
    fun clear(now: Long) {
        for (i in buckets.indices) {
            buckets.get(i) = 0f
        }
        lastUpdate = now
        time = lastUpdate
    }

    /**
     * @param factor
     * @return
     */
    @Deprecated(
        """Use instead: score(float).
      """
    )
    fun getScore(factor: Float): Float {
        return score(factor)
    }

    /**
     * @param factor
     * @return
     */
    @Deprecated(
        """Use instead: score(float).
      """
    )
    fun getScore(bucket: Int): Float {
        return bucketScore(bucket)
    }

    /**
     * Get a weighted sum score, weight for bucket i: w(i) = factor^i.
     * @param factor
     * @return
     */
    fun score(factor: Float): Float {
        return sliceScore(0, buckets.size, factor)
    }

    /**
     * Get score of a certain bucket. At own risk.
     * @param bucket
     * @return
     */
    fun bucketScore(bucket: Int): Float {
        return buckets.get(bucket)
    }

    /**
     * Get score of first end buckets, with factor.
     * @param end Number of buckets including start. The end is not included.
     * @param factor
     * @return
     */
    fun leadingScore(end: Int, factor: Float): Float {
        return sliceScore(0, end, factor)
    }

    /**
     * Get score from start on, with factor.
     * @param start This is included.
     * @param factor
     * @return
     */
    fun trailingScore(start: Int, factor: Float): Float {
        return sliceScore(start, buckets.size, factor)
    }

    /**
     * Get score from start on, until before end, with factor.
     * @param start This is included.
     * @param end This is not included.
     * @param factor
     * @return
     */
    fun sliceScore(start: Int, end: Int, factor: Float): Float {
        var score = buckets.get(start)
        var cf = factor
        for (i in start + 1 until end) {
            score += buckets.get(i) * cf
            cf *= factor
        }
        return score
    }

    /**
     * Set the value for a buckt.
     * @param n
     * @param value
     */
    fun setBucket(n: Int, value: Float) {
        buckets.get(n) = value
    }

    /**
     * Set the reference time and last update time.
     * @param time
     */
    fun setTime(time: Long) {
        this.time = time
        lastUpdate = time
    }

    /**
     * Get the reference time for the transition from the first to the second bucket.
     * @return
     */
    fun lastAccess(): Long { // TODO: Should rename this.
        return time
    }

    /**
     * Get the last time when update was called (adding).
     * @return
     */
    fun lastUpdate(): Long {
        return lastUpdate
    }

    /**
     * Get the number of buckets.
     * @return
     */
    fun numberOfBuckets(): Int {
        return buckets.size
    }

    /**
     * Get the duration of a bucket in milliseconds.
     * @return
     */
    fun bucketDuration(): Long {
        return durBucket
    }

    /**
     * Serialize to a String line.
     * @return
     */
    fun toLine(): String? {
        // TODO: Backwards-compatible lastUpdate ?
        val buffer = StringBuilder(50)
        buffer.append(buckets.size.toString() + "," + durBucket + "," + time)
        for (i in buckets.indices) {
            buffer.append("," + buckets.get(i))
        }
        return buffer.toString()
    }

    companion object {
        /**
         * Update and then reduce all given ActionFrequency instances by the given
         * amount, capped at a maximum of 0 for the resulting first bucket score.
         *
         * @param amount
         * The amount to subtract.
         * @param freqs
         */
        @kotlin.jvm.JvmStatic
        fun reduce(time: Long, amount: Float, vararg freqs: ActionFrequency?) {
            for (i in freqs.indices) {
                val freq = freqs[i]
                freq.update(time)
                freq.setBucket(0, Math.max(0f, freq.bucketScore(0) - amount))
            }
        }

        /**
         * Update and then reduce all given ActionFrequency instances by the given
         * amount, without capping the result.
         *
         * @param amount
         * The amount to subtract.
         * @param freqs
         */
        @kotlin.jvm.JvmStatic
        fun subtract(time: Long, amount: Float, vararg freqs: ActionFrequency?) {
            for (i in freqs.indices) {
                val freq = freqs[i]
                freq.update(time)
                freq.setBucket(0, freq.bucketScore(0) - amount)
            }
        }

        /**
         * Deserialize from a string.
         * @param line
         * @return
         */
        fun fromLine(line: String?): ActionFrequency? {
            // TODO: Backwards-compatible lastUpdate ?
            val split: Array<String?> = line.split(",".toRegex()).toTypedArray()
            if (split.size < 3) throw RuntimeException("Bad argument length.") // TODO
            val n = split[0].toInt()
            val durBucket = split[1].toLong()
            val time = split[2].toLong()
            val buckets = FloatArray(split.size - 3)
            if (split.size - 3 != buckets.size) throw RuntimeException("Bad argument length.") // TODO
            for (i in 3 until split.size) {
                buckets[i - 3] = split[i].toFloat()
            }
            val freq = ActionFrequency(n, durBucket)
            freq.setTime(time)
            for (i in buckets.indices) {
                freq.setBucket(i, buckets[i])
            }
            return freq
        }
    }
}