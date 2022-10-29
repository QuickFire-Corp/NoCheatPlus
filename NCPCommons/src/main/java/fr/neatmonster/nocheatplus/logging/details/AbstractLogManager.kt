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

import fr.neatmonster.nocheatplus.logging.LogManager
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
import java.lang.Exception
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Central access point for logging. Abstract class providing basic registration functionality.
 * @author dev1mc
 */
abstract class AbstractLogManager(// TODO: Visibility of methods.
    // TODO: Add option for per stream prefixes.
    // TODO: Concept for adding in the time at the point of call/scheduling.
    // TODO: Re-register with other options: Add methods for LoggerID + StreamID + options.
    // TODO: Hierarchical LogNode relations, to ensure other log nodes with the same logger are removed too [necessary to allow removing individual loggers].
    // TODO: Temporary streams, e.g. for players, unregistering with command and/or logout.
    // TODO: Mechanics of removing temporary streams (flush stream, remove entries from queues, wait with removal until tasks have run once more).
    // TODO: Consider generalizing the (internal) implementation right away (sub registry by content class).
    // TODO: Consider adding a global cache (good for re-mapping, contra: reload is not intended to happen on a regular basis).
    private val dispatcher: LogNodeDispatcher?,
    private val defaultPrefix: String?, // TODO: Future: Only an init string stream or (later) always "the init stream" for all content types.
    private val initStreamID: StreamID?
) : LogManager {
    /**
     * Fast streamID access map (runtime). Copy on write with registryLock.
     */
    private var idStreamMap: MutableMap<StreamID?, ContentStream<String?>?>? = IdentityHashMap()

    /**
     * Map name to Stream. Copy on write with registryLock.
     */
    private var nameStreamMap: MutableMap<String?, ContentStream<String?>?>? = HashMap()

    /**
     * Lower-case name to StreamID.
     */
    private var nameStreamIDMap: MutableMap<String?, StreamID?>? = HashMap()

    /**
     * LogNode registry by LoggerID. Copy on write with registryLock.
     */
    private var idNodeMap: MutableMap<LoggerID?, LogNode<String?>?>? = IdentityHashMap()

    /**
     * LogNode registry by lower-case name. Copy on write with registryLock.
     */
    private var nameNodeMap: MutableMap<String?, LogNode<String?>?>? = HashMap()

    /**
     * Lower-case name to LoggerID.
     */
    private var nameLoggerIDMap: MutableMap<String?, LoggerID?>? = HashMap()

    /** Registry changes have to be done under this lock (copy on write)  */
    @kotlin.jvm.JvmField
    protected val registryCOWLock: Any? = Any()
    private val voidStreamID: StreamID? = StreamID("void")

    /**
     * Fall-back StreamID, for the case of logging to a non-existing StreamID.
     * By default set to void, but can be altered. Set to null to have calls
     * fail.
     */
    private var fallBackStreamID = voidStreamID

    /**
     * Wrapping logging to the init stream.
     */
    protected val initLogger: ContentLogger<String?>? =
        ContentLogger { level, content -> this@AbstractLogManager.log(getInitStreamID(), level, content) }

    /**
     *
     * @param dispatcher
     * @param defaultPrefix
     * @param initStreamID This id is stored, the stream is created, but no loggers will be attached to it within this constructor.
     */
    init {
        createInitStream()
        registerInitLogger()
        dispatcher.setInitLogger(initLogger)
    }

    /**
     * Create INIT stream for strings, if it does not exist. Does not set a
     * prefix.
     */
    protected fun createInitStream() {
        synchronized(registryCOWLock) {
            if (!hasStream(initStreamID)) {
                createStringStream(initStreamID)
            }
        }
    }

    /**
     * Create the minimal init logger(s). Synchronize over registryCOWLock. It's preferable not to duplicate loggers. Prefer LoggerID("init...").
     */
    protected abstract fun registerInitLogger()
    protected fun getLogNodeDispatcher(): LogNodeDispatcher? {
        return dispatcher
    }

    override fun getDefaultPrefix(): String? {
        return defaultPrefix
    }

    override fun getInitStreamID(): StreamID? {
        return initStreamID
    }

    override fun getVoidStreamID(): StreamID? {
        return voidStreamID
    }

    override fun getStreamID(name: String?): StreamID? {
        return nameStreamIDMap.get(name.lowercase(Locale.getDefault()))
    }

    override fun getLoggerID(name: String?): LoggerID? {
        return nameLoggerIDMap.get(name.lowercase(Locale.getDefault()))
    }

    override fun debug(streamID: StreamID?, message: String?) {
        log(streamID, Level.FINE, message) // TODO: Not sure what happens with FINE and provided Logger instances.
    }

    override fun info(streamID: StreamID?, message: String?) {
        log(streamID, Level.INFO, message)
    }

    override fun warning(streamID: StreamID?, message: String?) {
        log(streamID, Level.WARNING, message)
    }

    override fun severe(streamID: StreamID?, message: String?) {
        log(streamID, Level.SEVERE, message)
    }

    override fun log(streamID: StreamID?, level: Level?, message: String?) {
        if (streamID !== voidStreamID) {
            val stream = idStreamMap.get(streamID)
            if (stream != null) {
                stream.log(level, message)
            } else {
                handleFallBack(streamID, level, message)
            }
        }
    }

    private fun handleFallBack(streamID: StreamID?, level: Level?, message: String?) {
        if (fallBackStreamID != null && streamID !== fallBackStreamID) {
            log(fallBackStreamID, level, message)
        } else {
            throw RuntimeException("Stream not registered: $streamID")
        }
    }

    override fun debug(streamID: StreamID?, t: Throwable?) {
        log(streamID, Level.FINE, t) // TODO: Not sure what happens with FINE and provided Logger instances.
    }

    override fun info(streamID: StreamID?, t: Throwable?) {
        log(streamID, Level.INFO, t)
    }

    override fun warning(streamID: StreamID?, t: Throwable?) {
        log(streamID, Level.WARNING, t)
    }

    override fun severe(streamID: StreamID?, t: Throwable?) {
        log(streamID, Level.SEVERE, t)
    }

    override fun log(streamID: StreamID?, level: Level?, t: Throwable?) {
        // Not sure adding streams for Throwable would be better.
        log(streamID, level, StringUtil.throwableToString(t))
    }

    override fun hasStream(streamID: StreamID?): Boolean {
        return idStreamMap.containsKey(streamID) || nameStreamMap.containsKey(streamID.name.lowercase(Locale.getDefault()))
    }

    override fun hasStream(name: String?): Boolean {
        return getStreamID(name) != null
    }

    /**
     * Call under lock.
     * @param streamID
     */
    private fun testRegisterStream(streamID: StreamID?) {
        if (streamID == null) {
            throw NullPointerException("StreamID must not be null.")
        } else if (streamID.name == null) {
            throw NullPointerException("StreamID.name must not be null.")
        } else if (streamID.name.equals(voidStreamID.name, ignoreCase = true)) {
            throw RuntimeException("Can not overrite void StreamID.")
        } else require(!hasStream(streamID)) { "Stream already registered: " + streamID.name.lowercase(Locale.getDefault()) }
    }

    protected fun createStringStream(streamID: StreamID?): ContentStream<String?>? {
        var stream: ContentStream<String?>
        synchronized(registryCOWLock) {
            testRegisterStream(streamID)
            val idStreamMap: MutableMap<StreamID?, ContentStream<String?>?> = IdentityHashMap(
                idStreamMap
            )
            val nameStreamMap: MutableMap<String?, ContentStream<String?>?> = HashMap(
                nameStreamMap
            )
            val nameStreamIDMap: MutableMap<String?, StreamID?> = HashMap(nameStreamIDMap)
            stream = DefaultContentStream(dispatcher)
            idStreamMap[streamID] = stream
            nameStreamMap[streamID.name.lowercase(Locale.getDefault())] = stream
            nameStreamIDMap[streamID.name.lowercase(Locale.getDefault())] = streamID
            this.idStreamMap = idStreamMap
            this.nameStreamMap = nameStreamMap
            this.nameStreamIDMap = nameStreamIDMap
        }
        return stream
    }

    override fun hasLogger(loggerID: LoggerID?): Boolean {
        return idNodeMap.containsKey(loggerID) || nameNodeMap.containsKey(loggerID.name.lowercase(Locale.getDefault()))
    }

    override fun hasLogger(name: String?): Boolean {
        return getLoggerID(name) != null
    }

    /**
     * Call under lock.
     * @param loggerID
     */
    private fun testRegisterLogger(loggerID: LoggerID?) {
        if (loggerID == null) {
            throw NullPointerException("LoggerID must not be null.")
        } else if (loggerID.name == null) {
            throw NullPointerException("LoggerID.name must not be null.")
        } else require(!hasLogger(loggerID)) { "Logger already registered: " + loggerID.name.lowercase(Locale.getDefault()) }
    }

    /**
     * Convenience method.
     * @param logger
     * @param options
     * @return
     */
    protected fun registerStringLogger(logger: ContentLogger<String?>?, options: LogOptions?): LoggerID? {
        val loggerID = LoggerID(options.name)
        registerStringLogger(loggerID, logger, options)
        return loggerID
    }

    /**
     * Convenience method.
     * @param logger
     * @param options
     * @return
     */
    protected fun registerStringLogger(logger: Logger?, options: LogOptions?): LoggerID? {
        val loggerID = LoggerID(options.name)
        registerStringLogger(loggerID, logger, options)
        return loggerID
    }

    /**
     * Convenience method.
     * @param logger
     * @param options
     * @return
     */
    protected fun registerStringLogger(file: File?, options: LogOptions?): LoggerID? {
        return registerStringLogger(file, null, options)
    }

    /**
     * Convenience method.
     * @param logger
     * @param prefix Prefix for all log messages.
     * @param options
     * @return
     */
    protected fun registerStringLogger(file: File?, prefix: String?, options: LogOptions?): LoggerID? {
        val loggerID = LoggerID(options.name)
        registerStringLogger(loggerID, file, prefix, options)
        return loggerID
    }

    protected fun registerStringLogger(
        loggerID: LoggerID?,
        logger: ContentLogger<String?>?,
        options: LogOptions?
    ): LogNode<String?>? {
        var node: LogNode<String?>
        synchronized(registryCOWLock) {
            testRegisterLogger(loggerID)
            val idNodeMap: MutableMap<LoggerID?, LogNode<String?>?> = IdentityHashMap(
                idNodeMap
            )
            val nameNodeMap: MutableMap<String?, LogNode<String?>?> = HashMap(
                nameNodeMap
            )
            val nameLoggerIDMap: MutableMap<String?, LoggerID?> = HashMap(nameLoggerIDMap)
            node = LogNode(loggerID, logger, options)
            idNodeMap[loggerID] = node
            nameNodeMap[loggerID.name.lowercase(Locale.getDefault())] = node
            nameLoggerIDMap[loggerID.name.lowercase(Locale.getDefault())] = loggerID
            this.idNodeMap = idNodeMap
            this.nameNodeMap = nameNodeMap
            this.nameLoggerIDMap = nameLoggerIDMap
        }
        return node
    }

    protected fun registerStringLogger(loggerID: LoggerID?, logger: Logger?, options: LogOptions?): LogNode<String?>? {
        var node: LogNode<String?>?
        synchronized(registryCOWLock) {
            val adapter = LoggerAdapter(logger) // Low cost.
            // TODO: Store loggers too to prevent redundant registration.
            node = registerStringLogger(loggerID, adapter, options)
        }
        return node
    }

    protected fun registerStringLogger(loggerID: LoggerID?, file: File?, options: LogOptions?): LogNode<String?>? {
        return registerStringLogger(loggerID, file, null, options)
    }

    protected fun registerStringLogger(
        loggerID: LoggerID?,
        file: File?,
        prefix: String?,
        options: LogOptions?
    ): LogNode<String?>? {
        var node: LogNode<String?>
        synchronized(registryCOWLock) {
            testRegisterLogger(loggerID) // Redundant checking, because file loggers are expensive.
            // TODO: Detect duplicate loggers (register same logger with given id and options).
            val adapter = FileLoggerAdapter(file, prefix)
            if (adapter.isInoperable) {
                adapter.detachLogger()
                throw RuntimeException("Failed to set up file logger for id '$loggerID': $file")
            }
            node = try {
                registerStringLogger(loggerID, adapter, options)
            } catch (ex: Exception) {
                // TODO: Exception is still bad.
                adapter.detachLogger()
                throw RuntimeException(ex)
            }
        }
        return node
    }

    /**
     * Attach a logger to a stream. Redundant attaching will mean no changes.
     * @param loggerID Must exist.
     * @param streamID Must exist.
     */
    protected fun attachStringLogger(loggerID: LoggerID?, streamID: StreamID?) {
        // TODO: More light-weight locking concept (thinking of dynamically changing per player streams)?
        synchronized(registryCOWLock) {
            if (!hasLogger(loggerID)) {
                throw RuntimeException("Logger is not registered: $loggerID")
            }
            if (!hasStream(streamID)) {
                // Note: This should also ensure the voidStreamID can't be used, because that one can't be registered.
                throw RuntimeException("Stream is not registered: $streamID")
            }
            val node = idNodeMap.get(loggerID)
            if (streamID === initStreamID) {
                // TODO: Not sure about restrictions here. Could allow attaching other streams temporarily if other stuff is wanted.
                when (node.options.callContext) {
                    CallContext.PRIMARY_THREAD_ONLY, CallContext.ANY_THREAD_DIRECT -> {}
                    else -> throw RuntimeException("Unsupported call context for init stream " + streamID + ": " + node.options.callContext)
                }
            }
            idStreamMap.get(streamID).addNode(node)
        }
    }
    // TODO: Methods to replace options for loggers (+ loggers themselves)
    // TODO: Later: attach streams to streams ? [few loggers: attach loggers rather]
    // TODO: logger/stream: allow id lookup logger, file, etc. ?
    /**
     * Remove all loggers and streams including init, resulting in roughly the
     * same state as is after calling the AbstractLogger constructor. Call from
     * the primary thread (policy pending). If fallBackStreamID is set, it will be replaced by the init stream (if available) or by the void stream.
     *
     * @param msWaitFlush
     */
    protected fun clear(msWaitFlush: Long, recreateInitLogger: Boolean) {
        // TODO: enum (remove_all, recreate init, remap init, remap all to init)
        synchronized(registryCOWLock) {

            // Remove loggers from string streams.
            for (stream in idStreamMap.values) {
                stream.clear()
            }
            // Flush queues.
            dispatcher.flush(msWaitFlush)
            // Close/flush/shutdown string loggers, where possible, remove all from registry.
            for (node in idNodeMap.values) {
                if (node.logger is FileLoggerAdapter) {
                    val logger = node.logger as FileLoggerAdapter
                    logger.flush()
                    logger.detachLogger()
                }
            }
            idNodeMap = IdentityHashMap()
            nameNodeMap = HashMap()
            nameLoggerIDMap = HashMap()
            // Remove string streams.
            idStreamMap = IdentityHashMap()
            nameStreamMap = HashMap()
            nameStreamIDMap = HashMap()
            if (recreateInitLogger) {
                createInitStream()
                registerInitLogger()
                if (fallBackStreamID != null && fallBackStreamID !== voidStreamID) {
                    fallBackStreamID = initStreamID
                }
            } else if (fallBackStreamID != null) {
                fallBackStreamID = voidStreamID
            }
        }
    }
    //    /**
    //     * Remove all registered streams and loggers, recreates init logger (and stream).
    //     */
    //    public void clear(final long msWaitFlush) {
    //        clear(msWaitFlush, true);
    //    }
    /**
     * Rather a graceful shutdown, including waiting for the asynchronous task, if necessary. Clear the registry. Also removes the init logger [subject to change].
     * Call from the primary thread (policy pending).
     */
    fun shutdown() {
        clear(500, false) // TODO: Policy / making sense.
    }
}