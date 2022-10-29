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

/**
 * Tree for some sequence lookup. <br></br>
 * Pretty fat, for evaluation purposes.
 * @author mc_dev
 */
open class PrefixTree<K, N : PrefixTree.Node<K?, N?>?, L : PrefixTree.LookupEntry<K?, N?>?>(
    protected val nodeFactory: NodeFactory<K?, N?>?, resultFactory: LookupEntryFactory<K?, N?, L?>?
) {
    /**
     * The real thing.
     * @author mc_dev
     *
     * @param <K>
     * @param <N>
    </N></K> */
    open class Node<K, N : Node<K?, N?>?> {
        protected var minCap = 4

        /** End of a sequence marker (not necessarily a leaf)  */
        @kotlin.jvm.JvmField
        var isEnd = false
        var children: MutableMap<K?, N?>? = null
        fun getChild(key: K?): N? {
            return if (children == null) null else children.get(key)
        }

        /**
         * Put the child into the children map.
         * @param key
         * @param child
         * @return The resulting child for the key.
         */
        fun putChild(key: K?, child: N?): N? {
            if (children == null) children = HashMap(minCap)
            children[key] = child
            return child
        }
    }

    /**
     * Convenience.
     * @author mc_dev
     *
     * @param <K>
    </K> */
    class SimpleNode<K> : Node<K?, SimpleNode<K?>?>()
    interface NodeFactory<K, N : Node<K?, N?>?> {
        /**
         *
         * @param parent Can be null (root).
         * @return
         */
        open fun newNode(parent: N?): N?
    }

    open class LookupEntry<K, N : Node<K?, N?>?>(
        /** The node, if lookup matched. */
        val node: N?,
        /** The node at which insertion did/would happen  */
        val insertion: N?,
        /** Depth to root from insertion point.  */
        val depth: Int,
        /** If the tree contained a prefix of the sequence,
         * i.e. one of the existent nodes matching the input was a leaf.  */
        val hasPrefix: Boolean
    )

    interface LookupEntryFactory<K, N : Node<K?, N?>?, L : LookupEntry<K?, N?>?> {
        open fun newLookupEntry(node: N?, insertion: N?, depth: Int, hasPrefix: Boolean): L?
    }

    protected val resultFactory: LookupEntryFactory<K?, N?, L?>?
    protected var root: N?

    /** If nodes visit method shall be called.  */
    protected var visit = false

    init {
        this.root = nodeFactory.newNode(null)
        this.resultFactory = resultFactory
    }

    /**
     * Look up without creating new nodes.
     * @param keys
     * @return
     */
    fun lookup(keys: MutableList<K?>?): L? {
        return lookup(keys, false)
    }
    /**
     * Look up sequence, if desired fill in the given sequence.
     * @param keys
     * @param create
     * @return
     */
    /**
     * Look up without creating new nodes.
     * @param keys
     * @return
     */
    @JvmOverloads
    fun lookup(keys: Array<K?>?, create: Boolean = false): L? {
        return lookup(Arrays.asList(*keys), create)
    }

    /**
     * Look up sequence, if desired fill in the given sequence.
     * @param keys
     * @param create
     * @return
     */
    open fun lookup(keys: MutableList<K?>?, create: Boolean): L? {
        val visit = visit
        var insertion = root
        var depth = 0
        var current = root
        var hasPrefix = false
        for (key in keys) {
            val child = current.getChild(key)
            if (child == null) {
                current = if (create) {
                    val temp = nodeFactory.newNode(current)
                    current.putChild(key, temp)
                } else break
            } else {
                // A node already exists, set as insertion point.
                current = child
                insertion = current
                depth++
                if (child.isEnd) hasPrefix = true
                if (visit) visit(child)
            }
        }
        var node: N? = null
        if (create) {
            node = current
            current.isEnd = true
        } else if (depth == keys.size) {
            node = current
        }
        val result = resultFactory.newLookupEntry(node, insertion, depth, hasPrefix)
        decorate(result)
        return result
    }

    /**
     * Visit a node during lookup. Override to make use, you need to det the visit flag for it to take effect.
     * @param node
     */
    protected open fun visit(node: N?) {}

    /**
     * Decorate before returning.
     * @param result
     */
    protected open fun decorate(result: L?) {}

    /**
     *
     * @param keys
     * @return If already inside (not necessarily as former end point).
     */
    fun feed(keys: MutableList<K?>?): Boolean {
        val result = lookup(keys, true)
        return result.insertion === result.node
    }

    /**
     *
     * @param chars
     * @return If already inside (not necessarily as former end point).
     */
    fun feed(keys: Array<K?>?): Boolean {
        return feed(Arrays.asList(*keys))
    }

    /**
     * Check if the tree has a prefix of keys. This does not mean a common prefix, but that the tree contains an end point that is a prefix of the input.
     * @param keys
     * @return
     */
    fun hasPrefix(keys: MutableList<K?>?): Boolean {
        return lookup(keys, false).hasPrefix
    }

    /**
     * Check if the tree has a prefix of keys. This does not mean a common prefix, but that the tree contains an end point that is a prefix of the input.
     * @param keys
     * @return
     */
    fun hasPrefix(keys: Array<K?>?): Boolean {
        return hasPrefix(Arrays.asList(*keys))
    }

    /**
     * Check if the input is prefix of a path inside of the tree, need not be an end point.
     * @param keys
     * @return
     */
    fun isPrefix(keys: MutableList<K?>?): Boolean {
        return lookup(keys, false).depth == keys.size
    }

    /**
     * Check if the input is prefix of a path inside of the tree, need not be an end point.
     * @param keys
     * @return
     */
    fun isPrefix(keys: Array<K?>?): Boolean {
        return isPrefix(Arrays.asList(*keys))
    }

    /**
     * Check if the input is an inserted sequence (end point), but not necessarily a leaf.
     * @param keys
     * @return
     */
    fun matches(keys: MutableList<K?>?): Boolean {
        val result = lookup(keys, false)
        return result.node === result.insertion && result.insertion.isEnd
    }

    /**
     * Check if the input is an inserted sequence (end point), but not necessarily a leaf.
     * @param keys
     * @return
     */
    fun matches(keys: Array<K?>?): Boolean {
        return matches(Arrays.asList(*keys))
    }

    fun clear() {
        root = nodeFactory.newNode(null)
        // TODO: maybe more unlinking ?
    }

    companion object {
        /**
         * Factory method for a simple tree.
         * @param keyType
         * @return
         */
        fun <K> newPrefixTree(): PrefixTree<K?, SimpleNode<K?>?, LookupEntry<K?, SimpleNode<K?>?>?>? {
            return PrefixTree(object : NodeFactory<K?, SimpleNode<K?>?> {
                override fun newNode(parent: SimpleNode<K?>?): SimpleNode<K?>? {
                    return SimpleNode()
                }
            }, object : LookupEntryFactory<K?, SimpleNode<K?>?, LookupEntry<K?, SimpleNode<K?>?>?> {
                override fun newLookupEntry(
                    node: SimpleNode<K?>?,
                    insertion: SimpleNode<K?>?,
                    depth: Int,
                    hasPrefix: Boolean
                ): LookupEntry<K?, SimpleNode<K?>?>? {
                    return LookupEntry(node, insertion, depth, hasPrefix)
                }
            })
        }
    }
}