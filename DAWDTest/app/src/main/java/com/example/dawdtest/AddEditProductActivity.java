package com.example.dawdtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dawdtest.bean.Product;

public class AddEditProductActivity extends AppCompatActivity {

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;

    private EditText textName;
    private EditText textAge;
    private Button buttonSave;
    private Button buttonCancel;

    private Product product;
    private boolean needRefresh;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        this.textName = (EditText)this.findViewById(R.id.editText_name);
        this.textAge = (EditText) this.findViewById(R.id.edit_age) ;
        this.buttonSave = (Button)findViewById(R.id.button_save);
        this.buttonCancel = (Button)findViewById(R.id.button_cancel);

        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                buttonSaveClicked();
            }
        });

        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                buttonCancelClicked();
            }
        });

        Intent intent = this.getIntent();
        this.product = (Product) intent.getSerializableExtra("product");
        if(product== null)  {
            this.mode = MODE_CREATE;
        } else  {
            String age = ""+product.getQuantity();
            this.mode = MODE_EDIT;
            this.textName.setText(product.getName());
            this.textAge.setText(age);

        }
    }

    // User Click on the Save button.
    public void buttonSaveClicked()  {
        MyDatabaseHelper db = new MyDatabaseHelper(this);

        String name = this.textName.getText().toString();
        String agetext = this.textAge.getText().toString();


        if(name.equals("")  || agetext.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter Name or Quantity", Toast.LENGTH_LONG).show();
            return;
        }
        int quantity= Integer.parseInt(agetext);
        if(mode == MODE_CREATE ) {
            this.product= new Product(name,quantity);
            db.addProduct(product);
        } else  {
            this.product.setName(name);
            this.product.setQuantity(quantity);
            db.updateNote(product);
        }

        this.needRefresh = true;

        // Back to MainActivity.
        this.onBackPressed();
    }

    // User Click on the Cancel button.
    public void buttonCancelClicked()  {
        // Do nothing, back MainActivity.
        this.onBackPressed();
    }

    // When completed this Activity,
    // Send feedback to the Activity called it.
    @Override
    public void finish() {

        // Create Intent
        Intent data = new Intent();

        // Request MainActivity refresh its ListView (or not).
        data.putExtra("needRefresh", needRefresh);

        // Set Result
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }
}