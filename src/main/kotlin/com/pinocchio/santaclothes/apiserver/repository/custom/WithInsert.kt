package com.pinocchio.santaclothes.apiserver.repository.custom

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jdbc.core.JdbcAggregateTemplate

interface WithInsert<T> {
    fun insert(t: T): T
}

class WithInsertImpl<T>(@Autowired val template: JdbcAggregateTemplate) : WithInsert<T> {
    override fun insert(t: T): T {
        return template.insert(t)
    }
}
