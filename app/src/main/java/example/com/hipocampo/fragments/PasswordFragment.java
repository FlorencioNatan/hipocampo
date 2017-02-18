package example.com.hipocampo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import example.com.hipocampo.R;
import example.com.hipocampo.model.Password;
import example.com.hipocampo.util.FileManager;
import example.com.hipocampo.util.PasswordSingleton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordFragment extends Fragment {

    private Password model = null;
    private EditText description;
    private EditText username;
    private EditText password;
    private EditText observation;

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
    public static PasswordFragment newInstance(int id) {
        PasswordFragment fragment = new PasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.model = PasswordSingleton.getInstance().getPasswordList().get(id);
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
        if (model == null)
            setupUiInsert(view);
        else
            setupUiEdit(view);
        ToggleButton tBPassword = (ToggleButton) view.findViewById(R.id.show_password);
        tBPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });

        ToggleButton tBObservation = (ToggleButton) view.findViewById(R.id.show_observation);
        tBObservation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    observation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else {
                    observation.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        return view;
    }

    private void setupUiInsert(View view){
        description = (EditText) view.findViewById(R.id.description);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        observation = (EditText) view.findViewById(R.id.observation);

        Button btSave = (Button) view.findViewById(R.id.save);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertClick();
            }
        });
    }

    private void setupUiEdit(View view){
        description = (EditText) view.findViewById(R.id.description);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        observation = (EditText) view.findViewById(R.id.observation);

        description.setText(model.getDescription());
        username.setText(model.getUsername());
        password.setText(model.getPassword());
        observation.setText(model.getObservation());

        Button btSave = (Button) view.findViewById(R.id.save);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClick();
            }
        });

        Button btDelete = (Button) view.findViewById(R.id.delete);
        btDelete.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        btSave.setLayoutParams(param);
    }

    private void insertClick(){
        if (validateFields()) {
            Gson gson = new Gson();
            FileManager fileManager = new FileManager(getContext());
            PasswordSingleton.getInstance().getPasswordList().add(new Password(description.getText().toString(),
                    username.getText().toString(),
                    password.getText().toString(),
                    observation.getText().toString()));
            fileManager.writeFile(PasswordSingleton.getInstance().toString());
            getFragmentManager().popBackStack();
        }
    }

    private void editClick(){
        model.setDescription(description.getText().toString());
        model.setUsername(username.getText().toString());
        model.setPassword(password.getText().toString());
        model.setObservation(observation.getText().toString());
        FileManager fileManager = new FileManager(getContext());
        fileManager.writeFile(PasswordSingleton.getInstance().toString());
        getFragmentManager().popBackStack();
    }

    private boolean validateFields(){
        return !description.getText().toString().isEmpty() &&
                !username.getText().toString().isEmpty() &&
                !password.getText().toString().isEmpty() &&
                !observation.getText().toString().isEmpty();
    }

}
