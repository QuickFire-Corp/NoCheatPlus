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
import fr.neatmonster.nocheatplus.utilities.ds.map.HashMapLOW
import fr.neatmonster.nocheatplus.utilities.ds.map.HashMapLOW.LHMBucket
import java.util.concurrent.locks.ReentrantLock
import fr.neatmonster.nocheatplus.utilities.ds.map.HashMapLOW.LHMIterator
import fr.neatmonster.nocheatplus.utilities.ds.map.HashMapLOW.LHMIterable
import fr.neatmonster.nocheatplus.utilities.ds.map.ManagedMap.ValueWrap
import fr.neatmonster.nocheatplus.utilities.ds.map.AbstractCoordHashMap
import fr.neatmonster.nocheatplus.utilities.ds.map.CoordMap
import fr.neatmonster.nocheatplus.utilities.ds.map.CoordHashMap
import fr.neatmonster.nocheatplus.utilities.ds.map.LinkedCoordHashMap.LinkedHashEntry
import fr.neatmonster.nocheatplus.utilities.ds.map.LinkedCoordHashMap
import fr.neatmonster.nocheatplus.utilities.ds.map.LinkedCoordHashMap.MoveOrder
import fr.neatmonster.nocheatplus.utilities.ds.corw.DualCollection
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
import fr.neatmonster.nocheatplus.utilities.PenaltyTime
import fr.neatmonster.nocheatplus.test.TestWorkarounds
import fr.neatmonster.nocheatplus.test.TestAcceptDenyCounters
import fr.neatmonster.nocheatplus.workaround.SimpleWorkaroundRegistry
import fr.neatmonster.nocheatplus.utilities.ds.count.ActionAccumulator
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.SimpleCharPrefixTree
import org.junit.Assert
import org.junit.Test
import java.util.*

class TestCoordMap {
    class Pos(val x: Int, val y: Int, val z: Int) {
        private val hash: Int

        init {
            hash = getHash(x, y, z)
        }

        override fun equals(obj: Any?): Boolean {
            if (obj === this) {
                return true
            }
            return if (obj is Pos) {
                val other = obj as Pos?
                other.hash == hash && other.x == x && other.y == y && other.z == z
            } else {
                false
            }
        }

        override fun hashCode(): Int {
            return hash
        }

        companion object {
            private const val p1 = 73856093
            private const val p2 = 19349663
            private const val p3 = 83492791
            private fun getHash(x: Int, y: Int, z: Int): Int {
                return p1 * x xor p2 * y xor p3 * z
            }
        }
    }

    private val extraTesting = BuildParameters.testLevel > 0
    private val suggestedSamples = if (extraTesting) 40000 else 1250
    fun getRandomCoords(n: Int, max: Int, random: Random?): Array<IntArray?>? {
        val coords = Array<IntArray?>(n) { IntArray(3) }
        for (i in 0 until n) {
            for (j in 0..2) {
                coords[i].get(j) = random.nextInt(2 * max) - max
            }
        }
        return coords
    }

    fun getUniqueRandomCoords(n: Int, max: Int, random: Random?): Array<IntArray?>? {
        val present: MutableSet<Pos?> = HashSet()
        var failures = 0
        val coords = Array<IntArray?>(n) { IntArray(3) }
        for (i in 0 until n) {
            val unique = false
            var pos: Pos? = null
            while (!unique) {
                pos = Pos(random.nextInt(2 * max) - max, random.nextInt(2 * max) - max, random.nextInt(2 * max) - max)
                if (!present.contains(pos)) break
                failures++
                if (failures >= 2 * n) {
                    throw RuntimeException("Too many failed attempts to create a unique coordinate.")
                }
            }
            coords[i].get(0) = pos.x
            coords[i].get(1) = pos.y
            coords[i].get(2) = pos.z
            present.add(pos)
        }
        present.clear()
        return coords
    }

