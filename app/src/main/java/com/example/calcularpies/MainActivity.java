package com.example.calcularpies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class MainActivity extends Activity
{
    private EditText txtEspesor;
    private EditText txtAncho;
    private EditText txtLargo;

    private NumberPicker pickerCantidad;

    private TextView totalPies;

    private static final double CONSTANTE = 0.2734;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // identificamos las vistas
        txtEspesor = findViewById(R.id.txtEspesor);
        txtAncho = findViewById(R.id.txtAncho);
        txtLargo = findViewById(R.id.txtLargo);

        pickerCantidad = (NumberPicker) findViewById(R.id.cantidadPicker);

        totalPies = findViewById(R.id.labelTotalPies);

        Button btnLimpiar = findViewById(R.id.btnLimpiar);

        // formateamos su entrada de datos
        txtEspesor.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
        txtAncho.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
        txtLargo.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 1)});

        pickerCantidad.setMax(9999);
        pickerCantidad.setMin(1);
        pickerCantidad.setUnit(1);
        pickerCantidad.setValue(1);


        // le asignamos un textChangedListener
        darListeners();

        calcularPies();
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
                System.out.println("Editable --> " + s);

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
                System.out.println("Editable --> " + s);

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
                System.out.println("Editable --> " + s);

                calcularPies();
            }
        });

        pickerCantidad.setValueChangedListener(new ValueChangedListener()
        {
            @Override
            public void valueChanged(int value, ActionEnum action)
            {
                System.out.println("ActionEnum -> " + action);

                calcularPies();
            }
        });


    }

    public void limpiarDatos(View view)
    {
        System.out.println("LIMPIAR DATOS");

        this.txtEspesor.getText().clear();
        this.txtAncho.getText().clear();
        this.txtLargo.getText().clear();
        this.pickerCantidad.setValue(1);

        this.txtEspesor.requestFocus();
    }

}