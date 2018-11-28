package com.langmehdi.aperitmaison.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.langmehdi.aperitmaison.R;

import java.math.BigDecimal;

/**
 * Created by xpzp646 on 16/09/2016.
 */
public class Calcul  extends Fragment {
    private static Boolean isComputeLtr;

    private TextView textDessusBtn;
    private TextView textDessousBtn;
    private EditText textLtVin;
    private EditText textDgVin;
    private EditText textDgGout;
    private EditText textOperateur;
    private TextView textResCompute;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calcul_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isComputeLtr = true;

        textDessusBtn = (TextView) view.findViewById(R.id.textViewOperator);
        textDessousBtn = (TextView) view.findViewById(R.id.textViewRes);

        textLtVin      = (EditText) view.findViewById(R.id.editTextLtVin);
        textDgVin      = (EditText) view.findViewById(R.id.editTextDgVin);
        textDgGout     = (EditText) view.findViewById(R.id.editTextDgGout);
        textOperateur  = (EditText) view.findViewById(R.id.editTextOperator);
        textResCompute = (TextView) view.findViewById(R.id.TxtViewResCompute);

        // listener du bouton d'echange de convertion
        Button changeConv = (Button) view.findViewById(R.id.buttonChange);
        changeConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String operator;
                String[] op;

                if (isComputeLtr) {
                    // on etait dans l'etat calcul Litre
                    // on veut passer au calcul Degres
                    isComputeLtr = false;
                    textDessusBtn.setText("Litre Eau de vie : ");
                    textDessousBtn.setText("Degrés Apéritif : ");

                    //TODO editText non vide on les inverse et on recalcule
                    if (!textResCompute.getText().equals("")){
                        operator = textResCompute.getText().toString();
                        op = operator.split(" ");
                        textOperateur.setText(op[0].toString());
                        // Compute Degres
                        CalculDegresApero();
                    }

                } else {
                    // on etait dans l'etat calcul Litre
                    // on veut passer au calcul Degres
                    isComputeLtr = true;
                    textDessusBtn.setText("Degrés Apéritif : ");
                    textDessousBtn.setText("Litre Eau de vie : ");

                    //TODO editText non vide on les inverse et on recalcule
                    if (!textResCompute.getText().equals("")){
                        operator = textResCompute.getText().toString();
                        op = operator.split(" ");
                        textOperateur.setText(op[0]);
                        // Compute Degres
                        CalculLtGout();
                    }
                }
            }
        });

        Button buttonCalculLit = (Button) view.findViewById(R.id.buttonCalculLtGout);
        buttonCalculLit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                if (isComputeLtr){
                    CalculLtGout();
                } else {
                    CalculDegresApero();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            menu.clear();
            inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh :
                initDisplay();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Init data on display
     */
    private void initDisplay(){
        textLtVin.setText("");
        textDgVin.setText("");
        textDgGout.setText("");
        textOperateur.setText("");
        textResCompute.setText("");
    }

    // calcul le litrage d'eau de vie
    private void CalculLtGout (){
        float litre;

        if (textLtVin.getText().toString().equals("")
                || textDgVin.getText().toString().equals("")
                || textDgGout.getText().toString().equals("")
                || textOperateur.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs ...", Toast.LENGTH_LONG).show();
        }else {

            float ltVin = Float.valueOf(textLtVin.getText().toString());
            float dgVin = Float.valueOf(textDgVin.getText().toString());
            float dgGout = Float.valueOf(textDgGout.getText().toString());
            float dgApero = Float.valueOf(textOperateur.getText().toString());

            //TODO prendre en cpmte decimale dans la formule
            // Formule
            litre = ((float) (dgApero * ltVin - ltVin * dgVin)) / ((float) (dgGout - dgApero));

            float result;
            result = round(litre,2);
            textResCompute.setText(result + " L");
        }
    }

    // calcul le Degres alcool
    private void CalculDegresApero (){
        float dgApero;

        if (textLtVin.getText().toString().equals("")
                || textDgVin.getText().toString().equals("")
                || textDgGout.getText().toString().equals("")
                || textOperateur.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs ...", Toast.LENGTH_LONG).show();
        }else {

            float ltVin = Float.valueOf(textLtVin.getText().toString());
            float dgVin = Float.valueOf(textDgVin.getText().toString());
            float dgGout = Float.valueOf(textDgGout.getText().toString());
            float ltGout = Float.valueOf(textOperateur.getText().toString());

            //TODO prendre en cpmte decimale dans la formule
            // Formule
            dgApero = ((float) (ltGout * dgGout + ltVin * dgVin)) / ((float) (ltVin + ltGout));

            float result;
            result = round(dgApero,2);
            textResCompute.setText(result + " °");
        }
    }

    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}

