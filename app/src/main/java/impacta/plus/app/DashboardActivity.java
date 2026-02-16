package impacta.plus.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import impacta.plus.app.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment f = null;
            if (item.getItemId() == R.id.navigation_home) f = new HomeFragment();
            else if (item.getItemId() == R.id.navigation_participacoes) f = new HistoricoParticipacoesFragment();
            else if (item.getItemId() == R.id.navigation_perfil) f = new PerfilFragment();

            if (f!= null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                return true;
            }
            return false;
        });

        // Load default
        if (savedInstanceState == null) binding.bottomNavigation.setSelectedItemId(R.id.navigation_home);
    }
}