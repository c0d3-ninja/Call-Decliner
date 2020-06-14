package com.vandumurugancallblocker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vandumurugancallblocker.room.AgaraathiPudichchavan;
import com.vandumurugancallblocker.room.AgaraathiPudichchavansRepoVM;
import com.vandumurugancallblocker.room.FlagEntity;
import com.vandumurugancallblocker.room.FlagRepoVM;

import java.util.ArrayList;
import java.util.List;

public class VattachCheyalaalarFragment extends Fragment implements View.OnClickListener{
    private static final int READ_PHONE_STATE=1;
    private static final int READ_CALL_LOG=2;
    private static final int ANSWER_PHONE_CALLS=3;
    private JaameenAdapter jaameenAdapter;
    private ViewHolder viewHolder;
    private List<AgaraathiPudichchavan> agaraathiPudichchavans;
    private AgaraathiPudichchavansRepoVM agaraathiPudichchavansRepoVM;
    private FlagRepoVM flagRepoVM;
    private OnBackPressedCallback onBackPressedCallback;
    private boolean isSwitchClickedManually;
    public VattachCheyalaalarFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariables();
        initViews(view);
        initListeners();
        initObservers();
        getReadPhoneStatePermission();

    }


    private void initVariables(){
       agaraathiPudichchavansRepoVM =new ViewModelProvider(this).get(AgaraathiPudichchavansRepoVM.class);
       flagRepoVM = new ViewModelProvider(this).get(FlagRepoVM.class);
    }
    private void initViews(View view){
        viewHolder=new ViewHolder(view);
        jaameenAdapter=new JaameenAdapter(getContext(),getAgaraathiPudichchavans());
        jaameenAdapter.setItemClickListener(new ListItemClickListener());
        viewHolder.recyclerView.setAdapter(jaameenAdapter);
    }
    private void initListeners(){
        viewHolder.addBtn.setOnClickListener(this::onClick);
        viewHolder.allowPermissionBtn.setOnClickListener(this::onClick);
        viewHolder.serviceControlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isSwitchClickedManually){
                    if(isChecked){
                        showMsg(getString(R.string.call_dec_service_enabled));
                    }else{
                        showMsg(getString(R.string.call_dec_service_disabled));

                    }
                    flagRepoVM.insert(FlagEntity.DECLINE_DISABLED,!isChecked);
                }

            }
        });
        onBackPressedCallback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(viewHolder.addFragmentContainer.getVisibility()==View.VISIBLE){
                    hideAddFragment();
                }else{
                    onBackPressedCallback.setEnabled(false);
                    getActivity().onBackPressed();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
    }
    private void initObservers(){
        final LiveData<List<AgaraathiPudichchavan>> liveData = agaraathiPudichchavansRepoVM.getAgaraathiPudichavansAsLiveData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<AgaraathiPudichchavan>>() {
            @Override
            public void onChanged(List<AgaraathiPudichchavan> agaraathiPudichchavans) {
                liveData.removeObservers(getViewLifecycleOwner());
                if(agaraathiPudichchavans.size()==0){
                    showNoContentContainer();
                }else{
                    showRecyclerView();
                    getAgaraathiPudichchavans().addAll(agaraathiPudichchavans);
                    jaameenAdapter.notifyDataSetChanged();
                }

            }
        });
        final LiveData<FlagEntity> flagEntityLiveData = flagRepoVM.getFlagAsLiveData(FlagEntity.DECLINE_DISABLED);
        flagEntityLiveData.observe(getViewLifecycleOwner(), new Observer<FlagEntity>() {
            @Override
            public void onChanged(FlagEntity flagEntity) {
                flagEntityLiveData.removeObservers(getViewLifecycleOwner());
                viewHolder.serviceControlSwitch.setChecked(flagEntity==null || !flagEntity.isValue());
                isSwitchClickedManually =true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addFab:
                showAddFragment(null);
                break;
            case R.id.mainAllowPermissionsBtn:
                getReadPhoneStatePermission();
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            switch (requestCode){
                case READ_PHONE_STATE:
                    getReadCallLogsPermission();
                    break;
                case READ_CALL_LOG:
                    getAnswerPhoneCallsPermission();
                    break;
                case ANSWER_PHONE_CALLS:
                    hideAllowPermissionContainer();
                    break;
            }

        }else{
            showAllowPermissionContiner();
            showMsg(getString(R.string.allow_permissions_to_avoid_unwanted_calls));
        }
    }
    @Override
    public void onDestroyView() {
        onBackPressedCallback.setEnabled(false);
        super.onDestroyView();
    }

    private boolean isPermissionGranted(@NonNull String permission, @NonNull int requestCode){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission},requestCode);
                return  false;
            }
            else{
                return  true;
            }
        }
        else{
            return true;
        }

    }


    private void showAllowPermissionContiner(){
        viewHolder.permissionErrorContainer.setVisibility(View.VISIBLE);
    }
    private void hideAllowPermissionContainer(){
        viewHolder.permissionErrorContainer.setVisibility(View.GONE);
    }
    private void showAddFragment(AgaraathiPudichchavan agaraathiPudichchavan){
        AddFragment addFragment;
        if(agaraathiPudichchavan==null){
        addFragment= AddFragment.newInstance(null,AssistantConstants.CONTAINS,null,null);
        }else{
            addFragment= AddFragment.newInstance(agaraathiPudichchavan.getId(),agaraathiPudichchavan.getCondition(),agaraathiPudichchavan.getNumber(),agaraathiPudichchavan.getNote());
        }

        addFragment.setBtnClickListener(new AddItemListener());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.addContainer,addFragment).commit();
        viewHolder.addFragmentContainer.setVisibility(View.VISIBLE);
    }
    private void hideAddFragment(){
        viewHolder.addFragmentContainer.setVisibility(View.GONE);
    }

    private void toggleContentContainer(){
        if(getAgaraathiPudichchavans().size()==0){
            showNoContentContainer();
        }else {
            showRecyclerView();
        }
    }
    private void showNoContentContainer(){
        viewHolder.noContentContainer.setVisibility(View.VISIBLE);
        viewHolder.recyclerView.setVisibility(View.GONE);
    }
    private void showRecyclerView(){
        if(viewHolder.recyclerView.getVisibility()!=View.VISIBLE){
            viewHolder.recyclerView.setVisibility(View.VISIBLE);
            viewHolder.noContentContainer.setVisibility(View.GONE);
        }
    }


    private void getReadPhoneStatePermission(){
        if(isPermissionGranted(Manifest.permission.READ_PHONE_STATE,READ_PHONE_STATE)){
            getReadCallLogsPermission();
        }
    }
    private void getReadCallLogsPermission(){
        if(isPermissionGranted(Manifest.permission.READ_CALL_LOG,READ_CALL_LOG)){
            getAnswerPhoneCallsPermission();
        }
    }
    private void getAnswerPhoneCallsPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isPermissionGranted(Manifest.permission.ANSWER_PHONE_CALLS,ANSWER_PHONE_CALLS)){
            hideAllowPermissionContainer();
        }else{
            hideAllowPermissionContainer();
        }
    }


    private List<AgaraathiPudichchavan> getAgaraathiPudichchavans() {
        if(agaraathiPudichchavans==null){
            agaraathiPudichchavans=new ArrayList<>();
        }
        return agaraathiPudichchavans;
    }
    private int getListItemPosition(Long id){
        for (int i = 0; i < getAgaraathiPudichchavans().size(); i++) {
            AgaraathiPudichchavan agaraathiPudichchavan = getAgaraathiPudichchavans().get(i);
            if(agaraathiPudichchavan.getId().equals(id)){
                return i;
            }
        }
        return -1;
    }


    private void showMsg(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }



    private class AddItemListener implements AddFragment.BtnClickListener{
        @Override
        public void onSaveBtnClick(AgaraathiPudichchavan agaraathiPudichchavan,boolean isNew) {
            hideAddFragment();
            if(isNew){
                getAgaraathiPudichchavans().add(0,agaraathiPudichchavan);
                jaameenAdapter.notifyItemInserted(0);
                agaraathiPudichchavansRepoVM.insert(agaraathiPudichchavan);
            }else {
                int position = getListItemPosition(agaraathiPudichchavan.getId());
                if(position!=-1){
                    AgaraathiPudichchavan agaraathiPudichchavan1 = getAgaraathiPudichchavans().get(position);
                    agaraathiPudichchavan1.setCondition(agaraathiPudichchavan.getCondition());
                    agaraathiPudichchavan1.setNote(agaraathiPudichchavan.getNote());
                    agaraathiPudichchavan1.setNumber(agaraathiPudichchavan.getNumber());
                    jaameenAdapter.notifyItemChanged(position);
                    agaraathiPudichchavansRepoVM.insert(agaraathiPudichchavan1);
                }

            }
            toggleContentContainer();

        }

        @Override
        public void onCancelBtnClick() {
            hideAddFragment();
        }
    }
    private class ListItemClickListener implements JaameenAdapter.ItemClickListener{
        @Override
        public void onDeleteClick(AgaraathiPudichchavan agaraathiPudichchavan) {
            int position = getListItemPosition(agaraathiPudichchavan.getId());
            if(position!=-1){
                getAgaraathiPudichchavans().remove(position);
                jaameenAdapter.notifyItemRemoved(position);
                agaraathiPudichchavansRepoVM.delete(agaraathiPudichchavan);
            }
            toggleContentContainer();
        }

        @Override
        public void onListItemClick(AgaraathiPudichchavan agaraathiPudichchavan) {
            showAddFragment(agaraathiPudichchavan);
        }
    }
    private class ViewHolder{
        RecyclerView recyclerView;
        View addFragmentContainer,permissionErrorContainer,noContentContainer;
        FloatingActionButton addBtn;
        Button allowPermissionBtn;
        Switch serviceControlSwitch;
        public ViewHolder(View view) {
            recyclerView=view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            recyclerView.addItemDecoration(new SpacesItemDecoration(16,1));
            addFragmentContainer=view.findViewById(R.id.addContainer);
            addFragmentContainer.setVisibility(View.GONE);
            addBtn=view.findViewById(R.id.addFab);
            permissionErrorContainer=view.findViewById(R.id.permissionErrorContainer);
            permissionErrorContainer.setVisibility(View.GONE);
            allowPermissionBtn=view.findViewById(R.id.mainAllowPermissionsBtn);
            noContentContainer=view.findViewById(R.id.noContentContainer);
            noContentContainer.setVisibility(View.GONE);
            serviceControlSwitch=view.findViewById(R.id.serviceControlSwitch);
        }
    }


}
