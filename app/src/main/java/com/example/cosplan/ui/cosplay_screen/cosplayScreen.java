package com.example.cosplan.ui.cosplay_screen;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplan.R;

import com.example.cosplan.data.Coplay.Cosplay;
import com.example.cosplan.data.Coplay.CosplayAdapter;
import com.example.cosplan.data.Coplay.CosplayViewModel;
import com.example.cosplan.data.Coplay.Part.Part;
import com.example.cosplan.data.Coplay.Part.PartAdapter;
import com.example.cosplan.data.Coplay.Part.PartViewModel;
import com.example.cosplan.data.Coplay.RefImg.RefenceImgAdapter;
import com.example.cosplan.data.Coplay.RefImg.ReferenceImg;
import com.example.cosplan.data.Coplay.RefImg.ReferenceImgViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class cosplayScreen extends Fragment implements AdapterView.OnItemSelectedListener {

    private CosplayViewModel cosplayViewModel;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private CosplayAdapter cosplayAdapter;
    private PartViewModel partViewModel;
    private ReferenceImgViewModel referenceImgViewModel;

    TextView mName, mStartDate, mEndDate, mPercentage, mBudget;
    ImageView mImage;
    ImageButton mUpdateCosplay;
    private EditText mCosplayName, mCosplayStartDate, mCosplayEndDate, mCosplayBudget;
    private EditText mPartName, mPartLink, mPartCost, mPartEndDate,mCosplayNote;
    private Spinner mPartmakeBuy;
    private ImageView mPartImage;
    private Button mPartChooseImage, mPartCancel, mPartAddPart,mCosplayNotesSave,mRefImgAdd;
    private FloatingActionButton mfabAddPart;
    private GridView mRefImgGridView;
    private ImageView mCosplayImage;
    private Button mChoosePicture, mCancel, mUpdateCosplays, mCosplayParts, mCosplayNotes, mCosplayRefPic, mCosplayWIPPic, mCosplayChecklist, mCosplayShoppinglist, mCosplayWebshop, mCosplayEvents, mBtnTest;

    public static final int GALLERY_REQUEST_CODE_PART = 2;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;
    public static final int GALLERY_REQUEST_CODE = 1;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cosplay_screen, container, false);
        //Views of all the fragments
        final View PartsView=inflater.inflate(R.layout.cosplay_screen_parts,container,false);
        final View RefImgView=inflater.inflate(R.layout.cosplay_screen_ref_img,container,false);
        final View ShoppingListView=inflater.inflate(R.layout.cosplay_screen_shopping_list,container,false);
        final View NotesView = inflater.inflate(R.layout.cosplay_screen_notes, container, false);
        final View CheckListView=inflater.inflate(R.layout.cosplay_screen_checklist,container,false);
        final View WipImgView=inflater.inflate(R.layout.cosplay_screen_wip_img,container,false);
        final View EventsView=inflater.inflate(R.layout.cosplay_screen_events,container,false);
        final View WebshopsView=inflater.inflate(R.layout.cosplay_screen_webshops,container,false);

        final PartAdapter partAdapterMake = new PartAdapter(requireContext());
        final PartAdapter partAdapterBuy = new PartAdapter(requireContext());
        cosplayViewModel = new ViewModelProvider(this).get(CosplayViewModel.class);
        final Cosplay tempCosplay = cosplayScreenArgs.fromBundle(getArguments()).getCurrentCosplay();
        final ViewGroup fl = v.findViewById(R.id.inlcude3);
        //Initial view for the framelayout
        fl.addView(PartsView);
        cosplayAdapter = new CosplayAdapter(requireContext());

        //Items from the header
        mName = v.findViewById(R.id.CosScreenName);
        mEndDate = v.findViewById(R.id.CosScreenEndDate);
        mPercentage = v.findViewById(R.id.CosScreenPercentage);
        mBudget = v.findViewById(R.id.CosScreenBudget);
        mImage = v.findViewById(R.id.CosScreenImg);
        mUpdateCosplay = v.findViewById(R.id.CosScreenUpdate);
        //Items from the button bar
        mCosplayParts = v.findViewById(R.id.btn_cosplayparts);
        mCosplayNotes = v.findViewById(R.id.btn_cosplayNotes);
        mCosplayRefPic = v.findViewById(R.id.btn_cosplayReferencePic);
        mCosplayWIPPic = v.findViewById(R.id.btn_cosplayWIPImage);
        mCosplayChecklist = v.findViewById(R.id.btn_cosplayChecklist);
        mCosplayShoppinglist = v.findViewById(R.id.btn_cosplayShoppinnglist);
        mCosplayWebshop = v.findViewById(R.id.btn_cosplayWebshops);
        mCosplayEvents = v.findViewById(R.id.btn_cosplayEvents);
        //Items from the Notes
        mCosplayNote=NotesView.findViewById(R.id.EditTest_CosplayNote);
        mCosplayNotesSave=NotesView.findViewById(R.id.btn_CosplayNote_Save);
        //Items from the Ref Img
        mRefImgGridView=RefImgView.findViewById(R.id.GridView_RefImg);
        mRefImgAdd=RefImgView.findViewById(R.id.btn_addRefImg);

        //Header
        //Adding text to the items from the header
        mName.setText(tempCosplay.mCosplayName);
        mEndDate.setText(tempCosplay.mCosplayEndDate);
        mBudget.setText(Double.toString(tempCosplay.mCosplayBudget));
        mImage.setImageBitmap(tempCosplay.mCosplayIMG);
        mPercentage.setText("% complete");
        mfabAddPart = v.findViewById(R.id.fabAddPart);

        //onclick listener from the header
        mUpdateCosplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateCosplayDialog(tempCosplay);
            }
        });

        //Button Bar
        //onclick listener from the buttons
        mCosplayParts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl.removeAllViews();
                fl.addView(PartsView);
            }
        });
        mCosplayRefPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl.removeAllViews();
                fl.addView(RefImgView);
                mRefImgGridView.setAdapter(new RefenceImgAdapter(requireContext()));
            }
        });
        mCosplayShoppinglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl.removeAllViews();
                fl.addView(ShoppingListView);
            }
        });
        mCosplayNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl.removeAllViews();
                fl.addView(NotesView);

            }
        });
        mCosplayChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl.removeAllViews();
                fl.addView(CheckListView);
            }
        });
        mCosplayWIPPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl.removeAllViews();
                fl.addView(WipImgView);
            }
        });
        mCosplayEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl.removeAllViews();
                fl.addView(EventsView);
            }
        });
        mCosplayWebshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl.removeAllViews();
                fl.addView(WebshopsView);
            }
        });

        //Part View
        //recyclerview Make
        RecyclerView recyclerViewMake = v.findViewById(R.id.RecViewMake);
        recyclerViewMake.setAdapter(partAdapterMake);
        recyclerViewMake.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewMake.setLayoutManager(new LinearLayoutManager(requireContext()));
        ItemTouchHelper helperMake = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // TODO: 11/11/2020 Add delete on swipe
            }
        });
        helperMake.attachToRecyclerView(recyclerViewMake);
        partViewModel = new ViewModelProvider(this).get(PartViewModel.class);
        partViewModel.getAllPartsToMake(tempCosplay.mCosplayId).observe(getViewLifecycleOwner(), new Observer<List<Part>>() {
            @Override
            public void onChanged(List<Part> parts) {
                partAdapterMake.setParts(parts);
            }
        });
        //recyclerview buy
        final RecyclerView recyclerViewBuy = v.findViewById(R.id.RecViewBuy);
        recyclerViewBuy.setAdapter(partAdapterBuy);
        recyclerViewBuy.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewBuy.setLayoutManager(new LinearLayoutManager(requireContext()));
        ItemTouchHelper helperBuy = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // TODO: 11/11/2020 Add delete on swipe
            }
        });
        helperBuy.attachToRecyclerView(recyclerViewBuy);
        partViewModel = new ViewModelProvider(this).get(PartViewModel.class);
        partViewModel.getAllPartsToBuy(tempCosplay.mCosplayId).observe(getViewLifecycleOwner(), new Observer<List<Part>>() {
            @Override
            public void onChanged(List<Part> parts) {
                partAdapterBuy.setParts(parts);
            }
        });


        //fab to add a new cosplay part
        mfabAddPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPartDialog(tempCosplay);
            }
        });
        //Notes
        mCosplayNote.setText(tempCosplay.mCosplayNote);
        mCosplayNotesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cosplay CosUP = new Cosplay(tempCosplay.mCosplayId, tempCosplay.mCosplayName, tempCosplay.mCosplayStartDate, tempCosplay.mCosplayEndDate, tempCosplay.mCosplayBudget, tempCosplay.mCosplayIMG,mCosplayNote.getText().toString());

                cosplayViewModel.update(CosUP);
                closeKeyboard(v);
                Toast.makeText(requireContext(),"The note is saved",Toast.LENGTH_SHORT).show();
            }
        });
        referenceImgViewModel=new ViewModelProvider(this).get(ReferenceImgViewModel.class);
        mRefImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReferenceImg tempRefImg=new ReferenceImg(tempCosplay.mCosplayId,0,tempCosplay.mCosplayIMG);
                referenceImgViewModel.insert(tempRefImg);

                mRefImgGridView.setAdapter(new RefenceImgAdapter(requireContext()));
            }
        });
        return v;
    }

    public void UpdateCosplayDialog(final Cosplay cosplay) {
        dialogBuilder = new AlertDialog.Builder(requireContext());
        final View cosplayPopUpView = getLayoutInflater().inflate(R.layout.add_cosplay, null);
        mCosplayName = cosplayPopUpView.findViewById(R.id.EditTextCosplayName);
        mCosplayStartDate = cosplayPopUpView.findViewById(R.id.editTextBeginDate);
        mCosplayEndDate = cosplayPopUpView.findViewById(R.id.editTextEndDate);
        mCosplayBudget = cosplayPopUpView.findViewById(R.id.editTextBudget);
        mCosplayImage = cosplayPopUpView.findViewById(R.id.imageViewCosplayImgPreview);
        mChoosePicture = cosplayPopUpView.findViewById(R.id.btnChooseCosplayImg);
        mCancel = cosplayPopUpView.findViewById(R.id.buttonCancel);
        mUpdateCosplays = cosplayPopUpView.findViewById(R.id.buttonAddCosplay);
        dialogBuilder.setView(cosplayPopUpView);

        mUpdateCosplays.setText("Update Cosplay");
        mChoosePicture.setEnabled(false);
        mCosplayName.setText(cosplay.mCosplayName);
        mCosplayStartDate.setText(cosplay.mCosplayStartDate);
        mCosplayEndDate.setText(cosplay.mCosplayEndDate);
        mCosplayBudget.setText(Double.toString(cosplay.mCosplayBudget));
        mCosplayImage.setImageBitmap(cosplay.mCosplayIMG);

        dialog = dialogBuilder.create();
        dialog.show();

        //create dateSelector and add the selected date to the Edit text
        mCosplayStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    int year;
                    int month;
                    int day;
                    String mtemp = mCosplayStartDate.getText().toString().trim();
                    if (mtemp.matches("")) {
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
                    if (mtemp.matches("")) {
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
        /*
        todo: fix this bug cardNr= COS-69
        mChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.txt_chooseImg_intent)), GALLERY_REQUEST_CODE);

            }
        });
*/
        //Add Copslay to the database
        mUpdateCosplays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cosplay CosUP = new Cosplay(cosplay.mCosplayId, mCosplayName.getText().toString(), mCosplayStartDate.getText().toString(), mCosplayEndDate.getText().toString(), Double.parseDouble(mCosplayBudget.getText().toString()), ((BitmapDrawable) mCosplayImage.getDrawable()).getBitmap(),mCosplayNote.getText().toString());

                cosplayViewModel.update(CosUP);
                dialog.dismiss();
                mName.setText(CosUP.mCosplayName);
                mEndDate.setText(CosUP.mCosplayEndDate);
                mBudget.setText(Double.toString(CosUP.mCosplayBudget));
                mImage.setImageBitmap(CosUP.mCosplayIMG);
                mPercentage.setText("% complete");
                mCosplayNote.setText(CosUP.mCosplayNote);
            }
        });

    }

    public void createNewPartDialog(final Cosplay cosplay) {
        dialogBuilder = new AlertDialog.Builder(requireContext());
        final View PartPopUpView = getLayoutInflater().inflate(R.layout.add_cosplay_part, null);

        mPartName = PartPopUpView.findViewById(R.id.EditText_PartName);

        mPartmakeBuy = PartPopUpView.findViewById(R.id.Spinner_PartBuyMake);
        if (mPartmakeBuy != null) {
            mPartmakeBuy.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.BuyMake, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPartmakeBuy.setAdapter(adapter);
        mPartLink = PartPopUpView.findViewById(R.id.EditText_partLink);
        mPartEndDate = PartPopUpView.findViewById(R.id.EditTextEndDate);
        mPartCost = PartPopUpView.findViewById(R.id.editTextCost);
        mPartImage = PartPopUpView.findViewById(R.id.imgView_PartImage);
        mPartChooseImage = PartPopUpView.findViewById(R.id.btn_Part_ChooseImg);
        mPartCancel = PartPopUpView.findViewById(R.id.btn_partCancel);
        mPartAddPart = PartPopUpView.findViewById(R.id.btn_PartAddPart);

        dialogBuilder.setView(PartPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();


        //create dateSelector and add the selected date to the Edit text
        mPartEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int year;
                    int month;
                    int day;
                    String mtemp = mPartEndDate.getText().toString().trim();
                    if (mtemp.matches("")) {
                        Calendar calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                    } else {
                        String mDateComlete = mPartEndDate.getText().toString();
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
                mPartEndDate.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };

        //Cancel. dismiss the popup
        mPartCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

       /* //Choose the picture from the gallery
        mPartChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.txt_chooseImg_intent)), GALLERY_REQUEST_CODE_PART);

            }
        });*/
        mPartAddPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Part temp = new Part();
                temp.mCosplayId = cosplay.mCosplayId;
                temp.mCosplayPartId = 0;
                temp.mCosplayPartName = mPartName.getText().toString();
                temp.mCosplayPartBuyMake = mPartmakeBuy.getSelectedItem().toString();
                temp.mCosplayPartLink = mPartLink.getText().toString();
                temp.mCosplayPartCost = Double.parseDouble(mPartCost.getText().toString());
                temp.mCosplayPartEndDate = mPartEndDate.getText().toString();
                temp.mCosplayPartImg = cosplay.mCosplayIMG;
                temp.mCosplayPartStatus = "Planned";
                partViewModel.insert(temp);
                dialog.dismiss();
            }
        });
    }
    private void closeKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}