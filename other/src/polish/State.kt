package polish

sealed class State {
    object Start : State()
    data class Error(val message: String) : State()
    object End : State()
    data class Digit(val value: Int) : State()
}
