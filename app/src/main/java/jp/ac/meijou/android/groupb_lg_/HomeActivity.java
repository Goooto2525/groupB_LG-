package jp.ac.meijou.android.groupb_lg_;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private final String[] morningComments = {
            "今日も一日頑張りましょう！",
            "朝ごはんはちゃんと食べましたか？",
            "いい一日になりますように。",
            "今日のタスク、確認しておきましょう。",
            "焦らず、焦らず、ゆっくりね",
            "深呼吸して、気持ちよく始めましょう。"
    };

    private final String[] afternoonComments = {
            "お昼は食べましたか？",
            "少し休憩を挟んでみては？",
            "午後も引き続き頑張りましょう。",
            "水分補給を忘れずに。",
            "眠くなる時間帯ですが、あと少しです。",
            "終わったら最高のご褒美を！"

    };

    private final String[] eveningComments = {
            "今日一日お疲れさまでした。",
            "夕食は何にしますか？",
            "今日の振り返りをしてみましょう。",
            "そろそろリラックスタイムです。",
            "明日の予定を確認しておくと安心です。"
    };

    private final String[] nightComments = {
            "そろそろ休んだ方がいいかもしれません。",
            "夜更かしは体に良くないですよ。",
            "静かな夜、ゆっくり過ごしてください。",
            "明日に備えて、そろそろ休みましょう。",
            "今日も一日お疲れさまでした。ゆっくり休んでください。"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupGreetingHeader();
        setupProgress();
        setupNextTask();
        setupMenu();
    }

    // ヘッダー(挨拶・時刻・コメント・ストリーク)の設定
    private void setupGreetingHeader() {
        TextView textGreeting = findViewById(R.id.textGreeting);
        TextView textTime = findViewById(R.id.textTime);
        TextView textComment = findViewById(R.id.textComment);
        TextView textStreak = findViewById(R.id.textStreak);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.JAPAN);
        String timeString = "現在時刻 " + sdf.format(now);

        String greeting;
        String comment;
        Random random = new Random();

        if (hour >= 5 && hour < 11) {
            greeting = "おはようございます！";
            comment = morningComments[random.nextInt(morningComments.length)];
        } else if (hour >= 11 && hour < 17) {
            greeting = "こんにちは！";
            comment = afternoonComments[random.nextInt(afternoonComments.length)];
        } else if (hour >= 17 && hour < 22) {
            greeting = "こんばんは！";
            comment = eveningComments[random.nextInt(eveningComments.length)];
        } else {
            greeting = "夜遅くまでお疲れさまです";
            comment = nightComments[random.nextInt(nightComments.length)];
        }

        textGreeting.setText(greeting);
        textTime.setText(timeString);
        textComment.setText(comment);

        // ストリーク(連続達成日数)。今は仮の数値。後で保存データと連動させます。
        int streakDays = 5;
        textStreak.setText(streakDays + "日目");
    }

    // 今日のルーティン進捗の設定
    private void setupProgress() {
        ProgressBar progressBar = findViewById(R.id.progressBarRoutine);
        TextView textProgressCount = findViewById(R.id.textProgressCount);

        // 仮のデータ(完了数 / 全体数)。後でToDoリストの実データと連動させます。
        int completed = 0;
        int total = 6;
        int percent = (int) ((completed / (float) total) * 100);

        progressBar.setProgress(percent);
        textProgressCount.setText(completed + " / " + total + " 完了(" + percent + "%)");
    }

    // 次のタスクカードの設定
    private void setupNextTask() {
        TextView textNextTaskName = findViewById(R.id.textNextTaskName);
        TextView textNextTaskTime = findViewById(R.id.textNextTaskTime);

        // 仮のデータ。後でToDoリストから「次の未完了タスク」を取得するようにします。
        textNextTaskName.setText("起床");
        textNextTaskTime.setText("08:00 〜 08:30");

        findViewById(R.id.buttonTimer).setOnClickListener(v ->
                Toast.makeText(this, "集中タイマー", Toast.LENGTH_SHORT).show());

        findViewById(R.id.buttonComplete).setOnClickListener(v ->
                Toast.makeText(this, "タスクを完了", Toast.LENGTH_SHORT).show());
    }

    // メニュー(ショートカット)のクリック処理
    private void setupMenu() {
        LinearLayout menuTodoList = findViewById(R.id.menuTodoList);
        LinearLayout menuCalendar = findViewById(R.id.menuCalendar);
        LinearLayout menuAppLock = findViewById(R.id.menuAppLock);

        menuTodoList.setOnClickListener(v -> {
            Intent intent = new Intent(this, TodoListActivity.class);
            startActivity(intent);
        });

        menuCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        });

        menuAppLock.setOnClickListener(v -> {
            Intent intent = new Intent(this, AppLockActivity.class);
            startActivity(intent);
        });
    }
}
