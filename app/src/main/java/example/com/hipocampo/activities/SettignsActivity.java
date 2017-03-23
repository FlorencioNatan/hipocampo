package example.com.hipocampo.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import example.com.hipocampo.R;
import example.com.hipocampo.dialogs.MasterPasswordDialog;
import example.com.hipocampo.model.Password;
import example.com.hipocampo.util.FileManager;
import example.com.hipocampo.util.ImportSeahorseFile;
import example.com.hipocampo.util.PasswordSingleton;

public class SettignsActivity extends AppCompatActivity
        implements MasterPasswordDialog.OnMasterPasswordDialogListener {

    private List<Folder> mItems = new ArrayList<Folder>();
    private MyFolderRecyclerViewAdapter adapter;

    private int IMPORT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settigns);
        setTitle(R.string.action_settings);

        View view = findViewById(R.id.folder_list);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        FileManager dir = new FileManager(this);
        ArrayList<String> list = dir.listAllFiles();
        int i = 1;
        for (String str : list){
            mItems.add(new Folder(i++, str.substring(0,str.indexOf(".key")), str));
        }

        adapter = new MyFolderRecyclerViewAdapter(mItems, this);
        recyclerView.setAdapter(adapter);

        Button btImport = (Button) findViewById(R.id.btImport);
        btImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("*/*");
                try {
                    startActivityForResult(fileintent, IMPORT_FILE);
                } catch (ActivityNotFoundException e) {
                    Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMPORT_FILE && resultCode == RESULT_OK) {
            try {
                ImportSeahorseFile isf = new ImportSeahorseFile(getContentResolver().openInputStream(data.getData()));
                PasswordSingleton.getInstance().setPasswordList(isf.importFile());
                PasswordSingleton.getInstance().getDirectories().put(isf.getFileName(), isf.getFileName()+".key");
                PasswordSingleton.getInstance().setCurrentFolder(isf.getFileName());
                new Handler().post(new Runnable() {
                    public void run() {
                        MasterPasswordDialog dialog = new MasterPasswordDialog();
                        dialog.show(getSupportFragmentManager(), "Master Password Dialog");
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onFolderListDeleteInteraction(Folder item){
        FileManager fileManager = new FileManager(this);
        fileManager.deleteFile(item.path);
        mItems.remove(item.id-1);
        for (int i = item.id; i <= mItems.size(); i++){
            mItems.get(i-1).id = i;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMasterPasswordDialogPositiveClick(String password) {
        PasswordSingleton.getInstance().setMasterPassword(password);
        FileManager fileManager = new FileManager(this);
        fileManager.createFile();
        fileManager.writeFile(PasswordSingleton.getInstance().toString());
    }

    @Override
    public void onMasterPasswordDialogNegativeClick(String password) {
        //TODO
    }

    private static class Folder {
        public  int id;
        public  String name;
        public  String path;

        public Folder(int id, String content, String path) {
            this.id = id;
            this.name = content;
            this.path = path;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private class MyFolderRecyclerViewAdapter extends RecyclerView.Adapter<MyFolderRecyclerViewAdapter.ViewHolder> {

        private final List<Folder> mValues;
        private final SettignsActivity mListener;

        public MyFolderRecyclerViewAdapter(List<Folder> items, SettignsActivity listener) {
            mValues = items;
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_folder, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(Integer.toString(mValues.get(position).id));
            holder.mNameView.setText(mValues.get(position).name);

            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onFolderListDeleteInteraction(holder.mItem);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mNameView;
            public final Button mDelete;

            public Folder mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mNameView = (TextView) view.findViewById(R.id.name);
                mDelete = (Button) view.findViewById(R.id.delete);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }
}
