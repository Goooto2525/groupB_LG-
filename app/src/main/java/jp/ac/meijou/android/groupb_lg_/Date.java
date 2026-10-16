package jp.ac.meijou.android.groupb_lg_;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.content.Intent;

import jp.ac.meijou.android.groupb_lg_.databinding.ActivityDateBinding;

public class Date extends AppCompatActivity {
    private ActivityDateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // findViewById(R.id.main) から binding.getRoot() に変更
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button buttonBack = findViewById(R.id.btnGoToCalendar);
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(Date.this, CalendarActivity_1.class);
            startActivity(intent);
        });

        // HomeActivity へ遷移
        Button buttonHome = findViewById(R.id.btnGoToMain);
        buttonHome.setOnClickListener(v -> {
            Intent intent = new Intent(Date.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}