package com.gosafe.controller;

import com.gosafe.model.Journey;
import com.gosafe.payload.MessageResponse;
import com.gosafe.repository.JourneyRepository;
import com.gosafe.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/journeys")
public class JourneyController {

    @Autowired
    private JourneyRepository journeyRepository;

    @PostMapping
    public ResponseEntity<?> saveJourney(@RequestBody Journey journey) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            Long userId = ((UserDetailsImpl) principal).getId();
            journey.setUserId(userId);
        } else {
            // Should be authenticated due to WebSecurityConfig, but just in case
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized user context."));
        }

        Journey savedJourney = journeyRepository.save(journey);
        return ResponseEntity.ok(savedJourney);
    }

    @GetMapping
    public ResponseEntity<?> getUserJourneys() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            Long userId = ((UserDetailsImpl) principal).getId();
            List<Journey> journeys = journeyRepository.findByUserIdOrderByCreatedAtDesc(userId);
            return ResponseEntity.ok(journeys);
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized user context."));
    }

    // Public endpoint for sharing & tracking
    @GetMapping("/share/{id}")
    public ResponseEntity<?> getSharedJourney(@PathVariable Long id) {
        Optional<Journey> journeyOpt = journeyRepository.findById(id);
        if (journeyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(journeyOpt.get());
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<?> updateJourneyLocation(
            @PathVariable Long id,
            @RequestParam Double lat,
            @RequestParam Double lng) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized user context."));
        }

        Long userId = ((UserDetailsImpl) principal).getId();
        Optional<Journey> journeyOpt = journeyRepository.findById(id);

        if (journeyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Journey journey = journeyOpt.get();
        if (!userId.equals(journey.getUserId())) {
            return ResponseEntity.status(403).body(new MessageResponse("Error: Unauthorized to update this journey location."));
        }

        journey.setCurrentLat(lat);
        journey.setCurrentLng(lng);
        Journey updated = journeyRepository.save(journey);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJourney(@PathVariable Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            Long userId = ((UserDetailsImpl) principal).getId();
            Optional<Journey> journeyOpt = journeyRepository.findById(id);

            if (journeyOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Journey journey = journeyOpt.get();
            if (!userId.equals(journey.getUserId())) {
                return ResponseEntity.status(403).body(new MessageResponse("Error: Unauthorized to delete this journey."));
            }

            journeyRepository.delete(journey);
            return ResponseEntity.ok(new MessageResponse("Journey deleted successfully."));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized user context."));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllUserJourneys() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            Long userId = ((UserDetailsImpl) principal).getId();
            List<Journey> journeys = journeyRepository.findByUserIdOrderByCreatedAtDesc(userId);
            journeyRepository.deleteAll(journeys);
            return ResponseEntity.ok(new MessageResponse("All journeys deleted successfully."));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized user context."));
    }
}
