package com.example.myappnotes.activity.main;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myappnotes.R;
import com.example.myappnotes.SessionManager;
import com.example.myappnotes.activity.Editor.EditorActivity;
import com.example.myappnotes.model.AddFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    private static final int INTENT_ADD = 100;
    private static final int INTENT_EDIT = 200;
    private static String URL_READ = "http://mydiary2021.atwebpages.com/read_detail.php";

    FloatingActionButton fab;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    Intent intent;
    TextView nav_user_name,nav_user_email;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    String getId;
    int id_Us;
    int Background_int;
    String name,email,photo,id;
    ImageView imgProfile;
    String Background_gallery;
    Drawable drawable_background;
    CoordinatorLayout coordinatorLayout;
    private static final String TAG = MainActivity.class.getSimpleName();

//    MainPresenter presenter;
//    MainAdapter adapter;
//    MainAdapter.ItemClickListener itemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //naviga
        coordinatorLayout = this.<CoordinatorLayout>findViewById(R.id.fragment_container);
        loadBackground();
        updateBackground();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        id_Us = (getId!=null&&getId!="")?Integer.parseInt(getId):0;

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        View headview = navigationView.getHeaderView(0);
        nav_user_email = headview.<TextView>findViewById(R.id.nav_user_email);
        nav_user_name = headview.<TextView>findViewById(R.id.nav_user_name);


        //
        intent=getIntent();
        email=intent.getStringExtra("email");
        name=intent.getStringExtra("name");
//        photo=intent.getStringExtra("photo");

        id=intent.getStringExtra("id");
        //
        nav_user_name.setText(name);
        nav_user_email.setText(email);

        getUserDetail();



        imgProfile = headview.<ImageView>findViewById(R.id.nav_user_photo);


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        swipeRefresh = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.add);
        fab.setOnClickListener(view ->
                startActivityForResult(
                        new Intent(this, EditorActivity.class),
                        INTENT_ADD)
        );
//        presenter = new MainPresenter(this);
//        presenter.getData();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AddFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_mainview);
        }
    }

    public void loadBackground()
    {
        sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        Background_int = sharedPreferences.getInt("Background_int", R.drawable.background1);
        Background_gallery = sharedPreferences.getString("Background_gallery", "");
    }

    public void updateBackground()
    {
        if(Background_gallery == "")
        {
            coordinatorLayout.setBackground(getResources().getDrawable(Background_int));
        } else
        {
            drawable_background = Drawable.createFromPath(Background_gallery);
            coordinatorLayout.setBackground(drawable_background);
        }
    }
    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")){

                                for (int i=0; i< jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("name").trim();
                                    String strEmail= object.getString("email").trim();
//                                    String strPhoto= object.getString("photo");

                                    nav_user_name.setText(strName);
                                    nav_user_email.setText(strEmail);


                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Error Reading Detail"+ e.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error Reading Detail"+ error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void setActionBarTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }
}