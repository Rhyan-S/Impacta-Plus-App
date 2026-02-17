package impacta.plus.app.mvp.perfil;

import android.database.Cursor;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.data.CriaBanco;
import impacta.plus.app.utils.AppExecutors;

public class PerfilPresenter implements PerfilContract.Presenter {

    private final PerfilContract.View view;
    private final BancoRepository repository;

    public PerfilPresenter(PerfilContract.View view, BancoRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadUserProfile(int userId) {
        view.showLoading();
        AppExecutors.getInstance().diskIO().execute(() -> {
            try (Cursor cursor = repository.getDadosUsuario(userId)) {
                if (cursor!= null && cursor.moveToFirst()) {
                    // Extraindo dados do Cursor de forma segura
                    String nome = cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.NOME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.EMAIL));
                    String telefone = cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.TELEFONE));

                    // Atualizando a UI na Thread Principal
                    AppExecutors.getInstance().mainThread().execute(() -> {
                        view.hideLoading();
                        view.showUserData(nome, email, telefone);
                    });
                } else {
                    notifyError("Usuário não encontrado.");
                }
            } catch (Exception e) {
                notifyError("Erro ao carregar perfil: " + e.getMessage());
            }
        });
    }

    @Override
    public void updateUserProfile(int userId, String nome, String telefone) {
        if (nome.isEmpty() | telefone.isEmpty()) {
            view.showError("Nome e Telefone são obrigatórios.");
            return;
        }

        view.showLoading();
        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean sucesso = repository.atualizarUsuario(userId, nome, telefone);
            AppExecutors.getInstance().mainThread().execute(() -> {
                view.hideLoading();
                if (sucesso) {
                    view.showUpdateSuccess();
                } else {
                    view.showError("Erro ao atualizar perfil.");
                }
            });
        });
    }

    private void notifyError(String msg) {
        AppExecutors.getInstance().mainThread().execute(() -> {
            view.hideLoading();
            view.showError(msg);
        });
    }
}