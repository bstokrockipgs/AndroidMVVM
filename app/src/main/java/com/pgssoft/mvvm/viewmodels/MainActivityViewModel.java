package com.pgssoft.mvvm.viewmodels;

import com.pgssoft.mvvm.model.api.ApiTable;
import com.pgssoft.mvvm.model.database.Rate;
import com.pgssoft.mvvm.services.interfaces.ApiService;
import com.pgssoft.mvvm.services.interfaces.ApiServiceCallback;
import com.pgssoft.mvvm.services.interfaces.Repository;
import com.pgssoft.mvvm.views.activities.interfaces.IMainActivityAccess;
import com.pgssoft.mvvm.views.adapters.interfaces.handlers.RateItemHandler;
import com.pgssoft.mvvm.views.adapters.interfaces.providers.BaseItemProvider;

import java.util.List;

/**
 * Created by bstokrocki on 29.01.2017.
 */
public class MainActivityViewModel implements BaseItemProvider<Rate>, RateItemHandler {
    private final IMainActivityAccess activityAccess;
    private final ApiService apiService;
    private final Repository repository;

    private List<Rate> rates;

    public MainActivityViewModel(IMainActivityAccess activityAccess, ApiService apiService,
                                 Repository repository) {
        this.activityAccess = activityAccess;
        this.apiService = apiService;
        this.repository = repository;
    }

    public void onInfrastructureReady() {
        loadRates();
    }

    private void loadRates() {
        apiService.loadRates(new ApiServiceCallback<ApiTable[]>() {
            @Override
            public void onSuccess(ApiTable[] apiTables) {
                repository.saveTables(apiTables);
                rates = repository.getAllRates();

                activityAccess.displayRates(MainActivityViewModel.this, MainActivityViewModel.this);
                activityAccess.hideProgressIndicator();
            }

            @Override
            public void onFailure(Throwable t) {
                activityAccess.hideProgressIndicator();
            }
        });
    }

    @Override
    public List<Rate> getItems() {
        return rates;
    }

    @Override
    public void showRateDetails(Rate rate) {
        activityAccess.openRateDetailsScreen(rate);
    }
}
