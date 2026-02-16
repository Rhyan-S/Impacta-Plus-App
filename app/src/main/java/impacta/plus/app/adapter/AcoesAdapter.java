package impacta.plus.app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import impacta.plus.app.databinding.ItemAcaoSocialBinding;
import impacta.plus.app.model.AcaoSocial;
import java.util.List;

public class AcoesAdapter extends RecyclerView.Adapter<AcoesAdapter.ViewHolder> {
    private List<AcaoSocial> lista;
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(int id); }

    public AcoesAdapter(List<AcaoSocial> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public void setLista(List<AcaoSocial> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAcaoSocialBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AcaoSocial acao = lista.get(position);
        holder.binding.textViewNomeAcao.setText(acao.getNomeVaga());
        holder.binding.textViewLocalAcao.setText(acao.getLocal());
        holder.binding.textViewDataAcao.setText(acao.getDataInicio());
        holder.binding.textViewDescricaoCurta.setText(acao.getDescricao());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(acao.getId()));
    }

    @Override
    public int getItemCount() { return lista!= null? lista.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAcaoSocialBinding binding;
        public ViewHolder(ItemAcaoSocialBinding b) { super(b.getRoot()); binding = b; }
    }
}