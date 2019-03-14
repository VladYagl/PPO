fun List<Int>.mysum(): Int {
    return this.reduce { a, b -> a + b }
}

fun main(args: Array<String>) {
    val test = listOf(0, 1, 2, 3)
    println(test.mysum())
//    val testStr = listOf("tes", "tasdf")
//    println(testStr.mysum())
}
