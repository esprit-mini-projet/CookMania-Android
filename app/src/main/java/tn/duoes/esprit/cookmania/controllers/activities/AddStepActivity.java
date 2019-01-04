package tn.duoes.esprit.cookmania.controllers.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.pix.Pix;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.helpers.RecyclerItemTouchHelper;
import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.Step;
import tn.duoes.esprit.cookmania.services.RecipeService;
import tn.duoes.esprit.cookmania.services.StepService;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;
import tn.duoes.esprit.cookmania.utils.SwipableViewHolder;

public class AddStepActivity extends AppCompatActivity {

    private static final String TAG = AddStepActivity.class.getSimpleName();
    public static final String RECIPE_KEY = "recipe";
    public static final int REQUEST_CODE = 101;

    private Recipe mRecipe;
    private RecyclerView mIngredientRecyclerView;
    private IngredientsRecyclerViewAdapter mIngredientAdapter;
    private LinearLayoutManager mLayoutManager;
    private File image;
    private Gson gson;
    TextView ingredientsErrorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_step);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gson = new Gson();

        ingredientsErrorTV = findViewById(R.id.add_step_ingredient_error_tv);
        ingredientsErrorTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    ingredientsErrorTV.setError(null);
                }
            }
        });

        mIngredientRecyclerView = findViewById(R.id.add_step_ingredients_rv);
        mIngredientRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mIngredientRecyclerView.setLayoutManager(mLayoutManager);
        mIngredientAdapter = new IngredientsRecyclerViewAdapter();
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                final int deletedIndex = viewHolder.getAdapterPosition();
                final Ingredient ingredient = mIngredientAdapter.mIngredients.get(deletedIndex);

                mIngredientAdapter.mIngredients.remove(deletedIndex);
                mIngredientAdapter.notifyItemRemoved(deletedIndex);
                mIngredientRecyclerView.requestLayout();

                Snackbar snackbar = Snackbar.make(viewHolder.itemView, "Ingredient removed!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIngredientAdapter.mIngredients.add(deletedIndex, ingredient);
                        mIngredientAdapter.notifyItemInserted(deletedIndex);
                        mIngredientRecyclerView.requestLayout();
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        });
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mIngredientRecyclerView);


        findViewById(R.id.add_step_image_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pix.start(AddStepActivity.this, AddStepActivity.REQUEST_CODE , 1);
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
                Step step;

                if(((step = createStep())==null) || !mIngredientAdapter.isValidIngredient())
                    return;
                mRecipe.getSteps().add(step);
                Intent intent = NavigationUtils.getNavigationFormattedIntent(AddStepActivity.this, AddStepActivity.class);
                intent.putExtra(RECIPE_KEY, gson.toJson(mRecipe));
                startActivity(intent);
            }
        });

        findViewById(R.id.add_step_finish_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Step step;
                if(!mIngredientAdapter.isValidIngredient()){
                    return;
                }
                if((step = createStep())==null && mRecipe.getSteps().isEmpty()){
                    Toast.makeText(AddStepActivity.this, "At least one step needs to be added!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!checkForIngredients(step)){
                    ingredientsErrorTV.setError("At least one ingredient must be added to this recipe");
                    ingredientsErrorTV.requestFocus();
                    return;
                }
                mRecipe.getSteps().add(step);
                final ProgressDialog progressDialog = new ProgressDialog(AddStepActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                RecipeService.getInstance().addRecipe(mRecipe, new RecipeService.RecipeServiceInsertCallBack() {
                    @Override
                    public void onResponse(int recipeId) {
                        Log.d(TAG, "onResponse: "+recipeId);
                        mRecipe.setId(recipeId);
                        saveSteps(0);

                        Intent intent = new Intent(AddStepActivity.this, RecipeDetailsActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, recipeId+"");
                        intent.putExtra(RecipeDetailsActivity.EXTRA_SHOULD_FINISH, false);
                        progressDialog.dismiss();
                        Toast.makeText(AddStepActivity.this, "Recipe added successfully", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(AddStepActivity.this, "Error adding recipe", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean checkForIngredients(Step step){
        List<Ingredient> ingredients = new ArrayList<>();
        Log.d(TAG, "checkForIngredients: "+mRecipe.getSteps());
        for(Step s : mRecipe.getSteps()){
            if(s.getIngredients() != null){
                ingredients.addAll(s.getIngredients());
            }
        }
        ingredients.addAll(step.getIngredients());
        return !ingredients.isEmpty();
    }

    private void saveSteps(final int position){
        Step step = mRecipe.getSteps().get(position);
        step.setRecipeId(mRecipe.getId());
        StepService.getInstance().addStep(step, new StepService.StepServiceInsertCallBack() {
            @Override
            public void onResponse() {
                if(position == mRecipe.getSteps().size()-1)
                    return;
                saveSteps(position+1);
            }

            @Override
            public void onFailure() {
                return;
            }
        });
    }

    private Step createStep(){
        EditText descriptionET;
        if((descriptionET = ((TextInputLayout)findViewById(R.id.add_step_description_layout)).getEditText()).getText().toString().equals("")){
            descriptionET.setError("Step description is required!");
            descriptionET.requestFocus();
            return null;
        }

        EditText durationET;
        int duration = 0;
        if(!(durationET = ((TextInputLayout)findViewById(R.id.add_step_duration_layout)).getEditText()).getText().toString().equals("")){
            duration = Integer.valueOf(durationET.getText().toString());
        }
        Step step = new Step(descriptionET.getText().toString(), duration, clearIngredientsList(mIngredientAdapter.mIngredients), image);
        Log.d(TAG, "createStep: "+step);
        return step;
    }

    private List<Ingredient> clearIngredientsList(List<Ingredient> ingredients){
        List<Ingredient> newIngredient = new ArrayList<>(ingredients);
        if(!newIngredient.isEmpty() && (newIngredient.get(ingredients.size()-1).getName() == null || newIngredient.get(ingredients.size()-1).getName().isEmpty())){
            newIngredient.remove(newIngredient.size()-1);
        }
        return newIngredient;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AddStepActivity.RESULT_OK && requestCode == AddStepActivity.REQUEST_CODE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Glide.with(AddStepActivity.this).load(returnValue.get(0)).into((ImageView) findViewById(R.id.add_step_iv));
            image = new File(returnValue.get(0));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRecipe = gson.fromJson(getIntent().getStringExtra(RECIPE_KEY), Recipe.class);
        Log.d(TAG, "onStart: addRecipe"+mRecipe);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = NavigationUtils.getParentActivityIntent(this, getIntent());
        if(!mRecipe.getSteps().isEmpty()){
            mRecipe.getSteps().remove(mRecipe.getSteps().size()-1);
        }
        intent.putExtra(RECIPE_KEY, gson.toJson(mRecipe));
        return intent;
    }

    public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder>{

        public List<Ingredient> mIngredients;

        public IngredientsRecyclerViewAdapter(){
            super();
            mIngredients = new ArrayList<>(Arrays.asList(new Ingredient()));
        }

        public boolean isValidIngredient(){
            for(Ingredient ingredient : mIngredients){
                if(ingredient.getQuantity() == -1 || ingredient.getName() == null || ingredient.getName().isEmpty()){
                    return false;
                }
            }
            return true;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_add_recipe_ingredient, viewGroup, false);
            return new ViewHolder(v, this);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            Ingredient ingredient = mIngredients.get(position);
            viewHolder.ingredientQuantityLayout.getEditText().setText(ingredient.getQuantity()==0?"":String.valueOf(ingredient.getQuantity()));
            viewHolder.ingredientNameLayout.getEditText().setText(ingredient.getName());
            viewHolder.ingredientUnitET.setSelection(
                    (ingredient.getUnit() == null || ingredient.getUnit().equals("N/A"))?
                            0:
                            ingredient.getUnit().equals("g")?
                                    1:2
            );
            if(position != 0){
                viewHolder.ingredientNameLayout.getEditText().requestFocus();
            }
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }

        public class ViewHolder extends SwipableViewHolder {

            TextInputLayout ingredientNameLayout;
            TextInputLayout ingredientQuantityLayout;
            Spinner ingredientUnitET;

            public ViewHolder(@NonNull View itemView, final IngredientsRecyclerViewAdapter viewAdapter) {
                super(itemView);
                foregroundView = itemView.findViewById(R.id.add_step_ingredient_fg);
                backgroundView = itemView.findViewById(R.id.add_step_ingredient_bg);
                ingredientNameLayout = itemView.findViewById(R.id.add_step_ingredient_name_layout);
                ingredientQuantityLayout = itemView.findViewById(R.id.add_step_ingredient_quantity_layout);
                ingredientUnitET = itemView.findViewById(R.id.add_step_ingredient_unit_sp);

                ingredientNameLayout.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(ingredientNameLayout.getEditText().getText().toString().isEmpty() && !ingredientQuantityLayout.getEditText().getText().toString().isEmpty()){
                            ingredientNameLayout.setError("Ingredient name is required!");
                        }else{
                            ingredientNameLayout.setError(null);
                        }
                        if(ingredientQuantityLayout.getEditText().getText().toString().isEmpty() && !ingredientNameLayout.getEditText().getText().toString().isEmpty()){
                            ingredientQuantityLayout.setError("Ingredient quantity is required!");
                        }else{
                            ingredientQuantityLayout.setError(null);
                        }

                        mIngredients.get(getAdapterPosition()).setName(ingredientNameLayout.getEditText().getText().toString());
                    }
                });

                ingredientNameLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus && ingredientNameLayout.getEditText().getText().toString().isEmpty() && ingredientQuantityLayout.getEditText().getText().toString().isEmpty() && mIngredients.size() != 1
                                && mIngredients.size()-1>=getAdapterPosition()){
                            mIngredients.remove(getAdapterPosition());
                            viewAdapter.notifyDataSetChanged();
                        }
                    }
                });

                ingredientQuantityLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus && ingredientNameLayout.getEditText().getText().toString().isEmpty() && ingredientQuantityLayout.getEditText().getText().toString().isEmpty() && mIngredients.size() != 1
                                && mIngredients.size()-1>=getAdapterPosition()){
                            mIngredients.remove(getAdapterPosition());
                            viewAdapter.notifyDataSetChanged();
                        }
                    }
                });

                ingredientQuantityLayout.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(ingredientNameLayout.getEditText().getText().toString().isEmpty() && !ingredientQuantityLayout.getEditText().getText().toString().isEmpty()){
                            ingredientNameLayout.setError("Ingredient name is required!");
                        }else{
                            ingredientNameLayout.setError(null);
                        }
                        if(ingredientQuantityLayout.getEditText().getText().toString().isEmpty() && !ingredientNameLayout.getEditText().getText().toString().isEmpty()){
                            ingredientQuantityLayout.setError("Ingredient quantity is required!");
                        }else{
                            ingredientQuantityLayout.setError(null);
                        }

                        String quantityString;
                        if((quantityString = ingredientQuantityLayout.getEditText().getText().toString()).isEmpty()) {
                            quantityString = "-1";
                        }
                        mIngredients.get(getAdapterPosition()).setQuantity(Integer.valueOf(quantityString));
                    }
                });

                ingredientUnitET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mIngredients.get(getAdapterPosition()).setUnit(ingredientUnitET.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }
}
