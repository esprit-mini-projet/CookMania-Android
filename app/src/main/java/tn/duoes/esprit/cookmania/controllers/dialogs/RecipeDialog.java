package tn.duoes.esprit.cookmania.controllers.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;
import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.utils.Constants;

public class RecipeDialog extends AppCompatDialogFragment {
    private static final String TAG = RecipeDialog.class.getSimpleName();
    public static final String RECIPE_KEY = "recipe";
    public static final String USER_IMAGE_KEY = "user_image";
    public static final String USER_NAME_KEY = "user_name";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Recipe recipe = Objects.requireNonNull(bundle).getParcelable(RECIPE_KEY);
        String userImageUrl = bundle.getString(USER_IMAGE_KEY);
        String userName = bundle.getString(USER_NAME_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_recipe, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ImageView recipeIv = view.findViewById(R.id.dialog_recipe_iv);
        ImageView recipeIvBackground = view.findViewById(R.id.dialog_bg);
        Glide.with(view).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).into(recipeIv);
        Glide.with(view.getContext()).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3)))
                .into(recipeIvBackground);


        Glide.with(view).load(userImageUrl).into((ImageView)view.findViewById(R.id.dialog_user_iv));
        ((TextView)view.findViewById(R.id.dialog_recipe_tv)).setText(recipe.getName());
        ((TextView)view.findViewById(R.id.dialog_user_tv)).setText(userName);
        ((AppCompatRatingBar)view.findViewById(R.id.dialog_recipe_rb)).setRating(recipe.getRating());

        return dialog;
    }


}
