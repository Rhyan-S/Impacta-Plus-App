package impacta.plus.app.mvp.cadastro;

public interface CadastroContract {
    interface View {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void onCadastroSucesso(String email);
    }
    interface Presenter {
        void cadastrar(String nome, String email, String cpf, String telefone, String senha);
    }
}