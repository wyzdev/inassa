package com.nassagroup.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nassagroup.APIInterfaces.RetrofitInterfaces;
import com.nassagroup.App;
import com.nassagroup.R;
import com.nassagroup.RetrofitClientInstance;
import com.nassagroup.core.Benefits;
import com.nassagroup.core.CheckLogin;
import com.nassagroup.core.Client;
import com.nassagroup.core.Historic;
import com.nassagroup.core.KeyRequestUser;
import com.nassagroup.core.PolicyExtension;
import com.nassagroup.core.SearchClient;
import com.nassagroup.core.SearchRequest;
import com.nassagroup.core.Signout;
import com.nassagroup.tools.Constants;
import com.nassagroup.tools.LogOutTimerTask;
import com.nassagroup.tools.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A screen that allows the user to search a client
 */
public class SearchClientActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;

    //    private static final int MY_SOCKET_TIMEOUT_MS = 30000;
    UserInfo userInfo;
    int current_date;
    TextView institution;
    String key;
    private Calendar calendar;
    private int year, month, day;
    private EditText editText_birthdate, editText_firstname, editText_lastname;
    ImageButton imagebutton_datepicker;
    String hero_name;
    int employee_id;
    String firstname_client;
    AlertDialog alertDialog;

    String firstname_str, lastname_str, dob_str;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(SearchClientActivity.this, HomeActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_search:
                    startActivity(new Intent(SearchClientActivity.this, SearchClientActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.navigation_user_guide:
                    startActivity(new Intent(SearchClientActivity.this, UserGuide.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        }

    };
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Rechercher un client");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);
        imagebutton_datepicker = (ImageButton) findViewById(R.id.search_client_imagebutton_calendar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        userInfo = new UserInfo(this);

        institution = (TextView) findViewById(R.id.institution);
        editText_birthdate = (EditText) findViewById(R.id.search_client_edittext_birthdate_client);
        editText_birthdate.setEnabled(false);

        institution.setText(userInfo.getUserFirstname() + "\n" + userInfo.getUserInstitution());

        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        current_date = Integer.parseInt(dateFormat.format(date));

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        alertDialog = new AlertDialog.Builder(SearchClientActivity.this).create();

        editText_firstname = (EditText) findViewById(R.id.search_firstname);
        editText_lastname = (EditText) findViewById(R.id.search_lastname);
        Button button_search = (Button) findViewById(R.id.search_button);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname_str = editText_firstname.getText().toString();
                lastname_str = editText_lastname.getText().toString();
                dob_str = editText_birthdate.getText().toString();

                if (!firstname_str.isEmpty() && !lastname_str.isEmpty() && !dob_str.isEmpty())
                    checkForSearch(firstname_str.toUpperCase(), lastname_str.toUpperCase(), dob_str);

            }
        });


        imagebutton_datepicker.setOnClickListener(this);
    }

    public void checkCanLogin(final String firstname_str, final String lastname_str, final String dob_str) {
        progressDialog.setMessage("Patientez s'il vous plait ...");
        progressDialog.show();

        RetrofitInterfaces service = RetrofitClientInstance.getClientForInassapp().create(RetrofitInterfaces.class);
        Call<CheckLogin> call = service.canLogin(userInfo.getUserId());
        call.enqueue(new Callback<CheckLogin>() {
            @Override
            public void onResponse(Call<CheckLogin> call, retrofit2.Response<CheckLogin> response) {
                CheckLogin checkLogin = response.body();
                if (checkLogin != null) {
                    if (checkLogin.can) {
                        progressDialog.dismiss();
                        //authAPI(firstname_str.toUpperCase(), lastname_str.toUpperCase(), dob_str);
                        proceedSearchClient(firstname_str.toUpperCase(), lastname_str.toUpperCase(), dob_str);
//                        checkForSearch(firstname_str.toUpperCase(), lastname_str.toUpperCase(), dob_str);
                    } else {
                        dialogError();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckLogin> call, Throwable t) {
                progressDialog.dismiss();
                dialogError();
//                Toast.makeText(SearchClientActivity.this, "Aucun client de correspond a vos critères de recherche.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void dialogError() {
        new AlertDialog.Builder(SearchClientActivity.this)
                .setTitle("Erreur")
                .setMessage(this.getResources().getString(R.string.server_problem))
//                            .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        ProgressDialog progressDialog = new ProgressDialog(SearchClientActivity.this);
                        progressDialog.setTitle("Déconnexion");
                        progressDialog.setMessage("Patientez s'il vous plait ...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        // logout
                        logout(progressDialog);
                        //Toast.makeText(SearchClientActivity.this, "Déconnexion", Toast.LENGTH_SHORT).show();
                    }
                })
                //.setNegativeButton(android.R.string.no, null)
                .show();
    }

    /**
     * Method that creates an option menu
     *
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
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Déconnexion");
                progressDialog.setMessage("Patientez s'il vous plait ...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                logout(progressDialog);
                return true;
            case R.id.change_password:
                startActivity(new Intent(SearchClientActivity.this, ChangePasswordActivity.class));
                return true;
            case R.id.user_guide:
                startActivity(new Intent(SearchClientActivity.this, UserGuide.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void proceedSearchClient(final String firstname, final String lastname, final String dob) {
        progressDialog.setTitle("Recherche client en cours");
        progressDialog.setMessage("Patientez s'il vous plait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Thread thread = new Thread() {
            @Override
            public void run() {
                RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClientForInassapp().create(RetrofitInterfaces.class);
                Call<SearchClient> call = retrofitInterfaces.searchClient(firstname, lastname, dob, userInfo.getUserId(), Constants.TOKEN);
                call.enqueue(new Callback<SearchClient>() {
                    @Override
                    public void onResponse(Call<SearchClient> call, retrofit2.Response<SearchClient> response) {

                        final SearchClient searchClient = response.body();
                        if (searchClient != null) {
                            List<Client> clients;
                            if (!searchClient.isSucces()) {
                                clients = searchClient.getClients();
                                if (clients.isEmpty()) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            alertDialog.setTitle("Info");
                                            alertDialog.setMessage(searchClient.getErrorMessage());
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                    });

                                    return;
                                }

                                if (!searchClient.isSucces() && clients.size() >= 1) {
                                    if (clients.size() == 1) {
                                        employee_id = (int) searchClient.getClients().get(0).getEmployeeId();
                                        firstname_client = searchClient.getClients().get(0).getFirstname();

                                        Gson gson = new Gson();
                                        String json = gson.toJson(searchClient);
                                        // print

                                        saveInLogs(json);
                                    } else if (clients.size() > 1) {

                                        Gson gson = new Gson();
                                        final String json = gson.toJson(searchClient);
                                        userInfo.setKey(key);
                                        // list
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(SearchClientActivity.this, ListClient.class);
                                                intent.putExtra("jsonObject", json);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(SearchClientActivity.this, InfoClientActivity.class);
                                                Bundle extras = new Bundle();
                                                extras.putString("info_client", "");
                                                // trois extras de plus "nom", "prenom", "date de naissance
                                                extras.putString("firstname", firstname_str);
                                                extras.putString("lastname", lastname_str);
                                                extras.putString("dob", dob_str);
                                                intent.putExtras(extras);
                                                intent.putExtra("info_client", "");
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    }
                                } else if (!searchClient.isSucces() && clients.size() == 0) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(SearchClientActivity.this, InfoClientActivity.class);
                                            Bundle extras = new Bundle();
                                            extras.putString("info_client", "");
                                            // trois extras de plus "nom", "prenom", "date de naissance
                                            extras.putString("firstname", firstname_str);
                                            extras.putString("lastname", lastname_str);
                                            extras.putString("dob", dob_str);
                                            intent.putExtras(extras);
                                            intent.putExtra("info_client", "");
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                }
                            }

                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SearchClientActivity.this, "Ne peut pas trouver le client.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }


                    }

                    @Override
                    public void onFailure(Call<SearchClient> call, Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SearchClientActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });

                    }

                });
            }
        };
        thread.start();
    }


    public void checkForSearch(final String firstname, final String lastname, final String dob) {
        progressDialog.setTitle("Recherche client en cours");
        progressDialog.setMessage("Patientez s'il vous plait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        long diff = 0L;
        boolean posponed = false;

        if (App.getInstance().getKeyRequestUser().getDate() != null) {
            diff = new Date().getTime() - App.getInstance().getKeyRequestUser().getDate().getTime();
            if (TimeUnit.DAYS.convert(diff, TimeUnit.MINUTES) > 4) {
                App.getInstance().requestKeyInBackground();
                posponed = true;
            }
        } else {
            KeyRequestUser user = new KeyRequestUser();
            user.setUsername();
            user.setPassword();
            App.getInstance().setKeyRequestUser(user);
            App.getInstance().requestKeyInBackground();
            posponed = true;
        }

        if (posponed) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchForClientInDirectAPI(firstname, lastname, dob);
                }
            }, 4000);
        } else {
            searchForClientInDirectAPI(firstname, lastname, dob);
        }
    }

    public void searchForClientInDirectAPI(final String firstname, final String lastname, final String dob) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                SearchRequest searchRequest = new SearchRequest();
                searchRequest.setDob(dob);
                searchRequest.setFirst_name(firstname);
                searchRequest.setLast_name(lastname);
                searchRequest.setKeyRequestUser(App.getInstance().getKeyRequestUser());
                RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getDirectAPIConnexion().create(RetrofitInterfaces.class);
                Call<SearchClient> call = retrofitInterfaces.directsearch(searchRequest);
                call.enqueue(new Callback<SearchClient>() {
                    @Override
                    public void onResponse(Call<SearchClient> call, Response<SearchClient> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                final SearchClient searchClient = response.body();
                                    List<Client> clients = searchClient.getClients();
                                    if (clients != null) {
                                        if (clients.size() > 0) {
                                            if (clients.size() == 1) {
                                                //OneCLient
                                                employee_id = (int) searchClient.getClients().get(0).getEmployeeId();
                                                firstname_client = searchClient.getClients().get(0).getFirstname();
                                                Gson gson = new Gson();
                                                String json = gson.toJson(searchClient);
                                                saveInLogs(json);
                                            } else {
                                                //ManyCLient
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Gson gson = new Gson();
                                                        final String json = gson.toJson(searchClient);
                                                        userInfo.setKey(key);
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(SearchClientActivity.this, ListClient.class);
                                                        intent.putExtra("jsonObject", json);
                                                        startActivity(intent);
//                                                finish();
                                                    }
                                                });
                                            }
                                        } else {
                                            //Aucun resultat
                                            openRequestNull();
//                                            requestfailed("Aucun résultat");
                                        }
                                    } else {
                                        //Aucun resultat
                                        requestfailed("Aucun résultat");
                                    }

                            } else {
                                //Requete non aboutie
                                requestfailed("Requète non aboutie");
                            }
                        } else {
                            //Requete non aboutie
                            requestfailed("Requète non aboutie");
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchClient> call, Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SearchClientActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
            }
        };
        thread.start();
    }


    public void requestfailed(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                alertDialog.setTitle("Info");
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    public void openRequestNull(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(SearchClientActivity.this, "Ne peut pas trouver le client.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Intent intent = new Intent(SearchClientActivity.this, InfoClientActivity.class);
                Bundle extras = new Bundle();
                extras.putString("info_client", "");
                // trois extras de plus "nom", "prenom", "date de naissance
                extras.putString("firstname", firstname_str);
                extras.putString("lastname", lastname_str);
                extras.putString("dob", dob_str);
                intent.putExtras(extras);
                intent.putExtra("info_client", "");
                startActivity(intent);
                finish();
            }
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void proceedGetBenefits(final ProgressDialog progressDialog, final int employee_id,
                                   final String firstname, final String info_client) {

        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClient().create(RetrofitInterfaces.class);

//        Toast.makeText(SearchClientActivity.this, key +
//                "\n" + employee_id +
//                "\n" + firstname, Toast.LENGTH_SHORT).show();

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse("{\"resquestkey\":{\"key\":\"" + key + " \"},\"employee_id\": " + employee_id + ",\"first_name\": \"" + firstname + "\"}").getAsJsonObject();

        Call<Benefits> call = retrofitInterfaces.postRawGetBenefits(o);
        call.enqueue(new Callback<Benefits>() {
            @Override
            public void onResponse(Call<Benefits> call, retrofit2.Response<Benefits> response) {
                progressDialog.dismiss();
                //Toast.makeText(SearchClientActivity.this, response.body().isSuccess() + "", Toast.LENGTH_LONG).show();
                Benefits Benefits = response.body();
                List<PolicyExtension> policyExtensions = Benefits.getPolicyExtensions();

                if (policyExtensions != null) {
                    int pos_extension_hero = posExtensionHero(policyExtensions);
                    if (pos_extension_hero != -1) {
                        hero_name = policyExtensions.get(pos_extension_hero).getNameForDisplay();
                    } else {
                        hero_name = "N/A";
                    }

                    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    final Date date = new Date();

                    JSONObject obj;
                    String firstname = "";
                    String lastname = "";
                    boolean status = false;
                    String dob = "";
                    DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    try {
                        obj = new JSONObject(info_client);
                        if (obj.getBoolean("success") && obj.getJSONArray("clients").length() > 0) {

                            obj = (JSONObject) obj.getJSONArray("clients").get(0);
                            firstname = obj.getString("first_name");
                            lastname = obj.getString("last_name");
                            status = obj.getBoolean("status");
                            Date birthdate = originalFormat.parse(obj.getString("dob"));
                            dob = targetFormat.format(birthdate);


                            int status_int = (status) ? 1 : 0;
                            int is_dependant_int = (obj.getLong("primary_employee_id") != obj.getLong("employee_id")) ? 1 : 0;

                            // Toast.makeText(SearchClientActivity.this, userInfo.getUserFirstname() + " " + userInfo.getUserLastname(), Toast.LENGTH_SHORT).show();
                            proceedSaveInHistoric(info_client,
                                    firstname,
                                    lastname,
                                    userInfo.getUserFirstname() + " " + userInfo.getUserLastname(),
                                    status_int,
                                    dateFormat.format(date),
                                    dob,
                                    userInfo.getUserInstitution(),
                                    Constants.TOKEN,
                                    obj.getInt("employee_id"),
                                    is_dependant_int,
                                    hero_name,
                                    obj.getString("primary_name"),
                                    userInfo.getUserId()
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Benefits> call, Throwable t) {
                progressDialog.dismiss();

            }

        });
//        return hero_name;
    }

    public void proceedSaveInHistoric(final String info_client,
                                      String first_name,
                                      String last_name,
                                      String doctor_name,
                                      int status,
                                      String date,
                                      String dob,
                                      String institution,
                                      String token,
                                      int employee_id,
                                      int is_dependant,
                                      String hero,
                                      String primary_name,
                                      int user_id) {
        if (is_dependant == 0)
            primary_name = "-";

        /*Create handle for the RetrofitInstance interface*/
        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClientForInassapp().create(RetrofitInterfaces.class);
        Call<Historic> call = retrofitInterfaces
                .saveInHistoric(first_name,
                        last_name,
                        doctor_name,
                        status,
                        date,
                        dob,
                        institution,
                        token,
                        employee_id,
                        is_dependant,
                        hero,
                        employee_id + " - " + primary_name,
                        user_id);
        call.enqueue(new Callback<Historic>() {
            @Override
            public void onResponse(Call<Historic> call, retrofit2.Response<Historic> response) {
                //progressDialog.dismiss();
                Historic historic = response.body();
                if (historic != null) {
                    if (historic.error) {
                        Toast.makeText(SearchClientActivity.this, "Erreur d'enregistrement", Toast.LENGTH_LONG).show();

                        return;
                    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    Intent intent = new Intent(SearchClientActivity.this, InfoClientActivity.class);
                    Bundle extras = new Bundle();

                    extras.putString("info_client", info_client);
                    extras.putString("hero_name", hero_name);
                    intent.putExtras(extras);


                    startActivity(intent);
                    editText_birthdate.setText("");
                    editText_firstname.setText("");
                    editText_lastname.setText("");

                    editText_lastname.requestFocus();
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                } else {
                    Toast.makeText(SearchClientActivity.this, "Client inconnu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Historic> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(SearchClientActivity.this, "Something went wrong...Please try later!",
                        Toast.LENGTH_LONG).show();

            }

        });
    }

    private int posExtensionHero(List<PolicyExtension> list_extensions) {
        for (int i = 0; i < list_extensions.size(); i++) {
            String extension = list_extensions.get(i).getExtension().toLowerCase();
            if (extension.contains("hero")) {
                return i;
            }
        }
        return -1;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Method that saves the client's information retrieved and doctor's full name
     * and institution in online database.
     *
     * @param info_client
     */
    private void saveInLogs(final String info_client) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setTitle("Enregistrement ...");
                progressDialog.setMessage("Patientez s'il vous plait");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.i("client_123", info_client);
                //proceedGetBenefits(progressDialog, employee_id, firstname_client, info_client);
                Intent intent = new Intent(SearchClientActivity.this, InfoClientActivity.class);
                Bundle extras = new Bundle();
                extras.putString("info_client", info_client);
                progressDialog.hide();
                intent.putExtras(extras);
                startActivity(intent);
//                editText_birthdate.setText("");
//                editText_firstname.setText("");
//                editText_lastname.setText("");
//                editText_lastname.requestFocus();
            }
        });

    }

    /**
     * Method that logs the user out when the day is changed
     */
    @Override
    protected void onResume() {

        if (userInfo.getUserFirstLogin()) {
            startActivity(new Intent(SearchClientActivity.this, ChangePasswordActivity.class));
            finish();
        }

        if (userInfo.getCurrentDate() != current_date) {
            userInfo.clear();
            userInfo.setLoggedin(false);
            startActivity(new Intent(SearchClientActivity.this, LoginActivity.class));
            finish();
        }
        super.onResume();

        if (timer != null) {
            timer.cancel();
            Log.i("Main", "Search cancel timer");
            timer = null;
        }
    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        String mm = (month < 10) ? "0" + String.valueOf(month) : String.valueOf(month);
        String dd = (day < 10) ? "0" + String.valueOf(day) : String.valueOf(day);
        String yyyy = String.valueOf(year);
        String date = mm + "/" + dd + "/" + yyyy;

        editText_birthdate.setText(date);
    }


    @Override
    public void onBackPressed() {

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

        timer = new Timer();
        Log.i("Main", "Search Invoking logout timer");
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask(SearchClientActivity.this);
        timer.schedule(logoutTimeTask, 600000); //auto logout in 10 minutes
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search_client_imagebutton_calendar:
                showDialog(999);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
            Log.i("Main", "Search cancel timer");
            timer = null;
        }
    }

    public void logout(final ProgressDialog progressDialog) {
        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClientForInassapp().create(RetrofitInterfaces.class);
        Call<Signout> call = retrofitInterfaces.signout(userInfo.getUserId());
        call.enqueue(new Callback<Signout>() {
            @Override
            public void onResponse(Call<Signout> call, retrofit2.Response<Signout> response) {
                progressDialog.dismiss();
                Signout signout = response.body();
                if (signout.error) {
                    Toast.makeText(SearchClientActivity.this, "Vous ne pouvez pas vous deconnecter",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                userInfo.setLoggedin(false);
                userInfo.clear();
                startActivity(new Intent(SearchClientActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<Signout> call, Throwable t) {
                progressDialog.dismiss();
            }

        });
    }
}
