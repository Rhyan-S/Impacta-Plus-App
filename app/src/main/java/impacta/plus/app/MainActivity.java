package impacta.plus.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.databinding.ActivityMainBinding;
import impacta.plus.app.mvp.login.LoginContract;
import impacta.plus.app.mvp.login.LoginPresenter;
import impacta.plus.app.utils.SessionManager;

public class MainActivity extends AppCompatActivity implements LoginContract.View {

    private ActivityMainBinding binding;
    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Verificar se já está logado
        SessionManager session = new SessionManager(this);
        if (session.isLoggedIn()) {
            navigateToDashboard();
            return;
        }

        presenter = new LoginPresenter(this, new BancoRepository(this), session);

        binding.buttonEntrar.setOnClickListener(v ->
                presenter.login(
                        binding.editTextEmail.getText().toString(),
                        binding.editTextPassword.getText().toString()
                )
        );

        binding.textViewCriarConta.setOnClickListener(v ->
                startActivity(new Intent(this, CadastroActivity.class))
        );

        // Verifica se veio do cadastro
        if (getIntent().hasExtra("email_cadastrado")) {
            binding.editTextEmail.setText(getIntent().getStringExtra("email_cadastrado"));
        }
    }

    @Override
    public void showLoading() {
        binding.buttonEntrar.setEnabled(false);
        binding.buttonEntrar.setText("Carregando...");
    }

    @Override
    public void hideLoading() {
        binding.buttonEntrar.setEnabled(true);
        binding.buttonEntrar.setText("Entrar");
    }

    @Override
    public void showLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}