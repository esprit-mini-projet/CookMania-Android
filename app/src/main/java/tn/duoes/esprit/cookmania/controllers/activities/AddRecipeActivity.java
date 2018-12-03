package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import tn.duoes.esprit.cookmania.R;

public class AddRecipeActivity extends AppCompatActivity{
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        imageView = findViewById(R.id.add_recipe_image_view);
        findViewById(R.id.add_recipe_add_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickSetup pickSetup = new PickSetup();
                pickSetup.setGalleryIcon(R.mipmap.gallery_colored);
                pickSetup.setCameraIcon(R.mipmap.camera_colored);
                PickImageDialog.build(pickSetup).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        if (pickResult.getError() == null) {
                            Glide.with(AddRecipeActivity.this).load(pickResult.getUri()).into(imageView);
                        } else {
                            Toast.makeText(AddRecipeActivity.this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).show(getSupportFragmentManager());
            }
        });
        findViewById(R.id.add_recipe_next_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
