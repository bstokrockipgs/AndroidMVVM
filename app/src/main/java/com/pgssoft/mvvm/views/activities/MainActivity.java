package com.pgssoft.mvvm.views.activities;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.pgssoft.mvvm.MVVMApplication;
import com.pgssoft.mvvm.databinding.ActivityMainBinding;
import com.pgssoft.mvvm.model.database.Rate;
import com.pgssoft.mvvm.services.ServiceProvider;
import com.pgssoft.mvvm.viewmodels.MainActivityViewModel;
import com.pgssoft.mvvm.views.activities.interfaces.IMainActivityAccess;
import com.pgssoft.mvvm.views.adapters.RatesAdapter;
import com.pgssoft.mvvm.views.adapters.interfaces.handlers.RateItemHandler;
import com.pgssoft.mvvm.views.adapters.interfaces.providers.RatesAdapterProvider;
import com.pgssoft.mvvm.views.adapters.interfaces.providers.BaseItemProvider;

import com.pgssoft.mvvm.R;

public class MainActivity extends AppCompatActivity implements IMainActivityAccess, RatesAdapterProvider {
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    private ObservableField<RatesAdapter> ratesAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ratesAdapter = new ObservableField<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ServiceProvider serviceProvider = MVVMApplication.getServiceProvider();
        viewModel = new MainActivityViewModel(this, serviceProvider.getApiService(),
                serviceProvider.getRepository());

        binding.setViewModel(viewModel);
        binding.setAdapterProvider(this);

        viewModel.onInfrastructureReady();
    }

    @Override
    public void displayRates(BaseItemProvider<Rate> ratesProvider, RateItemHandler rateItemHandler) {
        ratesAdapter.set(new RatesAdapter(this, ratesProvider, rateItemHandler));
    }

    @Override
    public void showProgressIndicator() {
        progressDialog = ProgressDialog.show(this, "", "Loading...", true, false);
    }

    @Override
    public void hideProgressIndicator() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void openRateDetailsScreen(Rate rate) {
        startActivity(RateActivity.prepareStartIntent(this, rate.getCurrencyCode()));
    }

    @Override
    public ObservableField<RatesAdapter> getAdapter() {
        return ratesAdapter;
    }
}