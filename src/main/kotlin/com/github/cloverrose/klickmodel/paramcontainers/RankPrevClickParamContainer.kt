package com.github.cloverrose.klickmodel.paramcontainers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.params.Param

abstract class RankPrevClickParamContainer<T: Param>(maxRank: Int): ParamContainer<T>() {
    protected var container: MutableList<MutableList<T>>
    init {
        container = mutableListOf()
        for(i in 0 until maxRank) {
            var temp: MutableList<T> = mutableListOf()
            for (j in 0 until maxRank) {
                temp.add(this.createParam())
            }
            container.add(temp)
        }
    }

    override fun size() = this.container.size * this.container[0].size

    fun get(rank: Int, rankPrevClick: Int):T {
        val _rank = if (rank == -1) container.size - 1 else rank
        val _rankPrevClick = if (rankPrevClick == -1) container[0].size - 1 else rankPrevClick
        return this.container[_rank][_rankPrevClick]
    }

    fun set(param: T, rank: Int, rankPrevClick: Int) {
        this.container[rank][rankPrevClick] = param
    }

    override fun getForSessionAtRank(searchSession: SearchSession, rank: Int) = get(rank, getPrevClickedRank(searchSession, rank))

    override fun applyEach(func: (T) -> Unit) {
        for (params in this.container) {
            for (param in params) {
                func(param)
            }
        }
    }

    fun getPrevClickedRank(searchSession: SearchSession, rank: Int): Int {
        val clicks = searchSession.getClicks()
        val prevClickRanks = clicks.subList(0, rank).withIndex().filter { it.value }.map { it.index }

        val prevClickRank = if (prevClickRanks.size > 0) prevClickRanks.last() else clicks.size - 1
        return prevClickRank
    }

    override fun toJson(): String {
        val mapper = jacksonObjectMapper()

        return mapper.writeValueAsString(container)
    }
}
