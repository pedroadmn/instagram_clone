package adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import models.User;
import pedroadmn.instagramclone.com.R;

public class FilterThumbnailsAdapter extends RecyclerView.Adapter<FilterThumbnailsAdapter.MyViewHolder> {

    private List<ThumbnailItem> filterList;
    private Context context;

    public FilterThumbnailsAdapter(List<ThumbnailItem> filterList, Context context) {
        this.filterList = filterList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_thumbnail_item_adapter, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ThumbnailItem thumbnailItem = filterList.get(position);
        holder.filterName.setText(thumbnailItem.filterName);
        holder.photo.setImageBitmap(thumbnailItem.image);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView filterName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.ivFilterThumbnail);
            filterName = itemView.findViewById(R.id.tvFilterName);
        }
    }
}
