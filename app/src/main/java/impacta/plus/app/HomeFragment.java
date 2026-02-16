package impacta.plus.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import impacta.plus.app.adapter.AcoesAdapter;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.databinding.FragmentHomeBinding;
import impacta.plus.app.model.AcaoSocial;
import impacta.plus.app.mvp.home.HomeContract;
import impacta.plus.app.mvp.home.HomePresenter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {
    private FragmentHomeBinding binding;
    private HomePresenter presenter;
    private AcoesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupRecycler();
        presenter = new HomePresenter(this, new BancoRepository(getContext()));
        presenter.carregarAcoes();
    }

    private void setupRecycler() {
        adapter = new AcoesAdapter(new ArrayList<>(), id -> {
            Intent intent = new Intent(getContext(), DetalheAcaoActivity.class);
            intent.putExtra("acao_id", id);
            startActivity(intent);
        });
        binding.recyclerViewAcoes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewAcoes.setAdapter(adapter);
    }

    @Override
    public void showLoading() { /* Opcional: mostrar ProgressBar */ }
    @Override
    public void hideLoading() { /* Opcional */ }

    @Override
    public void mostrarAcoes(List<AcaoSocial> acoes) {
        adapter.setLista(acoes);
    }

    @Override
    public void mostrarVazio() {
        // Mostrar aviso visual se quiser
    }
}