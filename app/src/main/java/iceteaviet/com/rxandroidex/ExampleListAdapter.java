package iceteaviet.com.rxandroidex;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Genius Doan on 10/29/2017.
 */

public class ExampleListAdapter extends RecyclerView.Adapter<ExampleListAdapter.ViewHolder> {
    private List<String> exampleList;
    private OnItemClickListener listener;

    public ExampleListAdapter(List<String> exampleList) {
        this.exampleList = exampleList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(exampleList.get(position));
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }
}
