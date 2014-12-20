package com.example.jascaniojah.smartpagos;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jascaniojah.libraries.DateParser;

import java.util.ArrayList;
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
        Drawable tv=convertView.getResources().getDrawable(R.drawable.ic_tv);
        Drawable movil=convertView.getResources().getDrawable(R.drawable.ic_movil);
        Drawable fijo=convertView.getResources().getDrawable(R.drawable.ic_fijo);

        if(movimiento.getProducto().equals("01"))
        {
            icono.setImageDrawable(movil);

        }
        else if (movimiento.getProducto().equals("02"))
        {
            icono.setImageDrawable(fijo);
        }
        else if (movimiento.getProducto().equals("03"))
        {
            icono.setImageDrawable(tv);
        }
        else
            icono.setImageDrawable(movil);


        Log.i("TransaccionesAdapter","Numero "+movimiento.getTelefono());

        //Log.i("TransaccionesAdapter","Monto "+movimiento.getMonto());


       // Date date=movimiento.getFechaHora();
//        String s = date.toString();
       String s= DateParser.DateTimeToString(movimiento.getFechaHora());
        numero.setText(movimiento.getTelefono());
        info.setText("Monto: "+movimiento.getMonto()+" Fecha: "+s+'\n'+" ID Recarga: "+movimiento.getSerial());

        return convertView;


    }

}
