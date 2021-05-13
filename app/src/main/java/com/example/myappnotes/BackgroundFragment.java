package com.example.myappnotes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;

import com.example.myappnotes.activity.main.MainActivity;
import com.example.myappnotes.methods.BackgroundAdapter;

public class BackgroundFragment extends Fragment {

//    Nhớ vào AndroidManifest để thêm 2 cái uses permission
//    Gần như là viết lại tất cả nên tốt nhất xóa cái BackgroundFragment cũ đi và copy hết cái mới vào lại

    GridView gridView;
    LinearLayout linearLayout;

    View view;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int Background, Background_position;
    Drawable drawable_background;
    private final int PICK_IMAGE = 1;
    Uri imageUri;
    String Background_gallery;
    File file;

    public static final int PERMISSION_CODE = 1001;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ((MainActivity)getActivity()).setActionBarTitle("Background");

        view = inflater.inflate(R.layout.fragment_background, container, false);

        getActivity().setTheme(R.style.AppThemeLogin);

        Anhxa();

        gridView.setAdapter(new BackgroundAdapter(getActivity()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permissions, PERMISSION_CODE);

                        }
                        else
                        {
                            openGallery();

                        }
                    }
                    else
                    {
                        openGallery();
                    }

                }
                else
                {
                    setBackground(position);

                    saveBackground();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        loadBackground();
        updateBackground();

        return view;
    }

    public void Anhxa()
    {
        gridView = view.<GridView>findViewById(R.id.gridview);
        linearLayout = view.<LinearLayout>findViewById(R.id.background);
    }

    public void setBackground(int position)
    {
        switch(position)
        {
            case 1:
                Background_position = R.drawable.background1;
                break;
            case 2:
                Background_position = R.drawable.background2;
                break;
            case 3:
                Background_position = R.drawable.background3;
                break;
            case 4:
                Background_position = R.drawable.background4;
                break;
            case 5:
                Background_position = R.drawable.background5;
                break;
            case 6:
                Background_position = R.drawable.background6;
                break;
            case 7:
                Background_position = R.drawable.background7;
                break;
            case 8:
                Background_position = R.drawable.background8;
                break;
            case 9:
                Background_position = R.drawable.background9;
                break;
            case 10:
                Background_position = R.drawable.background10;
                break;
            case 11:
                Background_position = R.drawable.background11;
                break;
        }
    }

    public void openGallery()
    {

        startActivityForResult(Intent.createChooser(new Intent().
                        setAction(Intent.ACTION_GET_CONTENT).
                        setType("image/*"), "Select background from gallery"),
                PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult)
    {
        switch (requestCode)
        {
            case PERMISSION_CODE:
            {
                if(grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openGallery();
                }
                else
                {
                    Toast.makeText(getActivity(), "Permission denied...!", Toast.LENGTH_SHORT ).show();

                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == PICK_IMAGE)
            {
                imageUri = data.getData();
                file = new File(getRealPathFromURI(imageUri));

                if(file.exists())
                {
                    saveBackground_uri();
                }
            }
        }
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public String getRealPathFromURI(Uri uri)
    {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if(cursor == null)
        {
            return uri.getPath();
        } else
        {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

//    Các hàm dưới để lưu lại hình được chọn trong SharedPreferences, nhằm khi tắt app mở lại,
//    background vẫn là cái mới được chọn, chứ không load lại ban đầu.


    public void saveBackground()
    {
        sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.remove("Background_gallery");

        editor.putInt("Background_int", Background_position);
        editor.commit();

    }

    public void loadBackground()
    {
        sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        Background = sharedPreferences.getInt("Background_int", R.drawable.background1);

        Background_gallery = sharedPreferences.getString("Background_gallery", "");


    }

    public void updateBackground()
    {
        if(Background_gallery == "")
        {
            linearLayout.setBackground(getActivity().getResources().getDrawable(Background));
        }
        else
        {
            drawable_background = Drawable.createFromPath(Background_gallery);
            linearLayout.setBackground(drawable_background);
        }

    }

    public void saveBackground_uri()
    {
        sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString("Background_gallery", file.getAbsolutePath());
        editor.commit();
    }


}
