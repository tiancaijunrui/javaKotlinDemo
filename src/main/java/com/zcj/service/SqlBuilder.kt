package com.zcj.service

import java.sql.Timestamp
import java.util.*
import kotlin.collections.Map.Entry

/**
 * 这个类用于帮助开发人员快速的构建sql语句和传递查询参数

 * @author ELF@TEAM
 * *
 * @since 2016年2月25日 10:41:32
 */
class SqlBuilder() {

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
    fun append(sqlSlice: String?): SqlBuilder {
        if (sqlSlice == null) {
            return this
        }

        this.builder!!.append(sqlSplit)
        this.builder!!.append(sqlSlice)
        return this
    }

    /**
     * 去掉当前Builder中最后的一个字符，如果当前串中没有任何字符。则返回自己。

     * @return 当前HqlBuilder
     */
    fun removeLastCharacter(): SqlBuilder {
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
    fun removeLastCharacters(n: Int): SqlBuilder {
        val length = this.builder!!.length
        if (length == 0) {
            return this
        }

        val start = if (n > length) 0 else length - n
        this.builder!!.delete(start, length)
        return this
    }

    fun enableSplit(): SqlBuilder {
        this.sqlSplit = BLANK
        return this
    }

    fun disableSplit(): SqlBuilder {
        this.sqlSplit = EMPTY
        return this
    }

    val sql: String
        get() = this.builder!!.toString()

    fun append(sqlBuilder: SqlBuilder?): SqlBuilder {
        if (sqlBuilder == null) {
            return this
        }

        if (sqlBuilder === this) {
            return this
        }

        this.append(sqlBuilder.sql)
        this.addParameterMap(sqlBuilder.getParameterMap()!!)
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
    fun addParameter(placeholder: String, value: Any): SqlBuilder {
        var value = value
        if (value is Date) {
            value = Timestamp(value.time)
        }
        parameterMap!!.put(placeholder, value)
        return this
    }

    fun addParameterMap(parameters: Map<String, Any>): SqlBuilder {
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

    fun getParameterMap(): MutableMap<String, Any>? {
        return parameterMap
    }

    fun getParameterList(): MutableList<Any>? {
        return parameterList
    }

    fun setParameters(parameters: List<Any>?): SqlBuilder {
        if (parameters == null) {
            this.parameterList = ArrayList<Any>()
        } else {
            for (parameter in parameters) {
                var newPar = parameter;
                if (parameter is Date) {
                    newPar = Timestamp(parameter.time)
                }
                this.parameterList!!.add(parameter)
            }
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
