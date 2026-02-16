package impacta.plus.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import impacta.plus.app.databinding.ActivityConfirmacaoParticipacaoBinding;

public class ConfirmacaoParticipacaoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConfirmacaoParticipacaoBinding binding = ActivityConfirmacaoParticipacaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String nome = getIntent().getStringExtra("nome_acao_confirmada");
        binding.textViewAcaoConfirmadaMsg.setText("Sua participação na ação '" + nome + "' foi confirmada.");

        binding.buttonConcluirConfirmacao.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}