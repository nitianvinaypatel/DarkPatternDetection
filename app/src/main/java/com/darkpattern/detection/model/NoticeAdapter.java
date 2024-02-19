package com.darkpattern.detection.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.darkpattern.detection.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {
    private Context context;
    private ArrayList<NoticeData> list;

    public NoticeAdapter(Context context, ArrayList<NoticeData> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item_row,parent,false);

        return new NoticeViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        NoticeData noticeData = list.get(position);
        String SenderImageUrl = noticeData.getSenderImage();
        Picasso.get().load(SenderImageUrl).placeholder(R.drawable.user_profile_icon).into(holder.Sender_Image);
        holder.Sender_Name.setText(noticeData.getSenderName().substring(0, 4) + "xxx");
        holder.notice_time.setText(noticeData.getTime());
        holder.notice_date.setText(noticeData.getDate());
        holder.message.setText(noticeData.getMessage());
        String NoticeImageUrl = noticeData.getImageUrl();

        if (NoticeImageUrl != null && !NoticeImageUrl.isEmpty()) {
            // Load the image using Picasso if NoticeImageUrl is not null or empty
            Picasso.get().load(NoticeImageUrl).into(holder.Notice_Image);
        }

        // Set initial values
        holder.like.setText(String.valueOf(noticeData.getLike()));
        holder.dislike.setText(String.valueOf(noticeData.getDislike()));

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int likeNo = noticeData.getLike();
                likeNo++;
                noticeData.setLike(likeNo);
                holder.like.setText(String.valueOf(likeNo));

                // Update the like count in the database
                updateLikeCount(noticeData.getKey(), likeNo);
            }
        });


        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dislikeNo = noticeData.getDislike();
                dislikeNo++;
                noticeData.setDislike(dislikeNo);
                holder.dislike.setText(String.valueOf(dislikeNo));

                // Update the dislike count in the database
                updateDislikeCount(noticeData.getKey(), dislikeNo);
            }
        });
    }

    private void updateLikeCount(String unikey, int likeCount) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Posts").child(unikey).child("like").setValue(likeCount);
    }

    private void updateDislikeCount(String unikey, int dislikeCount) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Posts").child(unikey).child("dislike").setValue(dislikeCount);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView Sender_Image;
        private final TextView Sender_Name;
        private final TextView notice_date;
        private final TextView notice_time;
        private final TextView message;

        private final ImageView Notice_Image;
        private final TextView like;
        private final TextView comment;
        private final TextView dislike;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);

            Sender_Name = itemView.findViewById(R.id.SenderName);
            Sender_Image = itemView.findViewById(R.id.SenderImage);
            notice_date = itemView.findViewById(R.id.Date);
            notice_time = itemView.findViewById(R.id.Time);
            message = itemView.findViewById(R.id.message_box);
            Notice_Image = itemView.findViewById(R.id.NoticeImage);
            like = itemView.findViewById(R.id.Like);
            dislike = itemView.findViewById(R.id.Dislike);
            comment = itemView.findViewById(R.id.Comment);

        }
    }
}
