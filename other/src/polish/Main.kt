package polish

import java.io.File

// TODO ERRORS

fun main(args: Array<String>) {
    val tokens = Tokenizer(File(args[0]).reader()).process()
    println(tokens)
    val parserVisitor = ParserVisitor()
    tokens.forEach { it.accept(parserVisitor) }
    val polishTokens = parserVisitor.getResult()
    println(polishTokens)

    val printVisitor = PrintVisitor()
    val calcVisitor = CalcVisitor()
    tokens.forEach { it.accept(printVisitor) }
    println()
    polishTokens.forEach { it.accept(printVisitor) }
    println()
    polishTokens.forEach { it.accept(calcVisitor) }
    println(calcVisitor.result)
}