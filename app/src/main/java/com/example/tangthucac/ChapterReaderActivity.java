package com.example.tangthucac;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChapterReaderActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextView chapterTitle, chapterContent;
    private Button btnPrev, btnNext, btnTTSSettings, btnPlay, btnStop;
    private Story story;
    private List<Chapter> chapterList;
    private int currentIndex;

    // Biến điều khiển TTS
    private TextToSpeech textToSpeech;
    private boolean isPlaying = false;
    private float currentSpeed = 1.0f;
    private float currentPitch = 1.0f;
    private float currentFontSize = 18f; // Giá trị mặc định cho cỡ chữ
    private Typeface currentTypeface = Typeface.DEFAULT; // Phông chữ mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_reader);

        // Ánh xạ view
        chapterTitle = findViewById(R.id.chapterTitle);
        chapterContent = findViewById(R.id.chapterContent);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnTTSSettings = findViewById(R.id.btnTTSSettings);
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);

        // Khởi tạo TextToSpeech
        textToSpeech = new TextToSpeech(this, this);

        // Nhận dữ liệu từ Intent
        story = (Story) getIntent().getSerializableExtra("story");
        currentIndex = getIntent().getIntExtra("chapterIndex", 0);

        if (story != null) {
            // Chuyển đổi map chapters thành list
            Map<String, Chapter> chaptersMap = story.getChapters();
            chapterList = new ArrayList<>(chaptersMap.values());
            displayChapter(currentIndex);

            // Xử lý nút chuyển chương
            btnPrev.setOnClickListener(v -> {
                if (currentIndex > 0) {
                    currentIndex--;
                    displayChapter(currentIndex);
                    stopSpeaking();
                } else {
                    Toast.makeText(this, "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show();
                }
            });

            btnNext.setOnClickListener(v -> {
                if (currentIndex < chapterList.size() - 1) {
                    currentIndex++;
                    displayChapter(currentIndex);
                    stopSpeaking();
                } else {
                    Toast.makeText(this, "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show();
                }
            });

            // Xử lý nút Play
            btnPlay.setOnClickListener(v -> {
                if (isPlaying) {
                    pauseSpeaking();
                    btnPlay.setText("Đọc");
                } else {
                    startSpeaking();
                    btnPlay.setText("Tạm dừng");
                }
                isPlaying = !isPlaying;
            });

            // Xử lý nút Stop
            btnStop.setOnClickListener(v -> {
                stopSpeaking();
                btnPlay.setText("Đọc");
                isPlaying = false;
            });

            // Xử lý nút TTS Settings
            btnTTSSettings.setOnClickListener(v -> showTTSControlDialog());
        }
    }

    private void showTTSControlDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_tts_control);

        SeekBar speedSeekBar = dialog.findViewById(R.id.seekBarSpeed);
        SeekBar pitchSeekBar = dialog.findViewById(R.id.seekBarPitch);
        SeekBar fontSizeSeekBar = dialog.findViewById(R.id.seekBarFontSize);
        Spinner spinnerFont = dialog.findViewById(R.id.spinnerFont);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        // Thiết lập giá trị hiện tại
        speedSeekBar.setProgress((int)((currentSpeed - 0.5f) * 100));
        pitchSeekBar.setProgress((int)((currentPitch - 0.5f) * 100));
        fontSizeSeekBar.setProgress((int)currentFontSize - 10); // Giả sử cỡ chữ từ 10-40

        // Thiết lập Spinner cho phông chữ
        String[] fontOptions = {"Mặc định", "Serif", "Sans Serif", "Monospace"};
        ArrayAdapter<String> fontAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fontOptions);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFont.setAdapter(fontAdapter);

        // Đặt lựa chọn phông chữ hiện tại
        if (currentTypeface == Typeface.DEFAULT) spinnerFont.setSelection(0);
        else if (currentTypeface == Typeface.SERIF) spinnerFont.setSelection(1);
        else if (currentTypeface == Typeface.SANS_SERIF) spinnerFont.setSelection(2);
        else if (currentTypeface == Typeface.MONOSPACE) spinnerFont.setSelection(3);

        // Xử lý thay đổi tốc độ
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentSpeed = 0.5f + (progress / 100f);
                if (textToSpeech != null) {
                    textToSpeech.setSpeechRate(currentSpeed);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Xử lý thay đổi cao độ
        pitchSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentPitch = 0.5f + (progress / 100f);
                if (textToSpeech != null) {
                    textToSpeech.setPitch(currentPitch);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Xử lý thay đổi cỡ chữ
        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentFontSize = progress + 10; // Cỡ chữ từ 10sp đến 40sp
                chapterContent.setTextSize(currentFontSize);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Xử lý thay đổi phông chữ
        spinnerFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        currentTypeface = Typeface.DEFAULT;
                        break;
                    case 1:
                        currentTypeface = Typeface.SERIF;
                        break;
                    case 2:
                        currentTypeface = Typeface.SANS_SERIF;
                        break;
                    case 3:
                        currentTypeface = Typeface.MONOSPACE;
                        break;
                }
                chapterContent.setTypeface(currentTypeface);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý nút đóng
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void startSpeaking() {
        if (textToSpeech != null) {
            textToSpeech.setSpeechRate(currentSpeed);
            textToSpeech.setPitch(currentPitch);
            String text = chapterList.get(currentIndex).getContent();
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void pauseSpeaking() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    private void stopSpeaking() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    private void displayChapter(int index) {
        Chapter chapter = chapterList.get(index);
        chapterTitle.setText(chapter.getTitle());
        chapterContent.setText(chapter.getContent());
        chapterContent.setTextSize(currentFontSize); // Áp dụng cỡ chữ hiện tại
        chapterContent.setTypeface(currentTypeface); // Áp dụng phông chữ hiện tại
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(new Locale("vi", "VN"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Ngôn ngữ không được hỗ trợ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Khởi tạo TextToSpeech thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}