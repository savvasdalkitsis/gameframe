package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.composition.model.AvailableBlendMode;
import com.savvasdalkitsis.gameframe.composition.model.AvailablePorterDuffOperator;
import com.savvasdalkitsis.gameframe.composition.model.BlendMode;
import com.savvasdalkitsis.gameframe.composition.model.PorterDuffOperator;

import lombok.Getter;
import lombok.experimental.Builder;

@Getter
@Builder
public class LayerSettings {

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

    public static LayerSettings.LayerSettingsBuilder from(LayerSettings layerSettings) {
        return builder()
                .alpha(layerSettings.getAlpha())
                .blendMode(layerSettings.getBlendMode())
                .porterDuffOperator(layerSettings.getPorterDuffOperator())
                .title(layerSettings.getTitle());
    }
}
