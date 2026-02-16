package impacta.plus.app.mvp.home;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.utils.AppExecutors;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private BancoRepository repo;

    public HomePresenter(HomeContract.View view, BancoRepository repo) {
        this.view = view; this.repo = repo;
    }

    @Override
    public void carregarAcoes() {
        view.showLoading();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List list = repo.listarAcoes();
            AppExecutors.getInstance().mainThread().execute(() -> {
                view.hideLoading();
                if (list.isEmpty()) view.mostrarVazio();
                else view.mostrarAcoes(list);
            });
        });
    }
}