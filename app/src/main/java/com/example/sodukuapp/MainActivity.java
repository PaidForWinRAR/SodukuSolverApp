package com.example.sodukuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.DialogFragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int[][] matrixVal;
    private static final int COLUMNS = 9; //Number of columns.
    private static final int ROWS = 9;   //Number of rows

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        matrixVal = new int[ROWS][COLUMNS];
        createTable();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //Sets the toolbar
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    int id = r * 10 + c;
                    EditText editText = findViewById(id);
                }
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    int id = r * 10 + c;
                    EditText editText = findViewById(id);
                    editText.setMaxHeight(editText.getMaxHeight()*3);
                }
            }
        }
    }

    /**
     * Adds menu items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Adds items to the toolbar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles menu item input.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu items
        switch (item.getItemId()) {
            case R.id.clearBar:
                clearGrid();
                return true;
            case R.id.solveBar:
                solve();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Clears the grid and the GUI.
     */
    private void clearGrid() {
        matrixVal = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int id = r * 10 + c;
                EditText editText = findViewById(id);
                editText.setText("");
            }
        }
    }

    /**
     * Attempts to solve the sodoku.
     */
    private void solve() {
        SodukuGrid sg = new SodukuGrid(matrixVal);
        if(!sg.isValid()) {
            DialogFragment d = new InvalidSodukuDialog();
            d.show(getSupportFragmentManager(), "Not valid");
        } else if (sg.sodokuSolver()) {
            Log.d("MainActivity", "Löste ;)");
            showSudoku(sg);
        } else {
            Log.d("MainActivity", "Inte bra ;)");
            DialogFragment d = new NoSolutionFoundDialog();
            d.show(getSupportFragmentManager(), "No solution.");
        }
    }

    /**
     * Displays the sodokugrid in the GUI.
     * @param sg The sodokugrid to be displayed.
     */
    private void showSudoku(SodukuGrid sg) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int id = r * 10 + c;
                EditText editText = findViewById(id);
                editText.setText("" + sg.getValue(r, c));
            }
        }
    }


    /**
     * Creates the layout programmatically.
     */
    public void createTable() {

        TableLayout table = findViewById(R.id.sodukuTable);
        for (int r = 0; r < ROWS; r++) {
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
            row.setLayoutParams(params);
            for (int c = 0; c < COLUMNS; c++) {
                final EditText tv = new EditText(this);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(R.drawable.border);
                tv.setInputType(InputType.TYPE_CLASS_NUMBER);
                tv.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable edt) {
                        int id = tv.getId();
                        int row = id / 10;
                        int col = id % 10;
                        if (edt.length() > 1 || edt.toString().equals("0")){
                            tv.setText("");
                            matrixVal[row][col] = 0;
                        }
                        else if (edt.length() == 1) {
                            matrixVal[row][col] = Integer.parseInt(edt.toString().trim());
                        }
                        else{
                            matrixVal[row][col] = 0;
                        }
                    }
                    //Requires implementation of these. TODO ???
                    public void beforeTextChanged(CharSequence c3, int a, int b, int c) {

                    }

                    public void onTextChanged(CharSequence c1, int a2, int b2, int c2) {

                    }
                });
                TableRow.LayoutParams rowParams = new TableRow.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 3f);
                tv.setLayoutParams(rowParams);


                if (c / 3 != 1 && r/3 == 0 || c / 3 == 1 && r/3 == 1 || c/3 != 1 && r/3 == 2) {
                    tv.setBackgroundResource(R.drawable.otherborder);
                }
                tv.setId(r * 10 + c);
                tv.setMaxWidth(tv.getWidth()); //Prevents resizing.
                row.addView(tv);
            }
            table.addView(row);
        }
    }
}
