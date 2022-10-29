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
import java.util.ArrayList

class TestWorkarounds {
    /**
     * Simple isolated testing for one WorkaroundCounter instance, plus parent
     * count.
     */
    @Test
    fun testWorkaroundCounter() {
        val wac = WorkaroundCounter("test.wac")
        val pc = AcceptDenyCounter()
        (wac.allTimeCounter as ICounterWithParent).parentCounter = pc
        for (i in 0..56) {
            checkCanUseAndUse(wac)
        }
        TestAcceptDenyCounters.Companion.checkSame(57, 0, "WorkaroundCounter(c/p)", wac.allTimeCounter, pc)
    }

    /**
     * Simple isolated testing for one WorkaroundCountDown instance, plus parent
     * count.
     */
    @Test
    fun testWorkaroundCountDown() {
        val wacd = WorkaroundCountDown("test.wacd", 1)
        val pc = AcceptDenyCounter()
        (wacd.allTimeCounter as ICounterWithParent).parentCounter = pc

        // Attempt to use a lot of times (all but one get denied).
        for (i in 0..140) {
            checkCanUseAndUse(wacd)
        }
        var accept = 1 // All time count.
        var deny = 140 // All time count.
        TestAcceptDenyCounters.Companion.checkSame(
            accept,
            deny,
            "Just use, a=1 (s/a/p)",
            wacd.stageCounter,
            wacd.allTimeCounter,
            pc
        )

        // Reset.
        wacd.resetConditions()
        TestAcceptDenyCounters.Companion.checkSame(accept, deny, "Reset (s/a/p)", wacd.allTimeCounter, pc)
        TestAcceptDenyCounters.Companion.checkCounts(wacd.stageCounter, 0, 0, "test.wacd.stage")

        // Attempt to use a lot of times (all but one get denied).
        for (i in 0..140) {
            checkCanUseAndUse(wacd)
        }
        accept *= 2
        deny *= 2
        TestAcceptDenyCounters.Companion.checkSame(
            accept,
            deny,
            "Just use, repeat a=1, (s/a/p)",
            wacd.allTimeCounter,
            pc
        )
        TestAcceptDenyCounters.Companion.checkCounts(wacd.stageCounter, 1, 140, "test.wacd.stage")

        // Set to 5 and use (5xaccept).
        wacd.resetConditions()
        wacd.currentCount = 5
        for (i in 0..140) {
            checkCanUseAndUse(wacd)
        }
        accept += 5
        deny += 141 - 5
        TestAcceptDenyCounters.Companion.checkSame(accept, deny, "Just use, a=5, (s/a/p)", wacd.allTimeCounter, pc)
        TestAcceptDenyCounters.Companion.checkCounts(wacd.stageCounter, 5, 141 - 5, "test.wacd.stage")

        // Set to -1 and use.
        wacd.resetConditions()
        wacd.currentCount = -1
        for (i in 0..140) {
            checkCanUseAndUse(wacd)
        }
        deny += 141
        TestAcceptDenyCounters.Companion.checkSame(accept, deny, "Just use, a=0 (s/a/p)", wacd.allTimeCounter, pc)
        TestAcceptDenyCounters.Companion.checkCounts(wacd.stageCounter, 0, 141, "test.wacd.stage")

        // Set to 14 and use (14xaccept).
        wacd.resetConditions()
        wacd.currentCount = 14
        for (i in 0..140) {
            checkCanUseAndUse(wacd)
        }
        accept += 14
        deny += 141 - 14
        TestAcceptDenyCounters.Companion.checkSame(accept, deny, "Just use, a=14 (s/a/p)", wacd.allTimeCounter, pc)
        TestAcceptDenyCounters.Companion.checkCounts(wacd.stageCounter, 14, 141 - 14, "test.wacd.stage")

        // TODO: Might also test getNewInstance().
    }

