package com.imaginariumfestival.android.competition;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.imaginariumfestival.android.R;

public class CompetitionDialog extends DialogFragment implements OnEditorActionListener {

	public interface CompetitionDialogListener {
        void onFinishCompetitionDialog(String inputText);
    }
	
    private EditText mEditText;

    public CompetitionDialog() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition, container);
        mEditText = (EditText) view.findViewById(R.id.competition_email_input);
        getDialog().setTitle( getResources().getString(R.string.competition_title ) );

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);
        
        return view;
    }
    
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
        	CompetitionDialogListener activity = (CompetitionDialogListener) getActivity();
            activity.onFinishCompetitionDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}