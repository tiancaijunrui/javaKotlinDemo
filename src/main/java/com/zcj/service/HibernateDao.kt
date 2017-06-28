package com.zcj.service

import org.apache.commons.collections.CollectionUtils
import org.apache.commons.collections.MapUtils
import org.hibernate.Criteria
import org.hibernate.Query
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.DetachedCriteria
import org.hibernate.criterion.Projections
import org.hibernate.metadata.ClassMetadata
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.orm.hibernate4.HibernateTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource
import java.io.Serializable
import java.util.ArrayList

@Repository
class HibernateDao {
    lateinit var hibernateTemplate: HibernateTemplate

    @Autowired
    constructor(sessionFactory: SessionFactory) {
        this.hibernateTemplate = HibernateTemplate(sessionFactory)
    }

    constructor() {}

    val session: Session
        get() = hibernateTemplate.sessionFactory.currentSession

    @Resource
    val jdbcTemplate: JdbcTemplate? = null

    fun countRecordsNumber(dc: DetachedCriteria, countDistinctProjections: String): Int {
        dc.setProjection(Projections.countDistinct(countDistinctProjections))
        val list = this.hibernateTemplate.findByCriteria(dc)
        var result = 0
        for (aList in list) {
            val item = Integer.parseInt(aList.toString() + "")
            result += item
        }

        dc.setProjection(null)// 避免对dc.setProjection影响到其它地方
        return result
    }

    fun findByCriteria(dc: DetachedCriteria, vararg firstResultAndMaxResults: Int): List<*> {
        dc.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)

        if (firstResultAndMaxResults != null && firstResultAndMaxResults.size == 2) {
            return this.hibernateTemplate.findByCriteria(dc, firstResultAndMaxResults[0],
                    firstResultAndMaxResults[1])
        }

