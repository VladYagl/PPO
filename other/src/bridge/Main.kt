package bridge

import java.lang.Exception

fun main(args: Array<String>) {
    when (args[0]) {
        "jfx" -> {
            JavaFx().main { drawingApi ->
                val graph: Graph = when (args[1]) {
                    "matrix" -> {
                        MatrixGraph(drawingApi)
                    }
                    "edge" -> {
                        EdgeGraph(drawingApi)
                    }
                    else -> throw Exception("WTF???")
                }
                graph.read(args[2])
                graph.draw()
            }
        }
        "awt" -> {
            val draw = Awt()
            val graph: Graph = when (args[1]) {
                "matrix" -> {
                    MatrixGraph(draw)
                }
                "edge" -> {
                    EdgeGraph(draw)
                }
                else -> throw Exception("WTF???")
            }
            graph.read(args[2])
            draw.main(graph)
        }
        else -> throw Exception("WTF???")
    }
}

