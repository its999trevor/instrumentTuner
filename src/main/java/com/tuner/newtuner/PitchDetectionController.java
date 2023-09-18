package com.tuner.newtuner;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PitchDetectionController {

    @Autowired
    private PitchDetectionService pitchDetectionService;

    @GetMapping("/")
    public String index() {
        return "index"; // Return the FreeMarker template name for the web interface
    }


    @PostMapping("/startDetection")
    public String startDetection() {
        pitchDetectionService.startPitchDetection();
        return "redirect:/";
    }

    @PostMapping("/stopDetection")
    public String stopDetection() {
        pitchDetectionService.stopPitchDetection();
        return "redirect:/";
    }
}
