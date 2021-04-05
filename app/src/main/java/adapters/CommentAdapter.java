package adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import models.Comment;
import models.User;
import pedroadmn.instagramclone.com.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private List<Comment> comments;
    private Context context;

    public CommentAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_adapter, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.userName.setText(comment.getUserName());
        holder.comment.setText(comment.getComment());

        if (comment.getPhotoPath() != null && !comment.getPhotoPath().isEmpty()) {
            Uri uri = Uri.parse(comment.getPhotoPath());
            Glide.with(context).load(uri).into(holder.userPhoto);
        } else {
            holder.userPhoto.setImageResource(R.drawable.avatar);
        }

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userPhoto;
        TextView userName;
        TextView comment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.civUser);
            userName = itemView.findViewById(R.id.tvUserName);
            comment = itemView.findViewById(R.id.tvComment);
        }
    }
}
