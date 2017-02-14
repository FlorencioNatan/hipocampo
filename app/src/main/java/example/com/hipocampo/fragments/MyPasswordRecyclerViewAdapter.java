package example.com.hipocampo.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.com.hipocampo.R;
import example.com.hipocampo.fragments.PasswordListFragment.OnListFragmentInteractionListener;
import example.com.hipocampo.model.Password;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Password} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPasswordRecyclerViewAdapter extends RecyclerView.Adapter<MyPasswordRecyclerViewAdapter.ViewHolder> {

    private final List<Password> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPasswordRecyclerViewAdapter(List<Password> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_password_show, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mDescriptionView.setText(mValues.get(position).getDescription());
        holder.mUserNameView.setText(mValues.get(position).getUsername());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDescriptionView;
        public final TextView mUserNameView;
        public Password mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDescriptionView = (TextView) view.findViewById(R.id.description);
            mUserNameView = (TextView) view.findViewById(R.id.username);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUserNameView.getText() + "'";
        }
    }
}
