package br.com.kualit.buscarendereoporcep;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {

    public static boolean statusInternet(Context context) {

        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo informacao = conexao.getActiveNetworkInfo();
        if (informacao != null && informacao.isConnected()) { return true;}
        else {return false;
        }
    }


}
