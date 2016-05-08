package com.github.cloverrose.klickmodel.evaluations

import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.models.ClickModel
import com.github.cloverrose.klickmodel.params.Param

class Perplexity: Evaluation() {
    private val RANK_MAX: Int = 10
    private val LOG2: Double = Math.log(2.0)

    private fun log2(n: Double) = Math.log(n) / LOG2

    override fun evaluate(clickModel: ClickModel<out Param>, searchSessions: List<SearchSession>): Double {
        var logPerplexityAtRank = DoubleArray(RANK_MAX)
        for (session in searchSessions) {
            var clickProbs = clickModel.getFullClickProbs(session)
            for ((rank, clickProb) in clickProbs.withIndex()) {
                var p = if (session.webResults[rank].isClicked) {
                    clickProb
                } else {
                    1 - clickProb
                }
                logPerplexityAtRank[rank] += log2(p)
            }
        }
        val perplexityAtRank = logPerplexityAtRank.map { Math.pow(2.0, -it  / searchSessions.size)}
        val perplexity = perplexityAtRank.sum() / perplexityAtRank.size
        return perplexity
    }
}
