package br.com.etechoracio.firebase.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.etechoracio.firebase.R;
import br.com.etechoracio.firebase.enums.EspecieEnum;
import br.com.etechoracio.firebase.enums.SexoEnum;
import br.com.etechoracio.firebase.enums.StatusEnum;
import br.com.etechoracio.firebase.model.Personagem;

public class InclusaoActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editApelido;
    private Spinner spnEspecie;
    private Spinner spnStatus;
    private RadioGroup groupSexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inclusao);
        editNome = findViewById(R.id.editNome);
        editApelido = findViewById(R.id.editApelido);
        spnEspecie = findViewById(R.id.spnEspecie);
        spnStatus = findViewById(R.id.spnStatus);
        groupSexo = findViewById(R.id.groupSexo);

        spnEspecie.setAdapter(getAdapter(EspecieEnum.class));
        spnStatus.setAdapter(getAdapter(StatusEnum.class));
    }

    private <T extends Enum<T>> ArrayAdapter getAdapter(Class<T> clazz) {
        return  new ArrayAdapter<T>(this,
                R.layout.support_simple_spinner_dropdown_item,
                clazz.getEnumConstants());
    }

    public void onSalvar(View view) {
        Personagem personagem = new Personagem();
        personagem.setApelido(editApelido.getText().toString());
        personagem.setEspecie(EspecieEnum.getBy(spnEspecie.getSelectedItemPosition()));
        personagem.setStatus(StatusEnum.getBy(spnStatus.getSelectedItemPosition()));
        personagem.setNome(editNome.getText().toString());
        personagem.setSexo(SexoEnum.getBy(((RadioButton) findViewById(groupSexo.getCheckedRadioButtonId())).getText().toString()));

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("personagens");
        String id = myRef.push().getKey();
        myRef.child(id).setValue(personagem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        personagem.setId(id);

        builder.setTitle("Sucesso");
        builder.setMessage("Registro inserido com Sucesso");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
