package com.github.cloverrose.klickmodel.params

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.cloverrose.klickmodel.domain.SearchSession

private val PROB_MIN = 0.000001

abstract class ParamEM: Param() {
    @JsonProperty("n")
    protected var numerator: Double = 1.0

    @JsonProperty("d")
    protected var denominator: Double = 5.0

    override fun value(): Double = Math.min(this.numerator / this.denominator, 1 - PROB_MIN)

    abstract fun update(searchSession: SearchSession, rank: Int, sessionParams: List<Map<String, ParamEM>>)
}
