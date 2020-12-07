package com.example.cosplan.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplan.R;
import com.example.cosplan.data.cosplay.Cosplay;
import com.example.cosplan.data.cosplay.CosplayAdapter;
import com.example.cosplan.data.cosplay.CosplayViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.getInteger;

public class CosplayFragment extends Fragment {
    private CosplayViewModel cosplayViewModel;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    private EditText mCosplayName, mCosplayStartDate, mCosplayEndDate, mCosplayBudget;
    private ImageView mCosplayImage;
    private Button mChoosePicture, mCancel, mAddNewCosplay;
    private FloatingActionButton mfabAddCosplay;

    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;

    public static final int GALLERY_REQUEST_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cosplay, container, false);
        final CosplayAdapter cosplayAdapter = new CosplayAdapter(requireContext());
        RecyclerView recyclerView = root.findViewById(R.id.RecView_Cosplay);
        recyclerView.setAdapter(cosplayAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position=viewHolder.getAdapterPosition();
                Cosplay myCosplay=cosplayAdapter.getCosplayAtPosition(position);
                DeleteCosplayDialog(myCosplay);
            }
        });

        helper.attachToRecyclerView(recyclerView);
        cosplayViewModel = new ViewModelProvider(this).get(CosplayViewModel.class);
        cosplayViewModel.getAllConventions().observe(getViewLifecycleOwner(), new Observer<List<Cosplay>>() {
            @Override
            public void onChanged(List<Cosplay> cosplays) {
                cosplayAdapter.setCosplays(cosplays);
            }
        });
        //Add the FAB to go to the add cosplay popup
        mfabAddCosplay = root.findViewById(R.id.Fab_CosplayAdd);
        mfabAddCosplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCosplayDialog();
            }
        });

        return root;
    }


    public void DeleteCosplayDialog(final Cosplay cosplay){
        dialogBuilder=new AlertDialog.Builder(requireContext());
        final View deleteCosplayView=getLayoutInflater().inflate(R.layout.delete,null);
        TextView mDeleteText=deleteCosplayView.findViewById(R.id.TextView_DeleteTitle);
        mDeleteText.setText(getString(R.string.ConformationDeleteCosplay)+cosplay.mCosplayName);
        Button yes,no;
        no=deleteCosplayView.findViewById(R.id.Btn_DeleteCancel);
        yes=deleteCosplayView.findViewById(R.id.Btn_DeleteDelete);
        dialogBuilder.setView(deleteCosplayView);
        dialog=dialogBuilder.create();
        dialog.show();
        yes.setOnClickListener(new View.OnClickListener()        {
            @Override
            public void onClick(View v) {
               cosplayViewModel.delete(cosplay);
                Toast.makeText(requireContext(), cosplay.mCosplayName+ " deleted",Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().recreate();
            }
        });
    }
    public void createNewCosplayDialog() {
        dialogBuilder = new AlertDialog.Builder(requireContext());
        final View cosplayPopUpView = getLayoutInflater().inflate(R.layout.add_cosplay, null);
        mCosplayName = cosplayPopUpView.findViewById(R.id.EditText_NewCosplayName);
        mCosplayStartDate = cosplayPopUpView.findViewById(R.id.EditText_NewCosplayBeginDate);
        mCosplayEndDate = cosplayPopUpView.findViewById(R.id.EditText_NewCosplayEndDate);
        mCosplayBudget = cosplayPopUpView.findViewById(R.id.EditText_NewCosplayBudget);
        mCosplayImage = cosplayPopUpView.findViewById(R.id.ImageView_NewCosplayImgPreview);
        mChoosePicture = cosplayPopUpView.findViewById(R.id.Btn_NewCosplayChooseImg);
        mCancel = cosplayPopUpView.findViewById(R.id.Btn_NewCosplayCancel);
        mAddNewCosplay = cosplayPopUpView.findViewById(R.id.Btn_NewCosplayAdd);
        dialogBuilder.setView(cosplayPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        //create dateSelector and add the selected date to the Edit text
        mCosplayStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if (hasFocus){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    int year;
                    int month;
                    int day;
                    String mtemp = mCosplayStartDate.getText().toString().trim();
                   if (!checkDateFormat(mtemp)) {
                        Calendar calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                    } else {
                        String mDateComlete = mCosplayStartDate.getText().toString();
                        String[] mDate = mDateComlete.split("/");
                        day = Integer.parseInt(mDate[0].trim());
                        month = Integer.parseInt(mDate[1].trim());
                        year = Integer.parseInt(mDate[2].trim());
                        month = month - 1;
                    }

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, mStartDateSetListener, year, month, day);
                    datePickerDialog.getDatePicker().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
                }
            }
        });
        mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                mCosplayStartDate.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };

        //create dateSelector and add the selected date to the Edit text
        mCosplayEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int year;
                    int month;
                    int day;
                    String mtemp = mCosplayEndDate.getText().toString().trim();
                    if (!checkDateFormat(mtemp)) {
                        Calendar calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                    } else {

                        String mDateComlete = mCosplayEndDate.getText().toString();
                        String[] mDate = mDateComlete.split("/");
                        day = Integer.parseInt(mDate[0].trim());
                        month = Integer.parseInt(mDate[1].trim());
                        year = Integer.parseInt(mDate[2].trim());
                        month = month - 1;
                    }

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, mEndDateSetListener, year, month, day);
                    datePickerDialog.getDatePicker().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
                }
            }
        });

        mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                mCosplayEndDate.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };

        //Cancel. dismiss the popup
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Choose the picture from the gallery
        mChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.txt_chooseImg_intent)), GALLERY_REQUEST_CODE);

            }
        });

        //Add Copslay to the database
        mAddNewCosplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCosplayName.getText().toString().equals("")){
                    double mCost;
                    if (!mCosplayBudget.getText().toString().equals("")) {
                        mCost = Double.parseDouble(mCosplayBudget.getText().toString());
                    } else {
                        mCost = 0.0;
                    }
                    Cosplay temp = new Cosplay(0,mCosplayName.getText().toString(),mCosplayStartDate.getText().toString(),mCosplayEndDate.getText().toString(),mCost,mCost,((BitmapDrawable)mCosplayImage.getDrawable()).getBitmap());

                    cosplayViewModel.insert(temp);
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(requireContext(), getResources().getString(R.string.FillOutAllFields), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public Boolean checkDateFormat(String date){
        if (date == null || !date.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"))
            return false;
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        try {
            format.parse(date);
            return true;
        }catch (ParseException e){
            return false;
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                Uri imageData = data.getData();
                mCosplayImage.setImageURI(imageData);
            }
        }
    }
}