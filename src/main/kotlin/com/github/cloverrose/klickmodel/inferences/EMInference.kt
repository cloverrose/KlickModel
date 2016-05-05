package com.github.cloverrose.klickmodel.inferences

import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.models.ClickModel
import com.github.cloverrose.klickmodel.params.ParamEM

class EMInference(val iterNum: Int=50): Inference<ParamEM>() {
    override fun inferParams(clickModel: ClickModel<ParamEM>, searchSessions: List<SearchSession>) {
        if (searchSessions.size == 0) {
            return
        }

        for (iteration in 0 until iterNum) {
            val newClickModel = clickModel.createNewModel()
            for (searchSession in searchSessions) {
                var currentSessionParams = clickModel.getSessionParams(searchSession)
                var newSessionParams = newClickModel.getSessionParams(searchSession)

                for (rank in searchSession.webResults.indices) {
                    for (param in newSessionParams[rank].values) {
                        param.update(searchSession, rank, currentSessionParams)
                    }
                }
            }
            clickModel.setParams(newClickModel.getParams())
        }
    }
}
