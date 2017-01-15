package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.composition.model.AvailableBlendMode;
import com.savvasdalkitsis.gameframe.composition.model.AvailablePorterDuffOperator;
import com.savvasdalkitsis.gameframe.composition.model.BlendMode;
import com.savvasdalkitsis.gameframe.composition.model.PorterDuffOperator;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

@Getter
@Setter
@Builder
public class LayerSettings implements Moment<LayerSettings> {

    private float alpha;
    private BlendMode blendMode;
    private PorterDuffOperator porterDuffOperator;
    private String title;

    public static LayerSettings.LayerSettingsBuilder create() {
        return builder()
                .title("")
                .blendMode(AvailableBlendMode.defaultMode())
                .porterDuffOperator(AvailablePorterDuffOperator.defaultOperator())
                .alpha(1);
    }

    @Override
    public LayerSettings replicateMoment() {
        return builder()
                .alpha(getAlpha())
                .blendMode(getBlendMode())
                .porterDuffOperator(getPorterDuffOperator())
                .title(getTitle())
                .build();
    }

    @Override
    public boolean isIdenticalTo(LayerSettings moment) {
        return alpha == moment.alpha
                && blendMode == moment.blendMode
                && porterDuffOperator == moment.porterDuffOperator
                && title.equals(moment.title);
    }
}
