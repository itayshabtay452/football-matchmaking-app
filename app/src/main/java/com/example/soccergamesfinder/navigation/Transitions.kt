package com.example.soccergamesfinder.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import androidx.compose.animation.ExperimentalAnimationApi

@OptIn(ExperimentalAnimationApi::class)
fun defaultEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
    { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) }

@OptIn(ExperimentalAnimationApi::class)
fun defaultExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
    { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) }

@OptIn(ExperimentalAnimationApi::class)
fun defaultPopEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
    { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }

@OptIn(ExperimentalAnimationApi::class)
fun defaultPopExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
    { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
