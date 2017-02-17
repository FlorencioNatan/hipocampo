package example.com.hipocampo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import example.com.hipocampo.R;
import example.com.hipocampo.fragments.PasswordListFragment;

/**
 * Created by florencio on 11/02/17.
 */

public class FolderNameDialog extends DialogFragment {
    private EditText etName;
    private OnFolderNameDialogPositiveListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        etName = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.folder_name_dialog_title);
        builder.setView(etName);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onFolderNameDialogPositiveClick(etName.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PasswordListFragment.OnListFragmentInteractionListener) {
            mListener = (FolderNameDialog.OnFolderNameDialogPositiveListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFolderNameDialogPositiveListener {
        void onFolderNameDialogPositiveClick(String name);

    }
}
