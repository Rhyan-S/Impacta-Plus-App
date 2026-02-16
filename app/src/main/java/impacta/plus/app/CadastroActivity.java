package impacta.plus.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.databinding.ActivityCadastroBinding;
import impacta.plus.app.mvp.cadastro.CadastroContract;
import impacta.plus.app.mvp.cadastro.CadastroPresenter;

public class CadastroActivity extends AppCompatActivity implements CadastroContract.View {
    private ActivityCadastroBinding binding;
    private CadastroContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new CadastroPresenter(this, new BancoRepository(this));

        binding.buttonCadastrar.setOnClickListener(v -> presenter.cadastrar(
                binding.editTextNome.getText().toString(),
                binding.editTextEmailCadastro.getText().toString(),
                binding.editTextCpf.getText().toString(),
                binding.editTextTelefone.getText().toString(),
                binding.editTextSenhaCadastro.getText().toString()
        ));
    }

    @Override
    public void showLoading() { binding.buttonCadastrar.setEnabled(false); }

    @Override
    public void hideLoading() { binding.buttonCadastrar.setEnabled(true); }

    @Override
    public void showError(String msg) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }

    @Override
    public void onCadastroSucesso(String email) {
        Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email_cadastrado", email);
        startActivity(intent);
        finish();
    }
}