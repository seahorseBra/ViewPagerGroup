package adapter;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zchao.viewpagergroup.R;

import java.util.List;

import javabean.ImageCategory;
import util.IFastJSON;

/**
 * Created by zchao on 2016/10/10.
 */

public class PopupAdapter extends RecyclerView.Adapter<PopupAdapter.ListHolder>{
    private static List<Integer> mCategory;
    private Context context;
    private LayoutInflater inflater;
    private int selectId = 4001;
    private OnItemClickListener listener;

    public PopupAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mCategory = ImageCategory.allIds;
    }

    public void setSelectId(Integer selectId) {
        if (this.selectId == selectId || !mCategory.contains(selectId)) {
            return;
        }
        this.selectId = selectId;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int typeId);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.category_select_item, parent, false);
        return new ListHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        final Integer typeId = mCategory.get(position);
        holder.mTypeText.setText(ImageCategory.getTypeNameById(typeId));
        if (typeId == selectId) {
            holder.mTypeText.setSelected(true);
        }
        holder.mTypeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(typeId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    class ListHolder extends RecyclerView.ViewHolder{
        TextView mTypeText;
        public ListHolder(View itemView) {
            super(itemView);
            mTypeText = (TextView) itemView.findViewById(R.id.item_text);
        }
    }
}
