package com.example.jascaniojah.smartpagos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Trans extends Fragment {

    TextView desde_fecha,hasta_fecha;
    EditText fecha_desde,fecha_hasta;
    Button boton_consultar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trans_frag, container, false);

        desde_fecha= (TextView) view.findViewById(R.id.desde_fecha);
        fecha_desde = (EditText) view.findViewById(R.id.fecha_desde);
        fecha_hasta = (EditText) view.findViewById(R.id.fecha_hasta);
        hasta_fecha=(TextView) view.findViewById(R.id.hasta_fecha);

        boton_consultar = (Button) view.findViewById(R.id.boton_consultar);
        boton_consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent i = new Intent(getApplicationContext(), Principal.class);
                //startActivity(i);
                //attemptLogin();

            }
        });

        return view;
    }}
