package polish

import polish.Token.*

interface TokenVisitor {
    fun visit(token: Token.Number)
    fun visit(token: OpenBrace)
    fun visit(token: CloseBrace)
    fun visit(token: Operation)
}