    fun getIndexMap(coords: Array<IntArray?>?): MutableMap<Int?, IntArray?>? {
        val indexMap: MutableMap<Int?, IntArray?> = HashMap(coords.size)
        for (i in coords.indices) {
            indexMap[i] = coords.get(i)
        }
        return indexMap
    }

    /**
     * Fill map and check if filled in elements are inside (no size check).
     * @param map
     * @param coords
     */
    fun fillMap(map: CoordMap<Int?>?, coords: Array<IntArray?>?) {
        for (i in coords.indices) {
            map.put(coords.get(i).get(0), coords.get(i).get(1), coords.get(i).get(2), i)
            val value = map.get(coords.get(i).get(0), coords.get(i).get(1), coords.get(i).get(2))
            if (value == null) Assert.fail("Value is null, get after put: $i") else if (value.toInt() != i) Assert.fail(
                "get right after put"
            )
            if (!map.contains(
                    coords.get(i).get(0),
                    coords.get(i).get(1),
                    coords.get(i).get(2)
                )
            ) Assert.fail("Contains returns false: $i")
        }
    }

    /**
     * Match map contents (must match exactly).
     * @param map
     * @param coords
     */
    fun matchAll(map: CoordMap<Int?>?, coords: Array<IntArray?>?) {
        for (i in coords.indices) {
            val value = map.get(coords.get(i).get(0), coords.get(i).get(1), coords.get(i).get(2))
            if (value == null) Assert.fail("Value is null instead of $i")
            if (value.toInt() != i) Assert.fail("Wrong value: $value vs. $i")
            if (!map.contains(
                    coords.get(i).get(0),
                    coords.get(i).get(1),
                    coords.get(i).get(2)
                )
            ) Assert.fail("Contains returns false.")
        }
        if (map.size() != coords.size) Assert.fail("Iterator wrong number of elements: " + map.size() + "/" + coords.size)
    }

    /**
     * Match map contents with an (must match exactly).
     * @param map
     * @param indexMap
     */
    fun matchAllIterator(map: CoordMap<Int?>?, indexMap: MutableMap<Int?, IntArray?>?) {
        val it = map.iterator()
        val found: MutableSet<Int?> = HashSet()
        while (it.hasNext()) {
            val entry = it.next()
            val value = entry.value
            if (value == null) Assert.fail("Null value.")
            val pos = indexMap.get(value)
            //			if (pos == null) fail
            if (pos.get(0) != entry.x || pos.get(1) != entry.y || pos.get(2) != entry.z) Assert.fail("Wrong coordinates.")
            if (map.get(pos.get(0), pos.get(1), pos.get(2)).toInt() != value.toInt()) Assert.fail("Wrong value.")
            if (found.contains(value)) Assert.fail("Already found: $value")
            if (!map.contains(pos.get(0), pos.get(1), pos.get(2))) Assert.fail("Contains returns false")
            found.add(value)
        }
        if (found.size != indexMap.size) Assert.fail("Iterator wrong number of elements: " + found.size + "/" + indexMap.size)
    }

    /**
     * Remove all coords (expect map to be only filled with those).
     * @param map
     * @param coords
     */
    fun removeAll(map: CoordMap<Int?>?, coords: Array<IntArray?>?) {
        for (i in coords.indices) {
            if (map.remove(coords.get(i).get(0), coords.get(i).get(1), coords.get(i).get(2)).toInt() != i) Assert.fail(
                "removed should be $i"
            )
            val expectedSize = coords.size - (i + 1)
            if (map.size() != expectedSize) Assert.fail("Bad size (" + map.size() + "), expect " + expectedSize)
            if (map.get(
                    coords.get(i).get(0),
                    coords.get(i).get(1),
                    coords.get(i).get(2)
                ) != null
            ) Assert.fail("get right after remove not null")
            if (map.contains(
                    coords.get(i).get(0),
                    coords.get(i).get(1),
                    coords.get(i).get(2)
                )
            ) Assert.fail("Still contains")
        }
        if (map.size() != 0) Assert.fail("Map not emptied, left: " + map.size())
    }

