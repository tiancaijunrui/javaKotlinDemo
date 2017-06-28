package com.zcj.service

import java.sql.Timestamp
import java.util.*
import kotlin.collections.Map.Entry

/**
 * 这个类用于帮助开发人员快速的构建hql语句和传递查询参数

 * @author ELF@TEAM
 * *
 * @since 2016年2月25日 10:41:32
 */
class HqlBuilder() {

    var sqlSplit: String? = null
        private set
    private var builder: StringBuilder? = null
    private var parameterList: MutableList<Any>? = null
    private var parameterMap: MutableMap<String, Any>? = null
    var firstRecordIndex: Int? = null
    var maxRecordNum: Int? = null

    init {
        init()
    }

    constructor(sqlSlice: String) : this() {
        append(sqlSlice)
    }

    /**
     * 初始化当前的Hql Builder.
     */
    protected fun init() {
        this.sqlSplit = BLANK
        this.builder = StringBuilder()
        this.parameterList = ArrayList<Any>()
        this.parameterMap = HashMap<String, Any>()
    }

    /**
     * 清理当前的Hql Builder,清空所有已经存在的数据.
     */
    fun clear() {
        init()
    }

    /**
     * 往Hql Builder里面增加新的Hql对象。

     * @param sqlSlice sql片段
     * *
     * @return 当前HqlBuilder
     */
    fun append(sqlSlice: String?): HqlBuilder {
        if (sqlSlice == null) {
            return this
        }

        this.builder!!.append(sqlSplit)
        this.builder!!.append(sqlSlice)
        return this
    }

    /**
     * 往Hql Builder里面增加Hql Slice,同时添加对应的参数.<br></br>
     * 注意这个参数是直接往参数List里面添加的.

     * @param sqlSlice   新的Hql片段.
     * *
     * @param parameters 需要添加的参数数组.
     * *
     * @return 当前对象.
     */
    fun append(sqlSlice: String, vararg parameters: Any): HqlBuilder {
        this.append(sqlSlice)
        this.addParameterList(Arrays.asList(*parameters))
        return this
    }

    /**
     * 往Hql Builder里面增加新的Hql片段,同时它不会添加与之前sql片段的分隔。

     * @param sqlSlice sql片段
     * *
     * @return 当前HqlBuilder
     */
    fun escapeAppend(sqlSlice: String?): HqlBuilder {
        if (sqlSlice == null) {
            return this
        }

        this.builder!!.append(sqlSlice)
        return this
    }

    /**
     * 去掉当前Builder中最后的一个字符，如果当前串中没有任何字符。则返回自己。

     * @return 当前HqlBuilder
     */
    fun removeLastCharacter(): HqlBuilder {
        val length = this.builder!!.length
        if (length <= 0) {
            return this
        }

        this.builder!!.deleteCharAt(length - 1)
        return this
    }

    /**
     * 去掉当前Builder中最后的一个字符，如果当前串中没有任何字符。<br></br>
     * 则返回自己。 如果已经存在的字符的个数少于要删除的字符个数，则删除全部字符。

     * @return 当前HqlBuilder
     */
    fun removeLastCharacters(n: Int): HqlBuilder {
        val length = this.builder!!.length
        if (length == 0) {
            return this
        }

        val start = if (n > length) 0 else length - n
        this.builder!!.delete(start, length)
        return this
    }

    /**
     * 向当前的Sql起始部分插入一个串。

     * @param slice 要插入的Sql片段。
     * *
     * @return 当前HqlBuilder
     */
    fun insertStartSlice(slice: String?): HqlBuilder {
        if (slice == null || slice.isEmpty()) {
            return this
        }

        this.builder!!.insert(0, sqlSplit!! + slice)
        return this
    }

    /**
     * 首先检查当前Sql的末尾部分，如果末尾部分和oldSlice相同，<br></br>
     * newSlice替换oldSlice.如果末尾部分和oldSlice不相同，<br></br>
     * 这把当前的newSilice追加到当前SqlSlice的末尾。

     * @param newSlice 要添加的SQL Slice。
     * *
     * @param oldSlice 可能已存在的Hql Slice
     * *
     * @return 当前HqlBuilder
     */
    fun replaceOrAddLastSlice(newSlice: String, oldSlice: String?): HqlBuilder {
        if (oldSlice == null || oldSlice.isEmpty()) {
            append(newSlice)
            return this
        }

        val sqlLength = this.builder!!.length
        val oldSliceLength = oldSlice.length
        if (oldSliceLength > sqlLength) {
            append(newSlice)
            return this
        }

        val lastIndexOf = this.builder!!.lastIndexOf(oldSlice)
        if (lastIndexOf == sqlLength - oldSliceLength) {
            this.builder!!.replace(lastIndexOf, sqlLength, newSlice)
        } else {
            append(newSlice)
        }

        return this
    }

