package com.savvasdalkitsis.gameframe.bmp.usecase;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;

import java.nio.ByteBuffer;

import rx.Emitter;
import rx.Observable;

public class BmpUseCase {

    private static final int DATA_OFFSET = 0x36;

    public Observable<byte[]> rasterizeToBmp(ColorGrid colorGrid) {
        return Observable.fromEmitter(emitter -> {
            int rasterByteSize = ColorGrid.SIDE * ColorGrid.SIDE * 3;
            int fileSize = rasterByteSize + DATA_OFFSET;

            try {
                ByteBuffer buffer = ByteBuffer.allocate(fileSize);
                writeHeader(buffer, fileSize, rasterByteSize);
                writeData(buffer, colorGrid);
                emitter.onNext(buffer.array());
                emitter.onCompleted();
            } catch (Throwable e) {
                emitter.onError(e);
            }

        }, Emitter.BackpressureMode.LATEST);
    }

    private void writeHeader(ByteBuffer buffer, int fileSize, int rasterByteSize) {
        write((byte) 0x42, buffer);
        write((byte) 0x4D, buffer);
        write(fileSize, buffer);
        write((short) 0, buffer);
        write((short) 0, buffer);
        write(DATA_OFFSET, buffer);
        write(0x28, buffer);
        write(ColorGrid.SIDE, buffer);
        write(ColorGrid.SIDE, buffer);
        write((short) 1, buffer);
        write((short) 24, buffer);
        write(0, buffer);
        write(rasterByteSize, buffer);
        write(0, buffer);
        write(0, buffer);
        write(0, buffer);
        write(0, buffer);
    }

    private void write(byte b, ByteBuffer buffer) {
        buffer.put(b);
    }

    private void write(byte[] b, ByteBuffer buffer) {
        buffer.put(b);
    }

    private void write(int i, ByteBuffer buffer) {
        byte[] b = new byte[4];
        b[0] = (byte) (i & 0x000000FF);
        b[1] = (byte) ((i & 0x0000FF00) >> 8);
        b[2] = (byte) ((i & 0x00FF0000) >> 16);
        b[3] = (byte) ((i & 0xFF000000) >> 24);
        write(b, buffer);
    }

    private void write(short s, ByteBuffer buffer) {
        byte[] b = new byte[2];
        b[0] = (byte) (s & 0x00FF);
        b[1] = (byte) ((s & 0xFF00) >> 8);
        write(b, buffer);
    }

    private void writeData(ByteBuffer buffer, ColorGrid colorGrid) {
        for (int row = ColorGrid.SIDE; row > 0; row--) {
            for (int col = 1; col <= ColorGrid.SIDE; col++) {
                buffer.put(toRgb(colorGrid.getColor(col, row)));
            }
        }
    }

    private byte[] toRgb(int value) {
        byte[] b = new byte[3];
        b[0] = (byte) (value & 0x000000FF);
        b[1] = (byte) ((value & 0x0000FF00) >> 8);
        b[2] = (byte) ((value & 0x00FF0000) >> 16);
        return b;
    }

}