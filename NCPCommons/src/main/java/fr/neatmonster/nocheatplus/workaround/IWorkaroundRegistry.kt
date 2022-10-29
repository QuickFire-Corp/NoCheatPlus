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
package fr.neatmonster.nocheatplus.workaround

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
 * An access point for fetching global WorkaroundCounter instances and a factory
 * for fetching new sets of per-player workarounds.
 *
 * @author asofold
 */
interface IWorkaroundRegistry {
    /**
     * Convenience to retrieve any type of per-player Workaround by id, for the
     * case one doesn't want to store the registry and/or individual Workaround
     * implementations as members. Groups allow resetting certain types of
     * workarounds in bunches. The resetConditions methods only call
     * resetConditions for instances of IStagedWorkaround.
     *
     * @author asofold
     */
    class WorkaroundSet(bluePrints: Array<IWorkaround?>?, groups: MutableMap<String?, Array<String?>?>?) {
        // TODO: getUseCount()
        // TODO: A list of just used IStageWorkaround / maybe other extra, or a flag (reset externally).
        // TODO: Alternative: provide a use(Collection<String>) method to add the id to on accept.
        // TODO: Better optimized constructor (instanceof-decisions can be pre-cached).
        /** Map workaround id to workaround.  */
        private val workaroundsById: MutableMap<String?, IWorkaround?>? = LinkedHashMap()

        /** Only the workarounds that might need resetting.  */
        private val stagedWorkarounds: Array<IStagedWorkaround?>?
        // TODO: Consider to make accessible (flexible log/stats command) or remove keeping entire groups.
        /** Map groupId to workarounds. Set to null, if no groups are present.  */
        private val groups: MutableMap<String?, Array<IWorkaround?>?>? = null

        /** Only the staged workarounds within a group by group id. Set to null, if no groups are present.  */
        private val stagedGroups: MutableMap<String?, Array<IStagedWorkaround?>?>? = null

        /**
         * Collection of just used ids, only set in use(...).
         */
        private var justUsedIds: MutableCollection<String?>? = null

        /**
         *
         * @param bluePrints
         * @param groups
         * Map groupId to workaroundIds, groups may be null if none
         * are set. All referenced workaround ids must be registered,
         * workarounds can be in multiple groups.
         */
        init {
            // Add new instances to workaroundsById and stagedWorkarounds.
            val stagedWorkarounds = ArrayList<IWorkaround?>(bluePrints.size)
            for (i in bluePrints.indices) {
                val workaround = bluePrints.get(i).getNewInstance()
                workaroundsById[workaround.id] = workaround
                if (workaround is IStagedWorkaround) {
                    stagedWorkarounds.add(workaround)
                }
            }
            this.stagedWorkarounds = stagedWorkarounds.toTypedArray<IStagedWorkaround?>()

            // Prepare fast to reset lists, if groups are given.
            if (groups != null) {
                this.groups = HashMap()
                stagedGroups = HashMap()
                for ((groupId, workaroundIds) in groups) {
                    val group = arrayOfNulls<IWorkaround?>(workaroundIds.size)
                    val stagedGroup = ArrayList<IStagedWorkaround?>(workaroundIds.size)
                    for (i in workaroundIds.indices) {
                        val workaround = getWorkaround(workaroundIds.get(i))
                        group[i] = workaround
                        if (workaround is IStagedWorkaround) {
                            stagedGroup.add(workaround as IStagedWorkaround?)
                        }
                    }
                    this.groups[groupId] = group
                    if (!stagedGroup.isEmpty()) {
                        stagedGroups[groupId] = stagedGroup.toTypedArray()
                    }
                }
            } else {
                this.groups = null
                stagedGroups = null
            }
        }

        fun <C : IWorkaround?> getWorkaround(id: String?, workaroundClass: Class<C?>?): C? {
            val present = getWorkaround(id)
            return if (!workaroundClass.isAssignableFrom(present.javaClass)) {
                throw IllegalArgumentException("Wrong type of registered workaround requested: " + workaroundClass.getName() + " instead of " + present.javaClass.name)
            } else {
                present as C?
            }
        }

        /**
         *
         * @param id
         * @return
         * @throws IllegalArgumentException
         * If no workaround is set for the id.
         */
        fun getWorkaround(id: String?): IWorkaround? {
            return workaroundsById.get(id) ?: throw IllegalArgumentException("Workaround id not registered: $id")
        }

        /**
         * Call resetConditions for all stored workarounds, excluding
         * WorkaroundCounter instances (sub classes get reset too).
         */
        fun resetConditions() {
            for (i in stagedWorkarounds.indices) {
                stagedWorkarounds.get(i).resetConditions()
            }
        }

        /**
         * Call resetConditions for all workarounds that are within the group
         * with the given groupId.
         *
         * @param groupId
         */
        fun resetConditions(groupId: String?) {
            if (stagedGroups != null) {
                val workarounds = stagedGroups[groupId]
                if (workarounds != null) {
                    for (i in workarounds.indices) {
                        workarounds[i].resetConditions()
                    }
                }
            }
        }

