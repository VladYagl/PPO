package bridge

import java.io.FileReader
import java.lang.Math.*

class EdgeGraph(private val api: DrawingApi) : Graph {
    private var n: Int = 0
    private var m: Int = 0
    private val list: ArrayList<Pair<Int, Int>> = ArrayList()

    override fun read(file: String) {
        FileReader(file).buffered().use {
            n = it.readLine().toInt()
            m = it.readLine().toInt()
            for (i in 1..m) {
                val x = it.readLine().split(' ').map(String::toInt)
                list.add(Pair(x[0] - 1, x[1] - 1))
            }
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

        for ((a, b) in list) {
            api.drawLine(vertexes[a].first, vertexes[a].second, vertexes[b].first, vertexes[b].second)
        }
    }
}