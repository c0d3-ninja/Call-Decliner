package com.vandumurugancallblocker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.vandumurugancallblocker.room.AgaraathiPudichchavan;

import java.util.Date;

public class AddFragment extends Fragment {

    private ViewHolder viewHolder;
    private BtnClickListener btnClickListener;
    public AddFragment() {
    }

    public static final AddFragment newInstance(Long id,int condition,String phoneNumber,String note){
        AddFragment addFragment=new AddFragment();
        Bundle args = new Bundle();
        if(id!=null){
            args.putLong(AgaraathiPudichchavan.COLUMN_ID,id);
        }
        args.putInt(AgaraathiPudichchavan.COLUMN_CONDITION,condition);
        args.putString(AgaraathiPudichchavan.COLUMN_NUMBER,phoneNumber!=null?phoneNumber:"");
        args.putString(AgaraathiPudichchavan.COLUMN_NOTE,note!=null?note:"");
        addFragment.setArguments(args);
        return addFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_condition,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariables();
        initViews(view);
        initListeners();
        initUIWithData();
    }
    private void initUIWithData(){
        checkCondition(getArguments().getInt(AgaraathiPudichchavan.COLUMN_CONDITION,AssistantConstants.CONTAINS));
        if(getArguments().get(AgaraathiPudichchavan.COLUMN_ID)!=null){
            viewHolder.phoneNumberEt.setText(getArguments().getString(AgaraathiPudichchavan.COLUMN_NUMBER));
            viewHolder.noteEt.setText(getArguments().getString(AgaraathiPudichchavan.COLUMN_NOTE));
        }
    }
    private void initVariables(){

    }
    private void initViews(View view){
        viewHolder=new ViewHolder(view);
    }
    private void initListeners(){
        viewHolder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSave();
            }
        });
        viewHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnClickListener!=null){
                    hideKeyBoard();
                    btnClickListener.onCancelBtnClick();
                }
            }
        });
    }

    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    private void validateAndSave() {
        String phoneNumber = viewHolder.phoneNumberEt.getText().toString().trim();
        if("".equals(phoneNumber)){
            Toast.makeText(getContext(),getString(R.string.enter_phone_number),Toast.LENGTH_SHORT).show();
            focusEt(viewHolder.phoneNumberEt);
            return;
        }
        int condition = AssistantConstants.CONTAINS;
        switch (viewHolder.radioGroup.getCheckedRadioButtonId()){
            case R.id.equalsCondition:
                condition= AssistantConstants.EQUALS;
                break;
            case R.id.startsWithCondition:
                condition= AssistantConstants.STARTS_WITH;
                break;
            case R.id.endsWithCondition:
                condition=AssistantConstants.ENDS_WITH;
                break;
        }
        if(btnClickListener!=null){
            hideKeyBoard();
            Object id = getArguments().get(AgaraathiPudichchavan.COLUMN_ID);
            btnClickListener.onSaveBtnClick(new AgaraathiPudichchavan(id==null?new Date().getTime():(Long) id,phoneNumber,condition,viewHolder.noteEt.getText().toString().trim()),id==null);
        }
    }
    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewHolder.phoneNumberEt.getWindowToken(), 0);
    }
    private void focusEt(TextInputEditText view){
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void showMsg(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    private void checkCondition(int condition){
        switch (condition){
            case AssistantConstants.CONTAINS:
                viewHolder.radioGroup.check(R.id.containsCondition);
                return;
            case AssistantConstants.EQUALS:
                viewHolder.radioGroup.check(R.id.equalsCondition);
                return;
            case AssistantConstants.STARTS_WITH:
                viewHolder.radioGroup.check(R.id.startsWithCondition);
                return;
            case AssistantConstants.ENDS_WITH:
                viewHolder.radioGroup.check(R.id.endsWithCondition);
                return;
        }
    }

    private class ViewHolder{
        RadioGroup radioGroup;
        TextInputEditText phoneNumberEt, noteEt;
        Button saveBtn,cancelBtn;
        public ViewHolder(View view) {
            radioGroup=view.findViewById(R.id.conditionRadioGroup);
            phoneNumberEt =view.findViewById(R.id.phoneNumberEt);
            noteEt =view.findViewById(R.id.noteEt);
            saveBtn=view.findViewById(R.id.saveBtn);
            cancelBtn=view.findViewById(R.id.cancelBtn);

        }
    }

    public interface BtnClickListener{
        void onSaveBtnClick(AgaraathiPudichchavan agaraathiPudichchavan,boolean isNew);
        void onCancelBtnClick();
    }
}