    @Test
    fun testSimpleWorkaroundRegistry() {
        // (all sorts of tests. Consistency for workarounds and counters.).
        val reg: IWorkaroundRegistry = SimpleWorkaroundRegistry()

        // Simple tests with registering individual instances.

        // Call get for non existing counter.
        if (reg.getGlobalCounter("exist.not") != null) {
            Assert.fail("getGlobalCounter: expect null for not registered id.")
        }
        // Create a counter.
        val c_man = reg.createGlobalCounter("c.man")
        if (c_man == null) {
            Assert.fail("createGlobalCounter: expect a counter")
        }
        // Ensure the same counter is returned as last time.
        if (reg.createGlobalCounter("c.man") !== c_man) {
            Assert.fail("createGlobalCounter must return the same instance each time.")
        }

        // Register a single workaround (no parent counter).
        checkSetWorkaroundBluePrint<WorkaroundCounter?>(WorkaroundCounter("wc.man"), reg)

        // Register a single workaround with a parent counter set (created from registry).
        val wrp: IWorkaround = WorkaroundCounter("wc.man.rp") // With parent counter from registry.
        (wrp.allTimeCounter as ICounterWithParent).parentCounter = c_man
        checkSetWorkaroundBluePrint<IWorkaround?>(wrp, reg)

        // Register a single workaround with an externally created parent counter set (not in registry).
        val wep: IWorkaround = WorkaroundCounter("wc.man.ep") // With externally created parent counter.
        (wep.allTimeCounter as ICounterWithParent).parentCounter = AcceptDenyCounter()
        checkSetWorkaroundBluePrint<IWorkaround?>(wep, reg)

        // WorkaroundSet

        // Register workarounds.
        val wg1 = getWorkaroundCounters("w.man", 15)
        val wg2 = getWorkaroundcountDowns("w.man", 15)
        val wgAll: MutableList<IWorkaround?> = ArrayList(30)
        wgAll.addAll(wg1)
        wgAll.addAll(wg2)
        try {
            reg.getCheckedIdSet(wg1)
            Assert.fail("Expect IllegalArgumentException for not registered workarounds.")
        } catch (ex: IllegalArgumentException) {
            // Success.
        }
        reg.setWorkaroundBluePrint(*wgAll.toTypedArray())
        val ids1: MutableList<String?> = ArrayList(reg.getCheckedIdSet(wg1))
        val ids2: MutableList<String?> = ArrayList(reg.getCheckedIdSet(wg2))
        val idsAll: MutableList<String?> = ArrayList(reg.getCheckedIdSet(wgAll))
        // Register groups.
        reg.setGroup("group.mix", Arrays.asList(ids1[0], ids2[0]))
        reg.setGroup("group.wc", ids1)
        reg.setGroup("group.wcd", ids2)
        // reg.setWorkaroundSet with string ids.
        reg.setWorkaroundSetByIds("ws.all", idsAll, "group.mix", "group.wc", "group.wcd")
        // reg.getWorkaroundSet.
        val ws = reg.getWorkaroundSet("ws.all")
        // Test the WorkaroundSet
        for (id in idsAll) {
            ws.getWorkaround(id)
        }
        // Test reset all.
        useAll(idsAll, ws)
        var accept = 1
        val deny = 0
        checkAllTimeCount(idsAll, ws, accept, deny)
        checkStageCount(ids2, ws, 1, 0)
        ws.resetConditions()
        checkAllTimeCount(idsAll, ws, accept, deny)
        checkStageCount(ids2, ws, 0, 0)
        // Reset group.wc.
        useAll(idsAll, ws)
        accept += 1
        ws.resetConditions("group.wc")
        checkAllTimeCount(idsAll, ws, accept, deny)
        checkStageCount(ids2, ws, 1, 0)
        ws.resetConditions()
        // group.wcd
        useAll(idsAll, ws)
        accept += 1
        ws.resetConditions("group.wcd")
        checkAllTimeCount(idsAll, ws, accept, deny)
        checkStageCount(ids2, ws, 0, 0)
        ws.resetConditions()
        // group.mix
        useAll(idsAll, ws)
        accept += 1
        ws.resetConditions("group.mix")
        checkAllTimeCount(idsAll, ws, accept, deny)
        TestAcceptDenyCounters.Companion.checkCounts(
            (ws.getWorkaround(ids2[0]) as IStagedWorkaround).stageCounter,
            0,
            0,
            "stageCounter/" + ids2[0]
        )
        for (i in 1 until ids2.size) {
            TestAcceptDenyCounters.Companion.checkCounts(
                (ws.getWorkaround(ids2[i]) as IStagedWorkaround).stageCounter,
                1,
                0,
                "stageCounter/" + ids2[i]
            )
        }
        ws.resetConditions()
        // TODO: Individual group reset (needs half of group.wcd).

        // TODO: More details/cases (also failure cases, exceptions, etc).
    }

