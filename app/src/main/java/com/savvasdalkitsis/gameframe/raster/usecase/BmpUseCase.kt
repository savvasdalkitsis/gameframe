package com.savvasdalkitsis.gameframe.raster.usecase

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.grid.model.Grid
import io.reactivex.Single
import java.nio.ByteBuffer

class BmpUseCase {

    fun rasterizeToBmp(colorGrid: Grid): Single<ByteArray> {
        return Single.create { source ->
            val rasterByteSize = ColorGrid.SIDE * ColorGrid.SIDE * 3
            val fileSize = rasterByteSize + DATA_OFFSET

            try {
                val buffer = ByteBuffer.allocate(fileSize)
                writeHeader(buffer, fileSize, rasterByteSize)
                writeData(buffer, colorGrid)
                source.onSuccess(buffer.array())
            } catch (e: Throwable) {
                source.onError(e)
            }
        }
    }

    private fun writeHeader(buffer: ByteBuffer, fileSize: Int, rasterByteSize: Int) {
        write(0x42.toByte(), buffer)
        write(0x4D.toByte(), buffer)
        write(fileSize, buffer)
        write(0.toShort(), buffer)
        write(0.toShort(), buffer)
        write(DATA_OFFSET, buffer)
        write(0x28, buffer)
        write(ColorGrid.SIDE, buffer)
        write(ColorGrid.SIDE, buffer)
        write(1.toShort(), buffer)
        write(24.toShort(), buffer)
        write(0, buffer)
        write(rasterByteSize, buffer)
        write(0, buffer)
        write(0, buffer)
        write(0, buffer)
        write(0, buffer)
    }

    private fun write(b: Byte, buffer: ByteBuffer) {
        buffer.put(b)
    }

    private fun write(b: ByteArray, buffer: ByteBuffer) {
        buffer.put(b)
    }

    private fun write(i: Int, buffer: ByteBuffer) {
        val b = ByteArray(4)
        b[0] = (i and 0x000000FF).toByte()
        b[1] = (i and 0x0000FF00 shr 8).toByte()
        b[2] = (i and 0x00FF0000 shr 16).toByte()
        b[3] = (i and 0xFF000000.toInt() shr 24).toByte()
        write(b, buffer)
    }

    private fun write(s: Short, buffer: ByteBuffer) {
        val b = ByteArray(2)
        b[0] = (s.toInt() and 0x00FF).toByte()
        b[1] = (s.toInt() and 0xFF00 shr 8).toByte()
        write(b, buffer)
    }

    private fun writeData(buffer: ByteBuffer, colorGrid: Grid) {
        for (row in ColorGrid.SIDE downTo 1) {
            for (col in 1..ColorGrid.SIDE) {
                buffer.put(toRgb(colorGrid.getColor(col, row)))
            }
        }
    }

    private fun toRgb(value: Int): ByteArray {
        val b = ByteArray(3)
        b[0] = (value and 0x000000FF).toByte()
        b[1] = (value and 0x0000FF00 shr 8).toByte()
        b[2] = (value and 0x00FF0000 shr 16).toByte()
        return b
    }

    companion object {

        private const val DATA_OFFSET = 0x36
    }

}