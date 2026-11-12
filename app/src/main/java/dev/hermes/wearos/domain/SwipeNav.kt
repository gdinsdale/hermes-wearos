package dev.hermes.wearos.domain

import kotlin.math.abs

/**
 * When a finger-up should advance [BackRing] (the forward swipe beside the hardware back button).
 *
 * The ring lives on the same surface as the lists, and those lists scroll two ways: a vertical
 * drag, and a finger circling the rim (edgeCircleScroll). Both travels are largely horizontal
 * on a round face — a circle's chord, a thumb's arc. Treating "14 dp sideways within 220 ms"
 * as a page change sent Settings and Chats to Chat (the start screen) at the end of an ordinary
 * scroll, and sent Sessions into the agent picker.
 *
 * A child that already consumed the stream (the circle locked, or the list took the drag) owns
 * the gesture. Otherwise only a move that ended more sideways than up-and-down advances, and a
 * rightward flick that began in the system-edge strip stays the OS back gesture.
 * No android imports — unit-tested.
 */
object SwipeNav {

    /** Thumb-flick window: a short sideways move counts only while it is still a flick. */
    const val FLICK_WINDOW_MS = 220L

    fun shouldAdvance(
        dx: Float,
        dy: Float,
        elapsedMs: Long,
        flickSlopPx: Float,
        dragSlopPx: Float,
        nearLeftEdge: Boolean,
        childConsumed: Boolean,
    ): Boolean {
        if (childConsumed) return false
        val absX = abs(dx)
        val absY = abs(dy)
        // A scroll, even one that arcs, travels farther along the list than across it.
        if (absY > absX) return false
        // Rightward from the left rim is the system edge-back, not a ring step.
        if (nearLeftEdge && dx >= flickSlopPx) return false
        val flick = absX >= flickSlopPx && elapsedMs <= FLICK_WINDOW_MS
        val drag = absX >= dragSlopPx
        return flick || drag
    }
}
