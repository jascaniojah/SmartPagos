package com.example.jascaniojah.smartpagos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Recarga extends Fragment {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
    TextView numero_recarga,monto,fecha_consulta,estado,id_recarga,resp_fecha,resp_estado_recarga,resp_id;
    EditText numero_a_recargar,monto_recarga;
    Button boton_recarga;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recarga_frag, container, false);
        fecha_consulta= (TextView) view.findViewById(R.id.fecha_consulta);
        numero_recarga = (TextView) view.findViewById(R.id.numero_recarga);
        monto= (TextView)view.findViewById(R.id.monto);
        estado= (TextView)view.findViewById(R.id.estado);
        id_recarga= (TextView)view.findViewById(R.id.id_recarga);
        resp_fecha=(TextView)view.findViewById(R.id.resp_fecha);
        resp_estado_recarga=(TextView)view.findViewById(R.id.resp_estado_recarga);
        resp_id=(TextView)view.findViewById(R.id.resp_id);
        numero_a_recargar = (EditText) view.findViewById(R.id.numero_a_recargar);
        monto_recarga = (EditText) view.findViewById(R.id.monto_recarga);

        boton_recarga = (Button) view.findViewById(R.id.boton_recarga);
        boton_recarga.setOnClickListener(new View.OnClickListener() {

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
        String fecha = df1.format(c.getTime());
        resp_fecha.setText(fecha);
        resp_estado_recarga.setText("Aprobado");
        resp_id.setText("123456");
    }
}