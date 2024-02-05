package com.example.to_do_v3;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.to_do_v3.TaskBackend.DataBaseHelper;
import com.example.to_do_v3.TaskBackend.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String tag = "AddNewTask";

    Calendar selectedDate;
    private EditText name;
    private Button saveButton;
    private DataBaseHelper db;

    //private EditText description; //description = args.getString("description");

    private TextView dueDate; //dueDate = args.getString("dueDate");

    //private Spinner type;

    private Bundle args ;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.addtask_layout,container, false );
        return v;
    }

    private void showDatePicker() {

        selectedDate= Calendar.getInstance();
        DatePickerDialog dp = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateSelectedDateTextView();
            }
        }, selectedDate.get(Calendar.YEAR),
        selectedDate.get(Calendar.MONTH),
        selectedDate.get(Calendar.DAY_OF_MONTH));

        dp.show();
    }

    private void updateSelectedDateTextView() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(selectedDate.getTime());
        dueDate.setText(formattedDate);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.task_name);
        dueDate = view.findViewById(R.id.due_date_txtview);
        saveButton = view.findViewById(R.id.save_button);
        db = new DataBaseHelper(getActivity());
        boolean isUpdate = true;

        args = getArguments();

        if(args == null) {
            isUpdate = false;

        } else {
            String taskname = args.getString("name");
            String datetxt = args.getString("dueDate");
//            if (datetxt == null) {
//                dueDate.setText("no args passed");
//            }
            name.setText(taskname);
            dueDate.setText(datetxt);

//            if (taskname.length() > 0) {
//                saveButton.setEnabled(false);
//            }
        }

        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        dueDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                saveButton.setEnabled(true);
                saveButton.setBackgroundColor(Color.BLUE);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                args = getArguments();
                if (args == null) {
                    if(s.toString().equals("")) {
                        saveButton.setEnabled(false);
                        saveButton.setBackgroundColor(Color.GRAY);
                    }else {
                        saveButton.setEnabled(true);
                        saveButton.setBackgroundColor(Color.BLUE);
                    }
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(Color.BLUE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(true);
                saveButton.setBackgroundColor(Color.BLUE);
            }
        });


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                saveButton.setEnabled(true);
                saveButton.setBackgroundColor(Color.BLUE);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                args = getArguments();
                if (args == null) {
                    if(s.toString().equals("")) {
                        saveButton.setEnabled(false);
                        saveButton.setBackgroundColor(Color.GRAY);
                    }else {
                        saveButton.setEnabled(true);
                        saveButton.setBackgroundColor(Color.BLUE);
                    }
               } else {
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(Color.BLUE);
               }
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(true);
                saveButton.setBackgroundColor(Color.BLUE);
            }
        });
        final boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = name.getText().toString();
                String date = dueDate.getText().toString();

                if (finalIsUpdate) {
                    db.updateTask(args.getInt("id"), "name", text);
                    db.updateTask(args.getInt("id"),"dueDate", date);
                } else {
                    Task item = new Task(text, date,0);
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