        return hibernateTemplate.findByCriteria(dc)
    }

    fun findByCriteria(dc: DetachedCriteria): List<*> {
        dc.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
        return hibernateTemplate.findByCriteria(dc)
    }

    /**
     * 求传入的QBC查询总记录条数

     * @param criteria QBC对象
     * *
     * @return
     */
    fun getTotalSizeForCriteria(criteria: DetachedCriteria): Int {
        // 获取根据条件分页查询的总行数
        var totalSize = 0
        criteria.setProjection(Projections.rowCount())
        val list = this.findByCriteria(criteria)
        if (list.isEmpty()) {
            return totalSize
        } else {
            totalSize = Integer.parseInt(list[0].toString())
        }

        criteria.setProjection(null)// 清除count函数
        return totalSize
    }

    /**
     * 使用HSQL语句检索数据

     * @param queryString
     * *
     * @return
     */
    fun find(queryString: String): List<*> {
        val query = this.session.createQuery(queryString)
        return query.list()
    }

    /**
     * 使用带参数的HSQL语句检索数据

     * @param queryString
     * *
     * @param value
     * *
     * @return
     */
    fun find(queryString: String, value: Any): List<*> {
        val query = this.session.createQuery(queryString)
        query.setParameter(0, value)
        return query.list()
    }


    /**
     * 使用带参数的HSQL语句检索数据

     * @param queryString
     * *
     * @param value
     * *
     * @return
     */
    fun find(queryString: String, value: Array<Any>?): List<*> {
        val query = this.session.createQuery(queryString)
        if (value != null) {
            for (i in value.indices) {
                query.setParameter(i, value[i])
            }
        }

        return query.list()
    }

    /**
     * 使用HSQL语句直接增加、更新、删除实体

     * @param queryString
     * *
     * @param values
     * *
     * @return
     */
    fun bulkUpdate(queryString: String, values: Array<Any>): Int {
        return hibernateTemplate.bulkUpdate(queryString, *values)
    }

    operator fun <T> get(entityClass: Class<T>, id: Serializable): T {
        return this.session.get(entityClass, id) as T
    }

    fun <T> load(entityClass: Class<T>, id: Serializable): T {
        return this.session.load(entityClass, id) as T
    }

    fun <T> merge(model: T): T {
        return this.session.merge(model) as T
    }

    fun saveOrUpdate(model: Any) {
        this.session.saveOrUpdate(model)
    }

    fun save(model: Any): String {
        return this.session.save(model) as String
    }

    fun <T> batchSave(models: List<T>): List<String> {
        val modelIds = ArrayList<String>(models.size)
        val session = this.session
        for (i in models.indices) {
            val model = models[i]
            val modelId = session.save(model) as String
            modelIds.add(modelId)
            if (i % 100 == 0) {
                session.flush()
                session.clear()
            }
        }

        return modelIds
    }

    fun <T> batchSaveOrUpdate(models: List<T>) {
        val session = this.session
        for (i in models.indices) {
            val model = models[i]
            session.saveOrUpdate(model)
            if (i % 100 == 0) {
                session.flush()
                session.clear()
            }
        }
    }

    fun <T> delete(entityClass: Class<T>, id: Serializable) {
        this.session.delete(get(entityClass, id))
    }

    fun <T> delete(entity: T) {
        this.session.delete(entity)
    }

    fun flush() {
        this.session.flush()
    }

    fun clean() {
        this.session.clear()
    }

    fun update(model: Any) {
        this.session.update(model)
    }

    /**
     * 查询某列的最大值id

     * @param criteria QBC对象
     * *
     * @return
     */
    fun getMaxIdForCriteria(criteria: DetachedCriteria, propertyName: String): Int {
        var maxSize = 0
        criteria.setProjection(Projections.max(propertyName))
        var list: List<*>
        list = this.findByCriteria(criteria)
        if (list.isEmpty()) {
            return maxSize
        } else {
            if (list[0] == null) {
                return maxSize
            } else {
                maxSize = Integer.parseInt(list[0].toString())
            }
        }
        criteria.setProjection(null)// 清除count函数
        return maxSize
    }
    //TODO ADD JdbcTemplate

    /**
     * <根据HQL语句查找唯一实体>

     * @param hqlString HQL语句
     * *
     * @param values    不定参数的Object数组
     * *
     * @return 查询实体
    </根据HQL语句查找唯一实体> */
    fun <T> getByHQL(hqlString: String, vararg values: Any): T {
        val query = this.session.createQuery(hqlString)
        if (values != null) {
            for (i in values.indices) {
                query.setParameter(i, values[i])
            }
        }

        return query.uniqueResult() as T
    }

    /**
     * <根据HQL语句查找唯一实体>

     * @param hqlString HQL语句
     * *
     * @return 查询实体
    </根据HQL语句查找唯一实体> */
    fun <T> getByHQL(hqlString: String): T {
        val query = this.session.createQuery(hqlString)
        return query.uniqueResult() as T
    }

    /**
     * <根据SQL语句查找唯一实体>

     * @param sqlString SQL语句
     * *
     * @param values    不定参数的Object数组
     * *
     * @return 查询实体
    </根据SQL语句查找唯一实体> */
    fun <T> getBySQL(sqlString: String, vararg values: Any): T {
        val query = this.session.createSQLQuery(sqlString)
        if (values != null) {
            for (i in values.indices) {
                query.setParameter(i, values[i])
            }
        }
        return query.uniqueResult() as T
    }

    /**
     * <根据HQL得到记录数>

     * @param hql    HQL语句
     * *
     * @param values 不定参数的Object数组
     * *
     * @return 记录总数
    </根据HQL得到记录数> */
    fun <T> countByHql(hql: String, vararg values: Any): T {
        val query = this.session.createQuery(hql)
        if (values != null) {
            for (i in values.indices) {
                query.setParameter(i, values[i])
            }
        }
        return query.uniqueResult() as T
    }

    /**
     * 查询一组数据

     * @param hqlBuilder hqlBuilder
     * *
     * @return 查询结果列表
     */
    fun <T> queryList(hqlBuilder: HqlBuilder): MutableList<Any?>? {
        val query = this.session.createQuery(hqlBuilder.sql)
        setParameter(hqlBuilder, query)
        return query.list()
    }

    /**
     * 查询单个数据

     * @param hqlBuilder hqlBuilder
     * *
     * @return 查询结果
     */
    fun <T> queryUniqueResult(hqlBuilder: HqlBuilder): T {
        val query = this.session.createQuery(hqlBuilder.sql)
        setParameter(hqlBuilder, query)
        return query.uniqueResult() as T
    }

    /**
     * 执行更新的hql

     * @param hqlBuilder hqlBuilder
     * *
     * @return 返回更新数据的条数
     */
    fun executeUpdate(hqlBuilder: HqlBuilder): Int {
        val query = this.session.createQuery(hqlBuilder.sql)
        setParameter(hqlBuilder, query)
        return query.executeUpdate()
    }

    /**
     * 设置hqlBuilder的参数
     */
    private fun setParameter(hqlBuilder: HqlBuilder, query: Query) {
        val parameterList = hqlBuilder.getParameterList()
        if (CollectionUtils.isNotEmpty(parameterList)) {
            for (i in parameterList!!.indices) {
                val obj = parameterList[i]
                query.setParameter(i, obj)
            }
        }

        val parameterMap = hqlBuilder.getParameterMap()
        if (MapUtils.isNotEmpty(parameterMap)) {
            for ((placeHolder, paramValue) in parameterMap!!) {
                if (paramValue is Collection<*>) {
                    query.setParameterList(placeHolder, paramValue)
                } else {
                    query.setParameter(placeHolder, paramValue)
                }
            }
        }

        val firstRecordIndex = hqlBuilder.firstRecordIndex
        if (firstRecordIndex != null && firstRecordIndex >= 0) {
            query.firstResult = firstRecordIndex
        }

        val maxRecordNum = hqlBuilder.maxRecordNum
        if (maxRecordNum != null && maxRecordNum > 0) {
            query.maxResults = maxRecordNum
        }
    }

    /**
     * 根据主键id列表批量删除一组数据

     * @param modelIds 主键列表
     * *
     * @param clazz    class对象
     * *
     * @return 删除数据的条数
     */
    fun delete(modelIds: List<String>, clazz: Class<*>): Int {
        val classMetadata = hibernateTemplate.sessionFactory.getClassMetadata(clazz)
        val idPropertyName = classMetadata.identifierPropertyName
        val batchDeleteBuilder = HqlBuilder()
        batchDeleteBuilder.append("delete from " + clazz.simpleName)
        batchDeleteBuilder.append("where $idPropertyName in (:modelIds)")
        batchDeleteBuilder.addParameter("modelIds", modelIds)
        return executeUpdate(batchDeleteBuilder)
    }

    /**
     * <使用SQL查询所需指定类型的结果>

     * @param sqlBuilder sql对象
     * *
     * @param entryClass 需要返回的类对象
     * *
     * @param <T>        泛型
     * *
     * @return 查询结果
    </T></使用SQL查询所需指定类型的结果> */
    fun <T> queryBySQL(sqlBuilder: SqlBuilder, entryClass: Class<T>): List<T> {
        return NamedParameterJdbcTemplate(jdbcTemplate!!.getDataSource())
                .queryForList(sqlBuilder.sql, sqlBuilder.getParameterMap(), entryClass)
    }

    /**
     * <使用SQL查询所需指定实体类型的结果>

     * @param sqlBuilder sql对象
     * *
     * @param entryClass 需要返回的类对象
     * *
     * @param <T>        泛型
     * *
     * @return 查询结果
    </T></使用SQL查询所需指定实体类型的结果> */
    fun <T> queryModelBySQL(sqlBuilder: SqlBuilder, entryClass: Class<T>): List<T> {
        return NamedParameterJdbcTemplate(jdbcTemplate!!.getDataSource())
                .query(sqlBuilder.sql, sqlBuilder.getParameterMap(), BeanPropertyRowMapper.newInstance(entryClass))
    }

    /**
     * <使用SQL查询所需指定实体类型的结果>

     * @param sqlBuilder sql对象
     * *
     * @return 查询结果
    </使用SQL查询所需指定实体类型的结果> */
    fun queryBySQL(sqlBuilder: SqlBuilder): List<Map<String, Any>> {
        return NamedParameterJdbcTemplate(jdbcTemplate!!.getDataSource()).queryForList(sqlBuilder.sql, sqlBuilder.getParameterMap())
    }

    /**
     * <使用SQL查询所需指定实体类型的结果>

     * @param sqlBuilder sql对象
     * *
     * @return 查询结果
    </使用SQL查询所需指定实体类型的结果> */
    fun queryForMap(sqlBuilder: SqlBuilder): Map<String, Any> {
        return NamedParameterJdbcTemplate(jdbcTemplate!!.getDataSource()).queryForMap(sqlBuilder.sql, sqlBuilder.getParameterMap())
    }

    /**
     * 批量保存实体方法

     * @param entities:实体列表
     * *
     * @param <T>:泛型
    </T> */
    fun <T> saveOrUpdateEntityList(entities: List<T>) {
        for (t in entities) {
            saveOrUpdate(t!!)
        }
    }
}