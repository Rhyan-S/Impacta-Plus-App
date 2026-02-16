package impacta.plus.app.mvp.login;

public interface LoginContract {
    interface View {
        void showLoading();
        void hideLoading();
        void showLoginError(String message);
        void navigateToDashboard();
    }
    interface Presenter {
        void login(String email, String senha);
    }
}