    /**
     * Remove all coords using an iterator (expect map to be only filled with those).
     * @param map
     * @param indexMap
     */
    fun removeAllIterator(map: CoordMap<Int?>?, indexMap: MutableMap<Int?, IntArray?>?) {
        val it = map.iterator()
        val removed: MutableSet<Int?> = HashSet()
        while (it.hasNext()) {
            val entry = it.next()
            val value = entry.value
            if (value == null) Assert.fail("Null value.")
            val pos = indexMap.get(value)
            //			if (pos == null) fail
            if (pos.get(0) != entry.x || pos.get(1) != entry.y || pos.get(2) != entry.z) Assert.fail("Wrong coordinates.")
            if (removed.contains(value)) Assert.fail("Already removed: $value")
            removed.add(value)
            it.remove()
            if (map.get(pos.get(0), pos.get(1), pos.get(2)) != null) Assert.fail("get right after remove not null")
            if (map.contains(pos.get(0), pos.get(1), pos.get(2))) Assert.fail("Still contains.")
        }
        if (map.size() != 0) Assert.fail("Map not emptied, left: " + map.size())
        if (removed.size != indexMap.size) Assert.fail("Iterator wrong number of elements: " + removed.size + "/" + indexMap.size)
    }

    fun assertSize(map: CoordMap<*>?, size: Int) {
        if (map.size() != size) Assert.fail("Map returns wrong size: " + map.size() + " instead of " + size)
        var found = 0
        val it: MutableIterator<*>? = map.iterator()
        while (it.hasNext()) {
            found++
            it.next()
        }
        if (found != size) Assert.fail("Iterator has wrong number of elements: $found instead of $size")
    }

    /**
     * One integrity test series with a map with given initial size.
     * @param coords
     * @param indexMap
     * @param initialSize
     */
    fun series(coords: Array<IntArray?>?, indexMap: MutableMap<Int?, IntArray?>?, initialSize: Int, loadFactor: Float) {

        // Fill and check
        for (map in Arrays.asList(
            CoordHashMap(initialSize, loadFactor),
            LinkedCoordHashMap<Int?>(initialSize, loadFactor)
        )) {
            fillMap(map, coords)
            assertSize(map, indexMap.size)
            matchAll(map, coords)
        }

        // Fill and check with iterator.
        for (map in Arrays.asList(
            CoordHashMap(initialSize, loadFactor),
            LinkedCoordHashMap<Int?>(initialSize, loadFactor)
        )) {
            fillMap(map, coords)
            assertSize(map, indexMap.size)
            matchAllIterator(map, indexMap)
        }

        // Normal removing
        for (map in Arrays.asList(
            CoordHashMap(initialSize, loadFactor),
            LinkedCoordHashMap<Int?>(initialSize, loadFactor)
        )) {
            fillMap(map, coords)
            assertSize(map, indexMap.size)
            removeAll(map, coords)
            assertSize(map, 0)
        }

        // Removing with iterator.
        for (map in Arrays.asList(
            CoordHashMap(initialSize, loadFactor),
            LinkedCoordHashMap<Int?>(initialSize, loadFactor)
        )) {
            fillMap(map, coords)
            assertSize(map, indexMap.size)
            removeAllIterator(map, indexMap)
            assertSize(map, 0)
        }

        // Fill twice.
        for (map in Arrays.asList(
            CoordHashMap(initialSize, loadFactor),
            LinkedCoordHashMap<Int?>(initialSize, loadFactor)
        )) {
            fillMap(map, coords)
            assertSize(map, indexMap.size)
            fillMap(map, coords)
            assertSize(map, indexMap.size)
            matchAll(map, coords)
            removeAll(map, coords)
            assertSize(map, 0)
        }

        // Fill twice iterator.
        for (map in Arrays.asList(
            CoordHashMap(initialSize, loadFactor),
            LinkedCoordHashMap<Int?>(initialSize, loadFactor)
        )) {
            fillMap(map, coords)
            assertSize(map, indexMap.size)
            fillMap(map, coords)
            assertSize(map, indexMap.size)
            matchAllIterator(map, indexMap)
            removeAllIterator(map, indexMap)
            assertSize(map, 0)
        }

        // TODO: test / account for identical keys.

        // ? random sequence of actions ?
    }

