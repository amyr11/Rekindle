package com.runtimeterror.rekindle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        private ImageView profilePic;
        private TextView memberName;
        private ImageButton optionButton;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profile_pic);
            memberName = itemView.findViewById(R.id.member_name);
            optionButton = itemView.findViewById(R.id.button_more);
        }
    }

    private List<UserInfo> memberList;
    private Context context;
    public MembersAdapter(Context context, List<UserInfo> members) {
        this.context = context;
        this.memberList = members;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        UserInfo current = memberList.get(position);
        new DownloadSpriteTask(holder.profilePic).execute(current.getPhotoURL());
        holder.memberName.setText(current.getUsername());
        //TODO: create menu on option button
    }

    private class DownloadSpriteTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        public DownloadSpriteTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());
            }
            catch (IOException e) {
                Log.e(Constants.TAG, "Download image error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // load the bitmap into the ImageView!
            this.imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}
