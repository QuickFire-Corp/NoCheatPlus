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
package fr.neatmonster.nocheatplus.utilities.ds.prefixtree

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

open class CharPrefixTree<N : CharNode<N?>?, L : CharLookupEntry<N?>?>(
    nodeFactory: NodeFactory<Char?, N?>?,
    resultFactory: LookupEntryFactory<Char?, N?, L?>?
) : PrefixTree<Char?, N?, L?>(nodeFactory, resultFactory) {
    open class CharNode<N : CharNode<N?>?> : Node<Char?, N?>()
    class SimpleCharNode : CharNode<SimpleCharNode?>()
    open class CharLookupEntry<N : CharNode<N?>?>(node: N?, insertion: N?, depth: Int, hasPrefix: Boolean) :
        LookupEntry<Char?, N?>(node, insertion, depth, hasPrefix)

    /**
     *
     * @param chars
     * @param create
     * @return
     */
    fun lookup(chars: CharArray?, create: Boolean): L? {
        return lookup(toCharacterList(chars), create)
    }

    /**
     *
     * @param chars
     * @param create
     * @return
     */
    fun lookup(input: String?, create: Boolean): L? {
        return lookup(input.toCharArray(), create)
    }

    /**
     *
     * @param chars
     * @return If already inside (not necessarily as former end point).
     */
    fun feed(input: String?): Boolean {
        return feed(input.toCharArray())
    }

    /**
     *
     * @param chars
     * @return If already inside (not necessarily as former end point).
     */
    fun feed(chars: CharArray?): Boolean {
        return feed(toCharacterList(chars))
    }

    fun feedAll(inputs: MutableCollection<String?>?, trim: Boolean, lowerCase: Boolean) {
        for (input in inputs) {
            if (trim) input = input.lowercase(Locale.getDefault())
            if (lowerCase) input = input.lowercase(Locale.getDefault())
            feed(input)
        }
    }

    /**
     * Check if the tree has a prefix of chars. This does not mean a common
     * prefix, but that the tree contains an end point that is a prefix of the
     * input.
     *
     * @param chars
     * @return
     */
    fun hasPrefix(chars: CharArray?): Boolean {
        return hasPrefix(toCharacterList(chars))
    }

    /**
     * Check if the tree has a prefix of input. This does not mean a common
     * prefix, but that the tree contains an end point that is a prefix of the
     * input.
     *
     * @param input
     * @return
     */
    fun hasPrefix(input: String?): Boolean {
        return hasPrefix(input.toCharArray())
    }

    /**
     * Quick and dirty addition: Test if a prefix is contained which either
     * matches the whole input or does not end inside of a word in the input,
     * i.e. the inputs next character is a space.
     *
     * @param input
     * @return
     */
    fun hasPrefixWords(input: String?): Boolean {
        // TODO build this in in a more general way (super classes + stop symbol)!
        val result = lookup(input, false)
        if (!result.hasPrefix) return false
        if (input.length == result.depth) return true
        return if (Character.isWhitespace(input.get(result.depth))) true else false
    }

    /**
     * Test hasPrefixWords for each given argument.
     *
     * @param inputs
     * @return true if hasPrefixWords(String) returns true for any of the
     * inputs, false otherwise.
     */
    fun hasAnyPrefixWords(vararg inputs: String?): Boolean {
        for (i in inputs.indices) {
            if (hasPrefixWords(inputs[i])) {
                return true
            }
        }
        return false
    }

    /**
     * Test hasPrefixWords for each element of the collection.
     *
     * @param inputs
     * @return true if hasPrefixWords(String) returns true for any of the
     * elements, false otherwise.
     */
    fun hasAnyPrefixWords(inputs: MutableCollection<String?>?): Boolean {
        for (input in inputs) {
            if (hasPrefixWords(input)) {
                return true
            }
        }
        return false
    }

    /**
     * Test if there is an end-point in the tree that is a prefix of any of the
     * inputs.
     *
     * @param inputs
     * @return
     */
    fun hasAnyPrefix(inputs: MutableCollection<String?>?): Boolean {
        for (input in inputs) {
            if (hasPrefix(input)) {
                return true
            }
        }
        return false
    }

    fun isPrefix(chars: CharArray?): Boolean {
        return isPrefix(toCharacterList(chars))
    }

    fun isPrefix(input: String?): Boolean {
        return isPrefix(input.toCharArray())
    }

    fun matches(chars: CharArray?): Boolean {
        return matches(toCharacterList(chars))
    }

    fun matches(input: String?): Boolean {
        return matches(input.toCharArray())
    }

    companion object {
        /**
         * Auxiliary method to get a List of Character.
         *
         * @param chars
         * @return
         */
        fun toCharacterList(chars: CharArray?): MutableList<Char?>? {
            val characters: MutableList<Char?> = ArrayList(chars.size)
            for (i in chars.indices) {
                characters.add(chars.get(i))
            }
            return characters
        }

        /**
         * Factory method for a simple tree.
         *
         * @param keyType
         * @return
         */
        fun newCharPrefixTree(): CharPrefixTree<SimpleCharNode?, CharLookupEntry<SimpleCharNode?>?>? {
            return CharPrefixTree({ SimpleCharNode() }) { node, insertion, depth, hasPrefix ->
                CharLookupEntry(
                    node,
                    insertion,
                    depth,
                    hasPrefix
                )
            }
        }
    }
}