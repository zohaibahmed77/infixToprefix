package com.example.infixprefix;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class InfixController {

    @GetMapping("/convert")
    public Map<String, Object> convert(@RequestParam String expression) {
        InfixToPrefix.ConversionResult result = InfixToPrefix.infixToPrefixWithSteps(expression);

        Map<String, Object> response = new HashMap<>();
        response.put("result", result.getResult());
        response.put("steps", result.getSteps());

        return response;
    }
}
