package polish

import polish.Token.*
import java.lang.Exception

class ParserVisitor : TokenVisitor {
    private val tokens = ArrayList<Token>()
    private val stack = ArrayList<Token>()

    private val operations = hashMapOf('+' to 0, '-' to 0, '*' to 1, '/' to 1)

    override fun visit(token: Token.Number) {
        tokens.add(token)
    }

    override fun visit(token: Operation) {
        while (stack.isNotEmpty() && stack.last() is Operation &&
            operations[(stack.last() as Operation).type]!! >= operations[token.type]!!
        ) {
            tokens.add(stack.last())
            stack.removeAt(stack.size - 1)
        }
        stack.add(token)
    }

    override fun visit(token: Token.OpenBrace) {
        stack.add(token)
    }

    override fun visit(token: Token.CloseBrace) {
        while (stack.isNotEmpty() && stack.last() !is OpenBrace) {
            tokens.add(stack.last())
            stack.removeAt(stack.size - 1)
        }
        if (stack.isNotEmpty() && stack.last() is OpenBrace) {
            stack.removeAt(stack.size - 1)
        } else {
            throw Exception("Not paired braces")
        }
    }

    fun getResult(): ArrayList<Token> {
        while (stack.isNotEmpty()) {
            if (stack.last() is OpenBrace) {
                throw Exception("Not paired braces")
            }
            tokens.add(stack.last())
            stack.removeAt(stack.size - 1)
        }
        return tokens
    }
}