package com.inassa.inassa.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.inassa.inassa.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserGuide extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    WebView mwebview;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(UserGuide.this, HomeActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(UserGuide.this, SearchClientActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_user_guide:
                    startActivity(new Intent(UserGuide.this, UserGuide.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_guide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mwebview = (WebView) findViewById(R.id.webview);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mwebview.getSettings().setJavaScriptEnabled(true);
        mwebview.getSettings().setAppCacheEnabled(true);
        mwebview.getSettings().setDomStorageEnabled(true);

        mwebview.getSettings().setPluginState(WebSettings.PluginState.ON);

        mwebview.loadData(page_first_login(), "text/html", "UTF-8");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this, SearchClientActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_first_login) {
            UserGuide.this.mwebview.loadData(page_first_login(), "text/html", "UTF-8");
        } else if (id == R.id.nav_change_password) {
            UserGuide.this.mwebview.loadData(page_change_password(), "text/html", "UTF-8");
        } else if (id == R.id.nav_forgot_password) {
            mwebview.loadData(page_forgot_password(), "text/html", "UTF-8");
        } else if (id == R.id.nav_search_client) {
            mwebview.loadData(page_search_client(), "text/html", "UTF-8");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String page_first_login() {
        return "<html>\n" +
                "<head>\n" +
                "\n" +
                "<meta charset=\"utf-8\"\n" +

                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div class=\"tab-pane text-style\">" +
                "<h2>Première connexion</h2>\n" +
                "<p>\n" +
                "<center>\n" +
                "    <iframe width=\"315\" height=\"315\" src=\"https://www.youtube.com/embed/5Rfgh9Ogmko?list=PLLQguOJMJ8uynL22T8DoRN1QlIGmTN9Y0\" frameborder=\"0\" allowfullscreen></iframe>\n" +
                "</center>\n" +
                "</p>\n" +
                "<p>\n" +
                "    Après la récupération du nom d'utilisateur et du mot de passe reçu par mail, rendez-vous sur la\n" +
                "    page \"Login\". Authentifiez-vous avec votre nom d'utilisateur et votre mot de passe. Lors de la\n" +
                "    redirection, vous serez ammené à choisir un nouveau mot de passe supérieur ou égal à six (6)\n" +
                "    caractères alpha-numériques. Confirmez ce mot de passe dans le second champ puis enregistrez\n" +
                "    celui-ci afin d'accéder à la page d'accueil de l'application. Un message de confirmation\n" +
                "    s'affichera.\n" +
                "</p>\n" +
                "</div>" +
                "</body>\n" +
                "</html>" +
                "";
    }

    private String page_change_password() {
        return "<html>\n" +
                "<head>\n" +
                "\n" +
                "<meta charset=\"utf-8\"\n" +

                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"tab-pane text-style\">" +
                "<h2>Changer de mot de passe</h2>\n" +
                "<p>\n" +
                "<center>\n" +
                "    <iframe width=\"315\" height=\"315\" src=\"https://www.youtube.com/embed/33ySlAl-tV0?list=PLLQguOJMJ8uynL22T8DoRN1QlIGmTN9Y0\" frameborder=\"0\" allowfullscreen></iframe>\n" +
                "</center>\n" +
                "</p>\n" +
                "<p>\n" +
                "    Afin de garder l’application sécurisée, il est conseillé de régulièrement changer son mot de\n" +
                "    passe. Pour cela, rendez-vous sur le menu principal dans la section « Changer de mot de passe »\n" +
                "    situé en haut à droite.\n" +
                "    Une boite de dialogue vous permettra de rentrer et confirmer votre nouveau mot de passe devant\n" +
                "    être composé d’au moins six (6) caractères alpha-numériques.\n" +
                "    Cliquez ensuite sur \"Enregistrer\" pour valider la saisie de votre nouveau mot de passe\n" +
                "</p>\n" +
                "</div>" +
                "</body>\n" +
                "</html>" +
                "";
    }

    private String page_forgot_password() {
        return "<html>\n" +
                "<head>\n" +
                "\n" +
                "<meta charset=\"utf-8\"\n" +

                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"tab-pane text-style\">" +
                "<h2>Mot de passe oublié</h2>\n" +
                "<p>\n" +
                "<center>\n" +
                "    <iframe width=\"315\" height=\"315\" src=\"https://www.youtube.com/embed/AsGFIyiy684?list=PLLQguOJMJ8uynL22T8DoRN1QlIGmTN9Y0\" frameborder=\"0\" allowfullscreen></iframe>\n" +
                "</center>\n" +
                "</p>\n" +
                "<p>\n" +
                "    Dans le cas où vouz oubliez votre mot de passe, cliquez sur « mot de passe oublié » sur la page\n" +
                "    d’authentification.\n" +
                "    Indiquez ensuite votre adresse électronique puis validez pour recevoir votre nouveau mot de\n" +
                "    passe par e-mail.\n" +
                "    Dans le cas où votre addresse mail ne correspond à aucun utilisateur de la inassa, un message\n" +
                "    d’erreur s'affichera.\n" +
                "</p>\n" +
                "</div>" +
                "</body>\n" +
                "</html>" +
                "";
    }

    private String page_search_client() {
        return "<html>\n" +
                "<head>\n" +
                "\n" +
                "<meta charset=\"utf-8\"\n" +

                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div class=\"tab-pane text-style\">" +
                "<h2>Rechercher un client</h2>\n" +
                "<p>\n" +
                "<center>\n" +
                "    <iframe width=\"315\" height=\"315\" src=\"https://www.youtube.com/embed/EQ9oxcIIYfc?list=PLLQguOJMJ8uynL22T8DoRN1QlIGmTN9Y0\" frameborder=\"0\" allowfullscreen></iframe>\n" +
                "</center>\n" +
                "</p>\n" +
                "<p>\n" +
                "    Lorsqu'un assuré se présente, vous pouvez vérifiez le status de son assurance en faisant une\n" +
                "    recherche client. Pour cela indiquez son nom, son prénom et sa date de naissance en utilisant le\n" +
                "    calendrier. Plusieurs résultats sont possible.\n" +
                "<ul>\n" +
                "    <li>Si le client est actif, le message \"ACTIF\" s'affichera</li>\n" +
                "    <li>Si le client est inactif, le message \"INACTIF\" s'affichera</li>\n" +
                "    <li>Dans le cas où les informations indiquées ne correspondent à aucun client de la INASSA, une\n" +
                "        bulle \"Client iconnu.\" s'affichera\n" +
                "    </li>\n" +
                "</ul>\n" +
                "</p>\n" +
                "</div>" +
                "</body>\n" +
                "</html>" +
                "";
    }

    private final List mBlockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_UP));

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mBlockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }


}
