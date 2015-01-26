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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 1/19/2015.
 */
public class ECAdapter  extends ArrayAdapter<Movimientos> {
    private Context mContext;
    private ArrayList<Movimientos> mMovimientosArray;


    public ECAdapter(Context context, List<Movimientos> movimientos) {
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
        Drawable nc=convertView.getResources().getDrawable(R.drawable.ic_action_registrar_pago);
        Drawable nd=convertView.getResources().getDrawable(R.drawable.iconsheet);

        if(movimiento.getTipoRecarga().equals("NC"))
        {
            icono.setImageDrawable(nc);

        }

        else if (movimiento.getTipoRecarga().equals("ND"))
        {
            icono.setImageDrawable(nd);
        }


      ;


        Log.i("TransaccionesAdapter", "Numero " + movimiento.getTelefono());


        String s= DateParser.DateTimeToString(movimiento.getFechaHora());
        String saldo=movimiento.getSaldo();
        String ln1=printTxt("ln1",movimiento.getProducto());
        String ln2=printTxt("ln2",movimiento.getProducto(),movimiento.getMonto(),movimiento.getSaldo(),movimiento.getTelefono(),s);
        numero.setText(ln1);
        info.setText(ln2);
        return convertView;


    }
    public String printTxt(String ln,String producto)
    {

        String s=null;
        String line=null;
        String spcs="";
        if (ln.equals("ln1"))
        {
         int spaces=14+10-producto.length();
         Log.i("ECADAPTER","Longitud "+producto.length())   ;
         for(int i=0; i<spaces;i++) {
             spcs+=" ";
             Log.i("ECADAPTER","*"+i)  ;
         }

        }
        line=producto+spcs+"Monto         Saldo";

        return line;
    }
    public String printTxt(String ln,String producto,Float monto,String saldo,String serial,String fecha)
    {

        String s=fecha;
        String line=null;
        String spcs="";
        String spcs2="";
        String montoS=""+monto;
        if (ln.equals("ln2"))
        {
            int spaces=14+25-producto.length();
            Log.i("ECADAPTER","Longitud "+producto.length())   ;
            for(int i=0; i<spaces;i++) {
                spcs+=" ";
            if(montoS.length()<=2)
            spcs2="     ";

            }


        }
        DecimalFormat formato = new DecimalFormat("#,###.###");
        String Monto=formato.format(monto);
        String Saldo=formato.format(Float.parseFloat(saldo));
        line=s+spcs+Monto+"                    "+spcs2+Saldo+"\n"+"Serial: "+serial;

        return line;
    }
}
