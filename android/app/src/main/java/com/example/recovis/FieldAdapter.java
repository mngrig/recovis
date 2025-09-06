package com.example.recovis;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recovis.model.PatientProfile;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {

    private List<PatientProfile> fields;

    public FieldAdapter(List<PatientProfile> fields) {
        this.fields = fields;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PatientProfile data = fields.get(position);

        holder.textInputLayout.setHint(data.getField().getDescription_gr());
        holder.measurementUnit.setText(data.getField().getMeasurement_unit());
        holder.helperText.setText("Normal range: " + data.getField().getAcceptable_range());

        // Set up focus change listener for the EditText
        holder.textInputEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                holder.helperText.setVisibility(View.VISIBLE);
                holder.textInputLayout.setHelperText(fields.get(position).getGuideline());
                holder.textInputLayout.setHelperTextEnabled(true);
            } else {
                holder.helperText.setVisibility(View.INVISIBLE);
                holder.textInputLayout.setHelperTextEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText textInputEditText;
        TextInputLayout textInputLayout;
        TextView measurementUnit;
        TextView helperText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textInputEditText = itemView.findViewById(R.id.editText);
            textInputLayout = itemView.findViewById(R.id.editTextLayout);
            measurementUnit = itemView.findViewById(R.id.measurementUnit);
            helperText = itemView.findViewById(R.id.helperText);
        }

        public String getTextInputEditTextValue() {
            return textInputEditText.getText().toString();
        }

    }
}
