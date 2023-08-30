package com.homework.demo.controller;

import com.homework.demo.model.MathOperation;
import com.homework.demo.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/calculator")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @PostMapping("/do-math")
    public String doMathOperation(@RequestBody List<MathOperation> mathOperationList){
        return "Calculations finished and the results are in " + calculatorService.doMathOperation(mathOperationList);
    }

    @GetMapping("/check-finished/{filename}")
    public ResponseEntity<String> checkFinished(@PathVariable String filename) {
        boolean calculationsFinished = calculatorService.areCalculationsFinished(filename);

        if (calculationsFinished) {
            String result = calculatorService.getResultHtmlFromFile(filename);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.ok("Calculations are not finished yet.");
        }
    }

    @GetMapping("/results/{filename}")
    public ResponseEntity<String> getResultsByFilename(@PathVariable String filename) {
        String resultsHtml = calculatorService.getResultHtmlFromFile(filename);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(resultsHtml, headers, HttpStatus.OK);
    }
}
