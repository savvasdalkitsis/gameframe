package com.savvasdalkitsis.gameframe.composition.usecase;

import com.savvasdalkitsis.gameframe.composition.model.ARGB;
import com.savvasdalkitsis.gameframe.composition.model.BlendMode;
import com.savvasdalkitsis.gameframe.composition.model.PorterDuffOperator;

public class BlendUseCase {

    public ARGB blend(int source, int dest, BlendMode blendMode, PorterDuffOperator porterDuffOperator, float alpha) {
        ARGB alphaSource = new ARGB(source).multiplyAlpha(alpha);
        ARGB destination = new ARGB(dest);
        ARGB blend = blend(alphaSource, destination, blendMode);
        return compose(blend, destination, porterDuffOperator);
    }

    private static ARGB blend(ARGB source, ARGB dest, BlendMode blendMode) {
        return source.multiply(1 - toFloat(dest.a)).add(blendMode.blend(source, dest).multiply(toFloat(dest.a)));
    }

    private static ARGB compose(ARGB source, ARGB dest, PorterDuffOperator porterDuffOperator) {
        return source.multiply(toFloat(source.a) * porterDuffOperator.fa(dest)).add(dest.multiply(toFloat(dest.a) * porterDuffOperator.fb(source)));
    }

    private static float toFloat(int inBytes) {
        return inBytes / 255f;
    }
}
