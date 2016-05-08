package com.github.cloverrose.klickmodel.models.ubm

import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.params.ParamEM

internal class UBMExamEM: ParamEM() {
    override fun update(searchSession: SearchSession, rank: Int, sessionParams: List<Map<String, ParamEM>>) {
        val attr = sessionParams[rank][ATTR]!!.value()
        val exam = sessionParams[rank][EXAM]!!.value()

        if (searchSession.webResults[rank].isClicked) {
            numerator++
        } else {
            numerator += (1 - attr) * exam / (1 - exam * attr)
        }
        denominator++
    }
}
