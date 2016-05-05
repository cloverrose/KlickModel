package com.github.cloverrose.klickmodel.params

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.cloverrose.klickmodel.domain.SearchSession

abstract class ParamEM: Param() {
    private var PROB_MIN = 0.000001

    @JsonProperty("n")
    private var numerator: Double = 1.0

    @JsonProperty("d")
    private var denominator: Double = 5.0

    protected fun incNumerator() {
        this.numerator++
    }

    protected fun addNumerator(n: Double) {
        this.numerator + n
    }

    protected fun incDenominator() {
        this.denominator++
    }

    override fun value(): Double = Math.min(this.numerator / this.denominator, 1 - this.PROB_MIN)

    abstract fun update(searchSession: SearchSession, rank: Int, sessionParams: List<Map<String, ParamEM>>)
}
