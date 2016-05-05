package com.github.cloverrose.klickmodel.paramcontainers

import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.params.Param

abstract class ParamContainer<T: Param> {
    abstract fun size(): Int
    abstract fun getForSessionAtRank(searchSession: SearchSession, rank: Int): T
    abstract fun applyEach(func: (T) -> Unit)
    abstract fun createParam(): T
}
