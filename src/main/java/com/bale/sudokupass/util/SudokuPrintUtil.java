package com.bale.sudokupass.util;

import com.bale.sudokupass.web.dto.Cell;
import com.bale.sudokupass.web.dto.SudokuDto;

import java.util.List;

public class SudokuPrintUtil {
    public static void printSudokuDto(SudokuDto sudokuDto, String message) {
        System.out.println(message);
        for(List<Cell> cells : sudokuDto.getSudokuList()) {
            for(Cell cell : cells) {
                System.out.print(cell.getValue() + "\t");
            }
            System.out.println();
        }
    }
}
