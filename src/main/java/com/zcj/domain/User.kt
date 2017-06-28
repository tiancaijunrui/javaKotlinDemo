package com.zcj.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

/**
 * @Since2017/6/26 ZhaCongJie@HF
 */
@Entity
@Table(name = "t_uc_user")
class User {
    @Id
    @Column(nullable = false)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    var id: String? = null
    @Column(nullable = false)
    var loginName: String? = null
    @Column(nullable = false)
    var passWord: String? = null
    @Column
    var nickName: String? = null
    @Column
    var phone: String? = null
    @Column
    var status: String? = null

    companion object {
        val _id = "id"
        val _loginName = "loginName"
        val _passWord = "passWord"
        val _nickName = "nickName"
        val _phone = "phone"
        val _status = "status"
    }
}
