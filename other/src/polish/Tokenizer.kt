package polish

import polish.State.*
import polish.Token.*
import java.io.Reader
import java.lang.Error

class Tokenizer(private val text: Reader) {
    val operations = listOf('+', '-', '/', '*')
    var state: State = Start

    fun process(): List<Token> {
        val tokens = ArrayList<Token>()
        do {
            val curState = state
            val x = text.read()
            val char = x.toChar()
            if (curState is Digit) {
                if (char in '0'..'9') {
                    state = Digit(curState.value * 10 + (char - '0'))
                    continue
                } else {
                    tokens.add(Number(curState.value))
                }
            }
            if (x == -1) {
                state = End
                break
            } else {
                state = Start
                when (char) {
                    in operations -> tokens.add(Operation(char))
                    '(' -> tokens.add(OpenBrace)
                    ')' -> tokens.add(CloseBrace)
                    in '0'..'9' -> state = Digit(char - '0')
                }
            }
        } while (true)

        return tokens
    }

    fun hasNext(): Boolean {
        return state !is End && state !is Error
    }
}