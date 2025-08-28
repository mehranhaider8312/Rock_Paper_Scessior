package com.example.rps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DevelporContactScreen extends AppCompatActivity {

    ImageView ivCall, ivMail, ivCallArrow, ivMailArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_develpor_contact_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivCallArrow = findViewById(R.id.ivPhoneArrow);
        ivMailArrow = findViewById(R.id.ivMailArrow);

        View.OnClickListener callClickListener = v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:03360654125"));
            startActivity(intent);
        };

        ivCallArrow.setOnClickListener(callClickListener);

        View.OnClickListener mailClickListener = v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:mehranhaider1786@gmail.com"));
            startActivity(intent);
        };
        ivMailArrow.setOnClickListener(mailClickListener);
    }
}
