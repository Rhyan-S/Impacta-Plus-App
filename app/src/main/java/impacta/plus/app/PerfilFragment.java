package impacta.plus.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.databinding.FragmentPerfilBinding;
import impacta.plus.app.mvp.perfil.PerfilContract;
import impacta.plus.app.mvp.perfil.PerfilPresenter;
import impacta.plus.app.utils.SessionManager;

public class PerfilFragment extends Fragment implements PerfilContract.View {

    private FragmentPerfilBinding binding;
    private PerfilContract.Presenter presenter;
    private int loggedInUserId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inicializar Dependências
        SessionManager session = new SessionManager(requireContext());
        loggedInUserId = session.getUserId();
        presenter = new PerfilPresenter(this, new BancoRepository(requireContext()));

        // 2. Carregar dados se o usuário estiver logado
        if (loggedInUserId!= -1) {
            presenter.loadUserProfile(loggedInUserId);
        } else {
            showError("Erro de sessão. Faça login novamente.");
        }

        // 3. Configurar Botões
        binding.buttonSalvarPerfil.setOnClickListener(v -> {
            String nome = binding.editTextPerfilNome.getText().toString();
            String telefone = binding.editTextPerfilTelefone.getText().toString();
            presenter.updateUserProfile(loggedInUserId, nome, telefone);
        });

        binding.buttonTrocarSenha.setOnClickListener(v ->
                Toast.makeText(getContext(), "Funcionalidade de troca de senha em breve!", Toast.LENGTH_SHORT).show()
        );

        binding.buttonSelecionarInteresses.setOnClickListener(v ->
                Toast.makeText(getContext(), "Seleção de interesses em breve!", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void showLoading() {
        // Se quiser adicionar uma ProgressBar no layout depois, controle ela aqui
        binding.buttonSalvarPerfil.setEnabled(false);
        binding.buttonSalvarPerfil.setText("Salvando...");
    }

    @Override
    public void hideLoading() {
        binding.buttonSalvarPerfil.setEnabled(true);
        binding.buttonSalvarPerfil.setText("Salvar Alterações");
    }

    @Override
    public void showUserData(String nome, String email, String telefone) {
        binding.editTextPerfilNome.setText(nome);
        binding.editTextPerfilEmail.setText(email);
        binding.editTextPerfilTelefone.setText(telefone);

        // O e-mail geralmente é fixo/chave de login, então desabilitamos a edição visualmente se quiser
        // binding.editTextPerfilEmail.setEnabled(false);
    }

    @Override
    public void showUpdateSuccess() {
        Toast.makeText(getContext(), "Perfil atualizado com sucesso!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evita vazamento de memória no ViewBinding
    }
}