    @Test
    fun testIntegrity() {
        val random = Random(System.nanoTime() - if (System.currentTimeMillis() % 2 == 1L) 37 else 137)
        val n = suggestedSamples // Number of coordinates.
        val max = 800 // Coordinate maximum.
        val coords = getUniqueRandomCoords(n, max, random)
        val indexMap = getIndexMap(coords)

        // No resize 
        series(coords, indexMap, 3 * n, 0.75f)

        // With some times resize.
        series(coords, indexMap, 1, 0.75f)

        // TODO: Also test with certain sets of coords that always are the same.
        // TODO: fill in multiple times + size, fill in new ones (careful random) + size
    }

    @Test
    fun testLinkedCoordHashMap() {
        val random = Random(System.nanoTime() - if (System.currentTimeMillis() % 2 == 1L) 37 else 137)
        val n = suggestedSamples // Number of coordinates.
        val max = 800 // Coordinate maximum.

        // Preparecoordinates.
        val coords = getUniqueRandomCoords(n, max, random)
        val map = LinkedCoordHashMap<Int?>(1, 0.75f)

        // Use a map with these coordinates.
        fillMap(map, coords)

        // Initial iteration order.
        testIterationOrder(map, coords, 1)

        // Re-put, moving to end.
        for (i in coords.indices) {
            map.put(coords.get(i).get(0), coords.get(i).get(1), coords.get(i).get(2), i, MoveOrder.END)
            testLast(map, coords.get(i), i)
        }
        if (map.size() != coords.size) {
            Assert.fail("Map different size than coords.")
        }
        testIterationOrder(map, coords, 1)

        // Re-put, moving to front.
        for (i in coords.indices.reversed()) {
            map.put(coords.get(i).get(0), coords.get(i).get(1), coords.get(i).get(2), i, MoveOrder.FRONT)
            testFirst(map, coords.get(i), i)
        }
        if (map.size() != coords.size) {
            Assert.fail("Map different size than coords.")
        }
        testIterationOrder(map, coords, 1)

        // Map.clear
        map.clear()
        if (map.size() != 0) {
            Assert.fail("Expect map size to be 0 after clear.")
        }
        if (map.iterator(false).hasNext()) {
            Assert.fail("Expect no first element on iteration after clear.")
        }
        if (map.iterator(true).hasNext()) {
            Assert.fail("Expect no last element on iteration after clear.")
        }

        // New map with all coordinates.
        fillMap(map, coords)
        // Half the coordinates.
        val halfCoords = Array<IntArray?>(n / 2) { IntArray(3) }
        for (i in 0 until n / 2) {
            for (j in 0..2) {
                halfCoords[i].get(j) = coords.get(i * 2).get(j)
            }
        }
        // Test remove every second entry.
        for (i in 0 until n / 2) {
            map.remove(coords.get(i * 2 + 1).get(0), coords.get(i * 2 + 1).get(1), coords.get(i * 2 + 1).get(2))
            if (map.contains(
                    coords.get(i * 2 + 1).get(0),
                    coords.get(i * 2 + 1).get(1),
                    coords.get(i * 2 + 1).get(2)
                )
            ) {
                Assert.fail("Expect removed entries not to be contained in the map.")
            }
        }
        if (map.size() != n / 2) {
            Assert.fail("Map size should be halfed after removing every second element (" + map.size() + " instead of " + n / 2 + ").")
        }
        testIterationOrder(map, halfCoords, 2)

        // Test iterator.remove every second entry.
        map.clear()
        fillMap(map, coords)
        var i = 0
        val it: MutableIterator<CoordMap.Entry<Int?>?>? = map.iterator(false)
        while (it.hasNext()) {
            val entry = it.next()
            if (i % 2 == 1) {
                it.remove()
                if (map.contains(entry.getX(), entry.getY(), entry.getZ())) {
                    Assert.fail("Expect entries removed by iterator not to be in the map.")
                }
            }
            i++
        }
        if (map.size() != n / 2) {
            Assert.fail("Map size should be halfed after removing every second element with an iterator (" + map.size() + " instead of " + n / 2 + ").")
        }
        testIterationOrder(map, halfCoords, 2)


        // TODO: Some random mixtures.
    }

