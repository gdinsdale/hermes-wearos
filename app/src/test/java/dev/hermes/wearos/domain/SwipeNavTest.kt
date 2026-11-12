package dev.hermes.wearos.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The forward swipe must not steal a scroll. Settings and Chats both rejoin the ring at Chat,
 * so a stolen scroll is "the app jumped back to the start."
 */
class SwipeNavTest {

    private val flick = 14f
    private val drag = 40f

    @Test fun `edge circle or list that consumed the touch does not advance`() {
        assertFalse(SwipeNav.shouldAdvance(80f, 10f, 100L, flick, drag, false, childConsumed = true))
    }

    @Test fun `vertical scroll does not advance even if it drifted sideways`() {
        assertFalse(SwipeNav.shouldAdvance(20f, 70f, 180L, flick, drag, false, childConsumed = false))
    }

    @Test fun `sideways flick inside the window advances`() {
        assertTrue(SwipeNav.shouldAdvance(-18f, 4f, 160L, flick, drag, false, childConsumed = false))
        assertTrue(SwipeNav.shouldAdvance(18f, 4f, 160L, flick, drag, false, childConsumed = false))
    }

    @Test fun `sideways flick after the window does not advance until it is a drag`() {
        assertFalse(SwipeNav.shouldAdvance(-18f, 2f, 400L, flick, drag, false, childConsumed = false))
        assertTrue(SwipeNav.shouldAdvance(-48f, 6f, 500L, flick, drag, false, childConsumed = false))
    }

    @Test fun `rightward flick from the left edge stays the system back gesture`() {
        assertFalse(SwipeNav.shouldAdvance(20f, 2f, 100L, flick, drag, nearLeftEdge = true, childConsumed = false))
    }

    @Test fun `leftward flick from the left edge still advances`() {
        assertTrue(SwipeNav.shouldAdvance(-20f, 2f, 100L, flick, drag, nearLeftEdge = true, childConsumed = false))
    }
}
