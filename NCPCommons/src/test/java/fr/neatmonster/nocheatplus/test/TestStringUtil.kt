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
import fr.neatmonster.nocheatplus.utilities.StringUtil
import fr.neatmonster.nocheatplus.workaround.SimpleWorkaroundRegistry
import fr.neatmonster.nocheatplus.utilities.ds.count.ActionAccumulator
import fr.neatmonster.nocheatplus.utilities.ds.prefixtree.SimpleCharPrefixTree
import org.junit.Assert
import org.junit.Test

/**
 * Tests for StringUtil.
 * @author dev1mc
 */
class TestStringUtil {
    private fun assertCount(data: String?, searchFor: Char, num: Int) {
        val res = StringUtil.count(data, searchFor)
        if (res != num) {
            Assert.fail("Expect to find '$searchFor' $num times in '$data', got instead: $res")
        }
    }

    @Test
    fun testCount() {
        assertCount("", 'x', 0)
        assertCount("o", 'x', 0)
        assertCount("x", 'x', 1)
        assertCount("xo", 'x', 1)
        assertCount("ox", 'x', 1)
        assertCount("oxo", 'x', 1)
        assertCount("xox", 'x', 2)
        assertCount("xxo", 'x', 2)
        assertCount("oxx", 'x', 2)
        assertCount("230489tuvn1374z1hxk,34htmc1", '3', 3)
    }

    private fun recursiveFail(now: Int, max: Int) {
        var now = now
        now++
        if (now >= max) {
            throw RuntimeException("Reached max. recursion depth: $max")
        } else {
            recursiveFail(now, max)
        }
    }

    /**
     * Indirectly by counting line breaks with StringUtil.
     * @param recursionDepth
     * @param minSize
     * @param trim
     */
    private fun assertMinimumStackTraceLength(recursionDepth: Int, minSize: Int, trim: Boolean) {
        try {
            recursiveFail(0, recursionDepth)
        } catch (ex: RuntimeException) {
            val s = StringUtil.stackTraceToString(ex, true, trim)
            val n = StringUtil.count(s, '\n')
            if (n < minSize) {
                Assert.fail("Excpect at least $minSize line breaks, got instead: $n")
            }
        }
    }

    /**
     * Indirectly by counting line breaks with StringUtil.
     * @param recursionDepth
     * @param maxSize
     * @param trim
     */
    private fun assertMaximumStackTraceLength(recursionDepth: Int, maxSize: Int, trim: Boolean) {
        try {
            recursiveFail(0, recursionDepth)
        } catch (ex: RuntimeException) {
            val s = StringUtil.stackTraceToString(ex, true, trim)
            val n = StringUtil.count(s, '\n')
            if (n > maxSize) {
                Assert.fail("Excpect at most $maxSize line breaks, got instead: $n")
            }
        }
    }

    @Test
    fun testStackTraceLinear() {
        assertMinimumStackTraceLength(1000, 1000, false)
    }

    @Test
    fun testStackTraceTrimmed() {
        assertMaximumStackTraceLength(1000, 50, true)
    }

    private fun testLeftTrim(input: String?, expectedResult: String?) {
        val result = StringUtil.leftTrim(input)
        if (expectedResult != result) {
            Assert.fail("Expect leftTrim for '$input' to return '$expectedResult', got instead: '$result'.")
        }
    }

    @Test
    fun testLeftTrim() {
        if (StringUtil.leftTrim(null) != null) {
            Assert.fail("Expect leftTrim to return null for null input, got instead: '" + StringUtil.leftTrim(null) + "'.")
        }
        for (spec in arrayOf<Array<String?>?>(
            arrayOf("", ""),
            arrayOf(" ", ""),
            arrayOf(" \t", ""),
            arrayOf("Z", "Z"),
            arrayOf("=(/CG%§87rgv", "=(/CG%§87rgv"),
            arrayOf(" X", "X"),
            arrayOf("Y ", "Y "),
            arrayOf("  TEST", "TEST"),
            arrayOf("\t\n TEST", "TEST"),
            arrayOf("   TEST ", "TEST ")
        )) {
            testLeftTrim(spec.get(0), spec.get(1))
        }
    }

    private fun testSplitChars(input: String?, expectedLength: Int, countChar: Char, vararg chars: Char) {
        val count = StringUtil.count(input, countChar)
        val res = StringUtil.splitChars(input, *chars)
        if (res.size != expectedLength) {
            Assert.fail("Expected length differs. expect=" + expectedLength + " actual=" + res.size)
        }
        if (StringUtil.count(StringUtil.join(res, ""), countChar) != count) {
            Assert.fail("Number of countChar has varied between input and output.")
        }
    }

    @Test
    fun testSplitChars() {
        testSplitChars("a,1,.3a-a+6", 6, 'a', ',', '.', '-', '+')
    }

    private fun testNonEmpty(nonEmpty: MutableCollection<String?>?) {
        for (x in nonEmpty) {
            if (x.isEmpty()) {
                Assert.fail("Empty string in non empty.")
            }
        }
    }

    private fun testNonEmptySplit(input: String?, expectedSize: Int, vararg chars: Char) {
        val res = StringUtil.getNonEmpty(StringUtil.splitChars(input, *chars), true)
        if (res.size != expectedSize) {
            Assert.fail("Expected length differs. expect=" + expectedSize + " actual=" + res.size)
        }
        testNonEmpty(res)
    }

    @Test
    fun testGetNonEmpty() {
        testNonEmptySplit("a,1,.3a-a+6", 5, ',', '.', '-', '+')
    }
}