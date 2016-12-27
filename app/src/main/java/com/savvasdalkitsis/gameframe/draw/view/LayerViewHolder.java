package com.savvasdalkitsis.gameframe.draw.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.composition.model.AvailableBlendMode;
import com.savvasdalkitsis.gameframe.composition.model.AvailablePorterDuffOperator;
import com.savvasdalkitsis.gameframe.draw.model.Layer;
import com.savvasdalkitsis.gameframe.grid.view.LedGridView;

class LayerViewHolder extends RecyclerView.ViewHolder {

    private final LedGridView ledGridView;
    private final Spinner blendMode;
    private final Spinner porterDuffMode;

    LayerViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_layer_view, parent, false));
        ledGridView = (LedGridView) itemView.findViewById(R.id.view_layer_thumbnail);
        ledGridView.setThumbnailMode();
        blendMode = (Spinner) itemView.findViewById(R.id.view_layer_blend_mode);
        porterDuffMode = (Spinner) itemView.findViewById(R.id.view_layer_porter_duff);
        blendMode.setAdapter(adapter(parent, AvailableBlendMode.values()));
        porterDuffMode.setAdapter(adapter(parent, AvailablePorterDuffOperator.values()));
    }

    @NonNull
    private <T> ArrayAdapter<T> adapter(ViewGroup parent, T[] values) {
        ArrayAdapter<T> adapter = new ArrayAdapter<>(parent.getContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void bind(Layer layer) {
        ledGridView.display(layer.getColorGrid());
        blendMode.setSelection(AvailableBlendMode.indexOf(layer.getBlendMode()));
        porterDuffMode.setSelection(AvailablePorterDuffOperator.indexOf(layer.getPorterDuffOperator()));
    }

    void setSelected(boolean selected) {
        itemView.setSelected(selected);
    }

    void setOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }

    void setOnBlendModeSelected(OnBlendModeSelected onBlendModeSelected) {
        blendMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                onBlendModeSelected.onBlendModeSelected(AvailableBlendMode.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    void setOnPorterDuffOperatorSelected(OnPorterDuffOperatorSelected onPorterDuffOperatorSelected) {
        porterDuffMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                onPorterDuffOperatorSelected.onPorterDuffOperatorSelected(AvailablePorterDuffOperator.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