        /**
         * Unchecked use.
         *
         * @param workaroundId
         * @return The result of IWorkaround.use() for the registered instance.
         * @throws IllegalArgumentException
         * If no workaround is registered for this id.
         */
        fun use(workaroundId: String?): Boolean {
            // TODO: For consistency might throw the same exception everywhere (IllegalArgument?). 
            val workaround = workaroundsById.get(workaroundId)
            return if (workaround == null) {
                throw IllegalArgumentException("Workaround id not registered: $workaroundId")
            } else {
                if (workaround.use()) {
                    if (justUsedIds != null) {
                        justUsedIds.add(workaround.id)
                    }
                    true
                } else {
                    false
                }
            }
        }

        /**
         * Unchecked canUse.
         *
         * @param workaroundId
         * @return The result of IWorkaround.canUse() for the registered
         * instance.
         * @throws NullPointerException
         * if no workaround is registered for this id.
         */
        fun canUse(workaroundId: String?): Boolean {
            // TODO: For consistency might throw the same exception everywhere (IllegalArgument?). 
            return workaroundsById.get(workaroundId).canUse()
        }

        /**
         * Set the just used ids collection or null, to set or not set on
         * use(...).
         *
         * @param justUsedIds
         */
        fun setJustUsedIds(justUsedIds: MutableCollection<String?>?) {
            this.justUsedIds = justUsedIds
        }
    }

    /**
     * Registers workaround.getNewInstance() for the set id as a blueprint. A
     * parent counter will be created, if it doesn't exist yet. The counter will
     * not be added to the allTimeCounter of the given IWorkaround instances,
     * but to the one of the internally stored instance.
     *
     * @param bluePrints
     */
    open fun setWorkaroundBluePrint(vararg bluePrints: IWorkaround?)

    /**
     * Specify what workaround ids belong to a certain group. Workarounds can be
     * in multiple groups. The workaroundIds must exist.
     *
     * @param groupId
     * @param workaroundIds
     */
    open fun setGroup(groupId: String?, workaroundIds: MutableCollection<String?>?)

    /**
     * Specify what workaround ids belong to a certain group. Workarounds can be
     * in multiple groups. The workaroundIds must exist.
     *
     * @param groupId
     * @param bluePrints
     * The ids are used, must exist.
     */
    open fun setGroup(groupId: String?, vararg bluePrints: IWorkaround?)

    /**
     * Define which workarounds and which groups belong to the WorkaroundSet of
     * the given workaroundSetId.
     *
     * @param workaroundSetId
     * @param bluePrintIds
     * @param groupIds
     */
    open fun setWorkaroundSetByIds(
        workaroundSetId: String?,
        bluePrintIds: MutableCollection<String?>?,
        vararg groupIds: String?
    )

    /**
     * Retrieve a pre-set WorkaroundSet instance with new Workaround instances
     * generated from the blueprints.
     *
     * @param workaroundSetId
     * @return
     */
    open fun getWorkaroundSet(workaroundSetId: String?): WorkaroundSet?

    /**
     * Get a registered global IAcceptDenyCounter instance, if registered.
     *
     * @param id
     * @return The registered IAcceptDenyCounter instance, or null if none is
     * registered for the given id.
     */
    open fun getGlobalCounter(id: String?): IAcceptDenyCounter?

    /**
     * Get a registered global IAcceptDenyCounter instance, create if not
     * present.
     *
     * @param id
     * @return
     */
    open fun createGlobalCounter(id: String?): IAcceptDenyCounter?

    /**
     * Retrieve a new instance, ready for use, attached to a global counter of
     * the same id.
     *
     * @param id
     * @param workaroundClass
     * Specific type to use. The registry may have a blueprint set
     * and just clone that.
     * @return
     * @throws IllegalArgumentException
     * If either of id or workaroundClass is not possible to use.
     */
    open fun <C : IWorkaround?> getWorkaround(id: String?, workaroundClass: Class<C?>?): C?

    /**
     * Retrieve a new instance, ready for use, attached to a global counter of
     * the same id.
     *
     * @param id
     * @return
     * @throws IllegalArgumentException
     * If either of id or workaroundClass is not possible to use.
     */
    open fun getWorkaround(id: String?): IWorkaround?

    /**
     * Retrieve an unmodifiable map for all registered global counters. The
     * counters are not copies, so they could be altered, discouraged though.
     *
     * @return
     */
    open fun getGlobalCounters(): MutableMap<String?, IAcceptDenyCounter?>?

    /**
     * Convenience to get the internally registered id.
     *
     * @param workaroundId
     * @return
     * @throws IllegalArgumentException
     * If an id is not registered for a given workaround.
     */
    open fun getCheckedWorkaroundId(workaroundId: String?): String?

    /**
     * Convenience method to get a set of ids, testing if bluePrints exist.
     *
     * @param workarounds
     * @return A set fit for iteration. Contained ids are taken from the
     * internally registered instances.
     * @throws IllegalArgumentException
     * If an id is not registered for a given workaround.
     */
    open fun getCheckedIdSet(workarounds: MutableCollection<out IWorkaround?>?): MutableSet<String?>? // UH.
}