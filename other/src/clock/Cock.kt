package clock

import java.time.Instant

interface Clock {
    val now: Instant
}

class NormalClock : Clock {
    override val now: Instant
        get() = Instant.now()
}

class SetableClock : Clock {
    override var now: Instant = Instant.now()
}