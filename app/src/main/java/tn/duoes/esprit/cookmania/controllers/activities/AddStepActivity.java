package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.Step;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class AddStepActivity extends AppCompatActivity {

    private static final String TAG = AddStepActivity.class.getSimpleName();
    public static final String RECIPE_KEY = "recipe";

    private Recipe mRecipe;
    private RecyclerView mIngredientRecyclerView;
    private IngredientsRecyclerViewAdapter mIngredientAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_step);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Gson gson = new Gson();
        mRecipe = gson.fromJson(getIntent().getStringExtra(RECIPE_KEY), Recipe.class);
        Log.d(TAG, "onCreate: "+mRecipe);

        mIngredientRecyclerView = findViewById(R.id.add_step_ingredients_rv);
        mIngredientRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mIngredientRecyclerView.setLayoutManager(mLayoutManager);
        mIngredientAdapter = new IngredientsRecyclerViewAdapter();
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        findViewById(R.id.add_step_image_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickSetup pickSetup = new PickSetup();
                pickSetup.setGalleryIcon(R.mipmap.gallery_colored);
                pickSetup.setCameraIcon(R.mipmap.camera_colored);
                PickImageDialog.build(pickSetup).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        if (pickResult.getError() == null) {
                            Glide.with(AddStepActivity.this).load(pickResult.getUri()).into((ImageView) findViewById(R.id.add_step_iv));
                        } else {
                            Toast.makeText(AddStepActivity.this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).show(getSupportFragmentManager());
            }
        });

        findViewById(R.id.add_step_add_ingredient_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ingredient lastIngredient = mIngredientAdapter.mIngredients.get(mIngredientAdapter.mIngredients.size()-1);

                if(lastIngredient.getName() == null || lastIngredient.getName().isEmpty() || lastIngredient.getQuantity() == 0){
                    return;
                }
                mIngredientAdapter.mIngredients.add(new Ingredient());
                mIngredientAdapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.add_step_next_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText descriptionET;
                if((descriptionET = ((TextInputLayout)findViewById(R.id.add_step_description_layout)).getEditText()).getText().toString().equals("")){
                    descriptionET.setError("Step description is required!");
                    return;
                }

                EditText durationET;
                if((durationET = ((TextInputLayout)findViewById(R.id.add_step_duration_layout)).getEditText()).getText().toString().equals("")){
                    durationET.setError("Step duration is required!");
                    return;
                }

                Step step = new Step(descriptionET.getText().toString(), "", Integer.valueOf(durationET.getText().toString()), mIngredientAdapter.mIngredients);
                mRecipe.getSteps().add(step);

                Intent intent = NavigationUtils.getNavigationFormattedIntent(AddStepActivity.this, AddStepActivity.class);
                intent.putExtra(RECIPE_KEY, gson.toJson(mRecipe));
                startActivity(intent);
            }
        });
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        return NavigationUtils.getParentActivityIntent(this, getIntent());
    }

    public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder>{

        public List<Ingredient> mIngredients;

        public IngredientsRecyclerViewAdapter(){
            super();
            mIngredients = new ArrayList<>(Arrays.asList(new Ingredient()));
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_add_recipe_ingredient, viewGroup, false);
            return new IngredientsRecyclerViewAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            viewHolder.position = position;
            if(position == (mIngredients.size() - 1)){
                viewHolder.ingredientNameET.requestFocus();
            }
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            EditText ingredientNameET;
            EditText ingredientQuantityET;
            Spinner ingredientUnitET;
            int position;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ingredientNameET = itemView.findViewById(R.id.add_step_ingredient_name_et);
                ingredientQuantityET = itemView.findViewById(R.id.add_step_ingredient_quantity_et);
                ingredientUnitET = itemView.findViewById(R.id.add_step_ingredient_unit_sp);

                ingredientNameET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mIngredients.get(position).setName(ingredientNameET.getText().toString());
                    }
                });

                ingredientQuantityET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mIngredients.get(position).setQuantity(Integer.valueOf(ingredientQuantityET.getText().toString()));
                    }
                });

                ingredientUnitET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mIngredients.get(position).setUnit(ingredientUnitET.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }
}
