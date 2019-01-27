package net.com.diagnose.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import net.com.diagnose.activity.MainActivity;


/**
 * Created by selva on 2018/11/2.
 */
public abstract class BaseFragment extends Fragment {

    protected BackHandledInterface mBackHandledInterface;

    /**
     * Owned Laws BackHandledFragment Elements ___ General_Count ___ ___ 0
           * FragmentActivity Acquisition Physical Reply Key Points Incidents After Case of Challenge emente Fragment Reasonable Consumption Expense Same
           * Fruit death FragmentActivity Self - celebrating party expensesexciting incident
     */
    public abstract boolean onBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!(getActivity() instanceof BackHandledInterface)){
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }else{
            this.mBackHandledInterface = (BackHandledInterface)getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() instanceof MainActivity) {
                //((MainActivity) getActivity()).changeStateBar(this);
            }
            if (mBackHandledInterface != null) {
                //Tell FragmentActivity, the current Fragment is at the top of the stack
                mBackHandledInterface.setSelectedFragment(this);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MainActivity) {
           // ((MainActivity) getActivity()).changeStateBar(this);
        }
    }
}
