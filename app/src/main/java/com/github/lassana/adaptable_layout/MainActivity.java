package com.github.lassana.adaptable_layout;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.lassana.adaptable_layout.R;

import java.util.Random;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        private Random mRandom = new Random();

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            AdaptableLayout layout = (AdaptableLayout) view.findViewById(R.id.layout);
            for (int i = 0; i < 100; ++i) {
                Button button = new Button(getActivity());
                int length = 5 + mRandom.nextInt(10);
                button.setText(randomString(CHARSET_AZ_09, length));
                layout.addView(button);
            }
        }

        public String randomString(char[] characterSet, int length) {
            char[] result = new char[length];
            for (int i = 0; i < result.length; i++) {
                result[i] = characterSet[mRandom.nextInt(characterSet.length)];
            }
            return new String(result);
        }
    }

}
