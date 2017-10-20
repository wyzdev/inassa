package com.nassagroup.activities;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.nassagroup.R;
import com.nassagroup.tools.UserInfo;

public class UserGuide extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    WebView mwebview;
    UserInfo userInfo;

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

        userInfo = new UserInfo(this);
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
        mwebview.getSettings().setDefaultTextEncodingName("utf-8");

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
            UserGuide.this.mwebview.loadData(page_first_login(), "text/html; charset=UTF-8", null);
        } else if (id == R.id.nav_change_password) {
            UserGuide.this.mwebview.loadData(page_change_password(), "text/html; charset=UTF-8", null);
        } else if (id == R.id.nav_forgot_password) {
            mwebview.loadData(page_forgot_password(), "text/html;UTF-8", null);
        } else if (id == R.id.nav_search_client) {
            mwebview.loadData(page_search_client(), "text/html; charset=UTF-8", null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String page_first_login() {
        return "<html>\n" +
                "<head>\n" +
                "\n" +
                "<meta charset=\"utf-8\"/>\n" +

                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div class=\"tab-pane text-style\">" +
                "<h2>Premi&#232;re connexion</h2>\n" +
                "<p>\n" +
                "<center>\n" +
                "    <iframe width=\"315\" height=\"315\" src=\"https://www.youtube.com/embed/5Rfgh9Ogmko?rel=0&showinfo=0\" frameborder=\"0\" allowfullscreen></iframe>\n" +
                "</center>\n" +
                "</p>\n" +
                "<p>\n" +
                "    Apr&#232;s la r&#233;cup&#233;ration du nom d'utilisateur et du mot de passe re&#231;u par mail, rendez-vous sur la\n" +
                "    page \"Login\". Authentifiez-vous avec votre nom d'utilisateur et votre mot de passe. Lors de la\n" +
                "    redirection, vous serez amen&#233; &#224; choisir un nouveau mot de passe sup&#233;rieur ou &#233;gal &#224; six (6)\n" +
                "    caract&#232;res alpha-num&#233;riques. Confirmez ce mot de passe dans le second champ puis enregistrez\n" +
                "    celui-ci afin d'acc&#233;der &#224; la page d'accueil de l'application. Un message de confirmation\n" +
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
                "<meta charset=\"utf-8\"/>\n" +

                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"tab-pane text-style\">" +
                "<h2>Changer de mot de passe</h2>\n" +
                "<p>\n" +
                "<center>\n" +
                "    <iframe width=\"315\" height=\"315\" src=\"https://www.youtube.com/embed/33ySlAl-tV0?rel=0&showinfo=0\" frameborder=\"0\" allowfullscreen></iframe>\n" +
                "</center>\n" +
                "</p>\n" +
                "<p>\n" +
                "    Afin de garder l’application s&#233;curis&#233;e, il est conseill&#233; de r&#233;guli&#232;rement changer son mot de\n" +
                "    passe. Pour cela, rendez-vous sur le menu principal dans la section « Changer de mot de passe »\n" +
                "    situ&#233; en haut &#224; droite.\n" +
                "    Une boite de dialogue vous permettra de rentrer et confirmer votre nouveau mot de passe devant\n" +
                "    être compos&#233; d’au moins six (6) caract&#232;res alpha-num&#233;riques.\n" +
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
                "<meta charset=\"utf-8\"/>\n" +

                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"tab-pane text-style\">" +
                "<h2>Mot de passe oubli&#233;</h2>\n" +
                "<p>\n" +
                "<center>\n" +
                "    <iframe width=\"315\" height=\"315\" src=\"https://www.youtube.com/embed/AsGFIyiy684?rel=0&showinfo=0\" frameborder=\"0\" allowfullscreen></iframe>\n" +
                "</center>\n" +
                "</p>\n" +
                "<p>\n" +
                "Dans le cas o&#249; vouz oubliez votre mot de passe, cliquez sur \"mot de passe oubli&#233;\" sur la page d'authentification. Indiquez ensuite votre adresse &#233;lectronique puis validez pour recevoir votre nouveau mot de passe par e-mail. Dans le cas o&#249; votre adresse mail ne correspond &#224; aucun utilisateur de la INASSA, un message d'erreur s'affichera.\n" +
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
                "<meta charset=\"utf-8\"/>\n" +

                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div class=\"tab-pane text-style\">" +
                "<h2>Rechercher un client</h2>\n" +
                "<p>\n" +
                "<center>\n" +
                "    <iframe width=\"315\" height=\"315\" src=\"https://www.youtube.com/embed/EQ9oxcIIYfc?rel=0&showinfo=0\" frameborder=\"0\" allowfullscreen></iframe>\n" +
                "</center>\n" +
                "</p>\n" +
                "<p>\n" +
                "    Lorsqu'un assur&#233; se pr&#233;sente, vous pouvez v&#233;rifier le statut de son assurance en faisant une\n" +
                "    recherche client. Pour cela indiquez son nom, son pr&#233;nom et sa date de naissance en utilisant le\n" +
                "    calendrier. Plusieurs r&#233;sultats sont possible.\n" +
                "<ul>\n" +
                "    <li>Si le client est actif, le message \"ACTIF\" s'affichera</li>\n" +
                "    <li>Si le client est inactif, le message \"INACTIF\" s'affichera</li>\n" +
                "    <li>Dans le cas o&#249; les informations indiqu&#233;es ne correspondent &#224; aucun client de la INASSA, une\n" +
                "        bulle \"Client inconnu.\" s'affichera\n" +
                "    </li>\n" +
                "</ul>\n" +
                "</p>\n" +
                "</div>" +
                "</body>\n" +
                "</html>" +
                "";
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    /**
     * Method that creates an option menu
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    /**
     * Method that allows the user to choose an item in the option menu
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                userInfo.setLoggedin(false);
                userInfo.clear();
                startActivity(new Intent(UserGuide.this, LoginActivity.class));
                finish();
                return true;
            case R.id.change_password:
                startActivity(new Intent(UserGuide.this, ChangePasswordActivity.class));
                return true;
            case R.id.user_guide:
                startActivity(new Intent(UserGuide.this, UserGuide.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
