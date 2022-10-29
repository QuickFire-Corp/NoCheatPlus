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
import java.lang.NoSuchMethodException
import java.lang.InstantiationException
import java.lang.ClassNotFoundException
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
import java.lang.reflect.*

/**
 * Auxiliary methods for dealing with reflection.
 *
 * @author asofold
 */
object ReflectionUtil {
    /**
     * Convenience method to check if members exist and fail if not. This checks
     * getField(...) == null.
     *
     * @param prefix
     * @param specs
     * @throws RuntimeException
     * If any member is not present.
     */
    fun checkMembers(prefix: String?, vararg specs: Array<String?>?) {
        try {
            for (spec in specs) {
                val clazz = Class.forName(prefix + spec.get(0))
                for (i in 1 until spec.size) {
                    if (clazz.getField(spec.get(i)) == null) {
                        throw NoSuchFieldException(prefix + spec.get(0) + " : " + spec.get(i))
                    }
                }
            }
        } catch (e: SecurityException) {
            // Let this one pass.
            //throw new RuntimeException(e);
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }

    /**
     * Convenience method to check if members exist and fail if not. This checks
     * getField(...) == null.
     *
     * @param clazz
     * The class for which to check members for.
     * @param type
     * The expected type of fields.
     * @param fieldNames
     * The field names.
     * @throws RuntimeException
     * If any member is not present or of wrong type.
     */
    fun checkMembers(clazz: Class<*>?, type: Class<*>?, vararg fieldNames: String?) {
        try {
            for (fieldName in fieldNames) {
                val field = clazz.getField(fieldName)
                if (field == null) {
                    throw NoSuchFieldException(clazz.getName() + "." + fieldName + " does not exist.")
                } else if (field.type != type) {
                    throw NoSuchFieldException(clazz.getName() + "." + fieldName + " has wrong type: " + field.type)
                }
            }
        } catch (e: SecurityException) {
            // Let this one pass.
            //throw new RuntimeException(e);
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }

    /**
     * Check for the given names if the method returns the desired type of
     * result (exact check).
     *
     * @param methodNames
     * @param returnType
     * @throws RuntimeException
     * If one method is not existing or not matching return type or
     * has arguments.
     */
    fun checkMethodReturnTypesNoArgs(objClass: Class<*>?, methodNames: Array<String?>?, returnType: Class<*>?) {
        // TODO: Add check: boolean isStatic.
        // TODO: Overloading !?
        try {
            for (methodName in methodNames) {
                val m = objClass.getMethod(methodName)
                if (m.parameterTypes.size != 0) {
                    throw RuntimeException("Expect method without arguments for " + objClass.getName() + "." + methodName)
                }
                if (m.returnType != returnType) {
                    throw RuntimeException("Wrong return type for: " + objClass.getName() + "." + methodName)
                }
            }
        } catch (e: SecurityException) {
            // Let this one pass.
            //throw new RuntimeException(e);
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }

    /**
     * Dirty method to call a declared method with a generic parameter type.
     * Does try+catch for method invocation and should not throw anything for
     * the normal case. Purpose for this is generic factory registration, having
     * methods with type Object alongside methods with more specialized types.
     *
     * @param obj
     * @param methodName
     * @param arg
     * Argument or invoke the method with.
     * @return null in case of errors (can not be distinguished).
     */
    @kotlin.jvm.JvmStatic
    fun invokeGenericMethodOneArg(obj: Any?, methodName: String?, arg: Any?): Any? {
        // TODO: Isn't there a one-line-call for this ??
        val objClass: Class<*> = obj.javaClass
        val argClass: Class<*> = arg.javaClass
        // Collect methods that might work.
        var methodFound: Method? = null
        var denyObject = false
        for (method in objClass.declaredMethods) {
            if (method.name == methodName) {
                val parameterTypes = method.parameterTypes
                if (parameterTypes.size == 1) {
                    // Prevent using Object as argument if there exists a method with a specialized argument.
                    if (parameterTypes[0] != Any::class.java && !parameterTypes[0].isAssignableFrom(argClass)) {
                        denyObject = true
                    }
                    // Override the found method if none found yet and assignment is possible, or if it has a specialized argument of an already found one.
                    if (methodFound == null && parameterTypes[0].isAssignableFrom(argClass) || methodFound != null && methodFound.parameterTypes[0].isAssignableFrom(
                            parameterTypes[0]
                        )
                    ) {
                        methodFound = method
                    }
                }
            }
        }
        return if (denyObject && methodFound.getParameterTypes()[0] == Any::class.java) {
            // TODO: Throw something !?
            null
        } else if (methodFound != null && methodFound.parameterTypes[0].isAssignableFrom(argClass)) {
            try {
                methodFound.invoke(obj, arg)
            } catch (t: Throwable) {
                // TODO: Throw something !?
                null
            }
        } else {
            // TODO: Throw something !?
            null
        }
    }

    /**
     * Invoke a method without arguments, get the method matching the return
     * types best, i.e. first type is preferred. At present a result is
     * returned, even if the return type does not match at all.
     *
     * @param obj
     * @param methodName
     * @param returnTypePreference
     * Most preferred return type first, might return null, might
     * return a method with a completely different return type,
     * comparison with ==, no isAssignableForm. TODO: really ?
     * @return
     */
    fun invokeMethodNoArgs(obj: Any?, methodName: String?, vararg returnTypePreference: Class<*>?): Any? {
        // TODO: Isn't there a one-line-call for this ??
        val objClass: Class<*> = obj.javaClass
        // Try to get it directly first.
        var methodFound = getMethodNoArgs(objClass, methodName, *returnTypePreference)
        if (methodFound == null) {
            // Fall-back to seek it.
            methodFound = seekMethodNoArgs(objClass, methodName, returnTypePreference)
        }
        // Invoke if found.
        return if (methodFound != null) {
            try {
                methodFound.invoke(obj)
            } catch (t: Throwable) {
                // TODO: Throw something !?
                null
            }
        } else {
            // TODO: Throw something !?
            null
        }
    }

    /**
     * More fail-safe method invocation.
     *
     * @param method
     * @param object
     * @return null in case of failures (!).
     */
    @kotlin.jvm.JvmStatic
    fun invokeMethodNoArgs(method: Method?, `object`: Any?): Any? {
        try {
            return method.invoke(`object`)
        } catch (e: IllegalAccessException) {
        } catch (e: IllegalArgumentException) {
        } catch (e: InvocationTargetException) {
        }
        return null
    }

    /**
     * Fail-safe call.
     *
     * @param method
     * @param object
     * @param arguments
     * @return null in case of errors.
     */
    @kotlin.jvm.JvmStatic
    fun invokeMethod(method: Method?, `object`: Any?, vararg arguments: Any?): Any? {
        try {
            return method.invoke(`object`, *arguments)
        } catch (e: IllegalAccessException) {
        } catch (e: IllegalArgumentException) {
        } catch (e: InvocationTargetException) {
        }
        return null
    }

    /**
     * Direct getMethod attempt.
     *
     * @param objClass
     * @param methodName
     * @param returnTypePreference
     * @return
     */
    @kotlin.jvm.JvmStatic
    fun getMethodNoArgs(objClass: Class<*>?, methodName: String?, vararg returnTypePreference: Class<*>?): Method? {
        try {
            val methodFound = objClass.getMethod(methodName)
            if (methodFound != null) {
                if (returnTypePreference == null || returnTypePreference.size == 0) {
                    return methodFound
                }
                val returnType = methodFound.returnType
                for (i in returnTypePreference.indices) {
                    if (returnType == returnTypePreference[i]) {
                        return methodFound
                    }
                }
            }
        } catch (e: SecurityException) {
        } catch (e: NoSuchMethodException) {
        }
        return null
    }

    /**
     * Iterate over all methods, attempt to return best matching return type
     * (earliest in array).
     *
     * @param objClass
     * @param methodName
     * @param returnTypePreference
     * @return
     */
    fun seekMethodNoArgs(
        objClass: Class<*>?, methodName: String?,
        returnTypePreference: Array<Class<*>?>?
    ): Method? {
        return seekMethodNoArgs(objClass, methodName, false, *returnTypePreference)
    }

    /**
     * Iterate over all methods, attempt to return best matching return type
     * (earliest in array).
     *
     * @param objClass
     * @param methodName
     * @param returnTypePreference
     * @return
     */
    @kotlin.jvm.JvmStatic
    fun seekMethodIgnoreArgs(
        objClass: Class<*>?, methodName: String?,
        vararg returnTypePreference: Class<*>?
    ): Method? {
        return seekMethodNoArgs(objClass, methodName, true, *returnTypePreference)
    }

    private fun seekMethodNoArgs(
        objClass: Class<*>?, methodName: String?,
        ignoreArgs: Boolean, vararg returnTypePreference: Class<*>?
    ): Method? {
        // Collect methods that might work.
        var methodFound: Method? = null
        var returnTypeIndex = returnTypePreference.size // This can be 0 for no preferences given.
        // TODO: Does there exist an optimized method for getting all by name?
        for (method in objClass.getMethods()) {
            if (method.name == methodName) {
                val parameterTypes = method.parameterTypes
                if (ignoreArgs || parameterTypes.size == 0) {
                    // Override the found method if none found yet or if the return type matches the preferred policy.
                    val returnType = method.returnType
                    if (methodFound == null) {
                        methodFound = method
                        for (i in 0 until returnTypeIndex) {
                            if (returnTypePreference[i] == returnType) {
                                returnTypeIndex = i
                                break
                            }
                        }
                    } else {
                        // Check if the return type is preferred over previously found ones.
                        for (i in 0 until returnTypeIndex) {
                            if (returnTypePreference[i] == returnType) {
                                methodFound = method
                                returnTypeIndex = i
                                break
                            }
                        }
                    }
                    if (returnTypeIndex == 0) {
                        // "Quick" return.
                        break
                    }
                }
            }
        }
        return methodFound
    }

    /**
     * Get the field by name (and type). Failsafe.
     *
     * @param clazz
     * @param fieldName
     * @param type
     * Set to null to get any type of field.
     * @return Field or null.
     */
    @kotlin.jvm.JvmStatic
    fun getField(clazz: Class<*>?, fieldName: String?, type: Class<*>?): Field? {
        try {
            val field = clazz.getField(fieldName)
            if (type == null || field.type == type) {
                return field
            }
        } catch (e: NoSuchFieldException) {
        } catch (e: SecurityException) {
        }
        return null
    }

    /**
     * Set the field fail-safe.
     *
     * @param field
     * @param object
     * @param value
     * @return
     */
    @kotlin.jvm.JvmStatic
    operator fun set(field: Field?, `object`: Any?, value: Any?): Boolean {
        try {
            field.set(`object`, value)
            return true
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
        return false
    }

    @kotlin.jvm.JvmStatic
    fun getBoolean(field: Field?, `object`: Any?, defaultValue: Boolean): Boolean {
        try {
            return field.getBoolean(`object`)
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
        return defaultValue
    }

    @kotlin.jvm.JvmStatic
    fun getInt(field: Field?, `object`: Any?, defaultValue: Int): Int {
        try {
            return field.getInt(`object`)
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
        return defaultValue
    }

    @kotlin.jvm.JvmStatic
    fun getFloat(field: Field?, `object`: Any?, defaultValue: Float): Float {
        try {
            return field.getFloat(`object`)
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
        return defaultValue
    }

    @kotlin.jvm.JvmStatic
    fun getDouble(field: Field?, `object`: Any?, defaultValue: Double): Double {
        try {
            return field.getDouble(`object`)
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
        return defaultValue
    }

    @kotlin.jvm.JvmStatic
    operator fun get(field: Field?, `object`: Any?, defaultValue: Any?): Any? {
        try {
            return field.get(`object`)
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
        return defaultValue
    }

    /**
     * Fail-safe getMethod.
     *
     * @param clazz
     * @param methodName
     * @param arguments
     * @return null in case of errors.
     */
    fun getMethod(clazz: Class<*>?, methodName: String?, vararg arguments: Class<*>?): Method? {
        try {
            return clazz.getMethod(methodName, *arguments)
        } catch (e: NoSuchMethodException) {
        } catch (e: SecurityException) {
        }
        return null
    }

    /**
     * Get a method matching one of the declared argument specifications.
     *
     * @param clazz
     * @param methodName
     * @param argumentLists
     * @return The first matching method (given order).
     */
    @kotlin.jvm.JvmStatic
    fun getMethod(clazz: Class<*>?, methodName: String?, vararg argumentLists: Array<Class<*>?>?): Method? {
        var method: Method? = null
        for (arguments in argumentLists) {
            method = getMethod(clazz, methodName, *arguments)
            if (method != null) {
                return method
            }
        }
        return null
    }

    /**
     * Fail-safe.
     *
     * @param clazz
     * @param parameterTypes
     * @return null on errors.
     */
    @kotlin.jvm.JvmStatic
    fun getConstructor(clazz: Class<*>?, vararg parameterTypes: Class<*>?): Constructor<*>? {
        try {
            return clazz.getConstructor(*parameterTypes)
        } catch (e: NoSuchMethodException) {
        } catch (e: SecurityException) {
        }
        return null
    }

    /**
     * Fail-safe.
     *
     * @param constructor
     * @param arguments
     * @return null on errors.
     */
    @kotlin.jvm.JvmStatic
    fun newInstance(constructor: Constructor<*>?, vararg arguments: Any?): Any? {
        try {
            return constructor.newInstance(*arguments)
        } catch (e: InstantiationException) {
        } catch (e: IllegalAccessException) {
        } catch (e: IllegalArgumentException) {
        } catch (e: InvocationTargetException) {
        }
        return null
    }

    /**
     * Fail-safe class getting.
     * @param fullName
     * @return
     */
    @kotlin.jvm.JvmStatic
    fun getClass(fullName: String?): Class<*>? {
        try {
            return Class.forName(fullName)
        } catch (e: ClassNotFoundException) {
            // Ignore.
        }
        return null
    }

    /**
     * Convenience for debugging: Print fields and methods with types separated
     * by line breaks. Probably not safe for production use.
     *
     * @param clazz
     * @return
     */
    fun getClassDescription(clazz: Class<*>?): String? {
        // TODO: Option to sort by names ?
        val builder = StringBuilder(512)
        builder.append("Class: ")
        builder.append(clazz)
        // TODO: superclass, interfaces, generics
        for (field in clazz.getFields()) {
            builder.append("\n  ")
            builder.append(getSimpleMemberModifierDescription(field))
            builder.append(field.type.name)
            builder.append(' ')
            builder.append(field.name)
        }
        for (method in clazz.getMethods()) {
            builder.append("\n  ")
            builder.append(getSimpleMemberModifierDescription(method))
            builder.append(method.returnType.name)
            builder.append(' ')
            builder.append(method.name)
            builder.append("(")
            for (type in method.parameterTypes) {
                builder.append(type.name)
                builder.append(", ")
            }
            builder.append(")")
        }
        return builder.toString()
    }

    private fun getSimpleMemberModifierDescription(member: Member?): String? {
        val accessible = member is AccessibleObject && (member as AccessibleObject?).isAccessible()
        val mod = member.getModifiers()
        val out = if (Modifier.isPublic(mod)) "(public" else if (accessible) "(accessible" else "( -"
        return out + if (Modifier.isStatic(mod)) " static) " else ") "
    }
}