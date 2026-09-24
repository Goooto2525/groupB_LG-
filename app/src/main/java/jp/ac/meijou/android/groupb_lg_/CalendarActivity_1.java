package jp.ac.meijou.android.groupb_lg_;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.WeekFields;

public class CalendarActivity_1 extends AppCompatActivity {

    private TextView monthText;
    private TextView selectedDateText;
    private LocalDate today;
    private LocalDate selectedDate;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        today = LocalDate.now();
        selectedDate = today; // 初期状態は今日を選択

        monthText = findViewById(R.id.monthText);
        selectedDateText = findViewById(R.id.selectedDateText);
        calendarView = findViewById(R.id.calendarView);

        // 初期表示の日付テキスト更新
        updateSelectedDateText(selectedDate);
        updateMonthText(YearMonth.now());

        // 1日分のデザインを割り当てるバインダーの設定
        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, CalendarDay day) {
                container.day = day;
                container.textView.setText(String.valueOf(day.getDate().getDayOfMonth()));
                
                // 選択されている日付の見た目 (最優先)
                if (day.getDate().equals(selectedDate)) {
                    container.textView.setTextColor(0xFFFFFFFF); // 文字色白
                    container.textView.setBackgroundResource(R.drawable.selected_day_bg); // 選択中（紫枠 or 塗りつぶし）
                } 
                // 今日（未選択時）の見た目
                else if (day.getDate().equals(today)) {
                    container.textView.setTextColor(0xFF6366F1); // 文字を紫色に
                    container.textView.setBackgroundResource(R.drawable.today_bg_light); // 薄い紫の丸背景
                } 
                // 通常の日付
                else {
                    container.textView.setBackground(null);
                    if (day.getPosition() == DayPosition.MonthDate) {
                        container.textView.setTextColor(0xFF1F2937); // 今月の通常日
                    } else {
                        container.textView.setTextColor(0xFF9CA3AF); // 前後の月の日付
                    }
                }
            }
        });

        // 範囲と曜日の初期化設定 (前後24ヶ月を表示し、スクロール可能に)
        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(24);
        YearMonth endMonth = currentMonth.plusMonths(24);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();

        calendarView.setup(startMonth, endMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);

        // カレンダーをスクロール（スワイプ）したときに、上部の年月テキストも自動更新する
        calendarView.setMonthScrollListener(calendarMonth -> {
            updateMonthText(calendarMonth.getYearMonth());
            return null;
        });

        // ボタンのクリックイベント追加
        findViewById(R.id.todayButton).setOnClickListener(v -> {
            LocalDate oldSelected = selectedDate;
            selectedDate = today;
            updateSelectedDateText(selectedDate);
            calendarView.notifyDateChanged(oldSelected);
            calendarView.notifyDateChanged(today);
            calendarView.smoothScrollToMonth(YearMonth.now());
        });

        ImageButton prevMonthButton = findViewById(R.id.prevMonthButton);
        prevMonthButton.setOnClickListener(v -> {
            YearMonth visibleMonth = calendarView.findFirstVisibleMonth().getYearMonth();
            calendarView.smoothScrollToMonth(visibleMonth.minusMonths(1));
        });

        ImageButton nextMonthButton = findViewById(R.id.nextMonthButton);
        nextMonthButton.setOnClickListener(v -> {
            YearMonth visibleMonth = calendarView.findFirstVisibleMonth().getYearMonth();
            calendarView.smoothScrollToMonth(visibleMonth.plusMonths(1));
        });
    }

    private void updateMonthText(YearMonth yearMonth) {
        String text = yearMonth.getYear() + "年\n" + yearMonth.getMonthValue() + "月";
        if (monthText != null) {
            monthText.setText(text);
        }
    }

    // カレンダー下部の「選択中の日付」テキスト（例: 9月4日 (金)）を更新するメソッド
    private void updateSelectedDateText(LocalDate date) {
        if (selectedDateText != null) {
            String month = String.valueOf(date.getMonthValue());
            String day = String.valueOf(date.getDayOfMonth());
            String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
            
            selectedDateText.setText(month + "月" + day + "日 (" + dayOfWeek + ")");
        }
    }

    // 各日のViewを保持するクラス
    public class DayViewContainer extends ViewContainer {
        public TextView textView;
        public CalendarDay day;

        public DayViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.calendarDayText);
            
            // 日付マスをタップしたときの処理
            view.setOnClickListener(v -> {
                if (day.getPosition() == DayPosition.MonthDate) {
                    LocalDate oldSelectedDate = selectedDate;
                    selectedDate = day.getDate();
                    
                    // 下部のテキストを更新
                    updateSelectedDateText(selectedDate);
                    
                    // 新しく選択された日と、古くなった日のリフレッシュ（色を塗り替える）
                    calendarView.notifyDateChanged(oldSelectedDate);
                    calendarView.notifyDateChanged(selectedDate);
                }
            });
        }
    }
}