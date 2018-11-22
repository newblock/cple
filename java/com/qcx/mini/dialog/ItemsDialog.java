package com.qcx.mini.dialog;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/24.
 */

public class ItemsDialog extends ActionSheetDialog {

    public ItemsDialog(Context context, String[] items){
        super(context,items,null);
    }

    public ItemsDialog(Context context, ArrayList<DialogMenuItem> baseItems, View animateView) {
        super(context, baseItems, animateView);
    }

    public ItemsDialog(Context context, String[] items, View animateView) {
        super(context, items, animateView);
    }

    public ItemsDialog(Context context, BaseAdapter adapter, View animateView) {
        super(context, adapter, animateView);
    }

    public void setOnItemClickListener(OnItemClick listener){
        super.setOnOperItemClickL(listener);
    }


    public interface OnItemClick extends OnOperItemClickL {

    }
}
