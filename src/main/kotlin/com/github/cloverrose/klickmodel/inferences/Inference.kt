package com.github.cloverrose.klickmodel.inferences

import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.models.ClickModel
import com.github.cloverrose.klickmodel.params.Param

abstract class Inference<T: Param> {
    abstract fun inferParams(clickModel: ClickModel<T>, searchSessions: List<SearchSession>)
}