    private fun testIterationOrder(map: LinkedCoordHashMap<Int?>?, coords: Array<IntArray?>?, multiplyId: Int) {
        // Test if the order of iteration is correct (!).
        var i = 0
        var it: MutableIterator<CoordMap.Entry<Int?>?>? = map.iterator(false) // New entries are put to the end.
        while (it.hasNext()) {
            testNext(it, coords, i, multiplyId)
            i++
        }
        if (i != coords.size) {
            Assert.fail("Iterator different size than coords.")
        }
        if (i != map.size()) {
            Assert.fail("Iterator different size than map.")
        }
        if (map.size() != coords.size) {
            Assert.fail("Map different size than coords.")
        }
        i = coords.size - 1
        it = map.iterator(true)
        while (it.hasNext()) {
            testNext(it, coords, i, multiplyId)
            i--
        }
        if (i != -1) {
            Assert.fail("Iterator wrong size.")
        }
        if (map.size() != coords.size) {
            Assert.fail("Map different size than coords.")
        }
    }

    /**
     * Test if the last element is the expected one.
     * @param map
     * @param is
     */
    private fun testLast(map: LinkedCoordHashMap<Int?>?, coords: IntArray?, value: Int) {
        if (map.get(coords.get(0), coords.get(1), coords.get(2)) != value) {
            Assert.fail("Not even in the map: $value")
        }
        val entry: CoordMap.Entry<Int?>? = map.iterator(true).next()
        if (entry.getValue() != value) {
            Assert.fail("Wrong id: " + entry.getValue() + " instead of " + value)
        }
        if (entry.getX() != coords.get(0) || entry.getY() != coords.get(1) || entry.getZ() != coords.get(2)) {
            Assert.fail("Coordinate mismatch on $value")
        }
    }

    /**
     * Test if the first element is the expected one.
     * @param map
     * @param is
     */
    private fun testFirst(map: LinkedCoordHashMap<Int?>?, coords: IntArray?, value: Int) {
        if (map.get(coords.get(0), coords.get(1), coords.get(2)) != value) {
            Assert.fail("Not even in the map: $value")
        }
        val entry: CoordMap.Entry<Int?>? = map.iterator().next()
        if (entry.getValue() != value) {
            Assert.fail("Wrong id: " + entry.getValue() + " instead of " + value)
        }
        if (entry.getX() != coords.get(0) || entry.getY() != coords.get(1) || entry.getZ() != coords.get(2)) {
            Assert.fail("Coordinate mismatch on $value")
        }
    }

    private fun testNext(
        it: MutableIterator<CoordMap.Entry<Int?>?>?,
        coords: Array<IntArray?>?,
        matchIndex: Int,
        multiplyId: Int
    ) {
        val entry = it.next()
        if (entry.getValue().toInt() != matchIndex * multiplyId) {
            Assert.fail("Index vs. value mismatch, expect " + matchIndex * multiplyId + ", got instead: " + entry.getValue())
        }
        if (entry.getX() != coords.get(matchIndex).get(0) || entry.getY() != coords.get(matchIndex)
                .get(1) || entry.getZ() != coords.get(matchIndex).get(2)
        ) {
            // Very unlikely.
            Assert.fail("Coordinate mismatch at index: $matchIndex")
        }
    }
}