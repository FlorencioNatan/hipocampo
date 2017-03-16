package example.com.hipocampo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import java.util.ArrayList;

import example.com.hipocampo.R;
import example.com.hipocampo.dialogs.FolderNameDialog;
import example.com.hipocampo.dialogs.MasterPasswordDialog;
import example.com.hipocampo.fragments.PasswordFragment;
import example.com.hipocampo.fragments.PasswordListFragment;
import example.com.hipocampo.util.FileManager;
import example.com.hipocampo.util.PasswordSingleton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PasswordListFragment.OnListFragmentInteractionListener,
        FolderNameDialog.OnFolderNameDialogPositiveListener,
        MasterPasswordDialog.OnMasterPasswordDialogListener{

    private NavigationView navigationView;
    private int selectedItem = -1;
    private int lastSelectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FileManager dir = new FileManager(this);
        ArrayList<String> list = dir.listAllFiles();
        PasswordSingleton.getInstance().newDirectories();
        MenuItem directoriesList = navigationView.getMenu().getItem(0);
        SubMenu subMenu = directoriesList.getSubMenu();
        int i = 0;
        for (String str : list){
            subMenu.add(R.id.folders, 0, Menu.NONE+i++, str.substring(0,str.indexOf(".key"))).
                    setIcon(R.drawable.ic_folder_open_black_24dp).setCheckable(true);
            PasswordSingleton.getInstance().getDirectories().put(str.substring(0,str.indexOf(".key")), str);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        FileManager dir = new FileManager(this);
        ArrayList<String> list = dir.listAllFiles();
        PasswordSingleton.getInstance().newDirectories();
        MenuItem directoriesList = navigationView.getMenu().getItem(0);
        SubMenu subMenu = directoriesList.getSubMenu();
        subMenu.clear();
        int i = 0;
        for (String str : list){
            subMenu.add(R.id.folders, 0, Menu.NONE+i++, str.substring(0,str.indexOf(".key"))).
                    setIcon(R.drawable.ic_folder_open_black_24dp).setCheckable(true);
            PasswordSingleton.getInstance().getDirectories().put(str.substring(0,str.indexOf(".key")), str);
        }

        if (selectedItem>=0)
            navigationView.getMenu().getItem(0).getSubMenu().getItem(selectedItem).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettignsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (selectedItem >=0 )
            navigationView.getMenu().getItem(0).getSubMenu().getItem(selectedItem).setChecked(false);
        lastSelectedItem = selectedItem;
        selectedItem = item.getOrder();

        if (id == R.id.nav_add_password) {
            FolderNameDialog dialog = new FolderNameDialog();
            dialog.show(getSupportFragmentManager(),"Folder Name Dialog");
        }else if (id == 0){
            FragmentManager fragmentManager = getSupportFragmentManager();
            MasterPasswordDialog dialog = new MasterPasswordDialog();
            dialog.show(fragmentManager, "Master Password Dialog");
        }

        System.out.println();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onListFragmentInteraction(int id) {
        Fragment fragment = PasswordFragment.newInstance(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack("").commit();
    }

    @Override
    public void onWrongPassword() {
        if (selectedItem >=0)
            navigationView.getMenu().getItem(0).getSubMenu().getItem(selectedItem).setChecked(false);
        if (lastSelectedItem >=0)
            navigationView.getMenu().getItem(0).getSubMenu().getItem(lastSelectedItem).setChecked(false);
        selectedItem = -1;
        lastSelectedItem = -1;
    }

    @Override
    public void onFolderNameDialogPositiveClick(String name) {
        MenuItem folderList = navigationView.getMenu().getItem(0);
        SubMenu subMenu = folderList.getSubMenu();
        subMenu.add(R.id.folders, 0, Menu.NONE+subMenu.size(), name).
                setIcon(R.drawable.ic_folder_open_black_24dp).setCheckable(true);
        lastSelectedItem = selectedItem;
        selectedItem = subMenu.size()-1;
        PasswordSingleton.getInstance().setCurrentFolder(name);
        PasswordSingleton.getInstance().getDirectories().put(name, name + ".key");

        FragmentManager fragmentManager = getSupportFragmentManager();
        MasterPasswordDialog dialog = new MasterPasswordDialog();
        dialog.show(fragmentManager, "Master Password Dialog");

    }

    @Override
    public void onMasterPasswordDialogPositiveClick(String password) {
        navigationView.getMenu().getItem(0).getSubMenu().getItem(selectedItem).setChecked(true);
        PasswordSingleton.getInstance().setCurrentFolder(navigationView.getMenu().getItem(0).
                getSubMenu().getItem(selectedItem).getTitle().toString());

        System.out.println("Folder: " + navigationView.getMenu().getItem(0).
                getSubMenu().getItem(selectedItem).getTitle().toString());

        PasswordSingleton.getInstance().setMasterPassword(password);

        FileManager file = new FileManager(this);
        file.createFile();

        Fragment fragment = PasswordListFragment.newInstance(1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack("").commit();
    }

    @Override
    public void onMasterPasswordDialogNegativeClick(String password) {
        selectedItem = lastSelectedItem;
        if (selectedItem >= 0)
            navigationView.getMenu().getItem(0).getSubMenu().getItem(selectedItem).setChecked(true);
    }
}
