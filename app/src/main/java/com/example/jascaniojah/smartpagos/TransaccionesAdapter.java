package com.example.jascaniojah.smartpagos;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix on 11/1/2014.
 */
public class TransaccionesAdapter extends ArrayAdapter<Movimientos> {
    private Context mContext;
    private ArrayList<Movimientos> mMovimientosArray;


    public TransaccionesAdapter(Context context, List<Movimientos> movimientos) {
        super(context, 0, movimientos);
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        Movimientos movimiento=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transacciones_row, parent, false);

        }

        ImageView icono=(ImageView) convertView.findViewById(R.id.iconoTransaccion);
        TextView numero=(TextView)convertView.findViewById(R.id.numeroTelTransaccion);
        TextView info=(TextView)convertView.findViewById(R.id.fechaTransaccion);
        numero.setText(movimiento.getTelefono());

        Date date=movimiento.getFechaHora();
        DateFormat df =DateFormat.getDateTimeInstance();
        String s = df.format(date);
        info.setText("Monto: "+movimiento.getMonto()+" Fecha: "+s);
        Drawable im=convertView.getResources().getDrawable(R.drawable.ic_pago);
        icono.setImageDrawable(im);




        return convertView;


    }

}
