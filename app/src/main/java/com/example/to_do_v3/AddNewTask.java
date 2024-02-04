package com.example.to_do_v3;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.to_do_v3.TaskBackend.DataBaseHelper;
import com.example.to_do_v3.TaskBackend.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String tag = "AddNewTask";

    private EditText name;
    private Button saveButton;
    private DataBaseHelper db;

    private EditText description; //description = args.getString("description");

    private EditText dueDate; //dueDate = args.getString("dueDate");

    private Spinner type;

    private Bundle args = getArguments();


    public static AddNewTask newInstance() {
        return new AddNewTask();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.addtask_layout,container, false );
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.task_name);
        saveButton = view.findViewById(R.id.save_button);
        db = new DataBaseHelper(getActivity());
        boolean isUpdate = false;

        if(args != null) {
            isUpdate = true;
            String taskname = args.getString("name");
            name.setText(taskname);

            if (taskname.length() > 0) {
                saveButton.setEnabled(false);
            }
        }
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(Color.GRAY);
                }else {
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(Color.BLUE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = name.getText().toString();

                if (finalIsUpdate) {
                    db.updateTask(args.getInt("id"), "name", text);
                } else {
                    Task item = new Task();
                    item.setName(text);
                    item.setStatus(0);
                    db.insertTask(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogClosedListener) {
            ((OnDialogClosedListener)activity).onDialogClose(dialog);
        }
    }
}
