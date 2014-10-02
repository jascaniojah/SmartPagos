package com.example.jascaniojah.smartpagos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jascaniojah.libraries.DataBaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Saldo extends Fragment {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
    TextView fecha_consulta,hora_consulta,fecha_ult_trans,hora_ult_trans,saldo_actual;
    TextView resp_fecha_consulta,resp_hora_consulta,resp_fecha_ult_trans,resp_hora_ult_trans,resp_saldo_actual;
    Button saldo_boton;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saldo_frag, container, false);
        fecha_consulta= (TextView) view.findViewById(R.id.fecha_consulta);
        fecha_ult_trans= (TextView)view.findViewById(R.id.fecha_ult_trans);
        saldo_actual= (TextView)view.findViewById(R.id.saldo);
        resp_fecha_consulta=(TextView)view.findViewById(R.id.resp_fecha_consulta);
        resp_fecha_ult_trans=(TextView)view.findViewById(R.id.resp_fecha_ult_trans);
        resp_saldo_actual=(TextView)view.findViewById(R.id.resp_saldo_actual);

        saldo_boton = (Button) view.findViewById(R.id.boton_saldo);
        saldo_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent i = new Intent(getApplicationContext(), Principal.class);
                //startActivity(i);
                //attemptLogin();
                Rellenar();

            }
        });

        return view;
    }
    public void Rellenar(){
        DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());
        HashMap cuenta = new HashMap();
        cuenta = db.getSaldo();
        resp_fecha_consulta.setText((CharSequence) cuenta.get("fecha_server"));
        resp_saldo_actual.setText((CharSequence)cuenta.get("saldo"));
        resp_fecha_ult_trans.setText((CharSequence)cuenta.get("fecha_trans"));
    }
}