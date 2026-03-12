package com.example.studenttimer.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttimer.R;
import com.example.studenttimer.data.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.VH> {

    private final Context ctx;
    private Cursor cursor;

    public SessionAdapter(Context ctx, Cursor cursor) {
        this.ctx = ctx;
        this.cursor = cursor;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_session, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) return;

        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.S_TITLE));
        String subject = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.S_SUBJECT));
        int totalSec = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.S_TOTAL_SEC));
        long completedAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.S_COMPLETED_AT));

        String mins = (totalSec / 60) + " min";
        String date = new SimpleDateFormat("MMM d", Locale.getDefault()).format(new Date(completedAt));

        h.tvTitle.setText(title == null || title.isEmpty() ? "Study Session" : title);
        h.tvMeta.setText((subject == null ? "General" : subject) + " • " + mins + " • " + date);
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMeta;
        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMeta = itemView.findViewById(R.id.tvMeta);
        }
    }
}