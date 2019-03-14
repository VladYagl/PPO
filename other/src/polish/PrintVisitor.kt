package polish

import polish.Token.*

class PrintVisitor : TokenVisitor {
    override fun visit(token: Token.Number) {
        print("" + token.value + " ")
    }

    override fun visit(token: OpenBrace) {
        print('(' + " ")
    }

    override fun visit(token: CloseBrace) {
        print(')' + " ")
    }

    override fun visit(token: Operation) {
        print(token.type + " ")
    }
}