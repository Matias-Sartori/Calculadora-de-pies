package com.example.calcularpies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    private EditText txtEspesor;
    private EditText txtAncho;
    private EditText txtLargo;

    private NumberPicker pickerCantidad;

    private TextView totalPies;

    private static final double CONSTANTE = 0.2734;

    SharedPreferences sharedPreferences;

    String tema;

    int temaSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // identificamos las vistas
        txtEspesor = findViewById(R.id.txtEspesor);
        txtAncho = findViewById(R.id.txtAncho);
        txtLargo = findViewById(R.id.txtLargo);

        pickerCantidad = (NumberPicker) findViewById(R.id.cantidadPicker);

        totalPies = findViewById(R.id.labelTotalPies);

        // formateamos su entrada de datos
        txtEspesor.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
        txtAncho.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
        txtLargo.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 1)});

        pickerCantidad.setMax(9999);
        pickerCantidad.setMin(1);
        pickerCantidad.setUnit(1);
        pickerCantidad.setValue(1);

        sharedPreferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);

        System.out.println("sharedPreferences.getAll() --> " + sharedPreferences.getAll());

        tema = sharedPreferences.getString("ThemeName", "Default");

        aplicarTema(tema);

        // le asignamos un textChangedListener
        darListeners();

        calcularPies();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle estado)
    {
        // guardamos el dato al bundle
        estado.putString("totalpies", String.valueOf(totalPies.getText()));

        // llamamos al metodo de la calse padre, pasandole nuestro bundle
        super.onSaveInstanceState(estado);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        totalPies.setText(savedInstanceState.getString("totalpies"));
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.tema:
                mostrarTemasDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class DecimalDigitsInputFilter implements InputFilter
    {
        private Pattern mPattern;

        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero)
        {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
        {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    private void calcularPies()
    {
        if(!txtEspesor.getText().toString().isEmpty()
                && !txtAncho.getText().toString().isEmpty()
                && !txtLargo.getText().toString().isEmpty()
                && pickerCantidad.getValue() > 0)
        {
            double espesor = Double.parseDouble(txtEspesor.getText().toString());
            double ancho = Double.parseDouble(txtAncho.getText().toString());
            double largo = Double.parseDouble(txtLargo.getText().toString());
            int cantidad = pickerCantidad.getValue();

            double pies = (CONSTANTE * espesor * ancho * largo) * cantidad;

            DecimalFormat df2 = new DecimalFormat("#.##");

            //df2.setRoundingMode(RoundingMode.DOWN);

           String piesFormateado = df2.format(pies);

            totalPies.setText(piesFormateado);
            totalPies.setVisibility(View.VISIBLE);
        }
        else
        {
            this.totalPies.setText("0");
            totalPies.setVisibility(View.INVISIBLE);
        }
    }

    public void darListeners()
    {
        txtEspesor.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                calcularPies();
            }
        });

        txtAncho.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                calcularPies();
            }
        });

        txtLargo.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                calcularPies();
            }
        });

        pickerCantidad.setValueChangedListener((value, action) -> calcularPies());
    }

    public void limpiarDatos(View view)
    {
        this.txtEspesor.getText().clear();
        this.txtAncho.getText().clear();
        this.txtLargo.getText().clear();
        this.pickerCantidad.setValue(1);

        this.txtEspesor.requestFocus();
    }

    private void mostrarTemasDialog()
    {
        String [] temas = {getString(R.string.claro), getString(R.string.oscuro)};

        int temaActual = !tema.equalsIgnoreCase("Oscuro") && !tema.equalsIgnoreCase("Dark") ? 0 : 1;

        temaSeleccionado = temaActual;

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.seleccionar_tema));
        builder.setSingleChoiceItems(temas, temaActual, (dialog, which) ->
        {
            temaSeleccionado = which;
        });

        builder.setPositiveButton(getString(R.string.aceptar), (dialog, which) ->
        {
            if(temaSeleccionado != temaActual)
            {
                tema = temas[temaSeleccionado];

                guardarTema(tema);
            }

            dialog.dismiss();
        });
        builder.setNegativeButton(getString(R.string.cancelar), (dialog, which) ->
        {

            dialog.dismiss();
        });

        builder.show();
    }

    private void aplicarTema(String nombreTema)
    {
        if(!nombreTema.equalsIgnoreCase("Oscuro") && !nombreTema.equalsIgnoreCase("Dark"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        System.out.println("SE APLICO EL TEMA! --> " + nombreTema);
    }

    private void guardarTema(String nombreTema)
    {
        // Create preference to store theme name
        SharedPreferences preferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ThemeName", nombreTema);
        editor.apply();
        recreate();

        System.out.println("SE GUARDO EL TEMA --> " + nombreTema);
    }
}