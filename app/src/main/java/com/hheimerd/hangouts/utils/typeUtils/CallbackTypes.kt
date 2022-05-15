package com.hheimerd.hangouts.utils.typeUtils

typealias Action = () -> Unit
typealias ActionWith<T> = (T) -> Unit

typealias Func<TResult> = () -> TResult
typealias FuncWith<T,TResult> = (T) -> TResult


