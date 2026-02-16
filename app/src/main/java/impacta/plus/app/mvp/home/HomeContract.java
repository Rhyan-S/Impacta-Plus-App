package impacta.plus.app.mvp.home;
import impacta.plus.app.model.AcaoSocial;
import java.util.List;

public interface HomeContract {
    interface View {
        void showLoading();
        void hideLoading();
        void mostrarAcoes(List<AcaoSocial> acoes);
        void mostrarVazio();
    }
    interface Presenter { void carregarAcoes(); }
}