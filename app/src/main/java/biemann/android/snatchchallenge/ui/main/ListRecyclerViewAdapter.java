package biemann.android.snatchchallenge.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import biemann.android.snatchchallenge.R;
import biemann.android.snatchchallenge.data.api.MediawikiGeosearchModel;


public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder>
{

    private List<MediawikiGeosearchModel.QueryListItem> itemList;
    private ListInteractionListener onClickListener;
    private boolean isDataSet;

    public ListRecyclerViewAdapter(ListInteractionListener listener)
    {
        onClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final Double distance = itemList.get(position).getDist();

        holder.itemTitle.setText(itemList.get(position).getTitle());
        holder.itemDistance.setText(String.format(Locale.getDefault(), "%.1f m", distance));
        holder.viewItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != onClickListener)
                {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    onClickListener.onListClick(itemList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if (itemList == null)
            return 0;
        else
            return itemList.size();
    }

    public void setItemList(List<MediawikiGeosearchModel.QueryListItem> list)
    {
        isDataSet = true;
        this.itemList = list;
        notifyDataSetChanged();
    }

    public boolean getIsDataSet()
    {
        return isDataSet;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View viewItem;
        public final TextView itemTitle;
        public final TextView itemDistance;


        public ViewHolder(View view)
        {
            super(view);
            viewItem = view;
            itemTitle = (TextView) view.findViewById(R.id.name);
            itemDistance = (TextView) view.findViewById(R.id.distance);
        }
    }
}
