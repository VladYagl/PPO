package bridge

import java.io.FileReader
import java.lang.Math.*

class MatrixGraph(private val api: DrawingApi): Graph {
    private var n: Int = 0
    private var matrix: Array<Array<Int>> = emptyArray()

    override fun read(file: String) {
        FileReader(file).buffered().use {
            n = it.readLine().toInt()
            matrix = (1..n).map { _ ->
                it.readLine().split(' ').map(String::toInt).toTypedArray()
            }.toTypedArray()
        }
    }

    override fun draw() {
        val vertexes = (1..n).map {
            val part = (it - 1).toDouble() / n
            Pair(cos(2 * PI * part) * 200 + 400, sin(2 * PI * part) * 200 + 400)
        }

        for ((x, y) in vertexes) {
            api.drawCircle(x, y, 10.0)
        }

        for (i in 0 until n) {
            for (j in 0 until n) {
                if (matrix[i][j] == 1) {
                    api.drawLine(vertexes[i].first, vertexes[i].second, vertexes[j].first, vertexes[j].second)
                }
            }
        }
    }
}