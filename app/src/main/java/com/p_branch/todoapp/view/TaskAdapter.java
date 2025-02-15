package com.p_branch.todoapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.p_branch.todoapp.R;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskItemViewHolder> {

    Context context;
    ArrayList<TaskItem> taskItemList;

    Listener listener;

    public TaskAdapter(Context context, ArrayList<TaskItem> taskItemList) {
        this.context = context;
        this.taskItemList = taskItemList;
    }

    @Override
    public TaskItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new TaskItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskItemViewHolder holder, int position) {
        TaskItem item = taskItemList.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.endDateTextView.setText(item.getEndDate());
        // 達成度のドロップダウン作成
        String[] items = context.getResources().getStringArray(R.array.progress_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ドロップダウンのアイテムビューを設定
        holder.progressSpinner.setAdapter(adapter);
        holder.progressSpinner.setSelection(item.getProgress());
        holder.progressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int progress, long id) {
                if (item.getProgress() != progress) {
                    item.setProgress(progress);
                    setBackGroundColor(holder, item);
                    if (listener != null) {
                        listener.onItemUpdated(item);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        setBackGroundColor(holder, item);
        // 削除ボタンを押した時の処理
        holder.deleteImageView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.delete_task))
                    .setMessage(R.string.dialog_msg_confirm_delete)
                    // 削除ボタン押下
                    .setPositiveButton(context.getString(R.string.delete), (dialog, which) -> {
                        if (listener != null) {
                            listener.onItemDeleted(item);
                        }
                        deleteItem(position);
                        Toast.makeText(context, context.getString(R.string.msg_delete_task), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    // キャンセルボタン押下
                    .setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> {
                        dialog.dismiss(); // ダイアログを閉じる
                    })
                    .create()
                    .show();
            }
        );
    }

    private void setBackGroundColor(TaskItemViewHolder holder, TaskItem item) {
        int maxProgress = context.getResources().getStringArray(R.array.progress_items).length - 1;
        if (item.getProgress() == maxProgress) {
            holder.view.setBackgroundColor(context.getColor(R.color.complete));
        } else {
            switch (item.getPriority()) {
                case 0:
                    holder.view.setBackgroundColor(context.getColor(R.color.priorityLow));
                    break;
                case 2:
                    holder.view.setBackgroundColor(context.getColor(R.color.priorityHigh));
                    break;
                default:
                    holder.view.setBackgroundColor(context.getColor(R.color.white));
                    break;
            }
        }
    }

    public void addItem(TaskItem taskItem) {
        taskItemList.add(taskItem);  // 新しいアイテムをリストに追加
        notifyItemInserted(taskItemList.size() - 1);  // RecyclerViewに追加されたアイテムを通知
    }

    public void deleteItem(int position) {
        taskItemList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return taskItemList.size();
    }

    public static class TaskItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView titleTextView;
        TextView endDateTextView;
        Spinner progressSpinner;
        ImageView deleteImageView;
        public TaskItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            titleTextView = itemView.findViewById(R.id.titleTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
            progressSpinner = itemView.findViewById(R.id.progressSpinner);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        public void onItemUpdated(TaskItem item);
        public void onItemDeleted(TaskItem item);
    }
}
