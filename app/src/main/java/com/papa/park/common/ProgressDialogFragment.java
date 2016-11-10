package com.papa.park.common;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.papa.park.R;


public class ProgressDialogFragment extends DialogFragment {

    public static void showProgress(FragmentManager manager) {
        ProgressDialogFragment fragment = (ProgressDialogFragment) manager
                .findFragmentByTag(ProgressDialogFragment.class.getSimpleName());
        FragmentTransaction ft = manager.beginTransaction();

        if (fragment != null) {
            ft.remove(fragment);
        }
        fragment = new ProgressDialogFragment();
        ft.add(fragment, ProgressDialogFragment.class.getSimpleName());
        ft.commitAllowingStateLoss();

    }

    public static void dismissProgress(FragmentManager manager) {
        if (manager == null)
            return;
        ProgressDialogFragment fragment = (ProgressDialogFragment) manager
                .findFragmentByTag(ProgressDialogFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            fragment.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    // @Override
    // public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Dialog dialog = new ProgressDialog(getActivity());
    // // Dialog dialog = super.onCreateDialog(savedInstanceState);
    // dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    // dialog.setCanceledOnTouchOutside(false);
    //
    // return dialog;
    // }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.progress_layout, container, false);
        // return super.onCreateView(inflater, container, savedInstanceState);
    }

}
