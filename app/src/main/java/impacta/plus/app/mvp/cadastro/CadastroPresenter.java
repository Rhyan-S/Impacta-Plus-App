package impacta.plus.app.mvp.cadastro;

import android.util.Patterns;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.utils.AppExecutors;

public class CadastroPresenter implements CadastroContract.Presenter {
    private CadastroContract.View view;
    private BancoRepository repository;

    public CadastroPresenter(CadastroContract.View view, BancoRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void cadastrar(String nome, String email, String cpf, String telefone, String senha) {
        if (nome.isEmpty() | email.isEmpty() | senha.isEmpty()) {
            view.showError("Preencha os campos obrigatórios");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showError("E-mail inválido");
            return;
        }

        view.showLoading();
        AppExecutors.getInstance().diskIO().execute(() -> {
            long result = repository.cadastrarUsuario(nome, email, cpf, telefone, senha);
            AppExecutors.getInstance().mainThread().execute(() -> {
                view.hideLoading();
                if (result!= -1) {
                    view.onCadastroSucesso(email);
                } else {
                    view.showError("Erro ao cadastrar. E-mail já existe?");
                }
            });
        });
    }
}