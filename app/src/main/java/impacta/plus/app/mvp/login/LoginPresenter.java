package impacta.plus.app.mvp.login;

import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.utils.AppExecutors;
import impacta.plus.app.utils.SessionManager;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private BancoRepository repository;
    private SessionManager sessionManager;

    public LoginPresenter(LoginContract.View view, BancoRepository repository, SessionManager sessionManager) {
        this.view = view;
        this.repository = repository;
        this.sessionManager = sessionManager;
    }

    @Override
    public void login(String email, String senha) {
        view.showLoading();
        if (email.isEmpty() | senha.isEmpty()) {
            view.hideLoading();
            view.showLoginError("Preencha todos os campos");
            return;
        }

        AppExecutors.getInstance().diskIO().execute(() -> {
            int userId = repository.autenticarUsuario(email, senha);
            AppExecutors.getInstance().mainThread().execute(() -> {
                view.hideLoading();
                if (userId!= -1) {
                    sessionManager.saveUserId(userId);
                    view.navigateToDashboard();
                } else {
                    view.showLoginError("E-mail ou senha incorretos.");
                }
            });
        });
    }
}