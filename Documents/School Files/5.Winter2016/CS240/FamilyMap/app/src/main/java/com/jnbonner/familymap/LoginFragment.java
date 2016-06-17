package com.jnbonner.familymap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.Server.HttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private EditText username;
    private EditText password;
    private EditText serverHost;
    private EditText serverPort;
    private Button signInButton;


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String title) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_TITLE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        username = (EditText)v.findViewById(R.id.editText_Username);
        password = (EditText)v.findViewById(R.id.editText_Password);
        serverHost = (EditText)v.findViewById(R.id.editText_ServerHost);
        serverPort = (EditText)v.findViewById(R.id.editText_ServerPort);
        signInButton = (Button)v.findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (logInPressed(v)){
                    //Toast.makeText(v.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                }
                else if (!logInPressed(v)){
                    //Toast.makeText(v.getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    /** Processes the information that has been entered upon pressing login
     *      Either starts Async task to log in or alerts user to bad information
     */
    boolean logInPressed(View v){

        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        String serverHostString = serverHost.getText().toString();
        int serverPortInt;
        if (usernameString.equals("") || passwordString.equals("")){
            Toast.makeText(v.getContext(), "Please Enter a User Name and Password", Toast.LENGTH_SHORT).show();
        }
        else {

            if (!serverPort.getText().toString().equals("")) {
                serverPortInt = Integer.valueOf(serverPort.getText().toString());
                try {

                    String urlStringBase = "http://" + serverHostString + ":" + String.valueOf(serverPortInt);
                    AllModel.getServerAccess().login(urlStringBase, usernameString, passwordString, this.getContext());

                } catch (IOException e) {
                    Toast.makeText(v.getContext(), "Invalid Server Host", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(v.getContext(), "Invalid Server Port", Toast.LENGTH_SHORT).show();
            }

        }

        return true;
    }

}
