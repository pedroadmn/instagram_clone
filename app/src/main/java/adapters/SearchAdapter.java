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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import models.User;
import pedroadmn.instagramclone.com.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<User> users;
    private Context context;

    public SearchAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_adapter, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getName());

        if (user.getPhotoPath() != null && !user.getPhotoPath().isEmpty()) {
            Uri uri = Uri.parse(user.getPhotoPath());
            Glide.with(context).load(uri).into(holder.photo);
        } else {
            holder.photo.setImageResource(R.drawable.avatar);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView photo;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.civUser);
            name = itemView.findViewById(R.id.tvUserName);
        }
    }
}
