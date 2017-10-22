/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
package com.savvasdalkitsis.gameframe.feature.bmp.usecase

import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.ColorGrid
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import java.io.InputStream
import java.nio.ByteBuffer

class BmpUseCase {

    fun rasterizeToBmp(colorGrid: Grid): InputStream {
        val rasterByteSize = ColorGrid.SIDE * ColorGrid.SIDE * 3
        val fileSize = rasterByteSize + DATA_OFFSET

        val buffer = ByteBuffer.allocate(fileSize)
        writeHeader(buffer, fileSize, rasterByteSize)
        writeData(buffer, colorGrid)
        return buffer.array().inputStream()
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