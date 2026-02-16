package impacta.plus.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import impacta.plus.app.utils.SecurityUtils;

public class CriaBanco extends SQLiteOpenHelper {
    private static CriaBanco sInstance;
    private static final String NOME_BANCO = "impacta.db";
    private static final int VERSAO = 4;

    // Tabelas e Colunas (Constantes Públicas)
    public static final String TABELA_USUARIOS = "usuarios";
    public static final String ID = "_id";
    public static final String NOME = "nome";
    public static final String EMAIL = "email";
    public static final String CPF = "cpf";
    public static final String TELEFONE = "telefone";
    public static final String SENHA = "senha";

    public static final String TABELA_ACOES_SOCIAIS = "acoes_sociais";
    public static final String ACAO_ID = "_id";
    public static final String ACAO_NOME_VAGA = "nome_vaga";
    public static final String ACAO_LOCAL = "local";
    public static final String ACAO_DATA_INICIO = "data_inicio";
    public static final String ACAO_DATA_FINAL = "data_final";
    public static final String ACAO_DESCRICAO = "descricao_detalhada";
    public static final String ACAO_TIPO_ATIVIDADE = "tipo_atividade";
    public static final String ACAO_STATUS = "status_vaga";
    public static final String ACAO_VAGAS_DISPONIVEIS = "vagas_disponiveis";
    public static final String ACAO_IMPACTO = "impacto";
    public static final String ACAO_IMAGEM_URL = "imagem_url";

    public static final String TABELA_PARTICIPACOES = "participacoes";
    public static final String PART_ID = "_id";
    public static final String PART_ID_USUARIO = "id_usuario";
    public static final String PART_ID_ACAO = "id_acao";
    public static final String PART_DATA_PARTICIPACAO = "data_participacao";

    public static synchronized CriaBanco getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CriaBanco(context.getApplicationContext());
        }
        return sInstance;
    }

    private CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela Usuários
        db.execSQL("CREATE TABLE " + TABELA_USUARIOS + "(" + ID + " integer primary key autoincrement," + NOME + " text not null," + EMAIL + " text not null unique," + CPF + " text unique," + TELEFONE + " text," + SENHA + " text not null" + ")");

        // Admin padrão com senha hash
        String adminPass = SecurityUtils.hashPassword("123456");
        db.execSQL("INSERT INTO " + TABELA_USUARIOS + " (" + NOME + ", " + EMAIL + ", " + SENHA + ") VALUES ('Administrador', 'admin@impacta.com', '" + adminPass + "');");

        // Tabela Ações
        db.execSQL("CREATE TABLE " + TABELA_ACOES_SOCIAIS + "(" + ACAO_ID + " integer primary key autoincrement," + ACAO_NOME_VAGA + " text not null," + ACAO_LOCAL + " text not null," + ACAO_DATA_INICIO + " text not null," + ACAO_DATA_FINAL + " text," + ACAO_DESCRICAO + " text not null," + ACAO_TIPO_ATIVIDADE + " text," + ACAO_STATUS + " text not null," + ACAO_VAGAS_DISPONIVEIS + " integer," + ACAO_IMPACTO + " text," + ACAO_IMAGEM_URL + " text" + ")");

        // Inserts Ações (Simplificado para brevidade)
        db.execSQL("INSERT INTO " + TABELA_ACOES_SOCIAIS + " VALUES (null, 'Campanha do Agasalho', 'Centro A', '10/05/2024', null, 'Coleta de agasalhos', 'Social', 'Disponível', 50, 'Médio', null);");
        db.execSQL("INSERT INTO " + TABELA_ACOES_SOCIAIS + " VALUES (null, 'Reforço Escolar', 'Associação B', '15/05/2024', null, 'Ensino para crianças', 'Educação', 'Disponível', 20, 'Alto', null);");

        // Tabela Participações
        db.execSQL("CREATE TABLE " + TABELA_PARTICIPACOES + "(" + PART_ID + " integer primary key autoincrement," + PART_ID_USUARIO + " integer not null," + PART_ID_ACAO + " integer not null," + PART_DATA_PARTICIPACAO + " text not null," + "FOREIGN KEY(" + PART_ID_USUARIO + ") REFERENCES " + TABELA_USUARIOS + "(" + ID + ")," + "FOREIGN KEY(" + PART_ID_ACAO + ") REFERENCES " + TABELA_ACOES_SOCIAIS + "(" + ACAO_ID + ")" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ACOES_SOCIAIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PARTICIPACOES);
        onCreate(db);
    }
}