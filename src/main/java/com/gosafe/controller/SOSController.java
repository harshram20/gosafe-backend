package com.gosafe.controller;

import com.gosafe.model.SOSAlert;
import com.gosafe.model.User;
import com.gosafe.payload.MessageResponse;
import com.gosafe.payload.SOSRequest;
import com.gosafe.repository.UserRepository;
import com.gosafe.repository.SOSAlertRepository;
import com.gosafe.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/sos")
public class SOSController {
    private static final Logger logger = LoggerFactory.getLogger(SOSController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SOSAlertRepository sosAlertRepository;

    @PostMapping("/trigger")
    public ResponseEntity<?> triggerSOS(@Valid @RequestBody SOSRequest sosRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SOSAlert alert = new SOSAlert();
        alert.setLatitude(sosRequest.getLatitude());
        alert.setLongitude(sosRequest.getLongitude());

        String customMsg = sosRequest.getMessage() != null ? sosRequest.getMessage() : "I need help immediately!";
        alert.setMessage(customMsg);

        String dispatchName;
        String dispatchPhone;
        String travelerName;

        // Check if user is authenticated
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {

            Optional<User> userOpt = userRepository.findById(userDetails.getId());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                alert.setUserId(user.getId());
                alert.setUserName(user.getName());
                alert.setEmergencyContactName(user.getEmergencyContactName());
                alert.setEmergencyContactPhone(user.getEmergencyContactPhone());

                travelerName = user.getName();
                dispatchName = user.getEmergencyContactName();
                dispatchPhone = user.getEmergencyContactPhone();
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User record not found."));
            }
        } else {
            // Guest mode SOS
            if (sosRequest.getGuestEmergencyName() == null || sosRequest.getGuestEmergencyPhone() == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(
                        "Error: Guest emergency contact name and phone are required in guest mode."));
            }
            alert.setUserName("Guest Traveler");
            alert.setEmergencyContactName(sosRequest.getGuestEmergencyName());
            alert.setEmergencyContactPhone(sosRequest.getGuestEmergencyPhone());

            travelerName = "Guest Traveler";
            dispatchName = sosRequest.getGuestEmergencyName();
            dispatchPhone = sosRequest.getGuestEmergencyPhone();
        }

        sosAlertRepository.save(alert);

        // Standardized simulated WhatsApp dispatch log
        String whatsappText = String.format(
                "ALERT! EMERGENCY details for %s: Location coordinates: (%f, %f). Message: '%s'. Track live: http://localhost:5173/share-tracking?lat=%f&lng=%f",
                travelerName, alert.getLatitude(), alert.getLongitude(), customMsg, alert.getLatitude(), alert.getLongitude()
        );

        logger.info("========================================= SOS DISPATCH =========================================");
        logger.info("WHATSAPP DESTINATION: {} ({})", dispatchPhone, dispatchName);
        logger.info("WHATSAPP CONTENT: {}", whatsappText);
        logger.info("================================================================================================");

        // Return confirmation details to the frontend
        return ResponseEntity.ok(new MessageResponse(
                String.format("SOS Alert successfully registered! WhatsApp message prepared for %s (%s): \"%s\"",
                        dispatchName, dispatchPhone, whatsappText)
        ));
    }
}
