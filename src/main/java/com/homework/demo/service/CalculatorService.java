package com.homework.demo.service;

import com.homework.demo.model.MathOperation;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculatorService {

    // for "/do-math" endpoint
    private int fileCounter = 1;

    private void writeResultToFile(String resultLine, String filename) {
        try (FileWriter fileWriter = new FileWriter(filename, true)) {
            fileWriter.write(resultLine + "\n");
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file", e);
        }
    }

    public String doMathOperation(List<MathOperation> mathOperationList) {
        String filename = "results" + fileCounter + ".txt";
        fileCounter++;

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (MathOperation mathOperation : mathOperationList) {
            double result = calculateResult(mathOperation);
            String resultLine = mathOperation.getOperands()[0] + " " + mathOperation.getOperation() + " "
                    + mathOperation.getOperands()[1] + " = " + result;

            writeResultToFile(resultLine, filename);
        }

        return filename;
    }


    public double calculateResult(MathOperation mathOperation) {
        switch (mathOperation.getOperation()) {
            case "add" -> {
                return mathOperation.getOperands()[0] + mathOperation.getOperands()[1];
            }
            case "sub" -> {
                return mathOperation.getOperands()[0] - mathOperation.getOperands()[1];
            }
            case "mul" -> {
                return mathOperation.getOperands()[0] * mathOperation.getOperands()[1];
            }
            case "div" -> {
                if (mathOperation.getOperands()[1] != 0)
                    return mathOperation.getOperands()[0] / mathOperation.getOperands()[1];
                else {
                    throw new ArithmeticException("Division by zero");
                }
            }
            default -> throw new ArithmeticException("Unsupported operation: " + mathOperation.getOperation());
        }
    }

    // for "/check-finished" endpoint
    public boolean areCalculationsFinished(String filename) {

        File resultsFile = new File(filename);

        if (!resultsFile.exists() || resultsFile.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    // for "/results" endpoint
    private List<String> readResultsFromFile(String filename) {
        List<String> results = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                results.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }

        return results;
    }

    public String getResultHtmlFromFile(String filename) {
        List<String> results = readResultsFromFile(filename);
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body><h2>Results for ").append(filename).append("</h2>");
        htmlBuilder.append("<table border='1'><tr><th>Operand 1</th><th>Operand 2</th><th>Operation</th><th>Result</th></tr>");

        for (String resultLine : results) {
            String[] parts = resultLine.split(" ");

            htmlBuilder.append("<tr>")
                    .append("<td>").append(parts[0]).append("</td>")
                    .append("<td>").append(parts[2]).append("</td>")
                    .append("<td>").append(parts[1]).append("</td>")
                    .append("<td>").append(parts[4]).append("</td>")
                    .append("</tr>");
        }

        htmlBuilder.append("</table></body></html>");
        return htmlBuilder.toString();
    }
}
