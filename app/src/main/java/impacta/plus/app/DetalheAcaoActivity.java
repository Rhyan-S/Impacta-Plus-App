package impacta.plus.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.databinding.ActivityDetalheAcaoBinding;
import impacta.plus.app.model.AcaoSocial;
import impacta.plus.app.mvp.detalhe.DetalheContract;
import impacta.plus.app.mvp.detalhe.DetalhePresenter;
import impacta.plus.app.utils.SessionManager;

public class DetalheAcaoActivity extends AppCompatActivity implements DetalheContract.View {
    private ActivityDetalheAcaoBinding binding;
    private DetalhePresenter presenter;
    private int acaoId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalheAcaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        acaoId = getIntent().getIntExtra("acao_id", -1);
        userId = new SessionManager(this).getUserId();

        presenter = new DetalhePresenter(this, new BancoRepository(this));
        presenter.carregarAcao(acaoId, userId);

        binding.buttonParticipar.setOnClickListener(v -> presenter.participar(acaoId, userId));
        binding.imageViewClose.setOnClickListener(v -> finish());
    }

    @Override
    public void mostrarDetalhes(AcaoSocial acao) {
        binding.textViewDetalheNomeAcao.setText(acao.getNomeVaga());
        binding.textViewDetalheLocalAcao.setText(acao.getLocal());
        binding.textViewDetalheDataAcao.setText(acao.getDataInicio());
        binding.textViewDetalheDescricao.setText(acao.getDescricao());
        binding.textViewDetalheVagas.setText(String.valueOf(acao.getVagasDisponiveis()));
        binding.textViewDetalheImpacto.setText(acao.getImpacto());
    }

    @Override
    public void mostrarErro(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onParticipacaoConfirmada(String nomeAcao) {
        Intent intent = new Intent(this, ConfirmacaoParticipacaoActivity.class);
        intent.putExtra("nome_acao_confirmada", nomeAcao);
        startActivity(intent);
        finish();
    }

    @Override
    public void atualizarStatusBotao(boolean participou, boolean vagasEsgotadas) {
        if (participou) {
            binding.buttonParticipar.setText("Inscrito");
            binding.buttonParticipar.setEnabled(false);
        } else if (vagasEsgotadas) {
            binding.buttonParticipar.setText("Esgotado");
            binding.buttonParticipar.setEnabled(false);
        } else {
            binding.buttonParticipar.setEnabled(true);
        }
    }
}