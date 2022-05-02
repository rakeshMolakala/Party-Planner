//package com.example.partyplanner;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import androidx.appcompat.app.AppCompatDialogFragment;
//
//public class EditDialog extends AppCompatDialogFragment {
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View v = inflater.inflate(R.layout.dialog_edit,null);
//        builder.setView(v)
//                .setTitle("Edit Event")
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                })
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//        return builder.create();
//
//
//    }
//}
