package com.github.cloverrose.klickmodel.paramcontainers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.params.Param

abstract class QueryDocumentParamContainer<T: Param>: ParamContainer<T>() {
    protected var container: MutableMap<String, MutableMap<String, T>> = mutableMapOf()

    override fun size() = container.size

    fun get(query: String, resultId: String): T {
        if (query  !in container) {
            container[query] = mutableMapOf()
        }
        var c = container[query]!!
        if (resultId !in c) {
            c[resultId] = this.createParam()
        }

        return c[resultId]!!
    }

    override fun getForSessionAtRank(searchSession: SearchSession, rank: Int) = get(searchSession.query, searchSession.webResults[rank].id)

    override fun applyEach(func: (T) -> Unit) {
        for (paramDict in this.container.values) {
            for (param in paramDict.values) {
                func(param)
            }
        }
    }

    override fun toJson(): String {
        val mapper = jacksonObjectMapper()

        return mapper.writeValueAsString(container)
    }
}
