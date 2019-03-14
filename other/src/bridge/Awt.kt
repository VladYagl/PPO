package bridge

import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D


class Awt : Frame(), DrawingApi {
    private lateinit var ga: Graphics2D
    private lateinit var graph: Graph

    override fun drawCircle(x: Double, y: Double, r: Double) {
        ga.fill(Ellipse2D.Double(x - r / 2, y - r / 2, r, r))
    }

    override fun drawLine(x: Double, y: Double, x1: Double, y1: Double) {
        ga.drawLine(x.toInt(), y.toInt(), x1.toInt(), y1.toInt())
    }

    override fun paint(g: Graphics) {
        ga = g as Graphics2D

        graph.draw()
    }

    fun main(graph: Graph) {
        this.graph = graph
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent?) {
                System.exit(0)
            }
        })
        setSize(1360, 720)
        isVisible = true
    }
}