    /**
     * 用于删除字符串的最后一个字符

     * @param str 需要处理的字符串
     * *
     * @return 返回去除后的builder
     */
    fun removeLastSlice(str: String?): HqlBuilder {
        if (str == null || str.isEmpty()) {
            return this
        }

        val length = this.builder!!.length
        val lastIndexOf = this.builder!!.lastIndexOf(str)
        if (lastIndexOf != -1 && lastIndexOf == length - str.length) {
            this.builder!!.delete(lastIndexOf, length)
        }

        return this
    }

    fun enableSplit(): HqlBuilder {
        this.sqlSplit = BLANK
        return this
    }

    fun disableSplit(): HqlBuilder {
        this.sqlSplit = EMPTY
        return this
    }

    val sql: String
        get() = this.builder!!.toString()

    fun append(hqlBuilder: HqlBuilder?): HqlBuilder {
        if (hqlBuilder == null) {
            return this
        }

        if (hqlBuilder === this) {
            return this
        }

        this.append(hqlBuilder.sql)
        this.addParameterMap(hqlBuilder.getParameterMap()!!)
        this.addParameterList(hqlBuilder.getParameterList())
        return this
    }

    /**
     * 用当前的sql片段替换当前builder中的最后一个字符。

     * @param slice sql片段。
     * *
     * @return 当前HqlBuilder
     */
    fun replaceLastCharacter(slice: String?): HqlBuilder {
        if (slice == null) {
            return this
        }

        val length = this.builder!!.length
        if (length == 0) {
            return this
        }

        this.builder!!.replace(length - 1, length, slice)
        return this
    }

    /**
     * 拼写SQL语句 添加参数

     * @param placeholder 参数名
     * *
     * @param value       参数值
     * *
     * @return 当前SQL
     */
    fun addParameter(placeholder: String, value: Any): HqlBuilder {
        var value = value
        if (value is Date) {
            value = Timestamp(value.time)
        }
        parameterMap!!.put(placeholder, value)
        return this
    }

    fun addParameterMap(parameters: Map<String, Any>): HqlBuilder {
        val entrySet = parameters.entries
        for (entry in entrySet) {
            val key = entry.key
            var value = entry.value
            if (value is Date) {
                value = Timestamp(value.time)
            }
            parameterMap!!.put(key, value)
        }
        return this
    }

    fun setParameterMap(parameters: Map<String, Any>?): HqlBuilder {
        if (parameters == null) {
            parameterMap = HashMap<String, Any>()
        } else {
            val entrySet = parameters.entries
            for (entry in entrySet) {
                val key = entry.key
                var value = entry.value
                if (value is Date) {
                    value = Timestamp(value.time)
                }
                parameterMap!!.put(key, value)
            }
        }
        return this
    }

    fun getParameterMap(): MutableMap<String, Any>? {
        return parameterMap
    }

    fun getParameterList(): MutableList<Any>? {
        return parameterList
    }

    fun setParameters(parameters: List<Any>?): HqlBuilder {
        if (parameters == null) {
            this.parameterList = ArrayList<Any>()
        } else {
            for (parameter in parameters) {
                var newPar = parameter;
                if (parameter is Date) {
                    newPar = Timestamp(parameter.time)
                }
                this.parameterList!!.add(newPar)
            }
        }
        return this
    }

    fun addParameter(parameter: Any): HqlBuilder {
        var parameter = parameter
        if (parameter is Date) {
            parameter = Timestamp(parameter.time)
        }
        this.parameterList!!.add(parameter)
        return this
    }

    fun addParameters(vararg parameters: Any): HqlBuilder {
        val parameterList = Arrays.asList(*parameters)
        for (parameter in parameterList) {
            var newPar = parameter;
            if (parameter is Date) {
                newPar = Timestamp(parameter.time)
            }
            this.parameterList!!.add(parameter)
        }
        return this
    }

    fun addParameterList(parameters: List<Any>?): HqlBuilder {
        if (parameters == null || parameters.isEmpty()) {
            return this
        }

        for (parameter in parameters) {
            var newPar = parameter;
            if (parameter is Date) {
                newPar = Timestamp(parameter.time)
            }
            this.parameterList!!.add(parameter)
        }
        return this
    }

    override fun toString(): String {
        return this.sql
    }

    companion object {

        private val BLANK = " "
        private val EMPTY = ""
    }
}