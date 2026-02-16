package impacta.plus.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import impacta.plus.app.adapter.AcoesAdapter;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.databinding.FragmentHistoricoParticipacoesBinding;
import impacta.plus.app.utils.AppExecutors;
import impacta.plus.app.utils.SessionManager;

public class HistoricoParticipacoesFragment extends Fragment {
    private FragmentHistoricoParticipacoesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoricoParticipacoesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int userId = new SessionManager(getContext()).getUserId();

        AppExecutors.getInstance().diskIO().execute(() -> {
            var lista = new BancoRepository(getContext()).getHistorico(userId);
            AppExecutors.getInstance().mainThread().execute(() -> {
                AcoesAdapter adapter = new AcoesAdapter(lista, id -> {
                    Intent intent = new Intent(getContext(), DetalheAcaoActivity.class);
                    intent.putExtra("acao_id", id);
                    startActivity(intent);
                });
                binding.recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerViewHistorico.setAdapter(adapter);
            });
        });
    }
}