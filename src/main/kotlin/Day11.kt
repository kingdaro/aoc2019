import math.Point
import math.TAU
import kotlin.math.cos
import kotlin.math.sin

private const val quarterTurn = TAU * (1 / 4.0)

private const val robotProgram =
    "3,8,1005,8,311,1106,0,11,0,0,0,104,1,104,0,3,8,1002,8,-1,10,101,1,10,10,4,10,108,0,8,10,4,10,1002,8,1,28,2,103,7,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,1001,8,0,55,2,3,6,10,1,101,5,10,1,6,7,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,0,10,4,10,1001,8,0,89,1,1108,11,10,2,1002,13,10,1006,0,92,1,2,13,10,3,8,102,-1,8,10,1001,10,1,10,4,10,1008,8,0,10,4,10,101,0,8,126,3,8,1002,8,-1,10,101,1,10,10,4,10,108,1,8,10,4,10,1002,8,1,147,1,7,0,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,108,0,8,10,4,10,101,0,8,173,1006,0,96,3,8,102,-1,8,10,101,1,10,10,4,10,108,0,8,10,4,10,1001,8,0,198,1,3,7,10,1006,0,94,2,1003,20,10,3,8,102,-1,8,10,1001,10,1,10,4,10,1008,8,1,10,4,10,102,1,8,232,3,8,102,-1,8,10,101,1,10,10,4,10,108,1,8,10,4,10,102,1,8,253,1006,0,63,1,109,16,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,101,0,8,283,2,1107,14,10,1,105,11,10,101,1,9,9,1007,9,1098,10,1005,10,15,99,109,633,104,0,104,1,21102,837951005592,1,1,21101,328,0,0,1105,1,432,21101,0,847069840276,1,21101,0,339,0,1106,0,432,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21102,179318123543,1,1,21102,386,1,0,1106,0,432,21102,1,29220688067,1,21102,1,397,0,1106,0,432,3,10,104,0,104,0,3,10,104,0,104,0,21102,709580567396,1,1,21102,1,420,0,1105,1,432,21102,1,868498694912,1,21102,431,1,0,1106,0,432,99,109,2,22101,0,-1,1,21101,40,0,2,21101,0,463,3,21101,0,453,0,1105,1,496,109,-2,2106,0,0,0,1,0,0,1,109,2,3,10,204,-1,1001,458,459,474,4,0,1001,458,1,458,108,4,458,10,1006,10,490,1102,1,0,458,109,-2,2105,1,0,0,109,4,1202,-1,1,495,1207,-3,0,10,1006,10,513,21102,0,1,-3,21201,-3,0,1,21202,-2,1,2,21101,0,1,3,21101,0,532,0,1106,0,537,109,-4,2106,0,0,109,5,1207,-3,1,10,1006,10,560,2207,-4,-2,10,1006,10,560,22102,1,-4,-4,1105,1,628,21201,-4,0,1,21201,-3,-1,2,21202,-2,2,3,21101,0,579,0,1105,1,537,22101,0,1,-4,21102,1,1,-1,2207,-4,-2,10,1006,10,598,21102,1,0,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,620,22102,1,-1,1,21101,0,620,0,106,0,495,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2106,0,0"

private fun movementVector(angle: Double): Point {
    val x = sin(angle)
    val y = cos(angle)
    return Point(x.toInt(), -y.toInt())
}

private fun getPaintedSquares(startingColor: Int): Map<Point, Int> {
    var robotPosition = Point(0, 0)
    var direction = 0.0
    var program = IntcodeProgram.fromString(robotProgram)
    val paintedSquares = mutableMapOf(Point(0, 0) to startingColor)

    while (!program.isStopped) {
        program = program.addInput(paintedSquares[robotPosition] ?: 0).run()
        val (newColor, directionCode) = program.outputs.takeLast(2)

        // paint
        paintedSquares[robotPosition] = newColor.toInt()

        // turn
        if (directionCode == 0L) {
            direction -= quarterTurn
        } else {
            direction += quarterTurn
        }

        // move
        robotPosition += movementVector(direction)
    }

    return paintedSquares
}

private fun getImage(squares: Map<Point, Int>): String {
    val xValues = squares.keys.map { it.x }
    val yValues = squares.keys.map { it.y }

    val startX = xValues.min() ?: error("no x values?")
    val endX = xValues.max() ?: error("no x values?")

    val startY = yValues.min() ?: error("no y values?")
    val endY = yValues.max() ?: error("no y values?")

    return (startY..endY).joinToString("\n") { y ->
        (startX..endX)
            .map { x -> squares[Point(x, y)] ?: 0 }
            .joinToString(" ") { if (it == 1) "X" else " " }
    }
}

fun main() {
    println(getPaintedSquares(startingColor = 0).count())
    println(getImage(getPaintedSquares(startingColor = 1)))
}
