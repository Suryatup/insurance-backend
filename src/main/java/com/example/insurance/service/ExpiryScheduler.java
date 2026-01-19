package com.example.insurance.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.insurance.model.Customer;
import com.example.insurance.model.Vehicle;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExpiryScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(ExpiryScheduler.class);

    private final CustomerService customerService;
    private final MailService mailService;

    @Value("${insurance.expiry.warning-days}")
    private int warningDays;

    // 🔁 Testing: every 1 minute
    // @Scheduled(cron = "0 */1 * * * ?")
    // ⏰ Production:
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendExpiryWarnings() {

        try {
            List<Customer> customers = customerService.getAll();
            LocalDate today = LocalDate.now();

            for (Customer c : customers) {

                // ❌ Skip if mail already sent
                if (Boolean.TRUE.equals(c.getExpiryMailSent()))
                    continue;

                if (c.getEndDate() == null || c.getEndDate().isBlank()
                        || c.getEmail() == null || c.getEmail().isBlank())
                    continue;

                LocalDate endDate = LocalDate.parse(c.getEndDate());
                long daysLeft = ChronoUnit.DAYS.between(today, endDate);

                boolean expired = daysLeft < 0;
                boolean expiringSoon =
                        daysLeft <= warningDays && daysLeft >= 0;

                if (!expired && !expiringSoon)
                    continue;

                String vehicleInfo = buildVehicleInfo(c);

                // ✅ Mark FIRST (prevents duplicates)
                c.setExpiryMailSent(true);
                customerService.update(c.getId(), c);

                // ✅ Send ADMIN mail
                mailService.sendExpiryMail(
                        c.getName(),
                        c.getEmail(),
                        c.getPhone(),
                        c.getLocation(),
                        vehicleInfo,
                        c.getEndDate(),
                        expired
                );

                log.info(
                        "Expiry mail sent for customer {} ({})",
                        c.getName(),
                        c.getEmail()
                );
            }

        } catch (Exception e) {
            log.error("Error in expiry scheduler", e);
        }
    }

    private String buildVehicleInfo(Customer c) {
        if (c.getVehicles() == null || c.getVehicles().isEmpty())
            return "N/A";

        Vehicle v = c.getVehicles().get(0);
        return v.getType() + " - " + v.getNumber();
    }
}
