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
package fr.neatmonster.nocheatplus.utilities

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
import java.util.ArrayList

/**
 * String utility methods (working with or returning strings).
 * @author asofold
 */
object StringUtil {
    /** Decimal format for "#.###"  */
    @kotlin.jvm.JvmField
    val fdec3: DecimalFormat? = DecimalFormat()

    /** Decimal format for "#.#"  */
    @kotlin.jvm.JvmField
    val fdec1: DecimalFormat? = DecimalFormat()

    init {
        // 3 digits.
        val sym = fdec3.getDecimalFormatSymbols()
        fr.neatmonster.nocheatplus.utilities.sym.setDecimalSeparator('.')
        fdec3.setDecimalFormatSymbols(fr.neatmonster.nocheatplus.utilities.sym)
        fdec3.setMaximumFractionDigits(3)
        fdec3.setMinimumIntegerDigits(1)
        // 1 digit.
        fr.neatmonster.nocheatplus.utilities.sym = fdec1.getDecimalFormatSymbols()
        fr.neatmonster.nocheatplus.utilities.sym.setDecimalSeparator('.')
        fdec1.setDecimalFormatSymbols(fr.neatmonster.nocheatplus.utilities.sym)
        fdec1.setMaximumFractionDigits(1)
        fdec1.setMinimumIntegerDigits(1)
    }

    /**
     * List by boxing.
     * @param chars
     * @return
     */
    fun characterList(vararg chars: Char): MutableList<Char?>? {
        val res: MutableList<Char?> = ArrayList(chars.size)
        for (i in chars.indices) {
            res.add(chars[i])
        }
        return res
    }

    /**
     * Join parts with link, starting from startIndex.
     * @param input
     * @param startIndex
     * @param link
     * @return
     */
    fun <O : Any?> join(input: Array<O?>?, startIndex: Int, link: String?): String? {
        return join<O?>(Arrays.copyOfRange(input, startIndex, input.size), link)
    }

    /**
     * Join parts with link.
     *
     * @param input
     * @param link
     * @return
     */
    fun <O : Any?> join(input: Array<O?>?, link: String?): String? {
        return join(Arrays.asList(*input), link)
    }

    fun join(iterator: MutableIterator<out Any?>?, link: String?): String? {
        val builder = StringBuilder(1024)
        var first = true
        while (iterator.hasNext()) {
            val obj = iterator.next()
            if (!first) {
                builder.append(link)
            }
            builder.append(obj.toString())
            first = false
        }
        return builder.toString()
    }

    /**
     * Join parts with link.
     *
     * @param input
     * @param link
     * @return
     */
    fun join(input: MutableCollection<out Any?>?, link: String?): String? {
        return join(input, link, StringBuilder(Math.max(300, input.size * 10))).toString()
    }

    /**
     * Add joined parts with link.
     *
     * @param input
     * @param link
     * @return The given StringBuilder for chaining.
     */
    @kotlin.jvm.JvmStatic
    fun join(
        input: MutableCollection<out Any?>?, link: String?,
        builder: StringBuilder?
    ): StringBuilder? {
        var first = true
        for (obj in input) {
            if (!first) {
                builder.append(link)
            }
            builder.append(obj.toString())
            first = false
        }
        return builder
    }

    /**
     * Split input by all characters given (convenience method).
     *
     * @param input
     * @param chars
     * @return An (Array)List with the results.
     */
    fun split(input: String?, vararg chars: Char?): MutableList<String?>? {
        return split(input, Arrays.asList(*chars))
    }

    /**
     * Split input by all characters given (convenience method).
     *
     * @param input
     * @param chars
     * @return An (Array)List with the results.
     */
    @kotlin.jvm.JvmStatic
    fun splitChars(input: String?, vararg chars: Char): MutableList<String?>? {
        return split(input, characterList(*chars))
    }

    /**
     * Split input by all characters given (convenience method).
     *
     * @param input
     * @param chars
     * @return An (Array)List with the results.
     */
    fun split(input: String?, chars: MutableCollection<Char?>?): MutableList<String?>? {
        // TODO: Construct one regular expression to do the entire job!?
        var out: MutableList<String?>? = ArrayList()
        out.add(input)
        var queue: MutableList<String?>? = ArrayList()
        for (c in chars) {
            var hex = Integer.toHexString(c.code)
            when (hex.length) {
                1 -> hex = "000$hex"
                2 -> hex = "00$hex"
                3 -> hex = "0$hex"
            }
            for (s in out) {
                val split: Array<String?> = s.split("\\u$hex".toRegex()).toTypedArray()
                for (_s in split) {
                    queue.add(_s)
                }
            }
            val temp = out
            out = queue
            queue = temp
            queue.clear()
        }
        return out
    }

    /**
     * Add non empty strings to an output (Array)List.
     *
     * @param input
     * @param trim
     * @return
     */
    @kotlin.jvm.JvmStatic
    fun getNonEmpty(input: MutableList<String?>?, trim: Boolean): MutableList<String?>? {
        val output: MutableList<String?> = ArrayList()
        for (x in input) {
            if (trim) {
                x = x.trim { it <= ' ' }
            }
            if (!x.isEmpty()) {
                output.add(x)
            }
        }
        return output
    }

