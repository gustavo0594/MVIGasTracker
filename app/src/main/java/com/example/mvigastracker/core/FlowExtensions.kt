package com.example.mvigastracker.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

fun <T1, T2, R> zip(
    first: Flow<T1>,
    second: Flow<T2>,
    transform: suspend (T1, T2) -> R
): Flow<R> =
    first.zip(second) { a, b -> transform(a, b) }

fun <T1, T2, T3, R> zip(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
    transform: suspend (T1, T2, T3) -> R
): Flow<R> =
    first.zip(second) { a, b -> a to b }
        .zip(third) { (a, b), c -> transform(a, b, c) }