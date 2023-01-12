package org.carPooling.shared.application

interface QueryUseCase<Query, Result> {
    fun query(query: Query): Result
}