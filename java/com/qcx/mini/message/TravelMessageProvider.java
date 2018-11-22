package com.qcx.mini.message;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.utils.DateUtil;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by zsp on 2017/9/18.
 *
 */
@ProviderTag( messageContent = TravelMessage.class, showReadState = true)
public class TravelMessageProvider extends IContainerItemProvider.MessageProvider<TravelMessage> {

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_travel_message,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View v, int i, TravelMessage travelMessage, UIMessage uiMessage) {

        ViewHolder holder = (ViewHolder) v.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.v_view.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.v_view.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.setInfo(travelMessage);
    }

    @Override
    public Spannable getContentSummary(TravelMessage travelMessage) {
        if(TextUtils.isEmpty(travelMessage.getUserID())){
            return new SpannableString("您给对方发送了一个行程");
        }
        if(travelMessage.getUserID().equals(String.valueOf(User.getInstance().getPhoneNumber()))){
            return new SpannableString("您给对方发送了一个行程");
        }else {
            return new SpannableString("对方给您发送了一个行程");
        }
    }

    @Override
    public void onItemClick(View view, int i, TravelMessage travelMessage, UIMessage uiMessage) {
        if(TextUtils.isEmpty(travelMessage.getUserID())){
            return ;
        }
    }

    class ViewHolder {
        TextView tv_startAddress,tv_endAddress,tv_time;
        View v_view;
        ViewHolder(View view){
            tv_startAddress=view.findViewById(R.id.item_travel_message_startAddress);
            tv_endAddress=view.findViewById(R.id.item_travel_message_endAddress);
            tv_time=view.findViewById(R.id.item_travel_message_time);
            v_view=view.findViewById(R.id.item_travel_message_view);
        }

        void setInfo(TravelMessage travelMessage){
            tv_startAddress.setText(travelMessage.getStartAddress());
            tv_endAddress.setText(travelMessage.getEndAddress());
            tv_time.setText(DateUtil.formatDay("MM-dd",travelMessage.getStartTime()).concat(DateUtil.getTimeStr(travelMessage.getStartTime()," HH:mm")));
        }
    }

}
