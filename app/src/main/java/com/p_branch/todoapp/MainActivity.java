package com.p_branch.todoapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p_branch.todoapp.repository.TaskInfo;
import com.p_branch.todoapp.repository.TaskRepository;
import com.p_branch.todoapp.util.TodoAppUtil;
import com.p_branch.todoapp.view.TaskAdapter;
import com.p_branch.todoapp.view.TaskItem;

import java.util.ArrayList;
import java.util.Calendar;

import kotlinx.coroutines.scheduling.Task;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    private TaskAdapterListener taskAdapterListener;
    private TaskRepository taskRepository;

    // DBアクセス用の別スレッド
    private HandlerThread dbHandlerThread;
    // DBスレッドに処理を投げるためのHandler
    private Handler dbHandler;
    // UIスレッドに処理を投げるためのHandler
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new Handler(Looper.getMainLooper());
        // DB用の別スレッドを起動する
        dbHandlerThread = new HandlerThread("DB");
        dbHandlerThread.start();
        dbHandler = new Handler(dbHandlerThread.getLooper());

        taskRepository = new TaskRepository(this);

        initializeAdapter();
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> showCreateTaskDialog());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandlerThread.quitSafely();
    }

    /**
     * アダプターの初期化処理
     */
    private void initializeAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHandler.post(() -> {
            // DBからタスクのリストを取得
            ArrayList<TaskInfo> taskInfoList = taskRepository.getAllTaskInfo();
            mainHandler.post(() -> {
                // UIで表示するタスクのリスト
                ArrayList<TaskItem> taskItemList = new ArrayList<>();
                // DBのデータを変換
                for (TaskInfo taskInfo : taskInfoList) {
                    TaskItem taskItem = TodoAppUtil.convertTaskInfoToTaskItem(taskInfo);
                    taskItemList.add(taskItem);
                }
                taskAdapter = new TaskAdapter(this, taskItemList);
                taskAdapterListener = new TaskAdapterListener();
                taskAdapter.setListener(taskAdapterListener);
                recyclerView.setAdapter(taskAdapter);
            });
        });
    }

    // ダイアログでタスクを追加
    private void showCreateTaskDialog() {
        // ダイアログのビューを作成
        View dialogView = getLayoutInflater().inflate(R.layout.add_dialog, null);
        EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        EditText endDateEditText = dialogView.findViewById(R.id.endDateEditText);
        Button selectDateButton = dialogView.findViewById(R.id.selectDateButton);
        Spinner prioritySpinner = dialogView.findViewById(R.id.prioritySpinner);
        String[] items = getResources().getStringArray(R.array.priority_items);
        // EditTextにフォーカスが当たったときにキーボードを表示
        titleEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            } else {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        // 優先度のドロップダウン作成
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ドロップダウンのアイテムビューを設定
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setSelection(1);

        // 完了予定日の選択
        selectDateButton.setOnClickListener(v -> showDatePickerDialog(endDateEditText));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle(getString(R.string.create_task))
                .setView(dialogView)
                .setPositiveButton(getString(R.string.create), (v, which) -> {
                    v.dismiss(); // ダイアログを閉じる
                })
                .setNegativeButton(getString(R.string.cancel), (v, which) -> {
                    v.dismiss(); // ダイアログを閉じる
                })
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            TextView titleErrorTextView = dialogView.findViewById(R.id.titleValidationErrorTextView);
            TextView endDateErrorTextView = dialogView.findViewById(R.id.endDateErrorTextView);

            boolean isEmptyTitleEditText = titleEditText.getText().toString().isEmpty();
            boolean isEmptyEndDateEditText = endDateEditText.getText().toString().isEmpty();
            if (isEmptyTitleEditText) {
                titleErrorTextView.setVisibility(View.VISIBLE);
            } else {
                titleErrorTextView.setVisibility(View.INVISIBLE);
            }
            if (isEmptyEndDateEditText) {
                endDateErrorTextView.setVisibility(View.VISIBLE);
            } else {
                endDateErrorTextView.setVisibility(View.INVISIBLE);
            }
            if (isEmptyTitleEditText || isEmptyEndDateEditText)  {
                return;
            }

            dbHandler.post(() -> {
                // タスクをDBへ登録
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setTitle(titleEditText.getText().toString());
                taskInfo.setEndDate(endDateEditText.getText().toString());
                taskInfo.setProgress(0);
                taskInfo.setPriority(prioritySpinner.getSelectedItemPosition());
                Long id = taskRepository.insertTask(taskInfo);
                // DBで採番されたIDをセット
                taskInfo.setId(id);
                mainHandler.post(() -> {
                    taskAdapter.addItem(TodoAppUtil.convertTaskInfoToTaskItem(taskInfo));
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_create_task), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            });
        });
    }

    // 完了予定日を選択するDatePickerDialog
    private void showDatePickerDialog(EditText endDateEditText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                (view, year, month, dayOfMonth) -> {
                    month++; // monthは0から始まるので
                    endDateEditText.setText(year + "/" + month + "/" + dayOfMonth);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private class TaskAdapterListener implements TaskAdapter.Listener {
        @Override
        public void onItemUpdated(TaskItem item) {
            TaskInfo taskInfo = TodoAppUtil.convertTaskItemToTaskInfo(item);
            dbHandler.post(() -> {
               taskRepository.updateTask(taskInfo);
            });
        }

        @Override
        public void onItemDeleted(TaskItem item) {
            TaskInfo taskInfo = TodoAppUtil.convertTaskItemToTaskInfo(item);
            dbHandler.post(() -> {
                taskRepository.deleteTask(taskInfo.getId());
            });
        }
    }
}
