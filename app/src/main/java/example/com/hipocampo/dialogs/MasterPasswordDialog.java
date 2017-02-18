package example.com.hipocampo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.widget.EditText;

import example.com.hipocampo.R;
import example.com.hipocampo.activities.MainActivity;
import example.com.hipocampo.fragments.PasswordListFragment;
import example.com.hipocampo.util.FileManager;
import example.com.hipocampo.util.PasswordSingleton;

/**
 * Created by florencio on 11/02/17.
 */

public class MasterPasswordDialog extends DialogFragment {
    private EditText etPassword;
    private OnMasterPasswordDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        etPassword = new EditText(getActivity());
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.master_password_dialog_title);
        builder.setView(etPassword);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onMasterPasswordDialogPositiveClick(etPassword.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onMasterPasswordDialogNegativeClick(etPassword.getText().toString());
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mListener = (MasterPasswordDialog.OnMasterPasswordDialogListener) context;
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

    public interface OnMasterPasswordDialogListener {
        void onMasterPasswordDialogPositiveClick(String password);
        void onMasterPasswordDialogNegativeClick(String password);
    }
}
