package impacta.plus.app.model;

public class AcaoSocial {
    private int id;
    private String nomeVaga;
    private String local;
    private String dataInicio;
    private String descricao;
    private int vagasDisponiveis;
    private String imagemUrl;
    private String impacto;

    public AcaoSocial(int id, String nomeVaga, String local, String dataInicio, String descricao, int vagasDisponiveis, String imagemUrl, String impacto) {
        this.id = id;
        this.nomeVaga = nomeVaga;
        this.local = local;
        this.dataInicio = dataInicio;
        this.descricao = descricao;
        this.vagasDisponiveis = vagasDisponiveis;
        this.imagemUrl = imagemUrl;
        this.impacto = impacto;
    }

    // Getters
    public int getId() { return id; }
    public String getNomeVaga() { return nomeVaga; }
    public String getLocal() { return local; }
    public String getDataInicio() { return dataInicio; }
    public String getDescricao() { return descricao; }
    public int getVagasDisponiveis() { return vagasDisponiveis; }
    public String getImagemUrl() { return imagemUrl; }
    public String getImpacto() { return impacto; }
}