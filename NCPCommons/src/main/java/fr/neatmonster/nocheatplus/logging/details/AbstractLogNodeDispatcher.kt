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
package fr.neatmonster.nocheatplus.logging.details

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
import java.util.logging.Level

/**
 * Basic functionality, with int task ids, assuming a primary thread exists.
 * @author dev1mc
 */
abstract class AbstractLogNodeDispatcher : LogNodeDispatcher {
    // TODO: Name
    // TODO: Queues might need a drop policy with thresholds.
    // TODO: Allow multiple tasks for logging, e.g. per file, also thinking of SQL logging. Could pool + round-robin.
    /**
     * This queue has to be processed in a task within the primary thread with calling runLogsPrimary.
     */
    protected val queuePrimary: IQueueRORA<LogRecord<*>?>? = QueueRORA()
    @kotlin.jvm.JvmField
    protected val queueAsynchronous: IQueueRORA<LogRecord<*>?>? = QueueRORA()

    /** Once a queue reaches this size, it will be reduced (loss of content).  */
    protected var maxQueueSize = 5000

    /**
     * Task id, -1 means the asynchronous task is not running. Synchronize over
     * queueAsynchronous. Must be maintained.
     */
    @kotlin.jvm.JvmField
    protected var taskAsynchronousID = -1

    /**
     * Optional implementation for an asynchronous task, using the
     * taskAsynchronousID, synchronized over queueAsynchronous.
     */
    @kotlin.jvm.JvmField
    protected val taskAsynchronous: Runnable? = Runnable {
        // TODO: A more sophisticated System to allow "wake up on burst"?
        var i = 0
        while (i < 3) {
            if (runLogsAsynchronous()) {
                i = 0
                if (taskAsynchronousID == -1) {
                    // Shutdown, hard return;
                    return@Runnable
                }
                Thread.yield()
            } else {
                i++
                try {
                    Thread.sleep(25)
                } catch (e: InterruptedException) {
                    synchronized(queueAsynchronous) {
                        // Ensure re-scheduling can happen.
                        taskAsynchronousID = -1
                    }
                    // TODO: throw?
                    return@Runnable
                }
            }
            synchronized(queueAsynchronous) {
                if (queueAsynchronous.isEmpty()) {
                    if (i >= 3) {
                        // Ensure re-scheduling can happen.
                        taskAsynchronousID = -1
                    }
                } else {
                    i = 0
                }
            }
        }
    }

    /**
     * Optional init logger to log errors. Should log to the init stream, no queuing.
     */
    protected var initLogger: ContentLogger<String?>? = null
    override fun <C> dispatch(node: LogNode<C?>?, level: Level?, content: C?) {
        // TODO: Try/catch ?
        if (isWithinContext<C?>(node)) {
            node.logger.log(level, content)
        } else {
            scheduleLog<C?>(node, level, content)
        }
    }

    protected fun runLogsPrimary(): Boolean {
        return runLogs(queuePrimary)
    }

    protected fun runLogsAsynchronous(): Boolean {
        return runLogs(queueAsynchronous)
    }

    private fun runLogs(queue: IQueueRORA<LogRecord<*>?>?): Boolean {
        // TODO: Consider allowYield + msYield, calling yield after 5 ms if async.
        val records = queue.removeAll()
        if (records.isEmpty()) {
            return false
        }
        for (record in records) {
            record.run()
        }
        return true
    }

    override fun flush(ms: Long) {
        var ms = ms
        check(isPrimaryThread()) {
            // TODO: Could also switch policy to emptying the primary-thread queue if not called from within the primary thread.
            "Must only be called from within the primary thread."
        }
        // TODO: Note that all streams should be emptied here, except the fallback logger.

        // Cancel task.
        synchronized(queueAsynchronous) {
            if (taskAsynchronousID != -1) {
                // TODO: Allow queues to set to "no more input" ?
                cancelTask(taskAsynchronousID)
                taskAsynchronousID = -1
            } else {
                // No need to wait.
                ms = 0L
            }
        }
        if (ms > 0) {
            try {
                // TODO: Replace by a better concept.
                Thread.sleep(ms)
            } catch (e: InterruptedException) {
                // Ignore.
            }
        }

        // Log the rest (from here logging should be done via the appropriate direct-only stream).
        runLogsPrimary()
        runLogsAsynchronous()
    }

    protected fun <C> isWithinContext(node: LogNode<C?>?): Boolean {
        return when (node.options.callContext) {
            CallContext.PRIMARY_THREAD_DIRECT, CallContext.PRIMARY_THREAD_ONLY -> isPrimaryThread()
            CallContext.ANY_THREAD_DIRECT -> true
            CallContext.ASYNCHRONOUS_ONLY, CallContext.ASYNCHRONOUS_DIRECT -> !isPrimaryThread()
            CallContext.PRIMARY_THREAD_TASK, CallContext.ASYNCHRONOUS_TASK ->                 // TODO: Each: Consider detecting if within that task (should rather be in case of re-scheduling?).
                false // Always schedule (!).
            else -> false // Force scheduling.
        }
    }

    protected fun <C> scheduleLog(node: LogNode<C?>?, level: Level?, content: C?) {
        val record = LogRecord(node, level, content) // TODO: parameters.
        when (node.options.callContext) {
            CallContext.ASYNCHRONOUS_TASK, CallContext.ASYNCHRONOUS_DIRECT -> {
                if (queueAsynchronous.add(record) > maxQueueSize) {
                    reduceQueue(queueAsynchronous)
                }
                if (taskAsynchronousID == -1) { // Works, due to add being synchronized (not sure it's really better than full sync).
                    scheduleAsynchronous()
                }
            }
            CallContext.PRIMARY_THREAD_TASK, CallContext.PRIMARY_THREAD_DIRECT -> queuePrimary.add(record)
            CallContext.ASYNCHRONOUS_ONLY, CallContext.PRIMARY_THREAD_ONLY -> {}
            else -> throw IllegalArgumentException("Bad CallContext: " + node.options.callContext)
        }
    }

    /**
     * Hard reduce the queue (heavy locking!).
     * @param queue
     */
    private fun reduceQueue(queue: IQueueRORA<LogRecord<*>?>?) {
        // TODO: Different dropping strategies (drop first, last, alternate).
        val dropped: Int
        synchronized(queue) {
            val size = queue.size()
            if (size < maxQueueSize) {
                // Never mind :).
                return
            }
            logINIT(
                Level.WARNING,
                "Dropping log entries from the " + (if (queue === queueAsynchronous) "asynchronous" else "primary thread") + " queue to reduce memory consumption..."
            )
            dropped = queue.reduce(maxQueueSize / 2)
        }
        logINIT(
            Level.WARNING,
            "Dropped " + dropped + " log entries from the " + (if (queue === queueAsynchronous) "asynchronous" else "primary thread") + " queue."
        )
    }

    override fun setMaxQueueSize(maxQueueSize: Int) {
        this.maxQueueSize = maxQueueSize
    }

    override fun setInitLogger(logger: ContentLogger<String?>?) {
        initLogger = logger
    }

    protected fun logINIT(level: Level?, message: String?) {
        if (initLogger != null) {
            initLogger.log(level, message)
        }
    }

    protected abstract fun isPrimaryThread(): Boolean
    protected abstract fun scheduleAsynchronous()
    protected abstract fun cancelTask(taskId: Int)
}