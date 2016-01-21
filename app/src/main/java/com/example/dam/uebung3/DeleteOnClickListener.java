package com.example.dam.uebung3;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by dam on 19/01/16.
 */
public class DeleteOnClickListener implements View.OnClickListener {

    RecordFragment recordFragment;
    public DeleteOnClickListener(RecordFragment recordFragment) {
        this.recordFragment = recordFragment;
    }

    @Override public void onClick(View v) {
        MainActivity mainActivity = (MainActivity) v.getContext();
        LinearLayout l = (LinearLayout)mainActivity.findViewById(R.id.linearLayoutRecordsId);
        l.removeView(v);
        mainActivity.deleteRecordFragment(recordFragment);
    }

}
