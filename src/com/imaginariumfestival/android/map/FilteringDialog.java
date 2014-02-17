package com.imaginariumfestival.android.map;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.imaginariumfestival.android.R;

public class FilteringDialog extends DialogFragment {
	TreeMap<Integer, String> categories;
	List<Integer> mSelectedItems;
	FilteringDialogListener mListener;
	
	public FilteringDialog(FilteringDialogListener listener, TreeMap<MapItemView, String> mapItems) {
		super();
		this.mListener = listener;
		
		TreeSet<String> categoriesSet = new TreeSet<String>();
		categoriesSet.addAll( mapItems.values() );
		
		TreeMap<Integer, String> categories = new TreeMap<Integer, String>();
		int i = 0;
		for (String category : categoriesSet) {
			categories.put(i, category);
			i++;
		}
		this.categories = categories;
	}

	public interface FilteringDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog, List<String> selectedCategories);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mSelectedItems = new ArrayList<Integer>();
		
		TreeSet<String> categoriesSet = new TreeSet<String>();
		categoriesSet.addAll( categories.values() );
		String[] categoriesArray = categoriesSet.toArray(new String[categoriesSet.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.action_type_filter)
				.setMultiChoiceItems(categoriesArray, null,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {
									mSelectedItems.add(which);
								} else if (mSelectedItems.contains(which)) {
									mSelectedItems.remove(Integer
											.valueOf(which));
								}
							}
						})
				.setPositiveButton(R.string.filter,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								List<String> selectedCategories = new ArrayList<String>();
								for (Integer position : mSelectedItems) {
									selectedCategories.add(categories.get(position));
								}
								mListener.onDialogPositiveClick(FilteringDialog.this, selectedCategories);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (FilteringDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}
}