    /**
     * Return if the two Strings are similar based on the given threshold.
     *
     * @param s
     * the first String, must not be null
     * @param t
     * the second String, must not be null
     * @param threshold
     * the minimum value of the correlation coefficient
     * @return result true if the two Strings are similar, false otherwise
     */
    @kotlin.jvm.JvmStatic
    fun isSimilar(s: String?, t: String?, threshold: Float): Boolean {
        return 1.0f - levenshteinDistance(s, t).toFloat() / Math.max(
            1.0,
            Math.max(s.length, t.length).toDouble()
        ) > threshold
    }

    /**
     * Find the Levenshtein distance between two Strings.
     *
     * This is the number of changes needed to change one String into another,
     * where each change is a single character modification (deletion, insertion or substitution).
     *
     * @param s
     * the first String, must not be null
     * @param t
     * the second String, must not be null
     * @return result distance
     */
    fun levenshteinDistance(s: CharSequence?, t: CharSequence?): Int {
        var s = s
        var t = t
        require(!(s == null || t == null)) { "Strings must not be null" }
        var n = s.length
        var m = t.length
        if (n == 0) return m else if (m == 0) return n
        if (n > m) {
            val tmp = s
            s = t
            t = tmp
            n = m
            m = t.length
        }
        var p: IntArray? = IntArray(n + 1)
        var d: IntArray? = IntArray(n + 1)
        var _d: IntArray?
        var i: Int
        var j: Int
        var t_j: Char
        var cost: Int
        i = 0
        while (i <= n) {
            p.get(i) = i
            i++
        }
        j = 1
        while (j <= m) {
            t_j = t[j - 1]
            d.get(0) = j
            i = 1
            while (i <= n) {
                cost = if (s[i - 1] == t_j) 0 else 1
                d.get(i) = Math.min(Math.min(d.get(i - 1) + 1, p.get(i) + 1), p.get(i - 1) + cost)
                i++
            }
            _d = p
            p = d
            d = _d
            j++
        }
        return p.get(n)
    }

    /**
     * Just return the stack trace as new-line-separated string.
     * @param t
     * @param header Add a header about the exception itself, if set to true.
     * @param trim If to make the output more compact in case of repetitions.
     * @return
     */
    fun stackTraceToString(t: Throwable?, header: Boolean, trim: Boolean): String? {
        // TODO: Consider to use System.getProperty("line.separator").
        // TODO: Consider to add a trimDepth argument, for repetition of a sequence of elements.
        val b = StringBuilder(325)
        if (header) {
            b.append(t.toString()) // TODO: Check.
            b.append("\n")
        }
        val elements = t.getStackTrace()
        var last: StackTraceElement? = null // Assume this is faster than operating on the strings.
        var repetition = 0
        for (i in elements.indices) {
            val element = elements[i]
            if (trim) {
                if (element == last) {
                    repetition += 1
                    continue
                } else {
                    if (repetition > 0) {
                        if (header) {
                            b.append("\t")
                        }
                        b.append("(... repeated $repetition times.)\n")
                        repetition = 0
                    }
                    last = element
                }
            }
            if (header) {
                b.append("\t")
            }
            b.append(element)
            b.append("\n")
        }
        if (repetition > 0) {
            if (header) {
                b.append("\t")
            }
            b.append("(... repeated $repetition times.)\n")
        }
        return b.toString()
    }

    /**
     * Convenience method for stackTraceToString(t).
     * @param t
     * @return
     */
    @kotlin.jvm.JvmStatic
    fun throwableToString(t: Throwable?): String? {
        return stackTraceToString(t, true, true)
    }

    /**
     * Count number of needles left in a dart board.
     * @param dartBoard
     * @param needles
     * @return
     */
    fun count(dartBoard: String?, needles: Char): Int {
        var n = 0
        var index = 0
        while (index != -1) {
            index = dartBoard.indexOf(needles, index)
            if (index != -1) {
                n++
                index++
            }
        }
        return n
    }

    /**
     * Get a version of a String with all leading whitespace removed.
     *
     * @param input
     * @return String with leading whitespace removed. Returns the original
     * reference, if there is no leading whitespace.
     */
    @kotlin.jvm.JvmStatic
    fun leftTrim(input: String?): String? {
        if (input == null) {
            return null
        }
        val len = input.length
        var beginIndex = 0
        for (i in 0 until len) {
            if (Character.isWhitespace(input[i])) {
                ++beginIndex
            } else {
                break
            }
        }
        return if (beginIndex > 0) {
            if (beginIndex >= len) {
                ""
            } else {
                input.substring(beginIndex)
            }
        } else {
            input
        }
    }

    /**
     * Format to maximally 3 digits after the comma, always show the sign,
     * unless equal.
     *
     * @param current
     * @param previous
     * @return
     */
    @kotlin.jvm.JvmStatic
    fun formatDiff(current: Double, previous: Double): String? {
        return if (current == previous) "0" else (if (current > previous) "+" else "-") + fdec3.format(Math.abs(current - previous))
    }

    @kotlin.jvm.JvmStatic
    fun startsWithAnyOf(input: String?, vararg startsWith: String?): Boolean {
        for (i in startsWith.indices) {
            if (input.startsWith(startsWith[i])) {
                return true
            }
        }
        return false
    }

    fun endsWithAnyOf(input: String?, vararg endsWith: String?): Boolean {
        for (i in endsWith.indices) {
            if (input.endsWith(endsWith[i])) {
                return true
            }
        }
        return false
    }

    @kotlin.jvm.JvmStatic
    fun reverse(input: String?): String? {
        return StringBuilder(input).reverse().toString()
    }
}