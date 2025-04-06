package com.example.tangthucac;

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
import java.util.Locale;

public class TTSControlDialog extends AppCompatActivity {
    private SeekBar seekBarSpeed, seekBarPitch, seekBarFontSize;
    private Spinner spinnerFont;
    private Button btnClose;
    private TextToSpeech textToSpeech;
    private float currentSpeed = 1.0f;
    private float currentPitch = 1.0f;
    private float currentFontSize = 18f; // Giá trị mặc định
    private String[] fontOptions = {"Mặc định", "Serif", "Sans Serif", "Monospace"};
    private Typeface currentTypeface = Typeface.DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tts_control);

        seekBarSpeed = findViewById(R.id.seekBarSpeed);
        seekBarPitch = findViewById(R.id.seekBarPitch);
        seekBarFontSize = findViewById(R.id.seekBarFontSize);
        spinnerFont = findViewById(R.id.spinnerFont);
        btnClose = findViewById(R.id.btnClose);

        // Khởi tạo TextToSpeech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("vi", "VN"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Ngôn ngữ không được hỗ trợ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Khởi tạo TextToSpeech thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        // Thiết lập giá trị mặc định
        seekBarSpeed.setProgress((int)((currentSpeed - 0.5f) * 100));
        seekBarPitch.setProgress((int)((currentPitch - 0.5f) * 100));
        seekBarFontSize.setProgress((int)currentFontSize);

        // Thiết lập Spinner cho phông chữ
        ArrayAdapter<String> fontAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fontOptions);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFont.setAdapter(fontAdapter);

        // Xử lý thay đổi tốc độ
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        seekBarPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        seekBarFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentFontSize = progress + 10; // Giá trị từ 10sp đến 40sp
                // Áp dụng cỡ chữ cho chapterContent trong ChapterReaderActivity
                if (getIntent().hasExtra("chapterContentView")) {
                    TextView chapterContent = findViewById(R.id.chapterContent);
                    if (chapterContent != null) {
                        chapterContent.setTextSize(currentFontSize);
                    }
                }
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
                // Áp dụng phông chữ cho chapterContent trong ChapterReaderActivity
                if (getIntent().hasExtra("chapterContentView")) {
                    TextView chapterContent = findViewById(R.id.chapterContent);
                    if (chapterContent != null) {
                        chapterContent.setTypeface(currentTypeface);
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý nút đóng
        btnClose.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    // Phương thức để lấy giá trị hiện tại (dành cho ChapterReaderActivity)
    public float getCurrentFontSize() {
        return currentFontSize;
    }

    public Typeface getCurrentTypeface() {
        return currentTypeface;
    }
}