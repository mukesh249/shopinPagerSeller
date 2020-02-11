package shopinpager.wingstud.shopinpagerseller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.WaitingBinding;

public class Waiting extends AppCompatActivity {

    WaitingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.waiting);

        binding.toolbar.imvNotification.setVisibility(View.GONE);
        binding.toolbar.switchActive.setVisibility(View.GONE);
        binding.toolbar.bellRL.setVisibility(View.GONE);
        binding.toolbar.activityTitle.setText(getResources().getString(R.string.waiting));
        initialize();
    }

    private void initialize() {
        setSupportActionBar( binding.toolbar.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.toolbar.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white));
        binding.toolbar.toolbar.setNavigationOnClickListener(v -> finish());

    }
}
