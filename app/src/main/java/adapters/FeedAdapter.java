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
import com.like.LikeButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import models.Feed;
import pedroadmn.instagramclone.com.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    private List<Feed> feedList;
    private Context context;

    public FeedAdapter(List<Feed> feedList, Context context) {
        this.feedList = feedList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_adapter, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Feed feedItem = feedList.get(position);

        Uri userPhotoUrl = Uri.parse(feedItem.getUserPhoto());
        Uri postPhotoUrl = Uri.parse(feedItem.getPostPhoto());

        holder.tvPostDescription.setText(feedItem.getDescription());
        holder.tvPostUserName.setText(feedItem.getUserName());

        if (userPhotoUrl != null) {
            Glide.with(context).load(userPhotoUrl).into(holder.civPostUser);
        }

        if (postPhotoUrl != null) {
            Glide.with(context).load(postPhotoUrl).into(holder.ivPostPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civPostUser;
        ImageView ivPostPhoto;
        TextView tvPostUserName;
        TextView tvPostDescription;
        TextView tvLikes;
        LikeButton btLikeFeed;
        ImageView ivComments;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            civPostUser = itemView.findViewById(R.id.civPostUser);
            ivPostPhoto = itemView.findViewById(R.id.ivPostPhoto);
            tvPostUserName = itemView.findViewById(R.id.tvPostUserName);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            btLikeFeed = itemView.findViewById(R.id.btLikeFeed);
            ivComments = itemView.findViewById(R.id.ivComments);
        }
    }
}
