package br.com.kualit.buscarendereoporcep;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.kualit.buscarendereoporcep.dialog.DialogProgress;
import br.com.kualit.buscarendereoporcep.interfaces.WebmaniaBRInterface;
import br.com.kualit.buscarendereoporcep.model.Endereco;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private static final String BASE_URL = "https://webmaniabr.com/api/";
    private static final String APP_SECRET = "";
    private static final String API_KEY = "";
    private static final String TAG = "buscacep";



    private TextView txtEndereco, txtBairro, txtCidade, txtUF, txtIbge;
    ;
    private EditText edt_cep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        txtEndereco = findViewById( R.id.tv_endereco );
        txtBairro = findViewById( R.id.tv_bairro );
        txtCidade = findViewById( R.id.tv_cidade );
        txtUF = findViewById( R.id.tv_uf );
        txtIbge = findViewById( R.id.tv_ibge );
        edt_cep = findViewById( R.id.edt_cep );

        Button botao = findViewById( R.id.btn_mostrarDados );
        botao.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Util.statusInternet( getApplicationContext() )) {

                    if (edt_cep.getText().toString().trim().isEmpty()) {

                        Toast.makeText( getApplicationContext(), "Por favor, preencha o cep!", Toast.LENGTH_LONG ).show();

                    } else if (edt_cep.getText().toString().trim().length() < 8) {

                        Toast.makeText( getApplicationContext(), "O tamanho do cep é inválido!", Toast.LENGTH_LONG ).show();

                    } else {
                        obterEndereco();
                    }

                }else{
                    Toast.makeText( getApplicationContext(), "Por favor verifique sua conexão com a internet!", Toast.LENGTH_LONG ).show();

                }


            }
        } );


    }


    private void obterEndereco() {
        final DialogProgress dialog = new DialogProgress();
        dialog.show( getSupportFragmentManager(), "0" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() ).build();

        WebmaniaBRInterface service = retrofit.create( WebmaniaBRInterface.class );

        String cep = edt_cep.getText().toString();
        if (cep.length() == 8) {
            StringBuilder sb = new StringBuilder( cep );
            sb.insert( 5, "-" );
            edt_cep.setText( sb );
        }


        Call<Endereco> call = service.obterEnderecoPorCep( cep, API_KEY, APP_SECRET );
        call.enqueue( new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {

                Endereco endereco = response.body();
                int code = response.code();


                if (code == 200) {

                    txtEndereco.setText( endereco.getEndereco() );
                    txtBairro.setText( endereco.getBairro() );
                    txtCidade.setText( endereco.getCidade() );
                    txtUF.setText( endereco.getUf() );
                    txtIbge.setText( endereco.getIbge() );
                    dialog.dismiss();
                }


            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {


            }
        } );


    }

}
