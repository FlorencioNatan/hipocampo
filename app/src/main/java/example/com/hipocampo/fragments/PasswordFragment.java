package example.com.hipocampo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import example.com.hipocampo.R;
import example.com.hipocampo.model.Password;
import example.com.hipocampo.util.FileManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordFragment extends Fragment {

    private EditText description;
    private EditText username;
    private EditText password;
    private EditText observation;

    private OnFragmentInteractionListener mListener;

    public PasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PasswordFragment newInstance() {
        PasswordFragment fragment = new PasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        description = (EditText) view.findViewById(R.id.description);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        observation = (EditText) view.findViewById(R.id.observation);

        Button btsave = (Button) view.findViewById(R.id.save);
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                if (validateFields()) {
                    String json = gson.toJson(new Password(description.getText().toString(),
                            username.getText().toString(),
                            password.getText().toString(),
                            observation.getText().toString()));
                    FileManager fileManager = new FileManager(getContext(), "senhas.txt");
                    fileManager.appendData(json);
                    getFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }

    private boolean validateFields(){
        return !description.getText().toString().isEmpty() &&
                !username.getText().toString().isEmpty() &&
                !password.getText().toString().isEmpty() &&
                !observation.getText().toString().isEmpty();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
