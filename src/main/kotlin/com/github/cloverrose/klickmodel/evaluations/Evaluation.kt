package com.github.cloverrose.klickmodel.evaluations

import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.models.ClickModel
import com.github.cloverrose.klickmodel.params.Param

abstract class Evaluation {
    abstract fun evaluate(clickModel: ClickModel<out Param>, searchSessions: List<SearchSession>): Double
}
