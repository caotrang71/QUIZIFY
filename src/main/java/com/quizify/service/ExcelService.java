package com.quizify.service;

import com.quizify.model.Question;
import com.quizify.model.QuestionChoice;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    public static List<Question> parseExcelFile(MultipartFile file) throws Exception {
        List<Question> questions = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                rowCount++;
                Question question = new Question();
                Cell cell = row.getCell(0);

                int rowInExcel = row.getRowNum() + 1;

                if (cell == null || cell.getCellType() == CellType.BLANK) {
                    throw new Exception("Question content missing in row: " + rowInExcel);
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            question.setContent(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            question.setContent(String.valueOf((int) cell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            question.setContent(String.valueOf(cell.getBooleanCellValue()));
                            break;
                        case FORMULA:
                            question.setContent(cell.getCellFormula());
                            break;
                        default:
                            throw new Exception("Unsupported cell type encountered in question content at row: " + rowInExcel);
                    }
                }

                List<QuestionChoice> choices = new ArrayList<>();
                for (int i = 1; i <= 4; i++) {
                    Cell choiceCell = row.getCell(i);
                    if (choiceCell == null || choiceCell.getCellType() == CellType.BLANK) {
                        throw new Exception("Choice " + i + " content missing in row: " + rowInExcel);
                    }
                    QuestionChoice choice = new QuestionChoice();

                    switch (choiceCell.getCellType()) {
                        case STRING:
                            choice.setContent(choiceCell.getStringCellValue());
                            break;
                        case NUMERIC:
                            choice.setContent(String.valueOf((int) choiceCell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            choice.setContent(String.valueOf(choiceCell.getBooleanCellValue()));
                            break;
                        case FORMULA:
                            choice.setContent(choiceCell.getCellFormula());
                            break;
                        default:
                            throw new Exception("Unsupported cell type encountered in question choice at row: " + rowInExcel);
                    }

                    choice.setCorrectOrNot(false);
                    choices.add(choice);
                }

                // Set correct choice based on numeric value
                Cell correctChoiceCell = row.getCell(5);
                if (correctChoiceCell != null) {
                    CellType correctChoiceCellType = correctChoiceCell.getCellType();
                    System.out.println("Correct choice cell type: " + correctChoiceCellType);
                    switch (correctChoiceCellType) {
                        case NUMERIC:
                            int correctChoiceIndex = (int) correctChoiceCell.getNumericCellValue();
                            if (correctChoiceIndex >= 1 && correctChoiceIndex <= 4) {
                                choices.get(correctChoiceIndex - 1).setCorrectOrNot(true);
                            } else {
                                throw new Exception("Invalid correct choice index in row: " + rowInExcel);
                            }
                            break;
                        case STRING:
                            try {
                                int correctChoiceIndexFromString = Integer.parseInt(correctChoiceCell.getStringCellValue());
                                if (correctChoiceIndexFromString >= 1 && correctChoiceIndexFromString <= 4) {
                                    choices.get(correctChoiceIndexFromString - 1).setCorrectOrNot(true);
                                } else {
                                    throw new Exception("Invalid correct choice index in row: " + rowInExcel);
                                }
                            } catch (NumberFormatException e) {
                                throw new Exception("Invalid correct choice index format in row: " + rowInExcel);
                            }
                            break;
                        default:
                            System.out.println("Skipping unsupported cell type for correct choice index in row: " + rowInExcel);
                    }
                } else {
                    throw new Exception("Correct choice index not found or invalid in row: " + rowInExcel);
                }

                question.setQuestionChoices(choices);
                questions.add(question);

                System.out.println("Added question: " + question.getContent() + " with choices: " + choices.size()+1);
            }

            System.out.println("Total questions parsed: " + rowCount);

        } catch (Exception e) {
            throw new Exception("Error parsing Excel file: " + e.getMessage(), e);
        }

        return questions;
    }

    private static boolean isRowEmpty(Row row) {
        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
