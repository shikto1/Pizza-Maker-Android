package com.shishirappstudio.pizzamaker;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    protected Unbinder viewUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        viewUnbinder = ButterKnife.bind(this);
        onViewReady(savedInstanceState, getIntent());
    }

    protected abstract int getContentView();
    protected abstract void onViewReady(Bundle savedInstanceState, Intent data);

    @Override
    protected void onDestroy() {
        if(viewUnbinder != null){
            viewUnbinder.unbind();
        }
        super.onDestroy();
    }

}
