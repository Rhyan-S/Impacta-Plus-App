package impacta.plus.app.mvp.detalhe;
import impacta.plus.app.model.AcaoSocial;

public interface DetalheContract {
    interface View {
        void mostrarDetalhes(AcaoSocial acao);
        void mostrarErro(String msg);
        void onParticipacaoConfirmada(String nomeAcao);
        void atualizarStatusBotao(boolean participou, boolean vagasEsgotadas);
    }
    interface Presenter {
        void carregarAcao(int id, int userId);
        void participar(int id, int userId);
    }
}