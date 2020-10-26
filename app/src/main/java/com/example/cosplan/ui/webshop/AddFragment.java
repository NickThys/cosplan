package com.example.cosplan.ui.webshop;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cosplan.R;
import com.example.cosplan.data.Webshop;
import com.example.cosplan.data.WebshopViewModel;

public class AddFragment extends Fragment {
    private WebshopViewModel mWebshopViewModel ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_add, container, false);
        mWebshopViewModel= new ViewModelProvider(this).get(WebshopViewModel.class);
        Button btnAdd=view.findViewById(R.id.btn_addToDb);
        final EditText mName= view.findViewById(R.id.WebsiteName);
        final EditText mLink= view.findViewById(R.id.WebsiteLink);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                inserDataToDatabase(mName,mLink,view);
            }
        });

        return view;
    }

    public void inserDataToDatabase(EditText mName,EditText mLink,View view) {

        String name=mName.getText().toString();
        String link= mLink.getText().toString();
        if (inputCheck(name,link)){
            Webshop webshop=new Webshop(name,link);
            mWebshopViewModel.insert(webshop);
            Toast.makeText(requireContext(), R.string.ToastSuccessNewWebshop,Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_addFragment_to_nav_webshop);
        }
        else{
            Toast.makeText(requireContext(), R.string.ToastFailedNewWebshop,Toast.LENGTH_SHORT).show();
        }
        closeKeyboard(view);
    }

    private void closeKeyboard(View view) {
        InputMethodManager imm=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private boolean inputCheck(String name,String link){
        return !(name==null||link==null);
    }

}