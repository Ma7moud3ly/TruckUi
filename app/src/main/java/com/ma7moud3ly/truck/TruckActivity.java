package com.ma7moud3ly.truck;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.ma7moud3ly.truck.databinding.ActivityTruckBinding;

import androidx.appcompat.app.AppCompatActivity;


public class TruckActivity extends AppCompatActivity {
    private int screenWidth, screenHeight;
    private ActivityTruckBinding binding;
    //the ratio of table width to truck width 60:40
    private double tableWidth = 0.6, carWidth = 0.4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //read the layout with data binding
        binding = ActivityTruckBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init the sliding layout
        initSwapLayout();
        //init the truck webView
        initCar();
        //init the tables design
        initTables();
        //add click listener to cancel and save button
        binding.cancel.setOnClickListener(v -> showCloseDialog());
        binding.save.setOnClickListener(v -> showSaveDialog());
    }

    private void initCar() {
        //load the design from assets
        //then configure the webView to handle user click events
        binding.car.loadUrl("file:///android_asset/truck.html");
        binding.car.getSettings().setJavaScriptEnabled(true);
        binding.car.getSettings().setDomStorageEnabled(true);
        binding.car.getSettings().setLoadWithOverviewMode(true);
        binding.car.getSettings().setUseWideViewPort(false);
        binding.car.getSettings().setBuiltInZoomControls(false);
        binding.car.getSettings().setDisplayZoomControls(false);
        binding.car.getSettings().setSupportZoom(true);
        binding.car.getSettings().setDefaultTextEncodingName("utf-8");
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);
        binding.car.addJavascriptInterface(jsInterface, "JSInterface");

    }

    //init the sliding layouts
    private void initSwapLayout() {

        binding.car.post(() -> {
            screenWidth = binding.carContainer.getWidth();
            screenHeight = binding.carContainer.getHeight();
            changeWidthHeight(binding.car, screenWidth, screenHeight);
            changeWidth(binding.drawer, (int) (screenWidth * tableWidth));
            binding.drawer.setVisibility(View.GONE);
        });

        final OnSwipeTouchListener truck = new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                changeWidth(binding.carContainer, screenWidth);
                changeWidthHeight(binding.car, screenWidth, screenHeight);
                binding.drawer.setVisibility(View.GONE);
            }

            public void onSwipeLeft() {
                int sWidth = (int) (screenWidth * carWidth);
                int sHeight = screenHeight / (screenWidth / sWidth);
                changeWidth(binding.carContainer, sWidth);
                changeWidthHeight(binding.car, sWidth, sHeight);
                binding.drawer.setVisibility(View.VISIBLE);
            }

            public void onSwipeBottom() {
            }

        };
        final OnSwipeTouchListener table = new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (binding.drawer.getWidth() == screenWidth) {
                    binding.car.setVisibility(View.VISIBLE);
                    changeWidth(binding.carContainer, (int) (screenWidth * carWidth));
                    changeWidth(binding.drawer, (int) (screenWidth * tableWidth));
                } else if (binding.drawer.getWidth() < screenWidth) {
                    changeWidth(binding.carContainer, screenWidth);
                    changeWidthHeight(binding.car, screenWidth, screenHeight);
                    binding.drawer.setVisibility(View.GONE);
                    binding.car.setVisibility(View.VISIBLE);
                }
            }

            public void onSwipeLeft() {
                changeWidth(binding.drawer, screenWidth);
                binding.car.setVisibility(View.GONE);
            }

            public void onSwipeBottom() {
            }

        };
        binding.car.setOnTouchListener(truck);
        binding.drawer.setOnTouchListener(table);
        binding.table.drawerScroll.setOnTouchListener(table);

    }

    //init to the table
    private void initTables() {

        //listen for any changes in the (to) cells
        final TextWatcher onEdit = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String txt = editable.toString().trim();
                    if (txt.isEmpty()) {
                        highlightCell(-1);
                        return;
                    }
                    int value = Integer.valueOf(txt);
                    if (value > 22 || value < 1) {
                        Toast.makeText(getApplicationContext(), "Invalid numbers", Toast.LENGTH_SHORT).show();
                    } else {
                        highlightCell(value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        //add the text change event to all the (to) celss
        binding.table.toTyre1.addTextChangedListener(onEdit);
        binding.table.toTyre2.addTextChangedListener(onEdit);
        binding.table.toTyre3.addTextChangedListener(onEdit);
        binding.table.toTyre4.addTextChangedListener(onEdit);
        binding.table.toTyre5.addTextChangedListener(onEdit);
        binding.table.toTyre6.addTextChangedListener(onEdit);
        binding.table.toTyre7.addTextChangedListener(onEdit);
        binding.table.toTyre8.addTextChangedListener(onEdit);
        binding.table.toTyre9.addTextChangedListener(onEdit);
        binding.table.toTyre10.addTextChangedListener(onEdit);
        binding.table.toTyre11.addTextChangedListener(onEdit);
        binding.table.toTyre12.addTextChangedListener(onEdit);
        binding.table.toTyre13.addTextChangedListener(onEdit);
        binding.table.toTyre14.addTextChangedListener(onEdit);
        binding.table.toTyre15.addTextChangedListener(onEdit);
        binding.table.toTyre16.addTextChangedListener(onEdit);
        binding.table.toTyre17.addTextChangedListener(onEdit);
        binding.table.toTyre18.addTextChangedListener(onEdit);
        binding.table.toTyre19.addTextChangedListener(onEdit);
        binding.table.toTyre20.addTextChangedListener(onEdit);
        binding.table.toTyre21.addTextChangedListener(onEdit);
        binding.table.toTyre22.addTextChangedListener(onEdit);
    }

    //highlight the (from) cell when it is configured and  matched to a (to) cell
    private void highlightCell(int value) {
        //search all the from cells
        for (int i = 1; i <= 22; i++) {
            int id = getResources().getIdentifier("from_tyre" + i, "id", getPackageName());
            TextView view = (TextView) findViewById(id);
            //if any from cells matched to (to) cell, highlight it with red
            if (view.getText().equals("" + value))
                view.setBackgroundColor(Color.RED);
                //otherwise clear all highlighted cells
            else view.setBackground(getResources().getDrawable(R.drawable.borders));
        }
    }

    //show cancel dialog
    private void showCloseDialog() {
        final AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
        Dialog.setTitle("Do you want to leave this page ?");
        Dialog.setNegativeButton("Cancel", (arg0, arg1) -> Dialog.setCancelable(true));
        Dialog.setPositiveButton("Okay", (arg0, arg1) -> {
            Dialog.setCancelable(true);
            finish();
        });
        Dialog.show();
    }

    //show save dialog
    private void showSaveDialog() {
        final AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
        Dialog.setTitle("Do you want to leave this page ?");
        Dialog.setNegativeButton("Cancel", (arg0, arg1) -> Dialog.setCancelable(true));
        Dialog.setPositiveButton("Okay", (arg0, arg1) -> {
            Dialog.setCancelable(true);
            finish();
        });
        Dialog.show();
    }

    //modify the Truck and Table layouts based on screen size..
    private void changeWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }

    private void changeWidthHeight(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    //init javascript interface to listen from event coming from the webView
    //so that when a user click on a tyre, it should select it in (from) cells.
    private class JavaScriptInterface {
        private Activity activity;

        public JavaScriptInterface(Activity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        //this function is invoked from the assets/truck.html
        //when user click on a tyre in the webView, the webView sends events to this method.
        //tyre is tyre id
        //state: whether tyre is configured or not
        public void whichTyre(String tyre, int state) {
            try {
                //read the tyre number (tyre12) will be 12
                int num = (int) Double.parseDouble(tyre.replace("tyre", ""));
                //read the equivalent (from) cell
                int id = getResources().getIdentifier("from_tyre" + num, "id", getPackageName());
                TextView view = (TextView) findViewById(id);
                //if tyre is configured write its number in the (from) cell
                if (state == 1) {
                    runOnUiThread(() -> {
                        view.setText("" + num);
                    });
                    //if not configured, write N/A in the tyre (from) cell
                } else {
                    runOnUiThread(() -> {
                        view.setText("N/A");
                        view.setBackground(getResources().getDrawable(R.drawable.borders));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

