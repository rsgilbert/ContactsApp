package com.monstercode.contactsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ContactDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Contact through:").setItems(new String[]{"Call", "SMS"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        mListener.onDialogCall(ContactDialogFragment.this);
                    }
                    case 1: {
                        mListener.onDialogSms(ContactDialogFragment.this);
                    }
                }
            }
        });
        return builder.create();

    }

    public interface MyDialogListener {
        public void onDialogCall(DialogFragment df);
        public void onDialogSms(DialogFragment df);
    }

    MyDialogListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (MyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " Must implement MyDialogListener");
        }
    }


}
