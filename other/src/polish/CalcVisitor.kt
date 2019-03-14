package polish

import java.lang.Exception

class CalcVisitor : TokenVisitor {
    private val stack = ArrayList<Int>()
    val result: Int
        get() = stack.last()

    override fun visit(token: Token.Number) {
        stack.add(token.value)
    }

    override fun visit(token: Token.OpenBrace) {
        throw Exception("Only RPN accepted!")
    }

    override fun visit(token: Token.CloseBrace) {
        throw Exception("Only RPN accepted!")
    }

    override fun visit(token: Token.Operation) {
        if (stack.size < 2) {
            throw Exception("Bad expression")
        }
        val b = stack.last()
        stack.removeAt(stack.size - 1)
        val a = stack.last()
        stack.removeAt(stack.size - 1)
        when (token.type) {
            '+' -> stack.add(a + b)
            '-' -> stack.add(a - b)
            '*' -> stack.add(a * b)
            '/' -> stack.add(a / b)
        }
    }
}