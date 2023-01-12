package org.carPooling

import java.util.UUID
import kotlin.random.Random

fun anyPositiveInt(): Int =
    Random.nextInt(
        from = 1,
        until = Integer.MAX_VALUE
    )

fun anyPositiveShort(
    minValue: Short,
    maxValue: Short,
): Short =
    Random.nextInt(
        from = minValue.toInt(),
        until = maxValue.toInt().inc()
    ).toShort()

fun anyString(): String = UUID.randomUUID().toString()

fun anyGenericException(): Exception = Exception(anyString())

fun <T> anyNotEmptyListOf(function: () -> T): List<T> =
    (1..Random.nextInt(1, RANDOM_LIST_MAX_SIZE)).map { function.invoke() }

private const val RANDOM_LIST_MAX_SIZE = 10