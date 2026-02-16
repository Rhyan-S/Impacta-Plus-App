package impacta.plus.app.mvp.detalhe;
import impacta.plus.app.data.BancoRepository;
import impacta.plus.app.model.AcaoSocial;
import impacta.plus.app.utils.AppExecutors;

public class DetalhePresenter implements DetalheContract.Presenter {
    private DetalheContract.View view;
    private BancoRepository repo;
    private AcaoSocial acaoAtual;

    public DetalhePresenter(DetalheContract.View view, BancoRepository repo) {
        this.view = view; this.repo = repo;
    }

    @Override
    public void carregarAcao(int id, int userId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            acaoAtual = repo.getAcaoPorId(id);
            boolean jaParticipou = repo.usuarioJaParticipou(userId, id);

            AppExecutors.getInstance().mainThread().execute(() -> {
                if (acaoAtual!= null) {
                    view.mostrarDetalhes(acaoAtual);
                    view.atualizarStatusBotao(jaParticipou, acaoAtual.getVagasDisponiveis() <= 0);
                } else {
                    view.mostrarErro("Ação não encontrada");
                }
            });
        });
    }

    @Override
    public void participar(int id, int userId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            String resultado = repo.participarAcao(userId, id);
            AppExecutors.getInstance().mainThread().execute(() -> {
                if ("Sucesso".equals(resultado)) {
                    view.onParticipacaoConfirmada(acaoAtual.getNomeVaga());
                } else {
                    view.mostrarErro(resultado);
                }
            });
        });
    }
}