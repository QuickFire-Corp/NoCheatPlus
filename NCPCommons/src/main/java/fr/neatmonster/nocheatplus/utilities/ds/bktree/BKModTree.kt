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
package fr.neatmonster.nocheatplus.utilities.ds.bktree

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
 * BK tree for int distances.
 * @author mc_dev
 */
abstract class BKModTree<V, N : BKModTree.Node<V?, N?>?, L : BKModTree.LookupEntry<V?, N?>?>(
    protected val nodeFactory: NodeFactory<V?, N?>?, protected val resultFactory: LookupEntryFactory<V?, N?, L?>?
) {
    // TODO: Support for other value (equals) than used for lookup (distance).
    // TODO: What with dist = 0 -> support for exact hit !
    /**
     * Fat defaultimpl. it iterates over all Children
     * @author mc_dev
     *
     * @param <V>
     * @param <N>
    </N></V> */
    abstract class Node<V, N : Node<V?, N?>?>(
        var value: V?
    ) {
        abstract fun putChild(distance: Int, child: N?): N?
        abstract fun getChild(distance: Int): N?
        abstract fun hasChild(distance: Int): Boolean
        abstract fun getChildren(distance: Int, range: Int, nodes: MutableCollection<N?>?): MutableCollection<N?>?
    }

    /**
     * Node using a map as base, with basic implementation.
     * @author mc_dev
     *
     * @param <V>
     * @param <N>
    </N></V> */
    abstract class MapNode<V, N : HashMapNode<V?, N?>?>(value: V?) : Node<V?, N?>(value) {
        protected var children: MutableMap<Int?, N?>? = null // Only created if needed.
        protected var maxIterate = 12 // Maybe add a setter.
        override fun putChild(distance: Int, child: N?): N? {
            if (children == null) children = newMap()
            children[distance] = child
            return child
        }

        override fun getChild(distance: Int): N? {
            return if (children == null) null else children.get(distance)
        }

        override fun hasChild(distance: Int): Boolean {
            return if (children == null) false else children.containsKey(distance)
        }

        override fun getChildren(distance: Int, range: Int, nodes: MutableCollection<N?>?): MutableCollection<N?>? {
            if (children == null) return nodes
            // TODO: maybe just go for iterating till range (from 0 on) to have closest first (no keyset).
            if (children.size > maxIterate) {
                for (i in distance - range until distance + range + 1) {
                    val child = children.get(i)
                    if (child != null) nodes.add(child)
                }
            } else {
                for (key in children.keys) {
                    // TODO: Not sure this is faster than the EntrySet in average.
                    if (Math.abs(distance - key.toInt()) <= range) nodes.add(children.get(key))
                }
            }
            return nodes
        }

        /**
         * Map factory method.
         * @return
         */
        protected abstract fun newMap(): MutableMap<Int?, N?>?
    }

    /**
     * Node using a simple HashMap.
     * @author mc_dev
     *
     * @param <V>
     * @param <N>
    </N></V> */
    open class HashMapNode<V, N : HashMapNode<V?, N?>?>(value: V?) : MapNode<V?, N?>(value) {
        /** Map Levenshtein distance to next nodes.  */
        protected var initialCapacity = 4
        protected var loadFactor = 0.75f
        override fun newMap(): MutableMap<Int?, N?>? {
            return HashMap(initialCapacity, loadFactor)
        }
    }

    class SimpleNode<V>(content: V?) : HashMapNode<V?, SimpleNode<V?>?>(content)
    interface NodeFactory<V, N : Node<V?, N?>?> {
        open fun newNode(value: V?, parent: N?): N?
    }

    /**
     * Result of a lookup.
     * @author mc_dev
     *
     * @param <V>
     * @param <N>
    </N></V> */
    open class LookupEntry<V, N : Node<V?, N?>?>     // TODO: What nodes are in nodes, actually? Those from the way that were in range ?
    // TODO: This way one does not know which distance a node has. [subject to changes]
    // TODO: Add depth and some useful info ?
        (
        /** All visited nodes within range of distance.  */
        val nodes: MutableCollection<N?>?,
        /** Matching node  */
        val match: N?,
        /** Distance from value to match.value  */
        val distance: Int,
        /** If the node match is newly inserted. */
        val isNew: Boolean
    )

    interface LookupEntryFactory<V, N : Node<V?, N?>?, L : LookupEntry<V?, N?>?> {
        open fun newLookupEntry(nodes: MutableCollection<N?>?, match: N?, distance: Int, isNew: Boolean): L?
    }

    protected var root: N? = null

    /** Set to true to have visit called  */
    protected var visit = false
    fun clear() {
        root = null
    }

    /**
     *
     * @param value
     * @param range Maximum difference from distance of node.value to children.
     * @param seekMax If node.value is within distance but not matching, this is the maximum number of steps to search on.
     * @param create
     * @return
     */
    fun lookup(value: V?, range: Int, seekMax: Int, create: Boolean): L? { // TODO: signature.
        val inRange: MutableList<N?> = LinkedList()
        if (root == null) {
            return if (create) {
                root = nodeFactory.newNode(value, null)
                resultFactory.newLookupEntry(inRange, root, 0, true)
            } else {
                resultFactory.newLookupEntry(inRange, null, 0, false)
            }
        }
        // TODO: best queue type.
        val open: MutableList<N?> = ArrayList()
        open.add(root)
        var insertion: N? = null
        var insertionDist = 0
        do {
            val current = open.removeAt(open.size - 1)
            val distance = distance(current.value, value)
            if (visit) visit(current, value, distance)
            if (distance == 0) {
                // exact match.
                return resultFactory.newLookupEntry(inRange, current, distance, false)
            }
            // Set node as insertion point.
            if (create && insertion == null && !current.hasChild(distance)) {
                insertion = current
                insertionDist = distance
                // TODO: use
            }
            // Within range ?
            if (Math.abs(distance) <= range) {
                inRange.add(current)
                // Check special abort conditions.
                if (seekMax > 0 && inRange.size >= seekMax) {
                    // TODO: Keep this ?
                    // Break if insertion point is found, or not needed.
                    if (!create || insertion != null) {
                        break
                    }
                }
            }
            // Continue search with children.
            current.getChildren(distance, range, open)

            // TODO: deterministic: always same node visited for the same value ? [Not with children = HashMap...]
        } while (!open.isEmpty())

        // TODO: is the result supposed to be the closest match, if any ?
        return if (create && insertion != null) {
            val newNode = nodeFactory.newNode(value, insertion)
            insertion.putChild(insertionDist, newNode)
            resultFactory.newLookupEntry(inRange, newNode, 0, true)
        } else {
            resultFactory.newLookupEntry(inRange, null, 0, false)
        }
    }

    /**
     * Visit a node during lookup.
     * @param node
     * @param distance
     * @param value
     */
    protected fun visit(node: N?, value: V?, distance: Int) {
        // Override if needed.
    }
    //////////////////////////////////////////////
    // Abstract methods.
    //////////////////////////////////////////////
    /**
     * Calculate the distance of two values.
     * @param v1
     * @param v2
     * @return
     */
    abstract fun distance(v1: V?, v2: V?): Int
}