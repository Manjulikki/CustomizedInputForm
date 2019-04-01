package com.example.manjunath.customizedinputform.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import com.example.manjunath.customizedinputform.R;
import com.example.manjunath.customizedinputform.entity.InputEntity;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private final int IMAGE_POSITION = 0;
    private final int SINGLE_SELECTION_POSITION = 1;
    private final int COMMENTS_POSITION = 2;
    private RecyclerView recyclerView;
    public ArrayList<ArrayList<InputEntity>> data;
    private Activity context;
    private View view;
    private ImageView photoImageView;
    private Button xButton;
    private Switch provideCommentSwitchButton;
    private EditText commentEditText;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioGroup optionGroup;

    public RecyclerViewAdapter(Activity context, ArrayList<ArrayList<InputEntity>> data, RecyclerView recyclerView) {
        this.context = context;
        this.data = data;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       view = LayoutInflater.from(context).inflate(R.layout.items_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
       // photoImageView.setImageBitmap(data.get(position).get(0).getImage());
        option1.setText(data.get(position).get(SINGLE_SELECTION_POSITION).getOptions().get(0));
        option2.setText(data.get(position).get(SINGLE_SELECTION_POSITION).getOptions().get(1));
        option3.setText(data.get(position).get(SINGLE_SELECTION_POSITION).getOptions().get(2));
        data.get(position).get(SINGLE_SELECTION_POSITION).setTittle(option1.getText().toString());
        commentEditText.setText(data.get(position).get(COMMENTS_POSITION).getTittle());
    }

    @Override
    public int getItemCount() {
        return data!= null ? data.size() : 0;
    }

    public void setImageBitmap(Bitmap image, int position){
        data.get(position).get(IMAGE_POSITION).setImage(image);
        ImageView imageView = getViewAtPosition(position).findViewById(R.id.photo);
        imageView.setImageBitmap(image);
        getViewAtPosition(position).findViewById(R.id.xButton).setVisibility(View.VISIBLE);
        //notifyItemChanged(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photo);
            photoImageView.setOnClickListener(this);
            xButton = itemView.findViewById(R.id.xButton);
            xButton.setOnClickListener(this);
            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);
            optionGroup = itemView.findViewById(R.id.optionsRG);
            commentEditText = itemView.findViewById(R.id.commentsEditText);
            provideCommentSwitchButton = itemView.findViewById(R.id.provideCommentSwitchButton);

            optionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = view.findViewById(checkedId);
                    data.get(getAdapterPosition()).get(SINGLE_SELECTION_POSITION).setTittle(radioButton.getText().toString());
                }
            });

            commentEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    data.get(getAdapterPosition()).get(COMMENTS_POSITION).setTittle(s.toString());
                }
            });

            provideCommentSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    View view = getViewAtPosition(getAdapterPosition());
                    if (isChecked) {
                        view.findViewById(R.id.commentsEditText).setVisibility(View.VISIBLE);
                    } else {
                        view.findViewById(R.id.commentsEditText).setVisibility(View.GONE);
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.photo:
                    if (isPhotoCaptured(getAdapterPosition())) {
                        showEnlargedPhoto();
                    } else {
                        launchCamera(getAdapterPosition());
                    }
                    break;

                case R.id.xButton:
                    launchCamera(getAdapterPosition());
                    break;
            }
        }

        private void launchCamera(int position) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            context.startActivityForResult(cameraIntent, position);
        }

        private void showEnlargedPhoto() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.fullscreen_image_view, null);
            view.setMinimumWidth(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            view.setMinimumHeight(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            ImageView imageView  = view.findViewById(R.id.dialog_imageview);
            imageView.setImageBitmap(data.get(getAdapterPosition()).get(IMAGE_POSITION).getImage());
            builder.setView(view);
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int value) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }

    private View getViewAtPosition(int position) {
        return recyclerView.getLayoutManager().findViewByPosition(position);
    }

    private boolean isPhotoCaptured(int position){
        return data.get(position).get(IMAGE_POSITION).getImage()!= null ? true : false;
    }
}
