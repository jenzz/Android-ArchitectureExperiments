package com.jenzz.arch.mvi

import androidx.compose.runtime.mutableStateOf

interface State

interface Action

interface Reducer<S : State, A : Action> {

    fun reduce(state: S, action: A): S
}

interface Middleware<S : State, A : Action> {

    suspend fun process(state: S, action: A, store: Store<S, A>)
}

interface Store<S : State, A : Action> {

    val state: androidx.compose.runtime.State<S>

    suspend fun dispatch(action: A)
}

abstract class BaseStore<S : State, A : Action>(
    initialState: S,
    private val reducer: Reducer<S, A>,
    private val middlewares: List<Middleware<S, A>> = emptyList(),
) : Store<S, A> {

    private val _state = mutableStateOf(initialState)
    override val state: androidx.compose.runtime.State<S> = _state

    override suspend fun dispatch(action: A) {
        val newState = reducer.reduce(state.value, action)
        _state.value = newState

        middlewares.forEach { middleware ->
            middleware.process(state.value, action, this)
        }
    }
}