    companion object {
        /**
         * Get a collection of new WorkaroundCounter instances.
         *
         * @param name
         * Prefix of naming name.class.count
         * @param repeatCount
         * @return
         */
        fun getWorkaroundCounters(name: String?, repeatCount: Int): MutableList<WorkaroundCounter?>? {
            val workarounds: MutableList<WorkaroundCounter?> = ArrayList()
            for (i in 0 until repeatCount) {
                workarounds.add(WorkaroundCounter("$name.WorkaroundCounter.$i"))
            }
            return workarounds
        }

        /**
         * Get a collection of new WorkaroundCountDown instances, initialized with
         * counting up from 1.
         *
         * @param name
         * Prefix of naming name.class.count
         * @param repeatCount
         * @return
         */
        fun getWorkaroundcountDowns(name: String?, repeatCount: Int): MutableList<WorkaroundCountDown?>? {
            val workarounds: MutableList<WorkaroundCountDown?> = ArrayList()
            for (i in 0 until repeatCount) {
                workarounds.add(WorkaroundCountDown("$name.WorkaroundCountDown.$i", i + 1))
            }
            return workarounds
        }

        fun useAll(ids: MutableCollection<String?>?, ws: WorkaroundSet?) {
            for (id in ids) {
                ws.use(id)
            }
        }

        fun checkStageCount(ids: MutableCollection<String?>?, ws: WorkaroundSet?, acceptCount: Int, denyCount: Int) {
            for (id in ids) {
                val counter = (ws.getWorkaround(id) as IStagedWorkaround).stageCounter
                TestAcceptDenyCounters.Companion.checkCounts(counter, acceptCount, denyCount, "stageCounter/$id")
            }
        }

        fun checkAllTimeCount(ids: MutableCollection<String?>?, ws: WorkaroundSet?, acceptCount: Int, denyCount: Int) {
            for (id in ids) {
                val counter = ws.getWorkaround(id).allTimeCounter
                TestAcceptDenyCounters.Companion.checkCounts(counter, acceptCount, denyCount, "allTimeCounter/$id")
            }
        }

        /**
         * Set blueprint and test:
         *
         *  * global counter existence.
         *
         *
         * @param bluePrint
         * @param reg
         * @return The given bluePrint for chaining.
         */
        fun <W : IWorkaround?> checkSetWorkaroundBluePrint(bluePrint: W?, reg: IWorkaroundRegistry?): W? {
            // Remember old settings.
            val id = bluePrint.getId()
            val oldAllTimeCount = bluePrint.getAllTimeCounter()
            if (oldAllTimeCount == null) {
                Assert.fail("getAllTimeCounter must not return null: $id")
            }
            val oldAllTimeParent =
                if (bluePrint is ICounterWithParent) (bluePrint as ICounterWithParent?).getParentCounter() else null
            val stageCount =
                if (bluePrint is IStagedWorkaround) (bluePrint as IStagedWorkaround?).getStageCounter() else null
            val oldRegCounter = reg.getGlobalCounter(id)
            // Register.
            reg.setWorkaroundBluePrint(bluePrint)
            // Demand existence of a counter for that id.
            val regCount = reg.getGlobalCounter(id)
            if (oldAllTimeParent != null && regCount == null) {
                Assert.fail("There must be a global counter present, if no parent counter was present at the time of registration: $id")
            }
            // Demand existence of global counter, if none was set.
            if (oldAllTimeParent == null && reg.getGlobalCounter(id) == null) {
                Assert.fail("A parent counter must be present, after registering a workaround without a parent counter set: $id")
            }
            // Demand no counter to be registered, if none was set and the bluePrint had a parent counter set.
            if (oldRegCounter == null && oldAllTimeParent != null && reg.getGlobalCounter(id) != null) {
                Assert.fail("Expect no counter to be registered, if none was and a parent had already been set: $id")
            }
            // Demand the registered counter to stay the same, if it already existed.
            if (oldRegCounter != null && oldRegCounter !== reg.getGlobalCounter(id)) {
                Assert.fail("Expect an already registeded counter not to change: $id")
            }

            // Fetch an instance for this id.
            // W newInstance = reg.getWorkaround(id, bluePrint.getClass()); // FAIL
            val newInstance = reg.getWorkaround(id)
            // Demand newInstancse is not bluePrint.
            if (newInstance === bluePrint) {
                Assert.fail("getWorkaround must not return the same instance: $id")
            }
            // Demand class identity (for now).
            if (bluePrint.javaClass != newInstance.javaClass) {
                Assert.fail("Demand class identity for factory methods (subject to discussion: ): $id")
            }
            // Demand identity of a global counter, if none was set before.
            if (oldAllTimeParent != null && oldAllTimeParent !== newInstance.allTimeCounter) {
                Assert.fail("Expect the global counter to be the same as the parent of a new instance, if none was set: $id")
            }
            // Demand stage count to differ.
            if (newInstance is IStagedWorkaround && (newInstance as IStagedWorkaround).stageCounter === stageCount) {
                Assert.fail("Expect stage counter of a new instance to differ: $id")
            }

            // (More specific stuff is possible.)
            return bluePrint
        }

        /**
         * Check consistency of results of canUse and use called in that order.
         *
         * @param workaround
         * @return Result of use().
         */
        fun checkCanUseAndUse(workaround: IWorkaround?): Boolean {
            val preRes = workaround.canUse()
            val res = workaround.use()
            if (!preRes && res) {
                Assert.fail("Inconsistency: use() must not return true, if canUse() has returned false.")
            }
            return res
        }
    }
}