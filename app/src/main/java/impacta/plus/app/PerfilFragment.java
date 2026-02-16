package impacta.plus.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PerfilFragment extends Fragment {

    private EditText nomeEditText;
    private EditText emailEditText;
    private EditText telefoneEditText;
    private Button trocarSenhaButton;
    private Button selecionarInteressesButton;
    private Button salvarPerfilButton;

    private BancoController crud;
    private int loggedInUserId = -1; // ID do usuário logado

    public PerfilFragment() {
        // Construtor vazio é necessário para Fragments
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializar componentes da UI
        nomeEditText = view.findViewById(R.id.edit_text_perfil_nome);
        emailEditText = view.findViewById(R.id.edit_text_perfil_email);
        telefoneEditText = view.findViewById(R.id.edit_text_perfil_telefone);
        trocarSenhaButton = view.findViewById(R.id.button_trocar_senha);
        selecionarInteressesButton = view.findViewById(R.id.button_selecionar_interesses);
        salvarPerfilButton = view.findViewById(R.id.button_salvar_perfil);

        crud = new BancoController(getContext());

        // Obter o ID do usuário logado
        if (getContext() != null) {
            SharedPreferences sharedPref = getContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
            loggedInUserId = sharedPref.getInt("logged_in_user_id", -1);
        }

        if (loggedInUserId == -1) {
            Toast.makeText(getContext(), "Erro: Usuário não logado para ver perfil.", Toast.LENGTH_LONG).show();
        } else {
            carregarDadosUsuario(loggedInUserId); // Carrega os dados do perfil
        }

        // Listeners para botões (Trocar senha e Selecionar interesses são apenas visuais)
        trocarSenhaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Funcionalidade de troca de senha em desenvolvimento!", Toast.LENGTH_SHORT).show();
            }
        });

        selecionarInteressesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Funcionalidade de selecionar interesses em desenvolvimento!", Toast.LENGTH_SHORT).show();
            }
        });

        salvarPerfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDadosUsuario(); // Salva as alterações
            }
        });

        return view;
    }

    private void carregarDadosUsuario(int userId) {
        // SQLiteDatabase db = null; // A instância de DB é gerida pelo BancoController agora
        Cursor cursor = null;

        try {
            // Obter os dados do usuário do BancoController
            cursor = crud.carregaUsuarioPorId(userId); // Vamos criar este método no BancoController

            if (cursor != null && cursor.moveToFirst()) {
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.NOME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.EMAIL));
                String telefone = cursor.getString(cursor.getColumnIndexOrThrow(CriaBanco.TELEFONE));

                nomeEditText.setText(nome);
                emailEditText.setText(email);
                telefoneEditText.setText(telefone);
            } else {
                Toast.makeText(getContext(), "Dados do usuário não encontrados.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao carregar dados do usuário: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // O BancoController gerencia o fechamento do DB para o carregaUsuarioPorId
        }
    }

    private void salvarDadosUsuario() {
        String nome = nomeEditText.getText().toString();
        String email = emailEditText.getText().toString(); // Email não é editável, mas pode ser pego para referência
        String telefone = telefoneEditText.getText().toString();

        if (nome.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(getContext(), "Nome e Telefone não podem ser vazios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chamar o método de atualização no BancoController
        String resultado = crud.atualizaUsuario(loggedInUserId, nome, telefone); // Vamos criar este método
        Toast.makeText(getContext(), resultado, Toast.LENGTH_LONG).show();
    }
}
