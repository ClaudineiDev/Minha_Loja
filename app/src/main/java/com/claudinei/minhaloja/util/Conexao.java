package com.claudinei.minhaloja.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class Conexao {
    public static boolean isConnected(Context context){
        ConnectivityManager conmag = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conmag != null ) {
            conmag.getActiveNetworkInfo();

            //Verifica internet pela WIFI
            if (conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {

                return true;
            }

            //Verifica se tem internet móvel
            if (conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {

                return true;
            }
        }

        return false;
    }
}
