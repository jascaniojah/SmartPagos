package com.example.jascaniojah.smartpagos;

/**
 * Created by jascaniojah on 18/09/14.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Notificar extends Fragment {

    TextView cuenta, referencia, monto;
    EditText num_cuenta, num_referencia, monto_deposito;
    Button boton_notificar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificar_frag, container, false);
        cuenta = (TextView) view.findViewById(R.id.cuenta);
        referencia = (TextView) view.findViewById(R.id.referencia);
        monto = (TextView) view.findViewById(R.id.monto);
        num_cuenta = (EditText) view.findViewById(R.id.numero_cuenta);
        num_referencia = (EditText) view.findViewById(R.id.num_referencia);
        monto_deposito = (EditText) view.findViewById(R.id.monto_deposito);

        boton_notificar = (Button) view.findViewById(R.id.boton_notificar);
        boton_notificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent i = new Intent(getApplicationContext(), Principal.class);
                //startActivity(i);
                //attemptLogin();
                // Rellenar();
            }
        });

        return view;
    }
}