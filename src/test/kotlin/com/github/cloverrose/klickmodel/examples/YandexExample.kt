package com.github.cloverrose.klickmodel.examples

import com.github.cloverrose.klickmodel.evaluations.LogLikelihood
import com.github.cloverrose.klickmodel.evaluations.Perplexity
import com.github.cloverrose.klickmodel.models.ubm.UBM
import com.github.cloverrose.klickmodel.parsers.YandexRelPredChallengeParser
import com.github.cloverrose.klickmodel.utils.Utils

fun main(args: Array<String>) {
    val searchSessionsPath = "YandexRelPredChallenge"
    val searchSessionNum = 10000
    var clickModel = UBM()

    val searchSessions = YandexRelPredChallengeParser.parse(searchSessionsPath, searchSessionNum)

    val trainTestSplit = (searchSessions.size * 0.75).toInt()
    val trainSessions = searchSessions.subList(0, trainTestSplit)
    val trainQueries = Utils.getUniqueQueries(trainSessions)

    val testSessions = Utils.filterSessions(searchSessions.subList(trainTestSplit, searchSessions.size), trainQueries)
    val testQueries = Utils.getUniqueQueries(testSessions)

    println("-------------------------------")
    println("Training on ${trainSessions.size} search sessions (${trainQueries.size} unique queries).")
    println("-------------------------------")

    clickModel.train(trainSessions)

    println("\tTrained ${clickModel.javaClass.name} click model")

    println("-------------------------------")
    println("Testing on ${testSessions.size} search sessions (${testQueries.size} unique queries).")
    println("-------------------------------")

    val loglikelihood = LogLikelihood().evaluate(clickModel, testSessions)
    println("\tlog-likelihood: $loglikelihood")

    val perplexity = Perplexity().evaluate(clickModel, testSessions)
    println("\tperplexity: $perplexity")
}