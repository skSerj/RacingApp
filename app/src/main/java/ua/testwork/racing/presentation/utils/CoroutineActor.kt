package ua.testwork.racing.presentation.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor

private const val ACTOR_TAG = "completetion_actor"

sealed class ActorMessage {
    object ProcessCompleted : ActorMessage()
}

@OptIn(ObsoleteCoroutinesApi::class)
fun CoroutineScope.completionActor(targetCount: Int) =
    actor<ActorMessage> {
        var completedCount = 0
        for (message in channel) {
            when (message) {
                is ActorMessage.ProcessCompleted -> {
                    completedCount++
                    Log.i(ACTOR_TAG, "Completed count: $completedCount")
                    if (completedCount >= targetCount) {
                        channel.close()
                    }
                }
            }
        }
    }