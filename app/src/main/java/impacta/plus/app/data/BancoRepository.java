package impacta.plus.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import impacta.plus.app.model.AcaoSocial;
import impacta.plus.app.utils.SecurityUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BancoRepository {
    private CriaBanco dbHelper;

    public BancoRepository(Context context) {
        this.dbHelper = CriaBanco.getInstance(context);
    }

    // Login: Retorna ID ou -1
    public int autenticarUsuario(String email, String senha) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String senhaHash = SecurityUtils.hashPassword(senha);

        String selection = CriaBanco.EMAIL + " =? AND " + CriaBanco.SENHA + " =?";
        String[] args = {email, senhaHash};

        try (Cursor cursor = db.query(CriaBanco.TABELA_USUARIOS, new String[]{CriaBanco.ID}, selection, args, null, null, null)) {
            if (cursor!= null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        }
        return -1;
    }

    // Cadastro
    public long cadastrarUsuario(String nome, String email, String cpf, String telefone, String senha) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CriaBanco.NOME, nome);
        values.put(CriaBanco.EMAIL, email);
        values.put(CriaBanco.CPF, cpf);
        values.put(CriaBanco.TELEFONE, telefone);
        values.put(CriaBanco.SENHA, SecurityUtils.hashPassword(senha));
        return db.insert(CriaBanco.TABELA_USUARIOS, null, values);
    }

    // Listar Ações
    public List<AcaoSocial> listarAcoes() {
        List<AcaoSocial> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(CriaBanco.TABELA_ACOES_SOCIAIS, null, CriaBanco.ACAO_STATUS + " =?", new String[]{"Disponível"}, null, null, null)) {
            if (cursor!= null && cursor.moveToFirst()) {
                do {
                    lista.add(cursorToAcao(cursor));
                } while (cursor.moveToNext());
            }
        }
        return lista;
    }

    // Detalhe Ação
    public AcaoSocial getAcaoPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(CriaBanco.TABELA_ACOES_SOCIAIS, null, CriaBanco.ACAO_ID + " =?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor!= null && cursor.moveToFirst()) {
                return cursorToAcao(cursor);
            }
        }
        return null;
    }

    // Verificar Participação
    public boolean usuarioJaParticipou(int userId, int acaoId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String where = CriaBanco.PART_ID_USUARIO + " =? AND " + CriaBanco.PART_ID_ACAO + " =?";
        String[] args = {String.valueOf(userId), String.valueOf(acaoId)}; // Array simples

        // CORRIGIDO: Adicionado em new String
        try (Cursor cursor = db.query(CriaBanco.TABELA_PARTICIPACOES, new String[]{CriaBanco.PART_ID}, where, args, null, null, null)) {
            return cursor!= null && cursor.moveToFirst();
        }
    }

    // Participar
    public String participarAcao(int userId, int acaoId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Verifica Vagas
            try (Cursor c = db.query(CriaBanco.TABELA_ACOES_SOCIAIS, new String[]{CriaBanco.ACAO_VAGAS_DISPONIVEIS}, CriaBanco.ACAO_ID + " =?", new String[]{String.valueOf(acaoId)}, null, null, null)) {
                if (c!= null && c.moveToFirst()) {
                    int vagas = c.getInt(0);
                    if (vagas <= 0) return "Vagas Esgotadas";
                } else {
                    return "Ação não encontrada";
                }
            }

            // Insere Participação
            ContentValues partValues = new ContentValues();
            partValues.put(CriaBanco.PART_ID_USUARIO, userId);
            partValues.put(CriaBanco.PART_ID_ACAO, acaoId);
            partValues.put(CriaBanco.PART_DATA_PARTICIPACAO, new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
            db.insert(CriaBanco.TABELA_PARTICIPACOES, null, partValues);

            // Decrementa Vaga
            db.execSQL("UPDATE " + CriaBanco.TABELA_ACOES_SOCIAIS + " SET " + CriaBanco.ACAO_VAGAS_DISPONIVEIS + " = " + CriaBanco.ACAO_VAGAS_DISPONIVEIS + " - 1 WHERE " + CriaBanco.ACAO_ID + " = " + acaoId);

            db.setTransactionSuccessful();
            return "Sucesso";
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        } finally {
            db.endTransaction();
        }
    }

    // Perfil
    public Cursor getDadosUsuario(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(CriaBanco.TABELA_USUARIOS, null, CriaBanco.ID + " =?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    public boolean atualizarUsuario(int userId, String nome, String telefone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CriaBanco.NOME, nome);
        values.put(CriaBanco.TELEFONE, telefone);
        return db.update(CriaBanco.TABELA_USUARIOS, values, CriaBanco.ID + " =?", new String[]{String.valueOf(userId)}) > 0;
    }

    // Histórico
    public List<AcaoSocial> getHistorico(int userId) {
        List<AcaoSocial> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT a.* FROM " + CriaBanco.TABELA_ACOES_SOCIAIS + " a INNER JOIN " + CriaBanco.TABELA_PARTICIPACOES + " p ON a." + CriaBanco.ACAO_ID + " = p." + CriaBanco.PART_ID_ACAO + " WHERE p." + CriaBanco.PART_ID_USUARIO + " =?";

        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)})) {
            if (cursor!= null && cursor.moveToFirst()) {
                do {
                    lista.add(cursorToAcao(cursor));
                } while (cursor.moveToNext());
            }
        }
        return lista;
    }

    private AcaoSocial cursorToAcao(Cursor cursor) {
        return new AcaoSocial(
                cursor.getInt(cursor.getColumnIndexOrThrow(CriaBanco.ACAO_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.ACAO_NOME_VAGA)),
                cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.ACAO_LOCAL)),
                cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.ACAO_DATA_INICIO)),
                cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.ACAO_DESCRICAO)),
                cursor.getInt(cursor.getColumnIndexOrThrow(CriaBanco.ACAO_VAGAS_DISPONIVEIS)),
                cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.ACAO_IMAGEM_URL)),
                cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.ACAO_IMPACTO))
        );
    }
}