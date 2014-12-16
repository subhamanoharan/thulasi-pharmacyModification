package com.cryptic.imed.app;

import com.cryptic.imed.domain.enums.AppointmentType;
import com.cryptic.imed.domain.enums.MedicineType;
import com.cryptic.imed.domain.enums.TimeIntervalUnit;
import com.cryptic.imed.util.view.ViewUtils;
import com.google.inject.AbstractModule;

/**
 * @author sharafat
 */
public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        requestStaticInjection(AbstractDbHelper.class, MedicineType.class, AppointmentType.class,
                TimeIntervalUnit.class, ViewUtils.class);
    }
}
