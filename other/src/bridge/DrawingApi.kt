package bridge

interface DrawingApi {
    fun drawCircle(x: Double, y: Double, r: Double)
    fun drawLine(x: Double, y: Double, x1: Double, y1: Double)
}