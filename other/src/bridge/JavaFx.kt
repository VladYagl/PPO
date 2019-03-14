package bridge

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.stage.Stage

private lateinit var callback: (DrawingApi) -> Unit

class JavaFx : Application(), DrawingApi {
    private lateinit var gc: GraphicsContext
    private lateinit var graph: Graph

    override fun drawLine(x: Double, y: Double, x1: Double, y1: Double) {
        gc.strokeLine(x, y, x1, y1)
    }

    override fun drawCircle(x: Double, y: Double, r: Double) {
        gc.fillOval(x - r / 2, y - r / 2, r, r)
    }

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Drawing circle"
        val root = Group()
        val canvas = Canvas(1360.0, 720.0)
        gc = canvas.graphicsContext2D

        callback(this)

        root.children.add(canvas)
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

    fun main(call: (DrawingApi) -> Unit) {
        callback = call

        launch()
    }
}
