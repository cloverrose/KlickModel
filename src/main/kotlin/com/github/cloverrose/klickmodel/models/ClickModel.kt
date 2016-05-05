package com.github.cloverrose.klickmodel.models

import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.inferences.Inference
import com.github.cloverrose.klickmodel.paramcontainers.ParamContainer
import com.github.cloverrose.klickmodel.params.Param

abstract class ClickModel<T: Param> {
    abstract fun getParams(): Map<String, ParamContainer<T>>
    abstract fun setParams(params: Map<String, ParamContainer<T>>)
    abstract fun getInference(): Inference<T>

    fun train(searchSessions: List<SearchSession>){
        this.getInference().inferParams(this, searchSessions)
    }

    fun getSessionParams(searchSession: SearchSession): List<Map<String, T>> {
        val paramContainers: Map<String, ParamContainer<T>> = this.getParams()

        var sessionParams: MutableList<Map<String, T>> = mutableListOf()
        for (rank in searchSession.webResults.indices) {
            var paramDict: MutableMap<String, T> = mutableMapOf()
            for ((paramName, paramContainer) in paramContainers) {
                val param = paramContainer.getForSessionAtRank(searchSession, rank)
                paramDict[paramName] = param
            }
            sessionParams.add(paramDict)
        }
        return sessionParams
    }

    abstract fun getConditionalClickProbs(searchSession: SearchSession): List<Double>

    abstract fun getFullClickProbs(searchSession: SearchSession): List<Double>

    abstract fun predictRelevance(query: String, resultId: String): Double

    fun createNewModel(): ClickModel<T> {
        return this.javaClass.newInstance()
    }
}
