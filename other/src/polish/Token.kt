package polish

sealed class Token {
    abstract fun accept(visitor: TokenVisitor)

    object OpenBrace : Token() {
        override fun accept(visitor: TokenVisitor) {
            visitor.visit(this)
        }
    }

    object CloseBrace : Token() {
        override fun accept(visitor: TokenVisitor) {
            visitor.visit(this)
        }
    }

    data class Operation(val type: Char) : Token() {
        override fun accept(visitor: TokenVisitor) {
            visitor.visit(this)
        }
    }

    data class Number(val value: Int) : Token() {
        override fun accept(visitor: TokenVisitor) {
            visitor.visit(this)
        }
    }
}