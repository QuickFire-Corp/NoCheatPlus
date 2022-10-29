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
package fr.neatmonster.nocheatplus.test

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
import org.junit.Assert
import org.junit.Test

/**
 * Tests for HashMapLow.
 * @author asofold
 */
class TestHashMapLOW {
    // TODO: fill after remove, etc.
    // TODO: Concurrent iteration / adding / removal.
    /**
     * Basic tests.
     */
    @Test
    fun testBase() {
        val refMap: HashMap<String?, Int?> = LinkedHashMap()
        val map = HashMapLOW<String?, Int?>(100)
        testSize(map, refMap)
        fill(map, refMap, 1000, true)
        testSize(map, refMap)
        testValuesIdentity(map, refMap)
        testIterator(map)
        map.clear()
        refMap.clear()
        if (!map.isEmpty) {
            Assert.fail("Expect map to be empty after clear.")
        }
    }

    /**
     * Ordinary removal.
     */
    @Test
    fun testRemove() {
        val refMap: HashMap<String?, Int?> = LinkedHashMap()
        val map = HashMapLOW<String?, Int?>(100)
        fill(map, refMap, 1000, true)
        var i = 0
        val initialSize = map.size()
        for (entry in refMap.entries) {
            map.remove(entry.key)
            i++
            testRemoveStage(map, initialSize, i, entry)
        }
    }

    /**
     * Remove elements using an iterator.
     */
    @Test
    fun testRemoveWithIterator() {
        val refMap: HashMap<String?, Int?> = LinkedHashMap()
        val map = HashMapLOW<String?, Int?>(100)
        fill(map, refMap, 1000, true)
        var i = 0
        val initialSize = map.size()
        val it = map.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            it.remove()
            i++
            testRemoveStage(map, initialSize, i, entry)
        }
    }

    @Test
    fun testReplaceValues() {
        val refMap: HashMap<String?, Int?> = LinkedHashMap()
        val map = HashMapLOW<String?, Int?>(100)
        fill(map, refMap, 1000, true)
        for ((key, value) in refMap) {
            val tempValue: Int = -value
            // Override with tempValue (put).
            map.put(key, tempValue)
            testSize(map, refMap) // Must stay the same.
            testInPlace(map, key, tempValue)
            // Reset to the value before (put).
            map.put(key, value)
            testSize(map, refMap)
            testInPlace(map, key, value)
        }
        testValuesIdentity(map, refMap) // Finally check all.
    }

    @Test
    fun testReplaceValuesIterator() {
        val refMap: HashMap<String?, Int?> = LinkedHashMap()
        val map = HashMapLOW<String?, Int?>(100)
        fill(map, refMap, 1000, true)
        val it = map.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            val key = entry.key
            val value = entry.value
            val tempValue: Int = -value
            // Override with tempValue (put).
            map.put(key, tempValue)
            testSize(map, refMap) // Must stay the same.
            testInPlace(map, key, tempValue)
            // Reset to the value before (put).
            map.put(key, value)
            testSize(map, refMap)
            testInPlace(map, key, value)
            // Override with tempValue (entry.setValue).
            entry.setValue(tempValue)
            testSize(map, refMap) // Must stay the same.
            testInPlace(map, key, tempValue)
            // Reset to the value before (entry.setValue).
            entry.setValue(value)
            testSize(map, refMap)
            testInPlace(map, key, value)
        }
    }

    private fun testInPlace(map: HashMapLOW<String?, Int?>?, key: String?, value: Int?) {
        if (map.get(key) !== value) {
            Assert.fail("Overriding a value in-place fails. Got " + map.get(key) + " instead of " + value)
        }
    }

    private fun testRemoveStage(
        map: HashMapLOW<String?, Int?>?,
        initialSize: Int,
        removed: Int,
        entry: MutableMap.MutableEntry<String?, Int?>?
    ) {
        if (map.size() != initialSize - removed) {
            Assert.fail("Exepect entries to decrease from " + initialSize + " to " + (initialSize - removed) + " after removing " + removed + " elements.")
        }
        if (map.containsKey(entry.key)) {
            Assert.fail("Removed key still inside map: " + entry.key)
        }
        testIterator(map) // Somewhat concurrent.
    }

    private fun testIterator(map: HashMapLOW<String?, Int?>?) {
        val it = map.iterator()
        val refMap2: MutableMap<String?, Int?> = HashMap()
        var i = 0
        while (it.hasNext()) {
            i++
            val (key, value) = it.next()
            refMap2[key] = value
        }
        if (i != map.size()) {
            Assert.fail("Number of elements iterated is different to map size.")
        }
        // Test values identity vs. refMap2.
        testValuesIdentity(map, refMap2)
        testSize(map, refMap2)
        // (Should not need refMap.equals(refMap2)?)
    }

    /**
     * (Size is tested with testSize.)
     * @param map
     * @param refMap
     */
    private fun testValuesIdentity(map: HashMapLOW<String?, Int?>?, refMap: MutableMap<String?, Int?>?) {
        for ((key, value) in refMap) {
            // Assume identity of objects.
            if (map.get(key) !== value) {
                Assert.fail(
                    "Inconsistent entry: expect $value for key $key, got instead: " + map.get(
                        key
                    )
                )
            }
        }
    }

    /**
     * Fill maps with identical keys and values 0 to maxCount - 1, String ->
     * Integer with the 'same' content.
     *
     * @param map
     * @param refMap
     * @param maxCount
     * @param testSize
     */
    private fun fill(
        map: HashMapLOW<String?, Int?>?,
        refMap: MutableMap<String?, Int?>?,
        maxCount: Int,
        testSize: Boolean
    ) {
        for (i in 0 until maxCount) {
            val key = Integer.toString(i)
            map.put(key, i)
            if (!map.containsKey(key)) {
                Assert.fail("Key missing after put: $key")
            }
            refMap[key] = i
            if (testSize) {
                testSize(map, refMap)
            }
        }
    }

    private fun testSize(map: HashMapLOW<*, *>?, refMap: MutableMap<*, *>?) {
        if (map.size() != refMap.size) {
            Assert.fail("Sizes differ: low=" + map.size() + " ref=" + refMap.size)
        }
        if (map.size() == 0 && !map.isEmpty()) {
            Assert.fail("Expect isEmpty() on size == 0.")
        }
        if (map.size() > 0 && map.isEmpty()) {
            Assert.fail("Expect !isEmpty() on size > 0")
        }
        if (map.size() < 0) {
            Assert.fail("Expect size >= 0")
        }
    }
}