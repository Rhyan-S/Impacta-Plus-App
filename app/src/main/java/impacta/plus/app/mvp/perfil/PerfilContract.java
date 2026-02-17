package impacta.plus.app.mvp.perfil;

public interface PerfilContract {
    interface View {
        void showLoading();
        void hideLoading();
        void showUserData(String nome, String email, String telefone);
        void showUpdateSuccess();
        void showError(String message);
    }

    interface Presenter {
        void loadUserProfile(int userId);
        void updateUserProfile(int userId, String nome, String telefone);
    }
}