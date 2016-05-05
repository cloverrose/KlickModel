package com.github.cloverrose.klickmodel.models.ubm

import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.inferences.EMInference
import com.github.cloverrose.klickmodel.inferences.Inference
import com.github.cloverrose.klickmodel.models.ClickModel
import com.github.cloverrose.klickmodel.paramcontainers.ParamContainer
import com.github.cloverrose.klickmodel.paramcontainers.QueryDocumentParamContainer
import com.github.cloverrose.klickmodel.paramcontainers.RankPrevClickParamContainer
import com.github.cloverrose.klickmodel.params.ParamEM

internal val ATTR = "attr"
internal val EXAM = "exam"

class AttrContainer: QueryDocumentParamContainer<ParamEM>(){
    override fun createParam() = UBMAttrEM()
}
class ExamContainer(maxRank: Int): RankPrevClickParamContainer<ParamEM>(maxRank) {
    override fun createParam() = UBMExamEM()
}

class UBM: ClickModel<ParamEM>() {
    private val MAX_RANK = 10
    private var attrContainer = AttrContainer()
    private var examContainer = ExamContainer(MAX_RANK)

    override fun getParams() = mapOf(ATTR to attrContainer, EXAM to examContainer)

    override fun setParams(params: Map<String, ParamContainer<ParamEM>>) {
        val a = params[ATTR]
        if (a is AttrContainer) {
            attrContainer = a
        }
        val e = params[EXAM]
        if (e is ExamContainer) {
            examContainer = e
        }
    }

    override fun getInference(): Inference<ParamEM> = EMInference()

    override fun getConditionalClickProbs(searchSession: SearchSession): List<Double> {
        val sessionParams = getSessionParams(searchSession)

        val clickProbs: MutableList<Double> = mutableListOf()

        for ((rank, result) in searchSession.webResults.withIndex()) {
            val attr: Double = sessionParams[rank][ATTR]!!.value()
            val exam: Double = sessionParams[rank][EXAM]!!.value()

            val clickProb = if (result.isClicked) attr * exam else 1 - attr * exam
            clickProbs.add(clickProb)
        }
        return clickProbs
    }

    override fun getFullClickProbs(searchSession: SearchSession): List<Double> {
        val clickProbs: MutableList<Double> = mutableListOf()

        for(rank in searchSession.webResults.indices) {
            var clickProb = 0.0

            for (rankPrevClick in -1 until rank){
                var noClickBetween = 1.0
                for(rankBetween in rankPrevClick+1 until rank){
                    noClickBetween *= 1 - getClickProb(searchSession, rankBetween, rankPrevClick)
                }
                val x = if(rankPrevClick >= 0) clickProbs[rankPrevClick] else 1.0
                clickProb += x * noClickBetween * getClickProb(searchSession, rank, rankPrevClick)

            }
            clickProbs.add(clickProb)
        }
        return clickProbs
    }

    override fun predictRelevance(query: String, resultId: String): Double = attrContainer.get(query, resultId).value()

    fun getClickProb(searchSession: SearchSession, rank: Int, rankPrevClick: Int): Double {
        val attr = attrContainer.get(searchSession.query, searchSession.webResults[rank].id).value()
        val exam = examContainer.get(rank, rankPrevClick).value()
        return attr * exam
    }
}
