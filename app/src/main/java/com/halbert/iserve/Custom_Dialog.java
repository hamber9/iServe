package com.halbert.iserve;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

public class Custom_Dialog extends AppCompatDialogFragment {

    Custom_DialogInterFace dialogInterFace;
    EditText editTextQquantitat;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("Introdueix quantitat")
                .setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String quantitat = editTextQquantitat.getText().toString();
                        dialogInterFace.applyText(quantitat);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });



        editTextQquantitat = view.findViewById(R.id.edittextQuantitatProducte);

        return builder.create();


    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        dialogInterFace = (Custom_DialogInterFace) context;
    }

    public interface Custom_DialogInterFace{
        void applyText(String quantitat);
    }
}
