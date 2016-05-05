package com.github.cloverrose.klickmodel.evaluations

import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.models.ClickModel

import com.github.cloverrose.klickmodel.params.Param

class LogLikelihood: Evaluation() {
     override fun evaluate(clickModel: ClickModel<out Param>, searchSessions: List<SearchSession>): Double {
         var loglikelihood = 0.0

         for (session in searchSessions) {
             val clickProbs = clickModel.getConditionalClickProbs(session)
             val logClickProbs = clickProbs.map { Math.log(it) }
             loglikelihood += logClickProbs.sum() / logClickProbs.size
         }
         loglikelihood /= searchSessions.size
         return loglikelihood
    }
}
