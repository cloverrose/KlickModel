package com.github.cloverrose.klickmodel.parsers

import com.github.cloverrose.klickmodel.domain.SearchSession

abstract class Parser {
    abstract fun parse(sessionsFilename: String, sessionsMax: Int): List<SearchSession>
}
